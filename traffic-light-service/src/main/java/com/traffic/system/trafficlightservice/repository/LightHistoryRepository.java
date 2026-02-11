
package com.traffic.system.trafficlightservice.repository;


import com.traffic.system.trafficlightservice.entity.LightHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightHistoryRepository extends JpaRepository<LightHistory, Integer> {

    Page<LightHistory> findByIntersectionIdName(String intersectionIdName, Pageable pageable);
}
