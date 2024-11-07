package com.wanted.preonboarding.layered.presentation;

import com.wanted.preonboarding.layered.application.ReservationService;
import com.wanted.preonboarding.layered.application.cancel.CancelService;
import com.wanted.preonboarding.layered.application.ticketing.v3.TicketV3;
import com.wanted.preonboarding.layered.core.domain.response.ResponseHandler;
import com.wanted.preonboarding.layered.domain.dto.request.RequestReservation;
import com.wanted.preonboarding.layered.domain.dto.request.ReservationStatus;
import com.wanted.preonboarding.layered.domain.entity.ticketing.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {
    private final ReservationService reservationService;
    private final CancelService cancelService;

    /**
     * 예약 시스템
     * @param request : 고객 이름, 휴대전화, 잔고 금액, 예약공연ID, 회차, 좌석
     * @return response : 예매완료 공연정보(공연ID, 공연명, 회차, 좌석)
     * 주의 : 예약 결제 시 할인 정책 적용
     * @throws Exception
     */
    @PostMapping("/")
    public ResponseHandler<TicketV3> reservation(@RequestBody RequestReservation request) throws Exception {
        // FIXME : dto <-> entity 변환 mapstruct 로 개선 필요
        TicketV3 ticket = reservationService.reserveV3(RequestReservation.builder()
            .performanceId(request.getPerformanceId())
            .performanceName(request.getPerformanceName())
            .reservationName(request.getReservationName())
            .reservationPhoneNumber(request.getReservationPhoneNumber())
            .reservationStatus(ReservationStatus.RESERVE)
            .depositAmount(request.getDepositAmount())
            .fixedPrice(request.getFixedPrice())
            .round(request.getRound())
            .line(request.getLine())
            .seat(request.getSeat())
            .appliedPolicies(Arrays.asList(new String[]{"telecome"}))
            .build());
        return ResponseHandler.<TicketV3>builder()
            .statusCode(HttpStatus.OK)
            .message("SUCCESS")
            .data(ticket)
            .build();
    }

    /**
     * TODO : 예약 조회 시스템
     * @param request : 고객 정보(고객 이름, 연락처)
     * @return 예매 완료된 공연 정보(공연ID, 공연명, 회차, 좌석정보) + 고객 정보(고객 이름, 연락처)
     */
    @GetMapping("/")
    ResponseHandler<List<Reservation>> getReservation(@RequestBody RequestReservation request) throws Exception {
        List<Reservation> reservations = reservationService.getReservation(RequestReservation.builder()
            .reservationName(request.getReservationName())
            .reservationPhoneNumber(request.getReservationPhoneNumber())
            .build());

        return ResponseHandler.<List<Reservation>>builder()
            .statusCode(HttpStatus.OK)
            .message("SUCCESS")
            .data(reservations)
            .build();
    }

    @GetMapping("/cancel")
    public String cancel(){
        cancelService.cancelTicket();
        return "SUCCESS";
    }
}
