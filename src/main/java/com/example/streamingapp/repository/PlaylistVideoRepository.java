package com.example.streamingapp.repository;

import com.example.streamingapp.domain.PlaylistVideo;
import com.example.streamingapp.domain.PlaylistVideoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, PlaylistVideoPK> {

    Optional<PlaylistVideo> findFirstByPlaylistVideoPKPlaylistId(Integer playlistId);

    List<PlaylistVideo> findAllByPlaylistVideoPKPlaylistId(Integer playlistId);
}
