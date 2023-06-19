package com.example.streamingapp.controller;

import com.example.streamingapp.domain.Video;
import com.example.streamingapp.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private final VideoService videoService;

    @Autowired
    private final FileController fileController;


    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file")MultipartFile file){
        JSONObject resJobj = new JSONObject();

        try {
            String videoUrl = fileController.videoUpload(file);

            if (videoUrl.equals("")) {
                resJobj.put("status", "ERROR");
                return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
            }

            String thumbnailUrl = fileController.initialThumbnail(videoUrl);

            if (thumbnailUrl.equals("")) {
                resJobj.put("status", "ERROR");
                return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
            }

            Integer videoId = videoService.createVideo(videoUrl, thumbnailUrl);
            resJobj.put("status", "SUCCESS");
            resJobj.put("videoId", videoId);
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadThumbnail")
    public ResponseEntity uploadThumbnail(@RequestParam("file")MultipartFile file, @RequestParam("origin")String origin){
        JSONObject resJobj = new JSONObject();

        try {
            String thumbnailUrl = fileController.thumbnailUpload(file, origin);
            if (thumbnailUrl.equals("")) {
                resJobj.put("status", "ERROR");
                return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
            }
            resJobj.put("status", "SUCCESS");
            resJobj.put("thumbnailUrl", thumbnailUrl);
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getMyVideos")
    public ResponseEntity getMyVideos(){
        JSONObject resJobj = new JSONObject();
        try{
            List<Video> videoList = videoService.getMyVideos();

            JSONArray data = new JSONArray();
            for (Video video: videoList) {
                JSONObject videoJobj = new JSONObject();

                videoJobj.put("videoId", video.getVideoId());
                videoJobj.put("title", video.getTitle());
                videoJobj.put("description", video.getDescription());
                videoJobj.put("state", video.getState());
                videoJobj.put("createdDt", video.getCreatedDt());
                videoJobj.put("views", video.getViews());
                videoJobj.put("likes", video.getLikes());
                videoJobj.put("thumbnailUrl", video.getThumbnailUrl());
                videoJobj.put("videoUrl", video.getVideoUrl());

                data.add(videoJobj);
            }
            resJobj.put("status", "SUCCESS");
            resJobj.put("data", data);

            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj = new JSONObject();
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getVideos")
    public ResponseEntity getVideos(@RequestParam @Nullable String searchQuery){
        JSONObject resJobj = new JSONObject();
        try{
            List<Video> videoList = videoService.getVideos(searchQuery);

            JSONArray data = new JSONArray();
            for (Video video: videoList) {
                JSONObject videoJobj = new JSONObject();

                videoJobj.put("videoId", video.getVideoId());
                videoJobj.put("channelName", video.getMember().getName());
                videoJobj.put("channelImage", video.getMember().getImageUrl());
                videoJobj.put("title", video.getTitle());
                videoJobj.put("description", video.getDescription());
                videoJobj.put("state", video.getState());
                videoJobj.put("createdDt", video.getCreatedDt());
                videoJobj.put("views", video.getViews());
                videoJobj.put("likes", video.getLikes());
                videoJobj.put("thumbnailUrl", video.getThumbnailUrl());
                videoJobj.put("videoUrl", video.getVideoUrl());

                data.add(videoJobj);
            }
            resJobj.put("status", "SUCCESS");
            resJobj.put("data", data);

            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj = new JSONObject();
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getVideoInfo/{id}")
    public ResponseEntity getVideoInfo(@PathVariable String id){
        JSONObject resJobj = new JSONObject();
        try{
            Optional<Video> videoInfo = videoService.getVideoInfo(Integer.parseInt(id));
            if(!videoInfo.isPresent()){
                resJobj.put("status", "ERROR");
                resJobj.put("message", "존재하지 않는 동영상 입니다.");
                return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
            }

            JSONObject videoJobj = new JSONObject();

            videoJobj.put("videoId", videoInfo.get().getVideoId());
            videoJobj.put("channelName", videoInfo.get().getMember().getName());
            videoJobj.put("channelImage", videoInfo.get().getMember().getImageUrl());
            videoJobj.put("title", videoInfo.get().getTitle());
            videoJobj.put("description", videoInfo.get().getDescription());
            videoJobj.put("state", videoInfo.get().getState());
            videoJobj.put("createdDt", videoInfo.get().getCreatedDt());
            videoJobj.put("views", videoInfo.get().getViews());
            videoJobj.put("likes", videoInfo.get().getLikes());
            videoJobj.put("thumbnailUrl", videoInfo.get().getThumbnailUrl());
            videoJobj.put("videoUrl", videoInfo.get().getVideoUrl());

            resJobj.put("status", "SUCCESS");
            resJobj.put("data", videoJobj);

            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch(Exception e) {
            resJobj = new JSONObject();
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateVideo/{id}")
    public ResponseEntity updateVideo(@PathVariable String id, @RequestBody Map<String, Object> data){
        JSONObject resJobj = new JSONObject();
        try{
            videoService.updateVideo(Integer.parseInt(id), data);

            resJobj.put("status", "SUCCESS");
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch(Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/increaseViews/{id}")
    public ResponseEntity increaseViews(@PathVariable String id){
        JSONObject resJobj = new JSONObject();
        try{
            videoService.increaseViews(Integer.parseInt(id));

            resJobj.put("status", "SUCCESS");
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch(Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }
}
