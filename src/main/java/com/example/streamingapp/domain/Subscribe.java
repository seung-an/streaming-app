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
@Table(name = "subscribe")
public class Subscribe {

    @EmbeddedId
    SubscribePK subscribePK;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sub_channel_code", insertable=false, updatable=false)
    private Member channel;

    @Column(name = "sub_dt", nullable = false)
    private String subDt;
}
