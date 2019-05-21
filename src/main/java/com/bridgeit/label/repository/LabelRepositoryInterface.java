package com.bridgeit.label.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.label.model.Label;
import com.bridgeit.note.model.Note;
import com.bridgeit.user.model.User;


@Repository
public interface LabelRepositoryInterface extends JpaRepository<Label, Long> {

	//Optional<Label> findByLabelIdAndUserId(long labelId, long userId);
	// Optional<Note> findByLabelNotes(long LabelId);
	Optional<Label>findByLabelIdAndUser(long labelId,User user);
	Optional<Note>findByLabelName(String labelName);
}
