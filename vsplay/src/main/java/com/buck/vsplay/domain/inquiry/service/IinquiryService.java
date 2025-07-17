package com.buck.vsplay.domain.inquiry.service;

import com.buck.vsplay.domain.inquiry.dto.InquiryDto;

public interface IinquiryService {
    void createInquiry(InquiryDto.InquiryCreateRequest request);
}
