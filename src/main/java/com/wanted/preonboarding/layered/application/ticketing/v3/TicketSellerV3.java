package com.wanted.preonboarding.layered.application.ticketing.v3;

import com.wanted.preonboarding.layered.domain.dto.PerformanceInfo;
import com.wanted.preonboarding.layered.domain.dto.request.RequestReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketSellerV3 {
    private final TicketOffice ticketOffice;
    private final TicketFactory ticketFactory;

    public List<PerformanceInfo> fetchAllPerformanceInfoList() {
        return ticketOffice.getAllPerformanceInfoList();
    }

    public TicketV3 sellTo(RequestReservation requestReservationInfo) throws Exception {
        boolean availability = ticketOffice.checkSeatAvailability(requestReservationInfo.getPerformanceId(), requestReservationInfo.getRound(), requestReservationInfo.getLine(), requestReservationInfo.getSeat());
        if (availability)
            return ticketFactory.ticketing(requestReservationInfo)
                .calculateTotalFee();
        else
            throw new Exception(); // FIXME : 불가능 좌석
    }
}
