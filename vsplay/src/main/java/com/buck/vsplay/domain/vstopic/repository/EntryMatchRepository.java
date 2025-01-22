package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EntryMatchRepository extends JpaRepository<EntryMatch, Long> {
}
