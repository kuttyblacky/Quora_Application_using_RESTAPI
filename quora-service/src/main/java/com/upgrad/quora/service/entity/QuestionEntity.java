package com.upgrad.quora.service.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "question")
@NamedQueries({ 
	@NamedQuery(name = "findAllQuestions", query = "select u from QuestionEntity u where u.user is not null"),
	@NamedQuery(name = "getQuestionByUuid", query = "select u from QuestionEntity u where u.uuid =:uuid"),
	@NamedQuery(name = "getQuestionsByUserId", query = "select u from QuestionEntity u where u.user =:user"),
	@NamedQuery(name = "updateQuestion", query = "update QuestionEntity u SET u.content=:content where u.uuid =:uuid")
	
	})
public class QuestionEntity implements Serializable {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "uuid")
	@Size(max = 64)
	private String uuid;

	@Column(name = "content")
	@Size(max = 500)
	private String content;

	@Column(name = "date")
	@NotNull
	private ZonedDateTime date;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof QuestionEntity))
			return false;
		QuestionEntity that = (QuestionEntity) o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getUuid(), that.getUuid())
				&& Objects.equals(getContent(), that.getContent()) && Objects.equals(getDate(), that.getDate())
				&& Objects.equals(getUser(), that.getUser());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getUuid(), getContent(), getDate(), getUser());
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
