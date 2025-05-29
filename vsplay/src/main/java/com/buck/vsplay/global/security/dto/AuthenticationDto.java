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
    private Integer status;
    private String message;
    private String errorCode;
    private String timestamp;

    @Getter
    @Setter
    public static class SuccessResponse extends AuthenticationDto{
        public SuccessResponse() {
            super(200, "로그인 성공", null, DateTimeUtil.formatNow());
        }
    }

    @Getter
    @Setter
    public static class FailureResponse extends AuthenticationDto{
        public FailureResponse(String message, String errorCode) {
            super(401, message, errorCode, DateTimeUtil.formatNow());
        }

        // 정적 메서드 추가 (중복 제거)
        public static FailureResponse invalidCredentials() {
            return new FailureResponse("아이디 또는 비밀번호를 확인해주세요.", "INVALID_CREDENTIALS");
        }

        public static FailureResponse unauthorized() {
            return new FailureResponse("로그인이 필요합니다.", "UNAUTHORIZED");
        }
    }

    @Getter
    @Setter
    public static class LoginRequest{
        private String id;
        private String password;
    }
}
