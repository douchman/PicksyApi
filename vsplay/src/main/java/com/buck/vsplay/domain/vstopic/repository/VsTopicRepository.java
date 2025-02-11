package com.buck.vsplay.domain.vstopic.repository;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.constants.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VsTopicRepository extends JpaRepository<VsTopic, Long> {

    List<VsTopic> findAllByVisibility(Visibility visibility);
}
