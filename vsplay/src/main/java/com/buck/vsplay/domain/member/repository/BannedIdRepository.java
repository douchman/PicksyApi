package com.buck.vsplay.domain.member.repository;

import com.buck.vsplay.domain.member.entity.BannedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannedIdRepository extends JpaRepository<BannedId, Long> {

    @Query("SELECT b.word FROM BannedId b ")
    List<String> findAllBannedWords();
}
