package com.example.streamingapp.controller;

import com.example.streamingapp.domain.History;
import com.example.streamingapp.dto.HistoryDto;
import com.example.streamingapp.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private final HistoryService historyService;

    @GetMapping("/saveHistory/{id}")
    public ResponseEntity saveHistory(@PathVariable String id){
        JSONObject resJobj = new JSONObject();
        try{
            historyService.saveHistory(Integer.parseInt(id));

            resJobj.put("status", "SUCCESS");
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch(Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getHistories")
    public ResponseEntity getHistories(){
        JSONObject resJobj = new JSONObject();
        try{
            List<HistoryDto> histories = historyService.getHistories();

            resJobj.put("status", "SUCCESS");
            resJobj.put("data", histories);
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch(Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteHistory/{id}")
    public ResponseEntity deleteHistory(@PathVariable String id){
        JSONObject resJobj = new JSONObject();
        try{
            historyService.deleteHistory(Integer.parseInt(id));

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
