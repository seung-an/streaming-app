package com.example.streamingapp.service;

import com.example.streamingapp.domain.Member;
import com.example.streamingapp.domain.Video;
import com.example.streamingapp.dto.UserCustom;
import com.example.streamingapp.repository.MemberRepository;
import com.example.streamingapp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class VideoService {

    private  final MemberRepository memberRepository;
    private  final VideoRepository videoRepository;

    public void createVideo(String url){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        Member memberInfo = memberRepository.findById(memberCode).get();

        Video videoInfo = Video.builder()
                .member(memberInfo)
                .state("private")
                .createdDt(formatter.format(today))
                .views(0)
                .likes(0)
                .url(url)
                .build();

        videoRepository.save(videoInfo);

    }

    public List<Video> getMyVideos(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        return videoRepository.findAllByMember_memberCodeAndStateNot(memberCode, "deleted");
    }
}
