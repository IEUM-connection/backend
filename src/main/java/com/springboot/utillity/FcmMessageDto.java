package com.springboot.utillity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessageDto {

    private String topic;

    private String title;

    private String body;

    private String token;
}
