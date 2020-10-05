package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    //Method to signup and persit userAuthentication detail in Database
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        String[] encrytpedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encrytpedText[0]);
        userEntity.setPassword(encrytpedText[1]);
        UserEntity createdUser = new UserEntity();
        return userDao.createUser(userEntity);

    }
}