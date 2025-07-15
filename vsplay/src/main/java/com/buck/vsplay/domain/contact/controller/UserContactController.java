package com.buck.vsplay.domain.contact.controller;


import com.buck.vsplay.domain.contact.dto.UserContactDto;
import com.buck.vsplay.domain.contact.service.IUserContactService;
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
@RequestMapping("contact")
public class UserContactController {

    private final IUserContactService userContactService;

    @PostMapping()
    public ResponseEntity<SingleResponseDto<Integer>> createUserContact(
            @RequestBody  UserContactDto.ContactCreateRequest request) {
        userContactService.createUserContact(request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}
