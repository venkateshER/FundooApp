package com.bridgeit.label.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
		Long id = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(id).isPresent();
		if (!isuser) {
			Response response = ResponseUtil.getResponse(204, token, "User not found");
			return response;
		}
		boolean islabel = labelRepository.findByLabelName(labelDto.getLabelName()).isPresent();

		if (islabel) {
			Response response = ResponseUtil.getResponse(204, "0", "Label name already present");
			return response;
		}

		else {
			Label label = modelMapper.map(labelDto, Label.class);
			label.setUpdateStamp(Utility.todayDate());
			label.setCreateStamp(Utility.todayDate());
			User user = userRepository.findById(id).get();
			label.setUser(user);
			user.getLabels().add(label);
			//labelRepository.save(label);
			userRepository.save(user);
			

			Response response = ResponseUtil.getResponse(200, token, "label completed");
			return response;
		}
	}

	@Override
	public Response delete(long labelId, String token) {
		long uid = TokenUtil.verifyToken(token);
		User user=userRepository.findById(uid).get();
		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
		if (!isLabel) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		} else {

			//Label label = labelRepository.findById(labelId).get();
			//labelRepository.delete(label);
//			user.getLabels().remove(label);
//			userRepository.delete(user);
			labelRepository.deleteById(labelId);
			Response response = ResponseUtil.getResponse(204, "0", "Label deleted");
			return response;

		}

	}

	@Override
	public Response update(long id, LabelDto labelDto, String token) {
		long uid = TokenUtil.verifyToken(token);
		
		User user=userRepository.findById(uid).get();
		boolean isLabel = labelRepository.findByLabelIdAndUser(id, user).isPresent();
		if (!isLabel) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}
		Label label = labelRepository.findByLabelIdAndUser(id, user).get();
		label.setLabelName(labelDto.getLabelName());
		label.setUpdateStamp(Utility.todayDate());
		//User user = userRepository.findById(id).get();
		user.getLabels().add(label);
		//labelRepository.save(label);
		userRepository.save(user);
	
		Response response = ResponseUtil.getResponse(200, "0", "update Success");
		return response;

	}

	@Override
	public Response addNotes(long noteId, String token, long labelId) {
		long uid = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(uid).isPresent();
		User user=userRepository.findById(uid).get();
		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
		Label label=labelRepository.findById(labelId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId,user).isPresent();
		if (!(isUser && isNote && isLabel)) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		} else {
			Note note=noteRepository.findById(noteId).get();
			label.setUpdateStamp(Utility.todayDate());
			note.getLabelList().add(label);
			labelRepository.save(label);
			noteRepository.save(note);
			Response response = ResponseUtil.getResponse(200, "0", "Create Success");
			return response;
		}
	}

	@Override
	public Response removeNotes(long noteId, String token, long labelId) {
		long uid = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(uid).isPresent();
		User user=userRepository.findById(uid).get();
		boolean isLabel = labelRepository.findByLabelIdAndUser(labelId, user).isPresent();
		Note note=noteRepository.findById(noteId).get();
		boolean isNote = noteRepository.findByNoteIdAndUser(noteId,user).isPresent();
		if (!(isUser && isNote && isLabel)) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		}
		Label label = labelRepository.findByLabelIdAndUser(labelId, user).get();
		//Note note = noteRepository.findByNoteIdAndUserId(noteId, uid).get();
		label.setUpdateStamp(Utility.todayDate());
		note.getLabelList().remove(label);
		labelRepository.save(label);
		noteRepository.save(note);
		Response response = ResponseUtil.getResponse(200, "0", "removed Success");
		return response;
	}

	@Override
	public List<Label> getAllLabels(String token) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		List<Label> listLabels = user.getLabels();
		return listLabels;

	}

}
