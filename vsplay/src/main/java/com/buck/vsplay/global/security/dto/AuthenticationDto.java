package com.buck.vsplay.global.security.dto;

import com.buck.vsplay.global.util.DateTimeUtil;
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
    private String timestamp;

    @Getter
    @Setter
    public static class SuccessResponse extends AuthenticationDto{
        public SuccessResponse() {
            super("success", "로그인 성공", DateTimeUtil.formatNow());
        }
    }

    @Getter
    @Setter
    public static class FailureResponse extends AuthenticationDto{
        public FailureResponse(String message) {
            super("fail", message, DateTimeUtil.formatNow());
        }
    }

    @Getter
    @Setter
    public static class LoginRequest{
        private String id;
        private String password;
    }
}
