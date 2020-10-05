package com.upgrad.quora.service.business;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@Service
public class AnswerServiceImpl {

	@Autowired
	AnswerDao answerDao;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnswerEntity createAnswer(AnswerEntity answerEntity, UserEntity userEntity, QuestionEntity questionEntity)
			throws InvalidQuestionException, SignUpRestrictedException {

		AnswerEntity entity = new AnswerEntity();
		entity.setUuid(UUID.randomUUID().toString());
		entity.setAnswer(answerEntity.getAnswer());
		entity.setDate(ZonedDateTime.now());
		entity.setUser(userEntity);
		entity.setQuestion(questionEntity);
		return answerDao.createAnswer(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnswerEntity editAnswer(AnswerEntity answerEntity, UserEntity userEntity, String content)
			throws InvalidQuestionException, SignUpRestrictedException {
		return answerDao.updateAnswer(answerEntity,content);
	}

	public List<AnswerEntity> getAllAnswersToQuestionId(QuestionEntity questionEntity) throws UserNotFoundException {
		List<AnswerEntity> answerList =  answerDao.getAllAnswersToQuestionId(questionEntity);
		if (null == answerList || answerList.isEmpty()) {
			throw new UserNotFoundException("QUES-001",
					"The question with entered uuid whose details are to be seen does not exist");
		} else {
			return answerList;
		}
	}

}
