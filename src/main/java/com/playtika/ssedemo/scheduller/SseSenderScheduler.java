package com.playtika.ssedemo.scheduller;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.playtika.ssedemo.dto.MyEvent;
import com.playtika.ssedemo.holder.SseEmittersHolder;

@Component
@RequiredArgsConstructor
public class SseSenderScheduler {

    private final SseEmittersHolder sseEmittersHolder;

    @Scheduled(initialDelay = 10, fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        for (String id : sseEmittersHolder.getIds()) {
            sseEmittersHolder.sendToEmitter(id,
                MyEvent.builder()
                    .eventName(MyEvent.class.getName())
                    .uuid(UUID.randomUUID())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        }
    }
}
