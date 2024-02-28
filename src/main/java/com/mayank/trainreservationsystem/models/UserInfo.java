package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = Entities.USER_INFO)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(name = Fields.EMAIL_ID)
    private String emailId;

    @Column(name = Fields.FIRSTNAME)
    private String firstName;

    @Column(name = Fields.LASTNAME)
    private String lastName;
}
