package com.example.streamingapp.controller;

import com.example.streamingapp.dto.MemberLoginRequestDto;
import com.example.streamingapp.dto.TokenInfo;
import com.example.streamingapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @PostMapping("/getToken")
    public ResponseEntity getToken(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {

        try {
            String memberId = memberLoginRequestDto.getMemberId();
            String password = memberLoginRequestDto.getPassword();
            TokenInfo tokenInfo = memberService.login(memberId, password);

            createCookie("refreshToken", tokenInfo.getRefreshToken(), response);
            return new ResponseEntity(tokenInfo, HttpStatus.OK);
        }
        catch (Exception e){
            JSONObject error = new JSONObject();
            error.put("status", "ERROR");
            error.put("message", e.getMessage());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    public void createCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("localhost");

        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);
    }
}
