package com.buck.vsplay.domain.member.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDto {

    @Data
    public static class CreateMemberRequest{
        @NotNull(message = "아이디는 필수 입력 항목 입니다.")
        private String loginId;

        @NotNull(message = "이름이 비었습니다.")
        @Size(min = 3, max = 50, message = "이름은 2자이상 50자 이하여야 합니다.")
        private String memberName;

        @NotNull(message = "비밀번호가 비었습니다.")
        private String password;
    }

    @Data
    public static class UpdateMemberRequest{

        @NotNull(message = "이름이 비었습니다.")
        @Size(min = 3, max = 50, message = "이름은 2자이상 50자 이하여야 합니다.")
        private String memberName;

        @NotNull(message = "비밀번호가 비었습니다.")
        private String password;

    }

    @Data
    public static class AddressInfo{
        private String street;
        private String city;
        private String state;
    }

    @Data
    public static class MemberListResponse{
        private List<MemberInfo> memberList;
    }

}
