package com.bridgeit.note.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgeit.exceptions.NoteException;
import com.bridgeit.exceptions.UserException;
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
	private LabelRepositoryInterface labelRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EmailSenderUtil emailSender;
	@Autowired
	private Environment env;

	@Override
	public Response create(NoteDto noteDto, String token) {

		long userid = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userid).isPresent();// .orElseThrow(()->new NoteException(
																		// env.getProperty("user.notfound")));
		// User user = userRepository.findById(userid).get();
		if (!isuser) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("user.notfound"));
			return response;
		} else {
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
			userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.create.success"));
			return response;
		}
	}

	@Override
	public Response delete(long noteId, String token) {
		long userId = TokenUtil.verifyToken(token);
		User user = userRepository.findById(userId).get();// .orElseThrow(()->new
															// UserException(env.getProperty("user.password.change.success")));
		Note note = noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();

		if (!isNote) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		note.setTrash(true);
		noteRepository.delete(note);
		Response response = ResponseUtil.getResponse(200, env.getProperty("note.delete.success"));
		return response;
	}

	@Override
	public Response update(long noteId, NoteDto noteDto, String token) {
		Long userId = TokenUtil.verifyToken(token);
		User user = userRepository.findById(userId).get();
		Note note = noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();

		if (!isNote) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.delete.success"));
			return response;
		}
		note.setUpdateTime(Utility.todayDate());
		note.setTitle(noteDto.getTitle());
		note.setDescription(noteDto.getDescription());
		user.getNotes().add(note);
		noteRepository.save(note);
		// userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, env.getProperty("note.update.success"));
		return response;
	}

	@Override
	public Response pin(String token, long noteId) {

		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		// User user = userRepository.findById(userId).get();
		if (note.isPin() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setPin(true);
			noteRepository.save(note);
			// userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.pin"));
			return response;
		} else {
			note.setPin(false);
			noteRepository.save(note);
			// userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.unpin"));
			return response;
		}
	}

	@Override
	public Response color(String token, long noteId, String color) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		Note note = noteRepository.findById(noteId).get();

		note.setUpdateTime(Utility.todayDate());
		note.setColor(color);
		User user = userRepository.findById(userId).get();
		noteRepository.save(note);
		// userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, env.getProperty("note.update.success"));
		return response;
	}

	@Override
	public Response trash(String token, long noteId) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();
		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		// User user = userRepository.findById(userId).get();
		if (note.isTrash() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setTrash(true);
			noteRepository.save(note);
			// userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.trash"));
			return response;
		} else {
			note.setTrash(false);
			noteRepository.save(note);
			// userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.untrash"));
			return response;
		}
	}

	@Override
	public Response archive(String token, long noteId) {
		long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();
		boolean isnote = noteRepository.findById(noteId).isPresent();

		if (!isnote && !isuser) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		if (note.isArchive() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setArchive(true);
			noteRepository.save(note);
			// userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.archive"));
			return response;
		} else {
			note.setArchive(false);
			noteRepository.save(note);
			// userRepository.save(user);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.unarchive"));
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

	public Response addCollaborator(long noteId, String token, String emailId) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isEmailId = userRepository.findByEmailId(emailId).isPresent();
		boolean isnote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();
		if (!(isuser && isEmailId && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteIdAndUser(noteId, user).get();
			note.setUpdateTime(Utility.todayDate());
			note.getCollaborator().add(user);
			noteRepository.save(note);
			emailSender.mailSender(emailId, "Collobarator", "collobarator Success");
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.update.success"));
			return response;

		}
	}

	public Response removeCollaborator(long noteId, String token, String emailId) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isEmailId = userRepository.findByEmailId(emailId).isPresent();
		boolean isnote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();
		if (!(isuser && isEmailId && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteIdAndUser(noteId, user).get();
			note.setUpdateTime(Utility.todayDate());
			Set<User> userCol = new HashSet<User>();
			userCol = note.getCollaborator();
			if (userCol.stream().filter(u -> u.getEmailId().equals(note.getUser().getEmailId())).findFirst()
					.isPresent()) {
				User findNote = userCol.stream().filter(u -> u.getEmailId().equals(note.getUser().getEmailId()))
						.findFirst().get();
				userCol.remove(findNote);
				noteRepository.save(note);
				Response response = ResponseUtil.getResponse(200, "removed Successfully");
				return response;
			} else {
				Response response = ResponseUtil.getResponse(200, "removed UnSuccessfully");
				return response;
			}
		}
	}

	@Override
	public Response addLabels(long noteId, String token, long labelId) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isnote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();
		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
		if (!(isuser && isLabel && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteIdAndUser(noteId, user).get();
			Label label = labelRepository.findByLabelIdAndUser(labelId, user).get();
			Set<Label> setLabel = new HashSet<Label>();
			setLabel = note.getLabels();
			if (setLabel.stream().filter(l -> l.getLabelId() == label.getLabelId()).findFirst().isPresent()) {
				Response response = ResponseUtil.getResponse(202, "Duplicate label name");
				return response;

			} else {
				setLabel.add(label);
				noteRepository.save(note);
				Response response = ResponseUtil.getResponse(200, env.getProperty("note.update.success"));
				return response;
			}
		}
	}

	@Override
	public Response removeLabels(long noteId, String token, long labelId) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isnote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();
		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
		if (!(isuser && isLabel && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteIdAndUser(noteId, user).get();
			Label label = labelRepository.findByLabelIdAndUser(labelId, user).get();
			note.setUpdateTime(Utility.todayDate());
			Set<Label> setLabel = new HashSet<Label>();
			setLabel = note.getLabels();
			if (setLabel.stream().filter(l -> l.getLabelId() == label.getLabelId()).findFirst().isPresent()) {
				Label findLabel = setLabel.stream().filter(l -> l.getLabelId() == label.getLabelId()).findFirst().get();
				setLabel.remove(findLabel);
				noteRepository.save(note);
				Response response = ResponseUtil.getResponse(200, env.getProperty("note.remove.labels"));
				return response;
			} else {
				Response response = ResponseUtil.getResponse(201, "error");
				return response;
			}
		}

	}

}
