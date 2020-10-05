package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AdminController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    @Autowired
    private AdminBusinessService adminBusinessService;

    // Method to delete the User. Only the Admin User can delete the user
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //The Bearer Authorization Scheme  ideally accepts input as (Bearer <Token>) but the tests are written as Such that it accepts token directly
    public ResponseEntity<UserDeleteResponse> deleteUser(@RequestHeader("authorization") final String authorization, @PathVariable("userId") final String userId) throws AuthorizationFailedException, UserNotFoundException {
        // If the Bearer <token> Scheme needs to be implemented kindly uncomment the below code
        // String accessToken = authorization.split("Bearer")[1].trim();
        //accessToken = accessToken.substring(1, accessToken.length() - 1);
        final UserAuthTokenEntity userAuthTokenEntity = commonBusinessService.validateUser(authorization);
        adminBusinessService.getloggedinUserDeatils(userAuthTokenEntity.getUuid(), authorization);
        final UserEntity userEntity = adminBusinessService.getUserinfo(userId);
        adminBusinessService.deleteUser(userId);
        UserDeleteResponse deleteResponse = new UserDeleteResponse().id("userId").status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(deleteResponse, HttpStatus.OK);

    }
}
