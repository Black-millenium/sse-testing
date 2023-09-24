package com.playtika.ssedemo;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import com.playtika.ssedemo.dto.MyEvent;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SseDemoApplicationTests {



    @LocalServerPort
    protected int serverPort;

    @Test
    public void testEmitter() {
        log.info("Start request to the server");

        consumeSse(getEmitter());
        consumeSse(getEmitter());
        consumeSse(getEmitter());

        log.info("Finish request to the server");
    }

    private WebTestClient.RequestHeadersSpec<?> getEmitter() {
        return WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + serverPort)
            .responseTimeout(Duration.of(2, ChronoUnit.MINUTES))
            .build()
            .get()
            .uri("/emitter")
            .accept(MediaType.TEXT_EVENT_STREAM);
    }

    private void consumeSse(WebTestClient.RequestHeadersSpec<?> emitter) {
        emitter
            .exchange()
            .expectStatus().isOk()
            .returnResult(MyEvent.class)
            .getResponseBody()
            .subscribe(result -> log.info("========>>> RECEIVED VIA SSE: {}", result));
    }
}
