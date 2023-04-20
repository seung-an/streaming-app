package com.example.streamingapp.controller;

import com.example.streamingapp.dto.MemberLoginRequestDto;
import com.example.streamingapp.dto.TokenInfo;
import com.example.streamingapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {

        try {
            String memberId = memberLoginRequestDto.getMemberId();
            String password = memberLoginRequestDto.getPassword();
            TokenInfo tokenInfo = memberService.getToken(memberId, password);

            return new ResponseEntity(tokenInfo, HttpStatus.OK);
        }
        catch (Exception e){
            JSONObject error = new JSONObject();
            error.put("status", "ERROR");
            error.put("message", e.getMessage());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(@RequestHeader(value="Authorization") String refreshToken, HttpServletResponse response) {

        try {
            TokenInfo tokenInfo = memberService.refreshToken(resolveToken(refreshToken));

            return new ResponseEntity(tokenInfo, HttpStatus.OK);
        }
        catch (Exception e){
            JSONObject error = new JSONObject();
            error.put("status", "ERROR");
            error.put("message", e.getMessage());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody Map<String, Object> data, UriComponentsBuilder uriBuilder) {

        JSONObject resultJobj = new JSONObject();
        try {
            Integer memberCode = memberService.join(data);
            resultJobj.put("status", "SUCCESS");
            return new ResponseEntity(resultJobj, HttpStatus.OK);
        } catch (Exception e) {

            resultJobj.put("status", "ERROR");
            resultJobj.put("error_massage", e.getMessage());
            return new ResponseEntity(resultJobj.toJSONString(), HttpStatus.BAD_REQUEST);
        }
    }

    public void createCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("localhost");

//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);
    }

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }

        return null;
    }
}
