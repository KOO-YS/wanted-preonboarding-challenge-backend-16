package com.wanted.preonboarding.layered.application;

import com.wanted.preonboarding.layered.application.ticketing.v1.Factory;
import com.wanted.preonboarding.layered.application.ticketing.v1.TicketSeller;
import com.wanted.preonboarding.layered.application.ticketing.v3.Client;
import com.wanted.preonboarding.layered.application.ticketing.v3.TicketSellerV3;
import com.wanted.preonboarding.layered.domain.dto.PerformanceInfo;
import com.wanted.preonboarding.layered.domain.dto.Ticket;
import com.wanted.preonboarding.layered.application.ticketing.v3.TicketV3;
import com.wanted.preonboarding.layered.domain.dto.request.RequestReservation;
import com.wanted.preonboarding.layered.domain.entity.ticketing.Reservation;
import com.wanted.preonboarding.layered.infrastructure.repository.PerformanceRepository;
import com.wanted.preonboarding.layered.infrastructure.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final Factory policyFactory;
    private final PerformanceRepository performanceRepository;
    private final ReservationRepository reservationRepository;
    private final TicketSellerV3 ticketSellerV3;

    @Transactional
    public TicketV3 reserveV3(RequestReservation requestReservationInfo) throws Exception {
        Client client = new Client(requestReservationInfo.getReservationName(), requestReservationInfo.getReservationPhoneNumber(), requestReservationInfo.getDepositAmount());
        // TODO : 예약 가능 좌석 확인 및 좌석 예약 완료 처리

        TicketV3 ticket = ticketSellerV3.sellTo(requestReservationInfo);
        client.pay(ticket.getTotalPrice());

        // TODO : 고객 예약 완료 처리
        return ticket;
    }

    @Transactional
    public boolean reserveV1(Ticket ticket) throws ChangeSetPersister.NotFoundException {
        log.info("reserveInfo ID => {}", ticket.getPerformanceId());
        PerformanceInfo performanceInfo = PerformanceInfo.of(performanceRepository.findById(ticket.getPerformanceId())
            .orElseThrow(ChangeSetPersister.NotFoundException::new));
        TicketSeller seller = policyFactory.create(ticket.getAppliedPolicies(), performanceInfo);
        if (performanceInfo.canReservation()) {
            int price = seller.getTotalPrice();
            ticket.setAmount(ticket.getAmount() - price);
            return true;
        } else {
            return false;
        }
    }

    public List<Reservation> getReservation(RequestReservation requestReservationInfo) throws Exception {
        // TODO : 예약자 개인정보를 통한 예약 내역 조회

        return null;
    }
}
