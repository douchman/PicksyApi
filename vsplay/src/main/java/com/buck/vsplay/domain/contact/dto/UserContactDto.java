package com.buck.vsplay.domain.contact.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContactDto {

    @Getter
    @Setter
    @Builder
    public static class ContactCreateRequest{
        private String author;

        @NotNull(message = "이메일은 필수 입력 항목 입니다.")
        private String email;

        private String title;
        private String content;
    }
}
