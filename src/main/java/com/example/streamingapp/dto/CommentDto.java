package com.example.streamingapp.dto;

import com.example.streamingapp.domain.Comment;
import lombok.Data;

@Data
public class CommentDto {
    private Integer commentId;
    private ChannelDto channel;
    private Integer videoId;
    private String content;
    private String createdDt;

    public CommentDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.channel = new ChannelDto(comment.getMember());
        this.videoId = comment.getVideoId();
        this.content = comment.getContent();
        this.createdDt = comment.getCreatedDt();
    }
}
