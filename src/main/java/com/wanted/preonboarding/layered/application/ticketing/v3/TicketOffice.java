package com.wanted.preonboarding.layered.application.ticketing.v3;

import com.wanted.preonboarding.layered.domain.dto.ReservationInfo;
import com.wanted.preonboarding.layered.domain.dto.NotificationRegister;
import com.wanted.preonboarding.layered.domain.dto.PerformanceDiscountPolicyInfo;
import com.wanted.preonboarding.layered.domain.entity.ticketing.PerformanceSeatInfo;
import com.wanted.preonboarding.layered.domain.entity.ticketing.Reservation;
import com.wanted.preonboarding.layered.infrastructure.repository.PerformanceDiscountPolicyRepository;
import com.wanted.preonboarding.layered.infrastructure.repository.PerformanceRepository;
import com.wanted.preonboarding.layered.domain.dto.PerformanceInfo;
import com.wanted.preonboarding.layered.infrastructure.repository.PerformanceSeatInfoRepository;
import com.wanted.preonboarding.layered.infrastructure.repository.ReservationRepository;
import com.wanted.preonboarding.layered.infrastructure.repository.TicketCancelNotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketOffice {
    private final PerformanceRepository performanceRepository;
    private final PerformanceDiscountPolicyRepository performanceDiscountPolicyRepository;
    private final TicketCancelNotificationRepository ticketCancelNotificationRepository;
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PerformanceSeatInfoRepository performanceSeatInfoRepository;

    public List<PerformanceInfo> getAllPerformanceInfoList() {
        return performanceRepository.findByIsReserve("enable")
            .stream()
            .map(PerformanceInfo::of)
            .toList();
    }

    /**
     * 예약 가능 좌석 확인 및 좌석 예약 완료 처리
     *
     * @param performanceId 공연ID
     * @param round 회차
     * @param line 좌석 열 
     * @param seat 좌석 행
     * @return 좌석 예약 가능 여부
     */
    public boolean checkSeatAvailability(UUID performanceId, int round, char line, int seat) {
        PerformanceSeatInfo seatInfo = performanceSeatInfoRepository.findByPerformanceIdAndRoundAndLineAndSeat(performanceId, round, line, seat);

        if (seatInfo.getIsReserve().equals("enable")) {
            seatInfo.setSeatDisable();
            performanceSeatInfoRepository.save(seatInfo);
            return true;
        }
        return false;

    }

    public List<NotificationRegister> getSubscribers(UUID performanceId) {
        //TODO: 특정 공연 티켓 취소 알림 구독자 가져 오기
        return ticketCancelNotificationRepository.findAllByPerformanceId(performanceId).stream().map(NotificationRegister::of).toList();
    }

    public PerformanceInfo getPerformanceInfo(UUID performanceId) throws ChangeSetPersister.NotFoundException {
        return PerformanceInfo.of(
            performanceRepository.findById(performanceId).orElseThrow(ChangeSetPersister.NotFoundException::new)
        );
    }

    public PerformanceDiscountPolicyInfo getDiscountPolicyInfoBy(UUID performanceId, String policyName) {
        return PerformanceDiscountPolicyInfo.of(performanceDiscountPolicyRepository.findByPerformanceIdAndName(performanceId, policyName));
    }

    @Transactional
    public void ticketCancelBy(ReservationInfo cancelMessage) {
        Reservation reservation = getReservationInfo(cancelMessage);
        reservation.softDelete();
        applicationEventPublisher.publishEvent(cancelMessage);
    }

    private Reservation getReservationInfo(ReservationInfo info){
        return reservationRepository.findReservationByPerformanceIdAndRoundAndGateAndLineAndSeatAndNameAndPhoneNumber(
            info.getPerformanceId(),
            info.getRound(),
            info.getGate(),
            info.getLine(),
            info.getSeat(),
            info.getUserName(),
            info.getPhoneNumber());
    }

}
