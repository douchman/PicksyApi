package com.buck.vsplay.domain.inquiry.service.impl;

import com.buck.vsplay.domain.inquiry.constants.InquiryStatus;
import com.buck.vsplay.domain.inquiry.dto.InquiryDto;
import com.buck.vsplay.domain.inquiry.entity.Inquiry;
import com.buck.vsplay.domain.inquiry.mapper.InquiryMapper;
import com.buck.vsplay.domain.inquiry.repository.InquiryRepository;
import com.buck.vsplay.domain.inquiry.service.IinquiryService;
import com.buck.vsplay.domain.inquiry.service.support.InquiryEmail;
import com.buck.vsplay.domain.inquiry.service.support.InquirySlack;
import com.buck.vsplay.global.util.request.ClientInfoExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InquiryService implements IinquiryService {

    private final InquiryMapper inquiryMapper;
    private final InquiryRepository inquiryRepository;
    private final InquiryEmail inquiryEmail;
    private final InquirySlack inquirySlack;

    @Override
    public void createInquiry(InquiryDto.InquiryCreateRequest request) {
        Inquiry userContact = inquiryMapper.toEntityFromCreateRequestDto(request);

        userContact.setStatus(InquiryStatus.UNREAD);
        userContact.setIpAddress(ClientInfoExtractor.extractIpFromRequest());
        userContact.setUserAgent(ClientInfoExtractor.extractUserAgentFromRequest());
        inquiryRepository.save(userContact);

        // 알림 메일 전송
        try {
            inquiryEmail.sendInquiryMail(request);
        } catch (Exception e) {
            log.warn("문의 등록 알림 메일 전송에 실패했습니다.[ author: {}, email: {}] -{} ", request.getAuthor(), request.getEmail(), e.getMessage(), e);
        }

        // Slack 알림 메시지 전송
        try {
            inquirySlack.sendInquiryCreatedSlackMessage(request);
        } catch (Exception e) {
            log.warn("문의 등록 Slack 메시지 전송에 실패했습니다.[ author: {}, email: {}] -{} ", request.getAuthor(), request.getEmail(), e.getMessage(), e);
        }
    }
}
