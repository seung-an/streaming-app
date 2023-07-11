package com.example.streamingapp.controller;

import com.example.streamingapp.domain.Comment;
import com.example.streamingapp.domain.Video;
import com.example.streamingapp.dto.CommentDto;
import com.example.streamingapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private final CommentService commentService;

    @PostMapping("/addComment/{id}")
    public ResponseEntity addComment(@PathVariable String id, @RequestBody Map<String, Object> data){
        JSONObject resJobj = new JSONObject();
        try{
            commentService.addComment(Integer.parseInt(id), (String) data.get("content"));

            resJobj.put("status", "SUCCESS");
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch(Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getComments/{id}")
    public ResponseEntity getComments(@PathVariable String id) {

        JSONObject resJobj = new JSONObject();
        try{
            List<CommentDto> comments = commentService.getComments(Integer.parseInt(id));

            resJobj.put("status", "SUCCESS");
            resJobj.put("data", comments);

            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj = new JSONObject();
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deleteComment/{id}")
    public ResponseEntity addComment(@PathVariable String id){
        JSONObject resJobj = new JSONObject();
        try{
            commentService.deleteComment(Integer.parseInt(id));

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
