package com.example.streamingapp.service;

import com.example.streamingapp.domain.Comment;
import com.example.streamingapp.domain.Member;
import com.example.streamingapp.dto.UserCustom;
import com.example.streamingapp.repository.CommentRepository;
import com.example.streamingapp.repository.MemberRepository;
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
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public void addComment(Integer videoId, String content){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        Member member = memberRepository.findById(memberCode).get();

        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        Comment comment = Comment.builder()
                .videoId(videoId)
                .member(member)
                .content(content)
                .createdDt(formatter.format(today))
                .build();

        commentRepository.save(comment);
    }

    public List<Comment> getComments(Integer videoId){
        return commentRepository.findAllByVideoIdOrderByCreatedDtAsc(videoId);
    }

    public void deleteComment(Integer commentId){
        commentRepository.deleteById(commentId);
    }
}
