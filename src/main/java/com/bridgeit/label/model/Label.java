package com.bridgeit.label.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bridgeit.note.model.Note;
import com.bridgeit.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "label")
public class Label {

	private String labelName;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long labelId;
	private String createStamp;
	private String updateStamp;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@JoinColumn(name = "noteId")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Note> noteList;

	@Override
	public String toString() {
		return "Label [labelName=" + labelName + ", labelId=" + labelId + ", createStamp=" + createStamp
				+ ", updateStamp=" + updateStamp + ", user=" + user + ", noteList=" + noteList + "]";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Note> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<Note> noteList) {
		this.noteList = noteList;
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

}
