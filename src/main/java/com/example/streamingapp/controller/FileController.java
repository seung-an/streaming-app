package com.example.streamingapp.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.example.streamingapp.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

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

    @Value("${file.path}")
    private String tempPath;

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

    private Upload pgUpload;
    private int progress = 0;

    public String videoUploadProgress(MultipartFile multipartFile, SseEmitter emitter){
        String uploadKey = "";
        try {

            uploadKey = "video/" + UUID.randomUUID();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + uploadKey;

            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(amazonS3Client)
                    .build();

            PutObjectRequest request = new PutObjectRequest(
                    bucket, uploadKey, multipartFile.getInputStream(), metadata);


            progress = 0;
            pgUpload = null;

            request.setGeneralProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    if(pgUpload == null) return;

                    int percent = (int)pgUpload.getProgress().getPercentTransferred();
                    if(progress != percent) {
                        try {
                            emitter.send(SseEmitter.event().name("progress").data(percent));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        progress = percent;
                    }
                }
            });

            pgUpload = tm.upload(request);

            pgUpload.waitForCompletion();

            return fileUrl;
        }catch (AmazonClientException amazonClientException) {
            System.out.println("Unable to upload file, upload aborted.");
            amazonClientException.printStackTrace();
            return "";
        }
        catch (Exception e) {
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

    public String initialThumbnail(String videoUrl) throws Exception {
        UUID uuid = UUID.randomUUID();

        String uploadKey = "thumbnail/" + uuid;
//            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + uploadKey;

        Runtime run = Runtime.getRuntime();
        String command = "ffmpeg -i " + videoUrl + " -ss 00:00:01 -vcodec png -vframes 1 -vf scale=320:180 " + tempPath + "/" + uuid + ".png"; // 동영상 1초에서 Thumbnail 추출
        System.out.println(command);

        Process p = run.exec(command);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line = null;

        while((line = br.readLine()) != null){
            System.out.println(line);
        }

        File file = new File(tempPath + "/" + uuid + ".png");
        InputStream thumbnailStream = new FileInputStream(file);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        metadata.setContentLength(file.length());
        amazonS3Client.putObject(bucket, uploadKey, thumbnailStream, metadata);

        String fileUrl = amazonS3Client.getUrl(bucket, uploadKey).toString();

        String deleteCommand = "rm " + tempPath + "/" + uuid + ".png";
        System.out.println(deleteCommand);
        run.exec(deleteCommand);

        return fileUrl;
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

    public String createBasicChannelImage(String userName){
        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();    // Graphics2D를 얻어와 그림을 그린다

        Color[] colorList = {Color.magenta, Color.blue, Color.cyan, Color.green, Color.yellow,
                Color.red, Color.orange, Color.pink};

        Integer colorIdx = (int)(Math.random() * 7);

        graphics.setColor(colorList[colorIdx]);                       // 색상을 지정한다(파란색)
        graphics.fillRect(0,0,200, 200);              // 사각형을 하나 그린다

        graphics.setFont(new Font("나눔고딕", Font.BOLD, 80));
        graphics.setColor(Color.white);

        FontMetrics metrics = graphics.getFontMetrics(new Font("나눔고딕", Font.BOLD, 80));

        String text = userName;
        int x = 0;
        int y = 0;

        if(userName.length() > 1){
            text = userName.substring(0, 2);
        }

        x = (200 - metrics.stringWidth(text)) / 2;
        y = ((200 - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics.drawString(text, x, y); //설정한 위치에 따른 텍스트를 그림

        try{

            UUID uuid = UUID.randomUUID();

            String uploadKey = "channel/" + uuid;

            File imgfile = new File(tempPath + "/" + uuid + ".png");        // 파일의 이름을 설정한다
            ImageIO.write(img, "png", imgfile);                     // write메소드를 이용해 파일을 만든다

            InputStream imgStream = new FileInputStream(imgfile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(imgfile.length());
            amazonS3Client.putObject(bucket, uploadKey, imgStream, metadata);

            String fileUrl = amazonS3Client.getUrl(bucket, uploadKey).toString();

            imgfile.delete();

            return fileUrl;
        }
        catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String channelImageUpload(MultipartFile file, String origin) {
        try {
            if(!origin.equals("")) {
                String originKey = origin.substring(origin.lastIndexOf("/") + 1);
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "channel/" + originKey));
            }

            String uploadKey = "channel/" + UUID.randomUUID();
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



    private String convertFileName(String originFileName){
        UUID uuid = UUID.randomUUID();
        return uuid + "_" + originFileName;
    }

}
