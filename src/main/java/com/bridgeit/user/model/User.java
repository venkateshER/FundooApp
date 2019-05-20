package com.bridgeit.user.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.bridgeit.label.model.Label;
import com.bridgeit.note.model.Note;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;

	@NotNull
	@Length(max = 12)
	private String firstName;

	@NotNull
	@Length(max = 12)
	private String lastName;

	@NotNull
	@Email
	@Column(unique = true)
	private String emailId;

	@NotNull
	@Length(min = 10, max = 10)
	private String phoneNumber;

	@NotNull
	@Length(min = 6)
	private String password;

	@NotNull
	private String registerStamp;

	@NotNull
	private String updateStamp;

	@NotNull
	private boolean isVerified;

	@NotNull
	private String token;

	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnore
	private List<Note> notes;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnore
	private Set<Label> labels;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collaborator")
	@JsonIgnore
	private Set<Note> collaboNotes;

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public Set<Note> getCollaboNotes() {
		return collaboNotes;
	}

	public void setCollaboNotes(Set<Note> collaboNotes) {
		this.collaboNotes = collaboNotes;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegisterStamp() {
		return registerStamp;
	}

	public void setRegisterStamp(String registerStamp) {
		this.registerStamp = registerStamp;
	}

	public String getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(String updateStamp) {
		this.updateStamp = updateStamp;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", phoneNumber=" + phoneNumber + ", password=" + password + ", registerStamp=" + registerStamp
				+ ", updateStamp=" + updateStamp + ", isVerified=" + isVerified + ", token=" + token + ", image="
				+ image + ", notes=" + notes + ", labels=" + labels + ", collaboNotes=" + collaboNotes + "]";
	}
	
}
