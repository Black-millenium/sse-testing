package com.playtika.ssedemo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record MyEvent(String eventName, LocalDateTime timestamp, UUID uuid) {
}
