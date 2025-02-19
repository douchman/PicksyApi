package com.buck.vsplay.domain.member.controller;


import com.buck.vsplay.domain.member.dto.MemberDto;
import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.member.service.IMemberService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService memberService;


    @GetMapping
    public ResponseEntity<SingleResponseDto<List<Member>>> getMemberList() {
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), memberService.getMemberList()), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<SingleResponseDto<Integer>> createMember(@RequestBody @Validated MemberDto.CreateMemberRequest member) {
        memberService.createMember(member);


        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PatchMapping("{memberId}")
    public ResponseEntity<SingleResponseDto<Integer>> updateMember(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberDto.UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(memberId, updateMemberRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SingleResponseDto<Integer>> deleteMember(@PathVariable("id") Long id) {

        memberService.deleteMember(id);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}
