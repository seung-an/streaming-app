package com.example.streamingapp.service;

import com.example.streamingapp.domain.Member;
import com.example.streamingapp.domain.Video;
import com.example.streamingapp.dto.UserCustom;
import com.example.streamingapp.dto.VideoDto;
import com.example.streamingapp.repository.MemberRepository;
import com.example.streamingapp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class VideoService {

    private  final MemberRepository memberRepository;
    private  final VideoRepository videoRepository;

    public Integer createVideo(String videoUrl, String thumbnailUrl){

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
                .thumbnailUrl(thumbnailUrl)
                .videoUrl(videoUrl)
                .build();

        return videoRepository.save(videoInfo).getVideoId();
    }

    public List<VideoDto> getMyVideos(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        List<Video> videos = videoRepository.findAllByMember_memberCodeAndStateNotOrderByCreatedDtDesc(memberCode, "deleted");

        return videos.stream().map(v -> new VideoDto(v)).collect(Collectors.toList());
    }

    public List<VideoDto> getVideos(String searchQuery){

        List<Video> videos;

        if(searchQuery == null) {
            videos = videoRepository.findAllByStateOrderByCreatedDtDesc("public");
        }
        else{
            videos = videoRepository.findAllByStateAndTitleContainingIgnoreCaseOrderByCreatedDtDesc("public", searchQuery);
        }

        return videos.stream().map(v -> new VideoDto(v)).collect(Collectors.toList());
    }

    public List<VideoDto> getChannelVideos(String handle){
        List<Video> videos = videoRepository.findAllByMember_HandleAndStateOrderByCreatedDtDesc(handle, "public");
        return videos.stream().map(v -> new VideoDto(v)).collect(Collectors.toList());
    }

    public VideoDto getVideoInfo(Integer id) throws Exception {

        Optional<Video> info = videoRepository.findById(id);
        if(!info.isPresent()){
            throw new Exception("존재하지 않는 동영상 입니다.");
        }

        return new VideoDto(info.get());
    }

    public void updateVideo(Integer id, Map<String, Object> data) throws Exception{

        Video info = videoRepository.findById(id).get();

        info.setTitle((String) data.get("title"));
        info.setDescription((String) data.get("description"));
        info.setState((String) data.get("state"));

        String thumbnailUrl = (String) data.get("thumbnailUrl");
        if(thumbnailUrl != null && thumbnailUrl != "" && !thumbnailUrl.equals(info.getThumbnailUrl())){
            info.setThumbnailUrl(thumbnailUrl);
        }

        videoRepository.save(info);

    }

    public void increaseViews(Integer id){
        Video info = videoRepository.findById(id).get();
        info.setViews(info.getViews() + 1);
        videoRepository.save(info);
    }
}
