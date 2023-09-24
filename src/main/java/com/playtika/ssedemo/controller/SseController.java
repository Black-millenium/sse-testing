package com.playtika.ssedemo.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.playtika.ssedemo.holder.SseEmittersHolder;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmittersHolder sseEmittersHolder;

    @GetMapping(
        value = "/emitter",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter emit() {
        SseEmitter sseEmitter = new SseEmitter();
        sseEmittersHolder.putEmitter(String.valueOf(UUID.randomUUID()), sseEmitter);
        return sseEmitter;
    }
}
