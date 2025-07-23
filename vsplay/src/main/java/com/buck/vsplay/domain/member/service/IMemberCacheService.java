package com.buck.vsplay.domain.member.service;

import com.buck.vsplay.domain.member.dto.CachedMemberDto;

public interface IMemberCacheService {
    CachedMemberDto getMember(Long memberId);
}
