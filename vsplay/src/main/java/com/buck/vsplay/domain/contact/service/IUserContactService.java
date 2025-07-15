package com.buck.vsplay.domain.contact.service;

import com.buck.vsplay.domain.contact.dto.UserContactDto;

public interface IUserContactService {
    void createUserContact(UserContactDto.ContactCreateRequest request);
}
