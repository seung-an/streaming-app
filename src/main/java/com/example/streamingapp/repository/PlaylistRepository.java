package com.example.streamingapp.repository;

import com.example.streamingapp.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    List<Playlist> findAllByMemberCode(Integer memberCode);

    List<Playlist> findAllByMemberHandle(String handle);
}
