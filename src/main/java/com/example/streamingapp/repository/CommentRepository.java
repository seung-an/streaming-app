package com.example.streamingapp.repository;

import com.example.streamingapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByVideoIdOrderByCreatedDtAsc(Integer videoId);
}
