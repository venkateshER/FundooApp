package com.bridgeit.label.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.bridgeit.note.model.Note;
import com.bridgeit.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Label {

	private String labelName;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long labelId;
	private String createStamp;
	private String updateStamp;
	private long userId;

	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},mappedBy = "labels")
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@JsonIgnore
	private List<Note> notes;
	
	

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public long getLabelId() {
		return labelId;
	}

	public void setLabelId(long labelId) {
		this.labelId = labelId;
	}

	public String getCreateStamp() {
		return createStamp;
	}

	public void setCreateStamp(String createStamp) {
		this.createStamp = createStamp;
	}

	public String getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(String updateStamp) {
		this.updateStamp = updateStamp;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Label [labelName=" + labelName + ", labelId=" + labelId + ", createStamp=" + createStamp
				+ ", updateStamp=" + updateStamp + ", userId=" + userId + ", notes=" + notes + "]";
	}

	public long getUserId() {
		return userId;
	}

	
	
}
