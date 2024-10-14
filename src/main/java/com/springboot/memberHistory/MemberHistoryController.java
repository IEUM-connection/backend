package com.springboot.memberHistory;

import com.springboot.dto.SingleResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberHistoryController {

    @GetMapping("{member-id}")
    public ResponseEntity getMemberHistory(@PathVariable("member-id") Long memberId){


        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }
}
