package com.mayank.trainreservationsystem.repositories.impl;

import com.mayank.trainreservationsystem.models.UserInfo;
import com.mayank.trainreservationsystem.requests.BookingUserInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserInfoRepository {
    private static final Class<UserInfo> KLASS = UserInfo.class;
    private final EntityManager entityManager;

    private static final String FIND_BY_EMAIL_QUERY = "SELECT ui FROM UserInfo ui WHERE ui.emailId = :email_id";

    @Transactional
    public UserInfo createOrFetchUser(BookingUserInfo bookingUserInfo) {
        try {
            return fetchUserInfo(bookingUserInfo.getEmailId());
        } catch (NoResultException e) {
            var userInfoCreated = UserInfo.builder().lastName(bookingUserInfo.getLastName())
                    .firstName(bookingUserInfo.getFirstName()).emailId(bookingUserInfo.getEmailId()).build();

            entityManager.persist(userInfoCreated);

            return createOrFetchUser(bookingUserInfo);
        }
    }

    public UserInfo fetchUserInfo(String userEmail) {
        TypedQuery<UserInfo> query = entityManager.createQuery(FIND_BY_EMAIL_QUERY, KLASS);
        query.setParameter("email_id", userEmail);

        return query.getSingleResult();
    }
}
