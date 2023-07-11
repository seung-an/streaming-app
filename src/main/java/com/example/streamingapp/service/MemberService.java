package com.example.streamingapp.service;

import com.example.streamingapp.domain.Member;
import com.example.streamingapp.dto.ChannelDto;
import com.example.streamingapp.dto.TokenInfo;
import com.example.streamingapp.dto.UserCustom;
import com.example.streamingapp.repository.MemberRepository;
import com.example.streamingapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional(readOnly = false)
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

        // 4. refreshToken DB에 저장
        Member member = memberRepository.findByMemberId(memberId).get();
        member.setRefreshToken(tokenInfo.getRefreshToken());
        memberRepository.save(member);

        tokenInfo.setMemberCode(member.getMemberCode());
        tokenInfo.setMemberName(member.getName());
        tokenInfo.setMemberImage(member.getImageUrl());

        return tokenInfo;
    }

    @Transactional
    public TokenInfo refreshToken(String refreshToken) throws Exception{

        Member member = memberRepository.findById(jwtTokenProvider.getMemberCodeByRefreshToken(refreshToken)).get();

        if(!refreshToken.equals(member.getRefreshToken())){
            throw new Exception("refreshToken 이 유효하지 않습니다.");
        }

        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new Exception("refreshToken 이 유효하지 않습니다.");
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. refreshToken DB에 저장
        member.setRefreshToken(tokenInfo.getRefreshToken());
        memberRepository.save(member);
        return tokenInfo;
    }

    @Transactional
    public Integer join(Map<String, Object> data, String imageUrl) throws Exception{

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);

        // SALT 생성
        String salt = new String(Base64.getEncoder().encode(bytes));
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        Member member = Member.builder()
                .memberId((String)data.get("memberId"))
                .password(getSecurePassword((String)data.get("password"), salt))
                .name((String)data.get("name"))
                .email((String)data.get("email"))
                .imageUrl(imageUrl)
                .handle("CH-" + UUID.randomUUID())
                .salt(salt)
                .roles(roles)
                .build();

        memberRepository.save(member);

        return member.getMemberCode();
    }

    public ChannelDto getChannelInfoByCode(Integer code) throws Exception{

        Optional<Member> info = memberRepository.findById(code);

        if(!info.isPresent()){
            throw new Exception("존재하지 않는 채널 입니다.");
        }

        return new ChannelDto(info.get());
    }

    public ChannelDto getMemberInfoByHandle(String handle) throws Exception{
        Optional<Member> info = memberRepository.findByHandle(handle);

        if(!info.isPresent()){
            throw new Exception("존재하지 않는 채널 입니다.");
        }

        return new ChannelDto(info.get());
    }

    public Boolean checkName(String name){
        Integer memberCode = getMyCode();

        Optional<Member> info = memberRepository.findByName(name);

        if(info.isPresent() && info.get().getMemberCode() != memberCode){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean checkHandle(String handle){
        Integer memberCode = getMyCode();

        Optional<Member> info = memberRepository.findByHandle(handle);

        if(info.isPresent() && info.get().getMemberCode() != memberCode){
            return true;
        }
        else{
            return false;
        }
    }

    public ChannelDto updateMember(Map<String, Object> data){

        Integer memberCode = getMyCode();
        Member info = memberRepository.findById(memberCode).get();

        info.setName((String) data.get("name"));
        info.setHandle((String) data.get("handle"));
        info.setImageUrl((String) data.get("imageUrl"));

        return new ChannelDto(memberRepository.save(info));
    }


    private Integer getMyCode(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        return userDetails.getMemberCode();
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
