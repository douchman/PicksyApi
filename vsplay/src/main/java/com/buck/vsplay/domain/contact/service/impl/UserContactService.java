package com.buck.vsplay.domain.contact.service.impl;

import com.buck.vsplay.domain.contact.dto.UserContactDto;
import com.buck.vsplay.domain.contact.entity.UserContact;
import com.buck.vsplay.domain.contact.mapper.UserContactMapper;
import com.buck.vsplay.domain.contact.repository.UserContactRepository;
import com.buck.vsplay.domain.contact.service.IUserContactService;
import com.buck.vsplay.global.util.request.ClientInfoExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserContactService implements IUserContactService {

    private final UserContactMapper userContactMapper;
    private final UserContactRepository userContactRepository;

    @Override
    public void createUserContact(UserContactDto.ContactCreateRequest request) {
        UserContact userContact = userContactMapper.toEntityFromCreateRequestDto(request);

        userContact.setIpAddress(ClientInfoExtractor.extractIpFromRequest());
        userContact.setUserAgent(ClientInfoExtractor.extractUserAgentFromRequest());
        userContactRepository.save(userContact);
    }
}
