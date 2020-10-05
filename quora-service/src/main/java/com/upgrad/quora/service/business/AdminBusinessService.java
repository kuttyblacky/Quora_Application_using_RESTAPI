package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBusinessService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private AnswerDao answerDao;

	// Method to getUser Details
	public UserEntity getUserinfo(String Uuid) throws UserNotFoundException, AuthorizationFailedException {
		UserEntity userDetails = userDao.getUserDetails(Uuid);
		if (userDetails == null) {
			throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
		}
		return userDetails;
	}

	// Method to getLooged in UserDetails
	public UserAuthTokenEntity getloggedinUserDeatils(String userId, String token) throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out");
		}
		UserEntity loggedUser = userAuthDetails.getUser();
		if (loggedUser.getRole().equalsIgnoreCase("nonadmin")) {
			throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
		}
		return userAuthDetails;
	}

	// Method to getLoggedInfor in UserDetails Question
	public UserAuthTokenEntity getloggedinUserDetailsForDeleteQuestion(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out");
		}
		UserEntity loggedUser = userAuthDetails.getUser();
		if (loggedUser.getRole().equalsIgnoreCase("nonadmin")) {
			throw new AuthorizationFailedException("ATHR-003",
					"Only the question owner or admin can delete the question");
		}
		return userAuthDetails;
	}

	// Method to getLoggedInfor in UserDetails edit the question
	public UserAuthTokenEntity getloggedinUserDetailsForEditQuestion(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out");
		}
		UserEntity loggedUser = userAuthDetails.getUser();
		if (loggedUser.getRole().equalsIgnoreCase("nonadmin")) {
			throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
		}
		return userAuthDetails;
	}

	// Method to getLoggedInfor for UserDetails delete Answer
	public UserAuthTokenEntity getloggedinUserDetailsForDeleteAnswer(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out");
		}
		UserEntity loggedUser = userAuthDetails.getUser();
		if (loggedUser.getRole().equalsIgnoreCase("nonadmin")) {
			throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
		}
		return userAuthDetails;
	}

	// Method to getLoggedInfor for UserDetails edit Answer
	public UserAuthTokenEntity getloggedinUserDetailsForEditAnswer(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out");
		}
		UserEntity loggedUser = userAuthDetails.getUser();
		if (loggedUser.getRole().equalsIgnoreCase("nonadmin")) {
			throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
		}
		return userAuthDetails;
	}

	// Method to delete User
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(String Userid) {
		userDao.deleteUserInfo(Userid);
	}

	// Method to delete Question
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteQuestionById(QuestionEntity questionEntity) {
		questionDao.deleteQuestionById(questionEntity);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAnswerById(AnswerEntity answerEntity) {
		answerDao.deleteAnswerById(answerEntity);

	}
}
