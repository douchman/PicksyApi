package com.buck.vsplay.domain.gpt.controller;


import com.buck.vsplay.domain.gpt.dto.GptApiDto;
import com.buck.vsplay.domain.gpt.service.IGptService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gpt")
@RequiredArgsConstructor
public class GptApiController {

    private final IGptService gptService;


    @PostMapping("bad-word")
    public ResponseEntity<SingleResponseDto<Integer>> checkBadWord(
            @RequestBody GptApiDto.BadWordApiRequest badWordApiRequest
            ){

        gptService.moderateTextContent(badWordApiRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}
