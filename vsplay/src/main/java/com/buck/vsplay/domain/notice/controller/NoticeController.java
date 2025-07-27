package com.buck.vsplay.domain.notice.controller;


import com.buck.vsplay.domain.notice.dto.NoticeDto;
import com.buck.vsplay.domain.notice.service.impl.NoticeService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<SingleResponseDto<NoticeDto.SearchNoticeResponse>> searchNotice(
            NoticeDto.SearchNoticeRequest noticeSearchRequest
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), noticeService.searchNoticeList(noticeSearchRequest)), HttpStatus.OK);
    }

    @GetMapping("{noticeId}")
    public ResponseEntity<SingleResponseDto<NoticeDto.NoticeDetailResponse>> noticeDetail(
            @PathVariable Long noticeId
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), noticeService.getNoticeDetail(noticeId)), HttpStatus.OK);
    }

    @PostMapping // TODO 테스트 후 제거
    public ResponseEntity<SingleResponseDto<Integer>> createNotice(){
        noticeService.createNotice();
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}
