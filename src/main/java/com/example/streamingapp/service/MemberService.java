package com.example.streamingapp.service;

import com.example.streamingapp.dto.TokenInfo;
import com.example.streamingapp.repository.MemberRepository;
import com.example.streamingapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo getToken(String memberId, String password) throws Exception{

        String salt = memberRepository.getSalt(memberId);

        if(salt == null || salt == "") {
            throw new Exception("해당하는 유저를 찾을 수 없습니다.");
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, getSecurePassword(password, salt));

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Transactional
    public TokenInfo login(String memberId, String password) throws Exception{
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    public String getSecurePassword(String password, String salt) throws NoSuchAlgorithmException {

        String passwordAndSalt = password + salt;

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // 암호화
        md.update(passwordAndSalt.getBytes());
        return String.format("%064x", new BigInteger(1, md.digest()));
    }

    public String EncodeStringToBase64(String input){

        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public String DecodeBase64ToString(String input){

        byte[] decodedBytes = Base64.getDecoder().decode(input);
        String decodedString = new String(decodedBytes);

        return decodedString;
    }
}
