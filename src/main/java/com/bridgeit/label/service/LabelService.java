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
import com.bridgeit.user.model.Response;
import com.bridgeit.user.model.User;
import com.bridgeit.user.repository.UserRepositoryInterface;
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
			label.setUserId(id);
			User user=userRepository.findById(id).get();
			user.getLabels().add(label);
			userRepository.save(user);
			//labelRepository.save(label);

			Response response = ResponseUtil.getResponse(200, token, "label completed");
			return response;
		}
	}
//	public Response create1(LabelDto labelDto, String token) {
//		Long id = TokenUtil.verifyToken(token);
//		boolean isuser = userRepository.findById(id).isPresent();
//		if (!isuser) {
//			Response response = ResponseUtil.getResponse(204, token, "User not found");
//			return response;
//		}
//		boolean islabel = labelRepository.findByLabelName(labelDto.getLabelName()).isPresent();
//
//		if (islabel) {
//			Response response = ResponseUtil.getResponse(204, "0", "Label name already present");
//			return response;
//		}
//
//		else {
//			Label label = modelMapper.map(labelDto, Label.class);
//			label.setUpdateStamp(Utility.todayDate());
//			label.setCreateStamp(Utility.todayDate());
//			label.setUserId(id);
//			labelRepository.save(label);
//
//			Response response = ResponseUtil.getResponse(200, token, "label completed");
//			return response;
//		}
//	}

	public Response delete(long id, String token) {
		long uid = TokenUtil.verifyToken(token);
		boolean isLabel = labelRepository.findByLabelIdAndUserId(id, uid).isPresent();
		if (!isLabel) {
			Response response = ResponseUtil.getResponse(204, "0", "not found");
			return response;
		} else {
		
			Label label=labelRepository.findById(id).get();
			labelRepository.delete(label);
			Response response = ResponseUtil.getResponse(204, "0", "Label deleted");
			return response;

		}

	}
	
	public Response update(long id,LabelDto labelDto,String token)
	{
		long uid=TokenUtil.verifyToken(token);
		Optional<Label>optlabel=labelRepository.findByLabelIdAndUserId(id, uid);
		boolean islabel=optlabel.isPresent();
		if(!islabel)
		{
			Response response = ResponseUtil.getResponse(204, "0","not found");
			return response;
		}
		Label label=optlabel.get();
		label.setLabelName(labelDto.getLabelName());
		label.setUpdateStamp(Utility.todayDate());
		User user=userRepository.findById(id).get();
		user.getLabels().add(label);
		userRepository.save(user);
		//labelRepository.save(label);
		Response response = ResponseUtil.getResponse(200, "0","update Success");
		return response;
		
	}
	
	public Response addNotes(long noteId,String token,long labelId)
	{
		long uid=TokenUtil.verifyToken(token);
		boolean isUser=userRepository.findById(uid).isPresent();
		boolean isLabel=labelRepository.findByLabelIdAndUserId(labelId, uid).isPresent();
		boolean isNote=noteRepository.findByNoteIdAndUserId(noteId, uid).isPresent();
		if(!(isUser && isNote && isLabel))
		{
			Response response = ResponseUtil.getResponse(204, "0","not found");
			return response;
		}
		else
		{
			Label label=labelRepository.findByLabelIdAndUserId(labelId, uid).get();
			Note note=noteRepository.findByNoteIdAndUserId(noteId, uid).get();
			label.setUpdateStamp(Utility.todayDate());
			label.setNoteId(noteId);
			note.getLabelList().add(label);
			labelRepository.save(label);
			noteRepository.save(note);
			Response response = ResponseUtil.getResponse(200, "0","Create Success");
			return response;
		}
	}
	
	public Response removeNotes(long noteId,String token,long labelId)
	{
		long uid=TokenUtil.verifyToken(token);
		boolean isUser=userRepository.findById(uid).isPresent();
		boolean isLabel=labelRepository.findByLabelIdAndUserId(labelId, uid).isPresent();
		boolean isNote=noteRepository.findByNoteIdAndUserId(noteId, uid).isPresent();
		if(!(isUser && isNote && isLabel))
		{
			Response response = ResponseUtil.getResponse(204, "0","not found");
			return response;
		}
		Label label=labelRepository.findByLabelIdAndUserId(labelId, uid).get();
		Note note=noteRepository.findByNoteIdAndUserId(noteId, uid).get();
		label.setUpdateStamp(Utility.todayDate());
		label.setNoteId(noteId);
		note.getLabelList().remove(label);
		labelRepository.delete(label);
		noteRepository.delete(note);
		Response response = ResponseUtil.getResponse(200, "0","removed Success");
		return response;
	}
	
	public List<Label> getAllLabels(String token)
	{
		long uid=TokenUtil.verifyToken(token);
		User user=userRepository.findById(uid).get();
		List<Label>listLabels=user.getLabels();
		return listLabels;
		
	}

}
