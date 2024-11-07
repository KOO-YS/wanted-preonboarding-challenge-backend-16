package com.wanted.preonboarding.layered.domain.dto.request;

import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class RequestReservation {

    // FIXME : DTO 객체 분리 필요

    // 공연 정보
    private final UUID performanceId;
    private final String performanceName;
    private final int fixedPrice;
    private final int round;
    private final char line;
    private final int seat;

    // 고객 정보
    private final String reservationName;
    private final String reservationPhoneNumber;
    private final int depositAmount;

    private final ReservationStatus reservationStatus; // 예약; 취소;
    private final List<String> appliedPolicies; // ['telecome', 'new_member', 'okcashback', 'happy_point']
}
