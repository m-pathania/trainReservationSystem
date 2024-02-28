package com.mayank.trainreservationsystem.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SeatUnavailableException extends Exception {
    public SeatUnavailableException(String errorMessage) {
        super(errorMessage);
    }
}
