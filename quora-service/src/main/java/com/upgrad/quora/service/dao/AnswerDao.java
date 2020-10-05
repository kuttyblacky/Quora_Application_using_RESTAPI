package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;

@Repository
public class AnswerDao {
	@PersistenceContext
	private EntityManager entityManager;

	public AnswerEntity createAnswer(AnswerEntity answerEntity) throws SignUpRestrictedException {
		try {
			entityManager.persist(answerEntity);
			return answerEntity;
		} catch (PersistenceException ex) {
			Throwable t = ex.getCause();

			if (t instanceof ConstraintViolationException) {

				if (((ConstraintViolationException) t).getConstraintName().toString()
						.equalsIgnoreCase("users_username_key")) {
					throw new SignUpRestrictedException("SGR-001",
							"Try any other Username, this Username has already been taken");
				} else if (((ConstraintViolationException) t).getConstraintName().toString()
						.equalsIgnoreCase("users_email_key")) {
					throw new SignUpRestrictedException("SGR-002",
							"This user has already been registered, try with any other emailId");
				}
			}
			return null;
		}
	}

	public QuestionEntity updateAnswerById(QuestionEntity question) {
		try {
			Query qu = entityManager.createQuery("update QuestionEntity u SET u.content=:content where u.uuid =:uuid");
			qu.setParameter("content", question.getContent());
			qu.setParameter("uuid", question.getUuid());
			int result = qu.executeUpdate();
			return null;
		} catch (NoResultException nre) {
			return null;
		}
	}

	public AnswerEntity findAnswerById(String answerId) {
		try {
			return entityManager.createNamedQuery("getAnswerByUuid", AnswerEntity.class).setParameter("uuid", answerId)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public AnswerEntity updateAnswer(AnswerEntity answer, String content) {
		AnswerEntity answerQuestion = entityManager.find(AnswerEntity.class, answer.getId());
		answerQuestion.setAnswer(content);
		return entityManager.merge(answerQuestion);
	}

	public List<AnswerEntity> getAllAnswersToQuestionId(QuestionEntity question) {
		try {
			return entityManager.createNamedQuery("getAllAnswersToQuestionId", AnswerEntity.class)
					.setParameter("question", question).getResultList();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public void deleteAnswerById(AnswerEntity answerEntity) {
		AnswerEntity answer = entityManager.find(AnswerEntity.class, answerEntity.getId());
		entityManager.remove(answer);

	}
}
