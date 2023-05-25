package com.example.streamingapp.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.streamingapp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final AmazonS3Client amazonS3Client;


    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String fileUpload(MultipartFile file) {
        try {
            // 파일명
            String fileName = file.getOriginalFilename();
            // 파일 확장자
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            //
            String convertFileName= convertFileName(fileName) + "." + ext;
            String fileUrl= "https://" + bucket + ".s3." + region + ".amazonaws.com/" + convertFileName;

            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,convertFileName,file.getInputStream(),metadata);

            return fileUrl;
        }  catch (Exception e){
            return "";
        }
    }

    public String videoUpload(MultipartFile multipartFile){

        String uploadKey = "";
        String uploadId = "";
        try {

            uploadKey = "video/" + UUID.randomUUID();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + uploadKey;

            long contentLength = multipartFile.getSize(); // 파일 전체 크기
            long partSize = 5 * 1024 * 1024; // 한 부분당 바이트(멀티파트 업로드 최소 크기는 5Mb)

            List<PartETag> partETags = new ArrayList<>(); // 각 부분별 ETags를 저장 할 리스트

            // 멀티파트 업로드 시작
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, uploadKey);
            InitiateMultipartUploadResult initResponse = amazonS3Client.initiateMultipartUpload(initRequest);

            uploadId = initResponse.getUploadId();

            // 시작 바이트
            long filePosition = 0;

            for (int i = 1; filePosition < contentLength; i++) {
                // 마지막 파트 사이즈를 체크하기위해 전체 파일사이즈 에서 시작 바이트를 뺸값과 파트 사이즈를 비교하여 작은값을 파트 사이즈로 설정
                partSize = Math.min(partSize, (contentLength - filePosition));

                // 업로드할 파트 생성
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucket)
                        .withKey(uploadKey)
                        .withUploadId(uploadId)
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withInputStream(multipartFile.getInputStream())
                        .withPartSize(partSize);

                // 생성한 파트를 업로드 하고 ETag 저장
                UploadPartResult uploadResult = amazonS3Client.uploadPart(uploadRequest);
                partETags.add(uploadResult.getPartETag());

                //시작 바이트 갱신
                filePosition += partSize;
            }

            // 업로드 완료 처리
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucket, uploadKey,
                    initResponse.getUploadId(), partETags);
            amazonS3Client.completeMultipartUpload(compRequest);

            return fileUrl;
        } catch (Exception e) {
            if (!uploadKey.equals("") && !uploadId.equals("")) {
                // 멀티파트 요청 중단
                amazonS3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                        bucket, uploadKey, uploadId));
            }
            return "";
        }
    }

    public String thumbnailUpload(MultipartFile file, String origin) {
        try {

            if(!origin.equals("")) {
                String originKey = origin.substring(origin.lastIndexOf("/") + 1);
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "thumbnail/" + originKey));
            }

            String uploadKey = "thumbnail/" + UUID.randomUUID();
            String fileUrl= "https://" + bucket + ".s3." + region + ".amazonaws.com/" + uploadKey;

            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, uploadKey, file.getInputStream(), metadata);

            return fileUrl;
        }  catch (Exception e){
            return "";
        }
    }

    @GetMapping("/multiFileUploadAllStop")
    public ResponseEntity AllStop() {

        // 전체 멀티파트 요청 조회
        ListMultipartUploadsRequest allMultpartUploadsRequest =
                new ListMultipartUploadsRequest(bucket);
        MultipartUploadListing multipartUploadListing =
                amazonS3Client.listMultipartUploads(allMultpartUploadsRequest);

        for (MultipartUpload part : multipartUploadListing.getMultipartUploads()
             ) {
            // 멀티파트 요청 중단
            amazonS3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                    bucket, part.getKey(), part.getUploadId()));
        }
        return ResponseEntity.ok("ok");
    }



    private String convertFileName(String originFileName){
        UUID uuid = UUID.randomUUID();
        return uuid + "_" + originFileName;
    }
}
