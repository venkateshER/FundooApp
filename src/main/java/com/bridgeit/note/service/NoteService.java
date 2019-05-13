package com.bridgeit.note.service;

import java.util.ArrayList;
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
import com.bridgeit.user.model.Response;
import com.bridgeit.user.model.User;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.EmailSenderUtil;
import com.bridgeit.utility.EncryptUtil;
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
	@Autowired
	private EmailSenderUtil emailSender;
	@Autowired
	private EncryptUtil encryptUtil;
	@Autowired
	private Environment env;

	public Response create(NoteDto noteDto, String token) {

		long id = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(id).isPresent();
		if (!isuser) {
			Response response = ResponseUtil.getResponse(204, token, "User not found");
			return response;
		}

		User user = userRepository.findById(id).get();
		Note note = modelMapper.map(noteDto, Note.class);

		note.setCreateTime(Utility.todayDate());
		note.setUpdateTime(Utility.todayDate());
		note.setUserId(id);
		note.setArchive(false);
		note.setColor("white");
		note.setPin(false);
		note.setTrash(false);
		user.getNotes().add(note);
//		user.getNotes().add(note);
		// noteRepository.save(note);
		// user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, token, "note complete");
		return response;
	}

	public Response delete(long id, String token) {
		long userId = TokenUtil.verifyToken(token);
		// boolean isnote=noteRepository.findById(id).isPresent();
		// Optional<Note> noteRepo = noteRepository.findByNoteIdAndUser(id,userId);
		Optional<Note> noteRepo = noteRepository.findByNoteIdAndUserId(id, userId);
		boolean isnote = noteRepo.isPresent();

		if (!isnote) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}

		// Note note = noteRepo.get();
		Note note = noteRepository.findById(id).get();
		note.setTrash(true);
		noteRepository.delete(note);
		// noteRepository.deleteById(id);
		Response response = ResponseUtil.getResponse(200, token, "note delete complete");
		// noteRepository.deleteById(id);
		return response;
	}

	public Response update(long id, NoteDto noteDto, String token) {
		Long userId = TokenUtil.verifyToken(token);
		// Optional<Note> noteRepo=noteRepository.findByNoteIdAndUser(id,userId);
		Optional<Note> noteRepo = noteRepository.findByNoteIdAndUserId(id, userId);
		boolean isnote = noteRepo.isPresent();
		if (!isnote) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}
		// Note note=modelMapper.map(source, destination);
		Note note = noteRepo.get();
		note.setUpdateTime(Utility.todayDate());
		note.setTitle(noteDto.getTitle());
		note.setDescription(noteDto.getDescription());
		User user = userRepository.findById(userId).get();
		user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, token, "note update complete");
		return response;
	}

	public Response pin(String token, long noteId) {

		long userId = TokenUtil.verifyToken(token);
		boolean isuser=userRepository.findById(userId).isPresent();
		boolean isnote=noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}
		Note note=noteRepository.findById(noteId).get();
		if(note.isPin()==false)
		{
		note.setUpdateTime(Utility.todayDate());
		note.setPin(true);
		User user = userRepository.findById(userId).get();
		//user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, token, "note update complete");
		return response;
		}
		else
		{
			Response response = ResponseUtil.getResponse(200, token, "note pin already presented");
			return response;
		}
	}

	public Response trash(String token, long noteId) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser=userRepository.findById(userId).isPresent();
		boolean isnote=noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}
		Note note=noteRepository.findById(noteId).get();
		if(note.isTrash()==false)
		{
		note.setUpdateTime(Utility.todayDate());
		note.setTrash(true);
		User user = userRepository.findById(userId).get();
		//user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, token, "note update complete");
		return response;
		}
		else
		{
			Response response = ResponseUtil.getResponse(200, token, "already presente");
			return response;
		}
	}

	public Response archive(String token, long noteId) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser=userRepository.findById(userId).isPresent();
		boolean isnote=noteRepository.findById(noteId).isPresent();
		
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}
		Note note=noteRepository.findById(noteId).get();
		if(note.isArchive()==false)
		{
		note.setUpdateTime(Utility.todayDate());
		note.setArchive(true);
		User user = userRepository.findById(userId).get();
		//user.getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, token, "note update complete");
		return response;
		}
		else
		{
			Response response = ResponseUtil.getResponse(200, token, "already presente");
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

	public Response addLabels(long noteId, String token, long labelId) {
		// Label label=labelRespository.findByLabelName(labelName).get();
		// label.setLabelName(labelName);
		// labelRespository.save(label);
		long uId = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(uId).isPresent();
		boolean isNote = noteRepository.findByNoteIdAndUserId(noteId, uId).isPresent();
		boolean isLabel = labelRespository.findByLabelIdAndUserId(labelId, uId).isPresent();
		if (!(isUser && isNote && isLabel)) {
			Response response = ResponseUtil.getResponse(200, "not found");
			return response;
		} else {
			Note note = noteRepository.findByNoteIdAndUserId(noteId, uId).get();
			Label label = labelRespository.findByLabelIdAndUserId(labelId, uId).get();
			// Label label=labelRespository.findByLabelName(labelName).get();
			note.setUpdateTime(Utility.todayDate());
			note.setLabelId(labelId);
			label.getNoteList().add(note);
			noteRepository.save(note);
			labelRespository.save(label);

			Response response = ResponseUtil.getResponse(200, "Create complete");
			return response;
		}
	}

	public Response removeLabels(long noteId, String token, long labelId) {
		long uId = TokenUtil.verifyToken(token);

		boolean isUser = userRepository.findById(uId).isPresent();
		boolean isNote = noteRepository.findByNoteIdAndUserId(noteId, uId).isPresent();
		boolean isLabel = labelRespository.findByLabelIdAndUserId(labelId, uId).isPresent();
		if (!(isUser && isLabel && isNote)) {
			Response response = ResponseUtil.getResponse(200, "not found");
			return response;
		} else {
			Note note = noteRepository.findByNoteIdAndUserId(noteId, uId).get();
			Label label = labelRespository.findByLabelIdAndUserId(labelId, uId).get();
			note.setUpdateTime(Utility.todayDate());
			note.setLabelId(labelId);
			label.getNoteList().remove(note);

			noteRepository.delete(note);
			labelRespository.delete(label);
			Response response = ResponseUtil.getResponse(200, "Delete complete");
			return response;
		}

	}

}
