package com.playtika.ssedemo.holder;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.playtika.ssedemo.dto.MyEvent;


@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmittersHolder {

    private final Map<String, SseEmitter> holder = new ConcurrentHashMap<>();

    public void putEmitter(String id, SseEmitter emitter) {
        emitter.onTimeout(() -> removeEmitter(id, null, "Timeout"));
        emitter.onCompletion(() -> removeEmitter(id, null, "Completed"));
        emitter.onError(throwable -> removeEmitter(id, throwable, "Error"));

        log.info("Put SseEmitter in holder: {}", id);
        holder.put(id, emitter);
    }

    public Set<String> getIds() {
        return holder.keySet();
    }

    public void removeEmitter(String id, Throwable error, String statusText) {
        holder.remove(id);

        if (error == null) {
            log.warn("Emitter {} {} and deleted from holder", id, statusText);
        } else {
            log.error("Emitter {} {} and deleted from holder", id, statusText, error);
        }
    }

    public void sendToEmitter(String id, Object data) {
        log.info("Sending event to SseEmitter {}", id);

        try {
            holder.get(id).send(SseEmitter.event()
                .id(String.valueOf(UUID.randomUUID()))
                .data(data, MediaType.APPLICATION_JSON)
                .name(MyEvent.class.getName())
                .reconnectTime(TimeUnit.SECONDS.toMillis(1))
            );
        } catch (Throwable e) {
            holder.get(id).completeWithError(e);
        }
    }
}
