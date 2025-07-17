package com.buck.vsplay.domain.inquiry.mapper;

import com.buck.vsplay.domain.inquiry.dto.InquiryDto;
import com.buck.vsplay.domain.inquiry.entity.Inquiry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InquiryMapper {
    Inquiry toEntityFromCreateRequestDto(InquiryDto.InquiryCreateRequest contactCreateRequest);
}
