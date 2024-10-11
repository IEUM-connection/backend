package com.springboot.utillity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsDto
{
    private String body;

    private String from;

    private String to;

}
