package com.buck.vsplay.domain.inquiry.mapper;

import com.buck.vsplay.domain.inquiry.constants.InquiryType;
import com.buck.vsplay.domain.inquiry.dto.InquiryDto;
import com.buck.vsplay.domain.inquiry.entity.Inquiry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InquiryMapper {

    @Mapping(target = "inquiryType", expression = "java(mapOrDefault(contactCreateRequest.getInquiryType()))")
    Inquiry toEntityFromCreateRequestDto(InquiryDto.InquiryCreateRequest contactCreateRequest);

    default InquiryType mapOrDefault(InquiryType inquiryType){
        return inquiryType == null ? InquiryType.ETC : inquiryType;
    }
}
