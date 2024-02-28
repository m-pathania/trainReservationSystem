package com.mayank.trainreservationsystem.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.mayank.trainreservationsystem.TrainReservationSystemApplication;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest(classes = TrainReservationSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainReservationControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private long port;

    private final String pingEndpoint = "/ping";

    @Test
    void testPing() {
        URI uri = getUri(pingEndpoint);
        ResponseEntity<String> response = testRestTemplate.getForEntity(uri, String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("pong", response.getBody());
    }

    private URI getUri(String endpoint) {
        String uri = "http://localhost:" + port + "/train-reservation/v1" + endpoint;
        return URI.create(uri);
    }
}
