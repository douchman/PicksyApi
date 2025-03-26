package com.buck.vsplay.domain.member.dto;


import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthCheckDto {

    @Builder
    @Getter
    public static class AuthCheckResponse{
        boolean isAuth;
    }
}
