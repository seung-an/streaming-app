package com.example.streamingapp.controller;

import com.example.streamingapp.dto.MemberLoginRequestDto;
import com.example.streamingapp.dto.TokenInfo;
import com.example.streamingapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${public.domain}")
    private String domain;
    @Autowired
    private final MemberService memberService;

    @Autowired
    private final FileController fileController;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
        JSONObject resJobj = new JSONObject();
        try {
            String memberId = memberLoginRequestDto.getMemberId();
            String password = memberLoginRequestDto.getPassword();
            TokenInfo tokenInfo = memberService.getToken(memberId, password);

            createCookie("refreshToken", tokenInfo.getRefreshToken(), response);
            resJobj.put("status", "ERROR");
            resJobj.put("grantType", tokenInfo.getGrantType());
            resJobj.put("accessToken", tokenInfo.getAccessToken());
            resJobj.put("memberCode", tokenInfo.getMemberCode());
            resJobj.put("memberName", tokenInfo.getMemberName());
            resJobj.put("memberImage", tokenInfo.getMemberImage());
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){

            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resJobj = new JSONObject();
        try {
            String refreshToken = getCookie("refreshToken", request);
            TokenInfo tokenInfo = memberService.refreshToken(refreshToken);

            createCookie("refreshToken", tokenInfo.getRefreshToken(), response);
            resJobj.put("status", "ERROR");
            resJobj.put("grantType", tokenInfo.getGrantType());
            resJobj.put("accessToken", tokenInfo.getAccessToken());
            return new ResponseEntity(resJobj, HttpStatus.OK);
        }
        catch (Exception e){
            resJobj.put("status", "ERROR");
            resJobj.put("message", e.getMessage());
            return new ResponseEntity(resJobj, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody Map<String, Object> data, UriComponentsBuilder uriBuilder) {

        JSONObject resJobj = new JSONObject();
        try {
            String imageUrl = fileController.createBasicChannelImage((String) data.get("name"));

            Integer memberCode = memberService.join(data, imageUrl);
            resJobj.put("status", "SUCCESS");
            return new ResponseEntity(resJobj, HttpStatus.OK);
        } catch (Exception e) {

            resJobj.put("status", "ERROR");
            resJobj.put("error_massage", e.getMessage());
            return new ResponseEntity(resJobj.toJSONString(), HttpStatus.BAD_REQUEST);
        }
    }

    public void createCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);

//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);
    }

    public String getCookie(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies != null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals(cookieName)) {
                    return value;
                }
            }
        }
        return null;
    }
}
