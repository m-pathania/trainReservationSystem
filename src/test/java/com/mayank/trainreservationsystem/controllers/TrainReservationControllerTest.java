package com.mayank.trainreservationsystem.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.mayank.trainreservationsystem.TrainReservationSystemApplication;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.stream.IntStream;

import com.mayank.trainreservationsystem.enums.BookingStatus;
import com.mayank.trainreservationsystem.enums.Status;
import com.mayank.trainreservationsystem.models.UserInfo;
import com.mayank.trainreservationsystem.requests.BookingUserInfo;
import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import com.mayank.trainreservationsystem.responses.UserBookingsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    private final String userBookingsEndpoint = "/user_bookings";

    @Test
    void testTicketBooking() {
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

    @Test
    void testUserBookings() {
        String userEmail = "user1@gmail.com";
        var userInfo = BookingUserInfo.builder().firstName("firstName")
                .lastName("lastName").emailId(userEmail).build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-trs-user-email", userEmail);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        URI uri = getUri(userBookingsEndpoint);

        ResponseEntity<UserBookingsResponse> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, UserBookingsResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(userEmail, responseEntity.getBody().getUserEmail());
        Assertions.assertEquals(0, responseEntity.getBody().getBookings().size());

        URI bookTicketUri = getUri(bookTicketEndpoint);
        TicketBookingRequest ticketBookingRequest = TicketBookingRequest.builder().build();
        ticketBookingRequest.setFrom(2L);
        ticketBookingRequest.setTo(8L);
        ticketBookingRequest.setBookingDate(LocalDate.of(2024, Month.FEBRUARY, 1));
        ticketBookingRequest.setPricePaid(BigDecimal.valueOf(20));
        ticketBookingRequest.setUserInfo(userInfo);
        HttpEntity<TicketBookingRequest> bookTicketHttpEntity = new HttpEntity<>(ticketBookingRequest);
        ResponseEntity<TicketBookingResponse> response = testRestTemplate.postForEntity(bookTicketUri, bookTicketHttpEntity, TicketBookingResponse.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(Status.COMPLETED, response.getBody().getStatus());

        ResponseEntity<UserBookingsResponse> responseEntityPostBooking = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, UserBookingsResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntityPostBooking.getStatusCode());
        Assertions.assertNotNull(responseEntityPostBooking.getBody());
        Assertions.assertEquals(userEmail, responseEntityPostBooking.getBody().getUserEmail());
        Assertions.assertEquals(1, responseEntityPostBooking.getBody().getBookings().size());
        Assertions.assertEquals(new BigDecimal("20.00"), responseEntityPostBooking.getBody().getBookings().iterator().next().getAmountPaid());
    }

    private URI getUri(String endpoint) {
        String uri = "http://localhost:" + port + "/train-reservation/v1" + endpoint;
        return URI.create(uri);
    }
}
