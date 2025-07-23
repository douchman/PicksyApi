package com.buck.vsplay.global.security.service;

import com.buck.vsplay.domain.member.dto.CachedMemberDto;

import java.util.Optional;

public interface IAuthUserService {
    Optional<CachedMemberDto> getCachedMemberOptional();
    CachedMemberDto getCachedMember();
}
