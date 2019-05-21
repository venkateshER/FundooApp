package com.bridgeit.note.model;

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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.bridgeit.label.model.Label;
import com.bridgeit.user.model.User;

@Entity
//@Table(name = "note")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long noteId;
	private String title;
	private String description;
	private boolean trash;
	private boolean archive;
	private boolean isPin;
	private String color;
	private String createTime;
	private String updateTime;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToMany(fetch=FetchType.LAZY,cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name="note_label",joinColumns= {@JoinColumn(name="note_id")},inverseJoinColumns= {@JoinColumn(name="label_id")})
	private Set<Label> labels;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "NoteCollaborator", joinColumns = @JoinColumn(name = "note_id", referencedColumnName = "noteId"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"))
	private Set<User> collaborator;

	
	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public Set<User> getCollaborator() {
		return collaborator;
	}

	public void setCollaborator(Set<User> collaborator) {
		this.collaborator = collaborator;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isTrash() {
		return trash;
	}

	public void setTrash(boolean trash) {
		this.trash = trash;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean isPin() {
		return isPin;
	}

	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", title=" + title + ", description=" + description + ", trash=" + trash
				+ ", archive=" + archive + ", isPin=" + isPin + ", color=" + color + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", user=" + user + ", labels=" + labels + ", collaborator="
				+ collaborator + "]";
	}
	

}
