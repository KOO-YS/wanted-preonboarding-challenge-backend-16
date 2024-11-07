package com.wanted.preonboarding.layered.infrastructure.repository;

import com.wanted.preonboarding.layered.domain.entity.ticketing.PerformanceSeatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PerformanceSeatInfoRepository extends JpaRepository<PerformanceSeatInfo, Integer> {

    PerformanceSeatInfo findByPerformanceIdAndRoundAndLineAndSeat(UUID performanceId, int round, char line, int seat);
}
