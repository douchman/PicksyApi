package com.buck.vsplay.domain.contact.mapper;

import com.buck.vsplay.domain.contact.dto.UserContactDto;
import com.buck.vsplay.domain.contact.entity.UserContact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserContactMapper {
    UserContact toEntityFromCreateRequestDto(UserContactDto.ContactCreateRequest contactCreateRequest);
}
