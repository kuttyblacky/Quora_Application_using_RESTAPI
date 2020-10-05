package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_auth")

@NamedQueries(
        {
                @NamedQuery(name = "userByAccessToken", query = "select ut from UserAuthTokenEntity ut where ut.accessToken=:token"),
                @NamedQuery(name = "getUserAuthDetailsByUuid", query = "select ut from UserAuthTokenEntity ut where ut.uuid=:userUuid and ut.accessToken=:token and ut.logoutAt Is Null")

        }
)

public class UserAuthTokenEntity implements Serializable {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAuthTokenEntity)) return false;
        UserAuthTokenEntity that = (UserAuthTokenEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getAccessToken(), that.getAccessToken()) &&
                Objects.equals(getExpiresAt(), that.getExpiresAt()) &&
                Objects.equals(getLoginAt(), that.getLoginAt()) &&
                Objects.equals(getLogoutAt(), that.getLogoutAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUuid(), getUser(), getAccessToken(), getExpiresAt(), getLoginAt(), getLogoutAt());
    }
}
