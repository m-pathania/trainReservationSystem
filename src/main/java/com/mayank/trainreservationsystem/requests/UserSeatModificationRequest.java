package com.mayank.trainreservationsystem.requests;

import com.mayank.trainreservationsystem.enums.TrainSection;
import lombok.Data;

@Data
public class UserSeatModificationRequest {
    private String userEmail;
    private TrainSection toTrainSection;
    private String toSeatNumber;
}
