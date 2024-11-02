package com.wanted.preonboarding.layered.presentation;

import com.wanted.preonboarding.layered.application.ticketing.v3.TicketSellerV3;
import com.wanted.preonboarding.layered.core.domain.response.ResponseHandler;
import com.wanted.preonboarding.layered.domain.dto.PerformanceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("query")
@RequiredArgsConstructor
public class QueryController {
    private final TicketSellerV3 ticketSeller;

    /**
     * TODO : 공연 및 전시 정보 조회
     * (temp)param : 예매 가능 여부
     * @return : 예매 가능한 공연 리스트 (공연명, 회차, 시작 일시, 예매 가능 여부)
     */
    @GetMapping("/all/performance")
    public ResponseEntity<ResponseHandler<List<PerformanceInfo>>> getAllPerformanceInfoList() {
        System.out.println("getAllPerformanceInfoList");
        return ResponseEntity
            .ok()
            .body(ResponseHandler.<List<PerformanceInfo>>builder()
                .message("Success")
                .statusCode(HttpStatus.OK)
                .data(ticketSeller.fetchAllPerformanceInfoList())
                .build()
            );
    }

    /**
     * TODO : 공연 및 전시 정보 조회 - 상세
     * (temp)param : 공연ID 또는 공연명
     * @return : 공연 상세 정보  (공연명, 회차, 시작 일시, 예매 가능 여부)
     */
    @GetMapping("/performance/{keyword}")
    public ResponseEntity<ResponseHandler<PerformanceInfo>> getPerformanceInfo() {
        System.out.println("getAllPerformanceInfoList");
        return ResponseEntity
            .ok()
            .body(ResponseHandler.<PerformanceInfo>builder()
                .message("Success")
                .statusCode(HttpStatus.OK)
                .data(null)
                .build()
            );
    }
}
