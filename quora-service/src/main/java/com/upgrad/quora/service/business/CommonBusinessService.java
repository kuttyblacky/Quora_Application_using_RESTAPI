package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonBusinessService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private AnswerDao answerDao;

	// Method to get userdetails based on Uuid
	public UserEntity getUser(String Uuid) throws UserNotFoundException {
		UserEntity userDetails = userDao.getUserDetails(Uuid);
		if (userDetails == null) {
			throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
		}
		return userDetails;
	}

	// Method to get user based on AccessToken
	@Transactional(propagation = Propagation.REQUIRED)
	public UserAuthTokenEntity validateUser(String token) throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthEntity = userDao.validateUser(token);
		if (userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		return userAuthEntity;
	}

	// Method to get details of the signed in user
	public UserAuthTokenEntity getCurrentUserDeatils(String userId, String token) throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in question
	public UserAuthTokenEntity getCurrentUserQuestionDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in answer
	public UserAuthTokenEntity getCurrentUserQuestionAnswer(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in question for all user
	public UserAuthTokenEntity getAllUserQuestionDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in question for all user
	public UserAuthTokenEntity getEditUserQuestionDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in question for all user
	public UserAuthTokenEntity getEditUserAnswerDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in question for all user
	public UserAuthTokenEntity getDeleteUserQuestionDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
		}
		return userAuthDetails;
	}
	
	// Method to get details of the signed in question for all user
		public UserAuthTokenEntity getDeleteAnswerDetails(String userId, String token)
				throws AuthorizationFailedException {
			UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
			if (userAuthDetails == null) {
				throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
			}
			return userAuthDetails;
		}

	// Method to get details of the signed in question for all user
	public UserAuthTokenEntity getAllUserByIdQuestionDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002",
					"User is signed out.Sign in first to get all questions posted by a specific user");
		}
		return userAuthDetails;
	}

	// Method to get details of the signed in answer for all user
	public UserAuthTokenEntity getAllAnswerDetails(String userId, String token)
			throws AuthorizationFailedException {
		UserAuthTokenEntity userAuthDetails = userDao.getUserAuthDetails(userId, token);
		if (userAuthDetails == null) {
			throw new AuthorizationFailedException("ATHR-002",
					"User is signed out.Sign in first to get the answers");
		}
		return userAuthDetails;
	}
	
	// Method to get question Details based on questionID
	public QuestionEntity findQuestionById(String questionId) throws AuthorizationFailedException {
		QuestionEntity question = questionDao.findQuestionById(questionId);
		if (question == null) {
			throw new AuthorizationFailedException("QUES-001", "Entered question uuid does not exist");
		}
		return question;
	}

	// Method to get question Details based on questionID
	public AnswerEntity findAnswerById(String answerId) throws AuthorizationFailedException {
		AnswerEntity answer = answerDao.findAnswerById(answerId);
		if (answer == null) {
			throw new AuthorizationFailedException("ANS-001", "Entered answer uuid does not exist");
		}
		return answer;
	}

	// Method to get question Details based on questionID
	public QuestionEntity findQuestionAnswerById(String questionId) throws AuthorizationFailedException {
		QuestionEntity question = questionDao.findQuestionById(questionId);
		if (question == null) {
			throw new AuthorizationFailedException("QUES-001", "The question entered is invalid");
		}
		return question;
	}

	// Method to get question Details based on questionID
	public QuestionEntity findAllAnswersToQuestionId(String questionId) throws AuthorizationFailedException {
		QuestionEntity question = questionDao.findQuestionById(questionId);
		if (question == null) {
			throw new AuthorizationFailedException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
		}
		return question;
	}

}
