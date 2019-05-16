package com.bridgeit.note.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgeit.label.model.Label;
import com.bridgeit.label.repository.LabelRepositoryInterface;
import com.bridgeit.note.dto.NoteDto;
import com.bridgeit.note.model.Note;
import com.bridgeit.note.repository.NoteRepositoryInterface;

import com.bridgeit.user.model.User;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.EmailSenderUtil;
import com.bridgeit.utility.EncryptUtil;
import com.bridgeit.utility.Response;
import com.bridgeit.utility.ResponseUtil;
import com.bridgeit.utility.TokenUtil;
import com.bridgeit.utility.Utility;

@Service
@PropertySource("classpath:error.properties")
@PropertySource("classpath:message.properties")
public class NoteService implements NoteServiceInterface {

	@Autowired
	private UserRepositoryInterface userRepository;
	@Autowired
	private NoteRepositoryInterface noteRepository;
	@Autowired
	private LabelRepositoryInterface labelRespository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Response create(NoteDto noteDto, String token) {

		long userid = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userid).isPresent();
		if (!isuser) {
			Response response = ResponseUtil.getResponse(204, "User not found");
			return response;
		}
		else
		{
		User user = userRepository.findById(userid).get();
		Note note = modelMapper.map(noteDto, Note.class);

		note.setCreateTime(Utility.todayDate());
		note.setUpdateTime(Utility.todayDate());
		note.setArchive(false);
		note.setColor("white");
		note.setPin(false);
		note.setTrash(false);
		note.setUser(user);
		user.getNotes().add(note);
//		user.getNotes().add(note);
		// noteRepository.save(note);
		// user.getNotes().add(note);
		//noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, "note complete");
		return response;
		}
	}

	@Override
	public Response delete(long noteId, String token) {
		long userId = TokenUtil.verifyToken(token);
		User user=userRepository.findById(userId).get();
		Note note=noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId,user).isPresent();

		if (!isNote) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		}
		note.setTrash(true);
		noteRepository.delete(note);
		Response response = ResponseUtil.getResponse(200, "note delete complete");
		return response;
	}

	@Override
	public Response update(long noteId, NoteDto noteDto, String token) {
		Long userId = TokenUtil.verifyToken(token);
		// Optional<Note> noteRepo=noteRepository.findByNoteIdAndUser(id,userId);
		User user=userRepository.findById(userId).get();
		Note note=noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId,user).isPresent();
		
		if (!isNote) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		}
		// Note note=modelMapper.map(source, destination);
		//Note note = noteRepo.get();
		note.setUpdateTime(Utility.todayDate());
		note.setTitle(noteDto.getTitle());
		note.setDescription(noteDto.getDescription());
		//User user = userRepository.findById(userId).get();
		user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, "note update complete");
		return response;
	}

	@Override
	public Response pin(String token, long noteId) {

		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		if (note.isPin() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setPin(true);
			User user = userRepository.findById(userId).get();
			// user.getNotes().add(note);
			noteRepository.save(note);
			userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, "note update complete");
			return response;
		} else {
			Response response = ResponseUtil.getResponse(200, "note pin already presented");
			return response;
		}
	}

	@Override
	public Response color(String token, long noteId, String color) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		}
		Note note = noteRepository.findById(noteId).get();

		note.setUpdateTime(Utility.todayDate());
		note.setColor(color);
		User user = userRepository.findById(userId).get();
		// user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200,"note update");
		return response;
	}

	@Override
	public Response trash(String token, long noteId) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		if (note.isTrash() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setTrash(true);
			User user = userRepository.findById(userId).get();
			// user.getNotes().add(note);
			noteRepository.save(note);
			userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, "note update complete");
			return response;
		} else {
			Response response = ResponseUtil.getResponse(200, "already present");
			return response;
		}
	}

	@Override
	public Response archive(String token, long noteId) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();

		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		if (note.isArchive() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setArchive(true);
			User user = userRepository.findById(userId).get();
			// user.getNotes().add(note);
			noteRepository.save(note);
			userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, "note update complete");
			return response;
		} else {
			Response response = ResponseUtil.getResponse(200, "already present");
			return response;
		}
	}

	@Override
	public List<Note> getAllNotes(String token) {
		long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		List<Note> listNotes = user.getNotes();
		return listNotes;

	}

	@Override
	public Response addLabels(long noteId, String token, long labelId) {
		long uId = TokenUtil.verifyToken(token);

//		boolean isUser = userRepository.findById(uId).isPresent();
//		boolean isLabel=labelRespository.findById(labelId).isPresent();
//		boolean isNote = noteRepository.findById(noteId).isPresent();
//		
//			
//		if (!(isUser && isNote && isLabel)) {
//			Response response = ResponseUtil.getResponse(204, "not found");
//			return response;
		boolean isUser = userRepository.findById(uId).isPresent();
		User user=userRepository.findById(uId).get();
		Note note=noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId,user).isPresent();
		boolean isLabel = labelRespository.findByLabelIdAndUser(labelId, user).isPresent();
		if (!(isUser && isLabel && isNote)) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		} else {
			//Note note=noteRepository.findById(noteId).get();
			Label label =labelRespository.findById(labelId).get();
			note.setUpdateTime(Utility.todayDate());
			label.getNoteList().add(note);
			noteRepository.save(note);
			labelRespository.save(label);

			Response response = ResponseUtil.getResponse(200, "Create complete");
			return response;
		}
	}

	@Override
	public Response removeLabels(long noteId, String token, long labelId) {
		long uId = TokenUtil.verifyToken(token);

		boolean isUser = userRepository.findById(uId).isPresent();
		User user=userRepository.findById(uId).get();
		Note note=noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId,user).isPresent();
		boolean isLabel = labelRespository.findByLabelIdAndUser(labelId, user).isPresent();
		if (!(isUser && isLabel && isNote)) {
			Response response = ResponseUtil.getResponse(204, "not found");
			return response;
		} else {
			Label label = labelRespository.findById(labelId).get();
			note.setUpdateTime(Utility.todayDate());
			label.getNoteList().remove(note);
			noteRepository.save(note);
			labelRespository.save(label);
			Response response = ResponseUtil.getResponse(200, "Delete complete");
			return response;
		}

	}

}
