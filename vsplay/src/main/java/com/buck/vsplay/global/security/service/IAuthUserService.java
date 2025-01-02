package com.buck.vsplay.global.security.service;

import com.buck.vsplay.domain.member.entity.Member;

public interface IAuthUserService {
    Member getAuthUser();
    Long getAuthUserId();
    String getAuthUserLoginId();
}
