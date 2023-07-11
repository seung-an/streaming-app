package com.example.streamingapp.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaylistVideoPK implements Serializable {

    @Column(name = "playlist_id")
    private Integer playlistId;

    @Column(name = "video_id")
    private Integer videoId;
}
