package com.wanted.preonboarding.layered.application.cancel.v1;

import com.wanted.preonboarding.layered.domain.dto.ReservationInfo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TicketCancelNotification implements Observable<ReservationInfo> {
    private final Set<Observer<ReservationInfo>> subscribers;
    private ReservationInfo info;

    public void getMessage(ReservationInfo info){
        this.info = info;
    }

    @Override
    public boolean register(Observer<ReservationInfo> o) {
        // TODO: Insert into Table subscribe info
        return subscribers.add(o);
    }

    @Override
    public boolean remove(Observer o) {
        // TODO: Delete into Table subscribe info
        return subscribers.remove(o);
    }

    /**
     * TODO : 예약 가능 알림 서비스
     * 특정 공연에 대해서 취소 건이 발생하는 경우, 알림 신청을 해놓은 고객에게 취소된 예약이 있다는 사실을 알리는 알림 서비스
     * @return 예매 가능 공연 정보(공연ID, 공연명, 회차, 시작 일시 예매 가능한 좌석 정보)
     */
    @Override
    public boolean sendMessage() {
        // TODO: Called from Ticket Cancel Service
        return subscribers.stream().filter(observer -> {
            try {
                return observer.update(info);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).count() == subscribers.size();
    }

}
