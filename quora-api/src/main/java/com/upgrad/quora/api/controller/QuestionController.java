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

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.business.QuestionServiceImpl;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@RestController
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private CommonBusinessService commonBusinessService;

	@Autowired
	AdminBusinessService adminBusinessService;

	@Autowired
	QuestionServiceImpl service;

	@PostMapping(value = "/create")
	public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization,
			QuestionRequest questionRequest, @RequestParam("userId") final String userId)
			throws InvalidQuestionException, SignUpRestrictedException, AuthorizationFailedException,
			UserNotFoundException {

		QuestionEntity entity = new QuestionEntity();
		// Validation for Authorizing the signed user
		commonBusinessService.validateUser(authorization);
		UserAuthTokenEntity authTokenEntity = commonBusinessService.getCurrentUserQuestionDetails(userId,
				authorization);
		// to get the registered user from the database
		final UserEntity userEntity = commonBusinessService.getUser(userId);
		entity.setContent(questionRequest.getContent());

		final QuestionEntity createdQuesEntity = service.createQuestion(entity, userEntity);
		QuestionResponse questionResponse = new QuestionResponse().id(createdQuesEntity.getUuid())
				.status("QUESTION CREATED");
		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
	}

	@GetMapping(value = "/all")
	public ResponseEntity<List<QuestionDetailsResponse>> getAllUsers(
			@RequestHeader("authorization") final String authorization, @RequestParam("userId") final String userId)
			throws AuthorizationFailedException, UserNotFoundException {
		List<QuestionDetailsResponse> responseList = new ArrayList<>();

		commonBusinessService.validateUser(authorization);
		UserAuthTokenEntity authTokenEntity = commonBusinessService.getAllUserQuestionDetails(userId, authorization);
		List<QuestionEntity> listOfQuestions = service.getAllQuestions();
		listOfQuestions.stream().forEach(quest -> {
			QuestionDetailsResponse response = new QuestionDetailsResponse();
			response.setId(quest.getUuid());
			response.setContent(quest.getContent());
			responseList.add(response);
		});
		return new ResponseEntity<List<QuestionDetailsResponse>>(responseList, HttpStatus.CREATED);

	}

	@PutMapping(value = "/edit/{questionId}")
	public ResponseEntity<QuestionResponse> editQuestionsById(
			@RequestHeader("authorization") final String authorization,
			@PathVariable("questionId") final String questionId, @RequestParam("userId") final String userId,
			QuestionRequest questionRequest) throws AuthorizationFailedException, UserNotFoundException,
			InvalidQuestionException, SignUpRestrictedException {

		// Validation for Authorizing the signed user
		commonBusinessService.validateUser(authorization);
		UserAuthTokenEntity authTokenEntity = commonBusinessService.getEditUserQuestionDetails(userId, authorization);
		adminBusinessService.getloggedinUserDetailsForEditQuestion(authTokenEntity.getUuid(), authorization);
		// to get the registered user from the database
		final UserEntity userEntity = commonBusinessService.getUser(userId);
		final QuestionEntity questionEntity = commonBusinessService.findQuestionById(questionId);

		final QuestionEntity updatedQuesEntity = service.editQuestion(questionEntity, userEntity,
				questionRequest.getContent());
		QuestionResponse questionResponse = new QuestionResponse().id(updatedQuesEntity.getUuid())
				.status("QUESTION EDITED");

		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/delete/{questionId}")
	public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
			@RequestHeader("authorization") final String authorization,
			@PathVariable("questionId") final String questionId, @RequestParam("userId") final String userId)
			throws AuthorizationFailedException, UserNotFoundException {

		commonBusinessService.validateUser(authorization);
		UserAuthTokenEntity authTokenEntity = commonBusinessService.getDeleteUserQuestionDetails(userId, authorization);
		adminBusinessService.getloggedinUserDetailsForDeleteQuestion(authTokenEntity.getUuid(), authorization);
		final UserEntity userEntity = adminBusinessService.getUserinfo(userId);
		final QuestionEntity questionEntity = commonBusinessService.findQuestionById(questionId);
		adminBusinessService.deleteQuestionById(questionEntity);
		QuestionDeleteResponse deleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid())
				.status("QUESTION SUCCESSFULLY DELETED");
		return new ResponseEntity<QuestionDeleteResponse>(deleteResponse, HttpStatus.OK);

	}

	@GetMapping(value = "/all/{userId}")
	public ResponseEntity<List<QuestionDetailsResponse>> getQuestionByUserId(
			@RequestHeader("authorization") final String authorization, @PathVariable("userId") final String userId)
			throws AuthorizationFailedException, UserNotFoundException {

		List<QuestionDetailsResponse> responseList = new ArrayList<>();

		commonBusinessService.validateUser(authorization);

		UserAuthTokenEntity authTokenEntity = commonBusinessService.getAllUserByIdQuestionDetails(userId,
				authorization);
		final UserEntity userEntity = commonBusinessService.getUser(userId);

		List<QuestionEntity> listOfQuestions = service.getAllQuestionByUserId(userEntity);
		
		listOfQuestions.stream().forEach(quest -> {
			QuestionDetailsResponse response = new QuestionDetailsResponse();
			response.setId(quest.getUuid());
			response.setContent(quest.getContent());
			responseList.add(response);
		});
		return new ResponseEntity<List<QuestionDetailsResponse>>(responseList, HttpStatus.CREATED);

	}

}
