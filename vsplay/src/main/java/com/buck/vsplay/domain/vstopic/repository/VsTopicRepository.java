package com.buck.vsplay.domain.vstopic.repository;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VsTopicRepository extends JpaRepository<VsTopic, Long>, JpaSpecificationExecutor<VsTopic> {

    @Query("SELECT vt FROM VsTopic vt JOIN FETCH vt.tournaments WHERE vt.id = :topicId")
    VsTopic findWithTournamentsByTopicId(@Param("topicId") Long topicId);

}
