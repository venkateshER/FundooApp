package com.bridgeit.label.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgeit.exceptions.UserException;
import com.bridgeit.label.Dto.LabelDto;
import com.bridgeit.label.model.Label;
import com.bridgeit.label.repository.LabelRepositoryInterface;
import com.bridgeit.note.model.Note;
import com.bridgeit.note.repository.NoteRepositoryInterface;
import com.bridgeit.user.model.User;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.Response;
import com.bridgeit.utility.ResponseUtil;
import com.bridgeit.utility.TokenUtil;
import com.bridgeit.utility.Utility;

@Service
public class LabelService implements LabelServiceInterface {

	@Autowired
	private UserRepositoryInterface userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private LabelRepositoryInterface labelRepository;
	@Autowired
	private NoteRepositoryInterface noteRepository;
	@Autowired
	private Environment env;

	@Override
	public Response create(LabelDto labelDto, String token) {
		long id = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(id).isPresent();//.orElseThrow(() -> new UserException(env.getProperty("user.not.match")));
		User user=userRepository.findById(id).get();
		boolean islabel = labelRepository.findByLabelName(labelDto.getLabelName()).isPresent();
		if (islabel) {
			Response response = ResponseUtil.getResponse(204,env.getProperty("label.exist"));
			return response;
		}
		
		if(!isuser)
		{
			Response response = ResponseUtil.getResponse(204,env.getProperty("user.not.match"));
			return response;
		}
		else {
			
			Label label = modelMapper.map(labelDto, Label.class);
			label.setUpdateStamp(Utility.todayDate());
			label.setCreateStamp(Utility.todayDate());
			label.setUserId(id);
			//label.setUser(user);
			user.getLabels().add(label);
			userRepository.save(user);

			Response response = ResponseUtil.getResponse(200,env.getProperty("label.create.success"));
			return response;
		}
	}

	@Override
	public Response delete(long labelId, String token) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		boolean isLabel = labelRepository.findByLabelId(labelId).isPresent();
		if (!isLabel) {
			Response response = ResponseUtil.getResponse(204,env.getProperty("label.notfound"));
			return response;
		} else {
			labelRepository.deleteById(labelId);
			Response response = ResponseUtil.getResponse(200,env.getProperty("label.delete.success"));
			return response;

		}

	}

	@Override
	public Response update(long id, LabelDto labelDto, String token) {
		long uid = TokenUtil.verifyToken(token);

		User user = userRepository.findById(uid).get();
		boolean isLabel = labelRepository.findByLabelId(id).isPresent();
		if (!isLabel) {
			Response response = ResponseUtil.getResponse(204,env.getProperty("label.notfound"));
			return response;
		}
		Label label = labelRepository.findByLabelId(id).get();
		label.setLabelName(labelDto.getLabelName());
		label.setUpdateStamp(Utility.todayDate());
//		user.getLabels().add(label);
//		userRepository.save(user);
		labelRepository.save(label);

		Response response = ResponseUtil.getResponse(200,env.getProperty("label.update.success"));
		return response;

	}
//
//	@Override
//	public Response addNotes(long noteId, String token, long labelId) {
//		long uid = TokenUtil.verifyToken(token);
//		User user = userRepository.findById(uid).get();
//		boolean isuser = userRepository.findById(uid).isPresent();
//		boolean isnote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();
//		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
//		if (!(isuser && isLabel && isnote)) {
//			Response response = ResponseUtil.getResponse(204,env.getProperty("label.notfound"));
//			return response;
//		} else {
//			Note note = noteRepository.findByNoteIdAndUser(noteId, user).get();
//			Label label = labelRepository.findByLabelIdAndUser(labelId, user).get();
//			label.getNotes().add(note);
//			Response response = ResponseUtil.getResponse(200,env.getProperty("label.update.success"));
//			return response;
//		}
//	}
//
//	@Override
//	public Response removeNotes(long noteId, String token, long labelId) {
//		long uid = TokenUtil.verifyToken(token);
//		User user = userRepository.findById(uid).get();
//		boolean isuser = userRepository.findById(uid).isPresent();
//		boolean isnote = noteRepository.findByNoteIdAndUser(noteId, user).isPresent();
//		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
//		if (!(isuser && isLabel && isnote)) {
//			Response response = ResponseUtil.getResponse(204,env.getProperty("label.notfound"));
//			return response;
//		}
//		Note note = noteRepository.findByNoteIdAndUser(noteId, user).get();
//		Label label = labelRepository.findByLabelIdAndUser(labelId, user).get();
//		label.setUpdateStamp(Utility.todayDate());
//		
//		labelRepository.save(label);
//		noteRepository.save(note);
//		Response response = ResponseUtil.getResponse(200,env.getProperty("label.remove.notes"));
//		return response;
//	}

	
	public List<Label> getAllLabels(String token) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		List<Label> listLabels = user.getLabels();
		return listLabels;

	}
	public List<Note> getNotesFromLabel(long labelId, String token) {
		long userId = TokenUtil.verifyToken(token);
		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId).get();	
		List<Note> notes = label.getNotes().stream().collect(Collectors.toList());
		return notes;
	}
	public List<Label> getNoteLabels(long noteId,String token) {
		long uid = TokenUtil.verifyToken(token);
		//User user = userRepository.findById(uid).get();
		Note note=noteRepository.findByNoteIdAndUserId(noteId, uid).get();
		List<Label> labels=note.getLabels().stream().collect(Collectors.toList());
		return labels;

	}

}
