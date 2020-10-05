package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;


@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {
        try {
            entityManager.persist(userEntity);
            return userEntity;
        } catch (PersistenceException ex) {
            Throwable t = ex.getCause();

            if (t instanceof ConstraintViolationException) {

                if (((ConstraintViolationException) t).getConstraintName().toString().equalsIgnoreCase("users_username_key")) {
                    throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
                } else if (((ConstraintViolationException) t).getConstraintName().toString().equalsIgnoreCase("users_email_key")) {
                    throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
                }
            }
            return null;
        }
    }

    public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByusername", UserEntity.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthTokenEntity validateUser(final String token) {
        try {
            return entityManager.createNamedQuery("userByAccessToken", UserAuthTokenEntity.class).setParameter("token", token).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public void persisAuthtokenEntity(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
    }

    public UserEntity getUserDetails(String id) {
        try {
            return entityManager.createNamedQuery("getUserByUuid", UserEntity.class).setParameter("uuid", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthTokenEntity getUserAuthDetails(String userUuid, String token) {
        try {
            return entityManager.createNamedQuery("getUserAuthDetailsByUuid", UserAuthTokenEntity.class).setParameter("userUuid", userUuid).setParameter("token", token).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void deleteUserInfo(String Uuid) {

        UserEntity deletedUser = entityManager.createNamedQuery("getUserByUuid", UserEntity.class).setParameter("uuid", Uuid).getSingleResult();
        entityManager.remove(deletedUser);

    }
}




