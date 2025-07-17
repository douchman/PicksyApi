package com.buck.vsplay.domain.contact.repository;

import com.buck.vsplay.domain.contact.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

}
