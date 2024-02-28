package com.mayank.trainreservationsystem.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class TrainReservationController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
