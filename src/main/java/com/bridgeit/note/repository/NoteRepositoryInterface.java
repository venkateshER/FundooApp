package com.bridgeit.note.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.note.model.Note;
import com.bridgeit.user.model.User;

@Repository
public interface NoteRepositoryInterface extends JpaRepository<Note, Long> {
	//Optional<Note> findByNoteIdAndUserId(long noteId, long userId);
	Optional<Note> findByNoteIdAndUser(long noteId,User user);
	Optional<User> findByNoteId(long noteId);
	Optional<Note> findByNoteIdAndLabelList(long noteId,long labelId);
	Optional<Note> findByCollaboratorAndUser(long noteId,User user);
	// Optional<Note> findByNoteIdAndUser(long noteId,long userId);
	// List<Note>findByUserId(long id);
}
