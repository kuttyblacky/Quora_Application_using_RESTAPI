package com.upgrad.quora.service.business;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@Service
public class QuestionServiceImpl {

	@Autowired
	QuestionDao questionDao;

	@Autowired
	UserDao userDao;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QuestionEntity createQuestion(QuestionEntity questionEntity, UserEntity userEntity)
			throws InvalidQuestionException, SignUpRestrictedException {
		QuestionEntity question = new QuestionEntity();
		question.setUuid(UUID.randomUUID().toString());
		question.setContent(questionEntity.getContent());
		question.setDate(ZonedDateTime.now());
		question.setUser(userEntity);
		return questionDao.createQuestion(question);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<QuestionEntity> getAllQuestions() {
		return questionDao.getAllQuestions();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<QuestionEntity> getAllQuestionByUserId(UserEntity userEntity) throws UserNotFoundException {
		List<QuestionEntity> questionList = questionDao.getAllQuestionsByUserId(userEntity);
		if (null == questionList || questionList.isEmpty()) {
			throw new UserNotFoundException("USR-001",
					"User with entered uuid whose question details are to be seen does not exist");
		} else {
			return questionList;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QuestionEntity editQuestion(QuestionEntity questionEntity, UserEntity userEntity, String content)
			throws InvalidQuestionException, SignUpRestrictedException {
		return questionDao.updateQuestionById(questionEntity,content);
	}

}
