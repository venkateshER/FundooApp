package com.bridgeit.label.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.label.model.Label;
import com.bridgeit.note.model.Note;


@Repository
public interface LabelRepositoryInterface extends JpaRepository<Label, Long> {
	Optional<Label> findByLabelName(String name);

	Optional<Label> findByLabelIdAndUserId(long labelId, long userId);
	// Optional<Note> findByLabelNotes(long LabelId);

}
