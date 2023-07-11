package com.example.streamingapp.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "playlist_video")
public class PlaylistVideo {

    @EmbeddedId
    private PlaylistVideoPK playlistVideoPK;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="video_id", insertable=false, updatable=false)
    private Video video;
}
