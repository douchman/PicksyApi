package com.buck.vsplay.domain.inquiry.controller;


import com.buck.vsplay.domain.inquiry.dto.InquiryDto;
import com.buck.vsplay.domain.inquiry.service.IinquiryService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("inquiry")
public class InquiryContactController {

    private final IinquiryService userContactService;

    @PostMapping()
    public ResponseEntity<SingleResponseDto<Integer>> createInquiry(
            @RequestBody  InquiryDto.InquiryCreateRequest request) {
        userContactService.createInquiry(request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}
