package com.buck.vsplay.global.security.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationDto {
    private String status;
    private String message;
    private String token; // 성공시에만 반환

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse extends AuthenticationDto{
        public SuccessResponse(String token) {
            super("success", "로그인 성공", token);
        }
    }

    @Getter
    @Setter
    public static class FailureResponse extends AuthenticationDto{
        public FailureResponse(String message) {
            super("fail", message, null);
        }
    }
}
