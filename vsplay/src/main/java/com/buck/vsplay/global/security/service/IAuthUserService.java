package com.buck.vsplay.global.security.service;

import com.buck.vsplay.domain.member.entity.Member;

import java.util.Optional;

public interface IAuthUserService {
    Optional<Member> getAuthUserOptional();
    Member getAuthUser();
}
