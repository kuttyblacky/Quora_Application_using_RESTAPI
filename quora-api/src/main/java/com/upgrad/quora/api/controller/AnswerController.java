package com.upgrad.quora.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.business.AnswerServiceImpl;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@RestController
@RequestMapping("/")
public class AnswerController {

	@Autowired
	private CommonBusinessService commonBusinessService;

	@Autowired
	AdminBusinessService adminBusinessService;

	@Autowired
	AnswerServiceImpl service;

	@PostMapping(value = "/question/{questionId}/answer/create")
	public ResponseEntity<AnswerResponse> createQuestion(@RequestHeader("authorization") final String authorization,
			AnswerRequest answerRequest, @RequestParam("userId") final String userId,
			@PathVariable("questionId") final String questionId) throws InvalidQuestionException,
			SignUpRestrictedException, AuthorizationFailedException, UserNotFoundException {

		AnswerEntity entity = new AnswerEntity();
		// Validation for Authorizing the signed user
		commonBusinessService.validateUser(authorization);
		UserAuthTokenEntity authTokenEntity = commonBusinessService.getCurrentUserQuestionAnswer(userId, authorization);
		// to get the registered user from the database
		final UserEntity userEntity = commonBusinessService.getUser(userId);
		final QuestionEntity questionEntity = commonBusinessService.findQuestionAnswerById(questionId);
		entity.setAnswer(answerRequest.getAnswer());

		final AnswerEntity createdAnsEntity = service.createAnswer(entity, userEntity, questionEntity);
		AnswerResponse answerResponse = new AnswerResponse().id(createdAnsEntity.getUuid()).status("ANSWER CREATED");
		return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/answer/edit/{answerId}")
	public ResponseEntity<AnswerResponse> editQuestionsById(@RequestHeader("authorization") final String authorization,
			@PathVariable("answerId") final String answerId, @RequestParam("userId") final String userId,
			AnswerEditRequest answerRequest) throws AuthorizationFailedException, UserNotFoundException,
			InvalidQuestionException, SignUpRestrictedException {

		// Validation for Authorizing the signed user
		commonBusinessService.validateUser(authorization);
		UserAuthTokenEntity authTokenEntity = commonBusinessService.getEditUserAnswerDetails(userId, authorization);
		adminBusinessService.getloggedinUserDetailsForDeleteAnswer(authTokenEntity.getUuid(), authorization);
		// to get the registered user from the database
		final UserEntity userEntity = commonBusinessService.getUser(userId);
		final AnswerEntity answerEntity = commonBusinessService.findAnswerById(answerId);

		final AnswerEntity updatedAnsEntity = service.editAnswer(answerEntity, userEntity, answerRequest.getContent());
		AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");

		return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
	}

	@GetMapping(value = "/all/{questionId}")
	public ResponseEntity<List<AnswerDetailsResponse>> getQuestionByUserId(
			@RequestHeader("authorization") final String authorization,
			@PathVariable("questionId") final String questionId, @RequestParam("userId") final String userId)
			throws AuthorizationFailedException, UserNotFoundException {

		List<AnswerDetailsResponse> responseList = new ArrayList<>();

		commonBusinessService.validateUser(authorization);

		UserAuthTokenEntity authTokenEntity = commonBusinessService.getAllAnswerDetails(userId, authorization);
		final QuestionEntity questionEntity = commonBusinessService.findAllAnswersToQuestionId(questionId);
		List<AnswerEntity> listOfAnswers = service.getAllAnswersToQuestionId(questionEntity);

		listOfAnswers.stream().forEach(answer -> {
			AnswerDetailsResponse response = new AnswerDetailsResponse();
			response.setId(answer.getUuid());
			response.setQuestionContent(answer.getQuestion().getContent());
			response.setAnswerContent(answer.getAnswer());
			responseList.add(response);
		});
		return new ResponseEntity<List<AnswerDetailsResponse>>(responseList, HttpStatus.CREATED);

	}

	@DeleteMapping(value = "/answer/delete/{answerId}")
	public ResponseEntity<AnswerDeleteResponse> deleteQuestion(
			@RequestHeader("authorization") final String authorization, @PathVariable("answerId") final String answerId,
			@RequestParam("userId") final String userId) throws AuthorizationFailedException, UserNotFoundException {

		commonBusinessService.validateUser(authorization); // first use case

		UserAuthTokenEntity authTokenEntity = commonBusinessService.getDeleteAnswerDetails(userId, authorization);

		adminBusinessService.getloggedinUserDetailsForDeleteAnswer(authTokenEntity.getUuid(), authorization);

		final UserEntity userEntity = adminBusinessService.getUserinfo(userId);

		final AnswerEntity answerEntity = commonBusinessService.findAnswerById(answerId);
		adminBusinessService.deleteAnswerById(answerEntity);
		AnswerDeleteResponse response = new AnswerDeleteResponse().id(answerEntity.getUuid())
				.status("ANSWER SUCCESSFULLY DELETED");
		return new ResponseEntity<AnswerDeleteResponse>(response, HttpStatus.OK);

	}

}
