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
public class SubscribePK implements Serializable {

    @Column(name = "member_code")
    private Integer memberCode;

    @Column(name = "sub_channel_code")
    private Integer subChannelCode;
}
