package com.wanted.preonboarding.ticket.application;

import com.wanted.preonboarding.layered.application.ticketing.v2.policy.FeePolicy;
import com.wanted.preonboarding.layered.application.ticketing.v2.policy.BasicFeePolicy;
import com.wanted.preonboarding.layered.application.ticketing.v2.policy.NewMember;
import com.wanted.preonboarding.layered.application.ticketing.v2.policy.Telecom;
import com.wanted.preonboarding.layered.domain.entity.ticketing.PerformanceSeatInfo;
import com.wanted.preonboarding.layered.infrastructure.repository.PerformanceRepository;
import com.wanted.preonboarding.layered.infrastructure.repository.PerformanceSeatInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TicketSellerTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private PerformanceSeatInfoRepository performanceSeatInfoRepository;

    private final int ticketPrice = 10000;
    private final PriorityQueue<String> queue = new PriorityQueue<>();

    @Test
    @DisplayName("공연 정보 전체 조회")
    public void getAllPerformanceInfoListTests() {
        System.out.println("RESULT => " + performanceRepository.findAll());
    }

    @Test
    @DisplayName("선택 좌석 상태 확인")
    public void checkSeatAvailability() {
        PerformanceSeatInfo seatInfo = performanceSeatInfoRepository.findByPerformanceIdAndRoundAndLineAndSeat(UUID.fromString("3e748812-9ab5-11ef-b010-0242ac120002"), 1, 'A', 3);
        System.out.println(seatInfo);
    }

    @Test
    @DisplayName("공연 입장료 계산")
    public void calculatedFeeTests() {
        int p = new Telecom(
            new NewMember(
                new BasicFeePolicy("None")))
            .calculateFee(10000);
        System.out.println("Performance Fee ---> " + p);
    }

    public FeePolicy create(String type, FeePolicy feePolicy) {
        return switch (type) {
            case "telecom" -> new Telecom(feePolicy);
            case "new_member" -> new NewMember(feePolicy);
            default -> feePolicy;
        };
    }

    @Test
    public void factoryFeePolicy() {
        queue.add("basic");
        List<String> appliedDiscountPolicy = Arrays.asList(new String[]{"telecom", "new_member"});
        queue.addAll(appliedDiscountPolicy);
        FeePolicy appliedPolicy = new BasicFeePolicy("None");
        for(String policy : queue){
            appliedPolicy = create(policy, appliedPolicy);
        }
        System.out.println(appliedPolicy.calculateFee(10000));
    }

    @Test
    public void functionalTests(){

    }


}
