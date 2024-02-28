package com.mayank.trainreservationsystem.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.mayank.trainreservationsystem.TrainReservationSystemApplication;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.IntStream;

import com.mayank.trainreservationsystem.enums.Status;
import com.mayank.trainreservationsystem.models.UserInfo;
import com.mayank.trainreservationsystem.requests.BookingUserInfo;
import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest(classes = TrainReservationSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainReservationControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private long port;

    private final String bookTicketEndpoint = "/book_ticket";

    @Test
    void testPing() {
        URI uri = getUri(bookTicketEndpoint);
        TicketBookingRequest ticketBookingRequest = TicketBookingRequest.builder().build();
        HttpEntity<TicketBookingRequest> httpEntity = new HttpEntity<>(ticketBookingRequest);
        ResponseEntity<Object> response = testRestTemplate.postForEntity(uri, httpEntity, Object.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ticketBookingRequest.setFrom(6L);
        ticketBookingRequest.setTo(12L);
        ticketBookingRequest.setBookingDate(LocalDate.of(2024, Month.FEBRUARY, 1));
        ticketBookingRequest.setPricePaid(BigDecimal.valueOf(400));

        IntStream.range(0, 60).forEach(i -> {
            ticketBookingRequest.setUserInfo(BookingUserInfo.builder().firstName("firstName" + i + 1)
                    .lastName("lastName" + i + 1).emailId("email" + i + 1 + "@gmail.com").build());

            ResponseEntity<TicketBookingResponse> responseEntity = testRestTemplate.postForEntity(uri, httpEntity,
                    TicketBookingResponse.class);
            log.info("iteration : {}; response : {};", i, responseEntity);
            Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            Assertions.assertNotNull(responseEntity.getBody());
            Assertions.assertEquals(Status.COMPLETED, responseEntity.getBody().getStatus());
        });

        ticketBookingRequest.setUserInfo(BookingUserInfo.builder().firstName("firstName001").lastName("lastName001")
                .emailId("email001@gmail.com").build());

        ResponseEntity<TicketBookingResponse> responseEntity = testRestTemplate.postForEntity(uri, httpEntity,
                TicketBookingResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(Status.FAILED, responseEntity.getBody().getStatus());

        ticketBookingRequest.setFrom(3L);
        ticketBookingRequest.setTo(12L);
        ticketBookingRequest.setBookingDate(LocalDate.of(2024, Month.FEBRUARY, 1));
        ticketBookingRequest.setPricePaid(BigDecimal.valueOf(400));

        ResponseEntity<TicketBookingResponse> responseEntity1 = testRestTemplate.postForEntity(uri, httpEntity,
                TicketBookingResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        Assertions.assertNotNull(responseEntity1.getBody());
        Assertions.assertEquals(Status.FAILED, responseEntity1.getBody().getStatus());

        ticketBookingRequest.setFrom(3L);
        ticketBookingRequest.setTo(6L);
        ticketBookingRequest.setBookingDate(LocalDate.of(2024, Month.FEBRUARY, 1));
        ticketBookingRequest.setPricePaid(BigDecimal.valueOf(400));

        ResponseEntity<TicketBookingResponse> responseEntity2 = testRestTemplate.postForEntity(uri, httpEntity,
                TicketBookingResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        Assertions.assertNotNull(responseEntity2.getBody());
        Assertions.assertEquals(Status.COMPLETED, responseEntity2.getBody().getStatus());
    }

    private URI getUri(String endpoint) {
        String uri = "http://localhost:" + port + "/train-reservation/v1" + endpoint;
        return URI.create(uri);
    }
}
