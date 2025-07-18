package com.buck.vsplay.domain.inquiry.dto;


import com.buck.vsplay.domain.inquiry.constants.InquiryType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryDto {

    @Getter
    @Setter
    @Builder
    public static class InquiryCreateRequest{
        private InquiryType inquiryType;

        private String author;

        @NotNull(message = "이메일은 필수 입력 항목 입니다.")
        private String email;

        private String title;
        private String content;
    }
}
