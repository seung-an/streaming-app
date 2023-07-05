package com.example.streamingapp.repository;

import com.example.streamingapp.domain.Member;
import com.example.streamingapp.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    List<Video> findAllByMember_memberCodeAndStateNotOrderByCreatedDtDesc(Integer memberCode, String state);
    List<Video> findAllByStateOrderByCreatedDtDesc(String state);

    List<Video> findAllByStateAndTitleContainingIgnoreCaseOrderByCreatedDtDesc(String state, String query);

    List<Video> findAllByMember_HandleAndStateOrderByCreatedDtDesc(String handle, String state);

}
