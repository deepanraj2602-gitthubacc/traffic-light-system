
package com.traffic.system.trafficlightservice.repository;


import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IntersectionRepository extends JpaRepository<Intersection, Integer> {

    boolean existsByIdName(String idName);

    Optional<Intersection> findByIdName(String idName);

    List<Intersection> findByAutoRunStatus(IntersectionAutoRunStatus autoRunStatus);
}
