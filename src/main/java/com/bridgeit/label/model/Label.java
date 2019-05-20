package com.bridgeit.label.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "label")
public class Label {

	private String labelName;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long labelId;
	private String createStamp;
	private String updateStamp;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "note_label", joinColumns = @JoinColumn(name = "label_id", referencedColumnName = "labelId"), inverseJoinColumns = @JoinColumn(name = "note_id", referencedColumnName = "noteId"))
	@OnDelete(action = OnDeleteAction.CASCADE)
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
