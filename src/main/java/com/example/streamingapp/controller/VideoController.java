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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private final VideoService videoService;

    @GetMapping("/getMyVideos")
    public ResponseEntity getMyVideos(){
        JSONObject resJobj = new JSONObject();
        try{
            List<Video> videoList = videoService.getMyVideos();
            resJobj.put("status", "SUCCESS");

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
                videoJobj.put("url", video.getUrl());

                data.add(videoJobj);
            }
            resJobj.put("data", data);

            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
