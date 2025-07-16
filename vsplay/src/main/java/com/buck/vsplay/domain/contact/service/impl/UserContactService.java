package com.buck.vsplay.domain.contact.service.impl;

import com.buck.vsplay.domain.contact.dto.UserContactDto;
import com.buck.vsplay.domain.contact.entity.UserContact;
import com.buck.vsplay.domain.contact.mapper.UserContactMapper;
import com.buck.vsplay.domain.contact.repository.UserContactRepository;
import com.buck.vsplay.domain.contact.service.IUserContactService;
import com.buck.vsplay.domain.contact.service.support.InquiryEmail;
import com.buck.vsplay.domain.contact.service.support.InquirySlack;
import com.buck.vsplay.global.util.request.ClientInfoExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserContactService implements IUserContactService {

    private final UserContactMapper userContactMapper;
    private final UserContactRepository userContactRepository;
    private final InquiryEmail inquiryEmail;
    private final InquirySlack inquirySlack;

    @Override
    public void createUserContact(UserContactDto.ContactCreateRequest request) {
        UserContact userContact = userContactMapper.toEntityFromCreateRequestDto(request);

        userContact.setIpAddress(ClientInfoExtractor.extractIpFromRequest());
        userContact.setUserAgent(ClientInfoExtractor.extractUserAgentFromRequest());
        userContactRepository.save(userContact);

        // 알림 메일 전송
        try {
            inquiryEmail.sendInquiryMail(request.getAuthor(), request.getEmail(), request.getTitle(), request.getContent());
        } catch (Exception e) {
            log.warn("문의 등록 알림 메일 전송에 실패했습니다.[ author: {}, email: {}] -{} ", request.getAuthor(), request.getEmail(), e.getMessage(), e);
        }

        // Slack 알림 메시지 전송
        try {
            inquirySlack.sendInquiryCreatedSlackMessage(request.getAuthor(), request.getEmail(), request.getTitle(), request.getContent());
        } catch (Exception e) {
            log.warn("문의 등록 Slack 메시지 전송에 실패했습니다.[ author: {}, email: {}] -{} ", request.getAuthor(), request.getEmail(), e.getMessage(), e);
        }

    }
}
