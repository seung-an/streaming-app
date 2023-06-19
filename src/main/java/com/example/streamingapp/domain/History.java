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
@Table(name = "history")
public class History {

    @EmbeddedId
    private HistoryPK historyPK;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="video_id", insertable=false, updatable=false)
    private Video video;

    @Column(name = "watch_dt")
    private String watchDt;

}
