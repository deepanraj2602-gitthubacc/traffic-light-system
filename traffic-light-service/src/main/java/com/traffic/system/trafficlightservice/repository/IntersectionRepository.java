
package com.traffic.system.trafficlightservice.repository;


import com.traffic.system.trafficlightservice.entity.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntersectionRepository extends JpaRepository<Intersection, Integer> {

    boolean existsByIdName(String idName);

    Optional<Intersection> findByIdName(String idName);
}
