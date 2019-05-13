package com.bridgeit.label.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bridgeit.note.model.Note;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name="label")
public class Label {
	
	private String labelName;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long labelId;
	private long userId;
	private String createStamp;
	private String updateStamp;
	private long noteId;
	
	public long getNoteId() {
		return noteId;
	}
	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JsonIgnore
	private List<Note> noteList;
	
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	@Override
	public String toString() {
		return "Label [labelName=" + labelName + ", labelId=" + labelId + ", userId=" + userId + ", createStamp=" + createStamp + ", updateStamp=" + updateStamp + "]";
	}
	

}
