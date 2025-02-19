package com.buck.vsplay.domain.member.mapper;

import com.buck.vsplay.domain.member.dto.MemberDto;
import com.buck.vsplay.domain.member.entity.Address;
import com.buck.vsplay.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member toEntity(MemberDto.MemberInfo dto);
    MemberDto.MemberInfo toDto(Member entity);

    Address toEntity(MemberDto.AddressInfo addressInfo);
    MemberDto.AddressInfo toDto(Address address);

    Member toEntityFromCreateMemberDto(MemberDto.CreateMemberRequest createMemberRequest);
}
