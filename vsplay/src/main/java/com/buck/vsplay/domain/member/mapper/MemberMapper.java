package com.buck.vsplay.domain.member.mapper;

import com.buck.vsplay.domain.member.dto.MemberDto;
import com.buck.vsplay.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member toEntityFromCreateMemberDto(MemberDto.CreateMemberRequest createMemberRequest);
}
