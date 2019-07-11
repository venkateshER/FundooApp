package com.bridgeit.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.user.model.User;


@Repository
public interface UserRepositoryInterface extends JpaRepository<User, Long> {
	Optional<User> findByEmailId(String email);
//	Optional<User> findByNotes(long id);
	//Optional<User>findByCollaboNotesAndUserId(long noteId,long userId);
//	void save(Optional<User> user);
//
//	void delete(Optional<User> user);
}
