package com.mayank.trainreservationsystem.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingUserInfo {
    private String firstName;
    private String lastName;
    @NotBlank
    private String email;
}