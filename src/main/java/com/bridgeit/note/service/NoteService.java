package com.bridgeit.note.service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgeit.exceptions.NoteException;
import com.bridgeit.exceptions.UserException;
import com.bridgeit.label.model.Label;
import com.bridgeit.label.repository.LabelRepositoryInterface;
import com.bridgeit.note.dto.NoteDto;
import com.bridgeit.note.dto.ReminderDto;
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
	@Autowired
	private RedisTemplate<String, Object> redis;
	@Autowired
	private ElasticService elastic;
	
	
	private static final String KEY = "note";
	
	@Override
	public Response create(NoteDto noteDto, String token) {

		long userid = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userid).isPresent();
		if (!isuser) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("user.notfound"));
			return response;
		} else {
//			User user = userRepository.findById(userid).get();
			Note note = modelMapper.map(noteDto, Note.class);
			note.setCreateTime(Utility.todayDate());
			note.setUpdateTime(Utility.todayDate());
			note.setArchive(false);
			note.setColor("white");
			note.setPin(false);
			note.setTrash(false);
			//note.setUser(user);
			note.setUserId(userid);
//			user.getNotes().add(note);
//			
//			userRepository.save(user);
			
			Note eNote=noteRepository.save(note);
			redis.opsForHash().put(KEY,note.getNoteId(),note);
			
			try {
				
				elastic.escreateNote(eNote);
				
			} catch (Exception e) {
				
				System.out.println(""+e.getMessage());
				e.printStackTrace();
			}
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.create.success"));
			return response;
		}
	}
	
	public Note getRedisData(long id){
//	    long userid = tokengenerators.decodeToken(token);

	    return (Note) redis.opsForHash().get(KEY, id);
//	    return redisUtil.getMapAsSingleEntry(KEY, id);
	    
	}  
	
//	public Note getSingleNoteFromRedis(long userId, long noteId) {
//		Note note = redis.getMapAsSingleEntry("note", noteId);
//		System.out.println("Note is " + note);
//		Map<Object, Note> map = redisService.getMapAsAll("note");
//		System.out.println("map " + map);
//		return note;
//	}

	public Object getNoteId(long noteId) {
		
		
		Note note=noteRepository.findById(noteId).get();
		
//		try {
//			elastic.findById(String.valueOf(noteId));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
		
	}
	
	
	
	public Response reminder(ReminderDto reminderDto,String token,long noteId) {
		
		long uid=TokenUtil.verifyToken(token);
		boolean isuser=userRepository.findById(uid).isPresent();
		boolean isnote=noteRepository.findByNoteIdAndUserId(noteId, uid).isPresent();
		if(!(isuser && isnote)) {
			Response response = ResponseUtil.getResponse(204,"not present");
			return response;
		}
		//User user=userRepository.findById(uid).get();
		Note note=noteRepository.findByNoteIdAndUserId(noteId, uid).get();
		note.setReminder(reminderDto.getReminder());
	
		noteRepository.save(note);
		
		Response response = ResponseUtil.getResponse(200,"Reminder Success");
		return response;
	}
	
	public Response unreminder(String token,long noteId) {
		
		long uid=TokenUtil.verifyToken(token);
		boolean isuser=userRepository.findById(uid).isPresent();
		boolean isnote=noteRepository.findByNoteIdAndUserId(noteId, uid).isPresent();
		if(!(isuser && isnote)) {
			Response response = ResponseUtil.getResponse(204,"not present");
			return response;
		}
		//User user=userRepository.findById(uid).get();
		Note note=noteRepository.findByNoteIdAndUserId(noteId, uid).get();
		note.setReminder(null);
	
		noteRepository.save(note);
		
		Response response = ResponseUtil.getResponse(200,"unReminder Success");
		return response;
	}

	@Override
	public Response delete(long noteId, String token) {
		long userId = TokenUtil.verifyToken(token);
		User user = userRepository.findById(userId).get();// .orElseThrow(()->new
															// UserException(env.getProperty("user.password.change.success")));
		
		boolean isNote = noteRepository.findByNoteId(noteId).isPresent();

		if (!isNote) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		Note note = noteRepository.findById(noteId).get();
		String note1=String.valueOf(noteId);
		//noteRepository.deleteById(noteId);//(note);
		noteRepository.delete(note);
		try {
			elastic.deleteNote(String.valueOf(note.getNoteId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Response response = ResponseUtil.getResponse(200, env.getProperty("note.delete.success"));
		return response;
	}

	@Override
	public Response update(long noteId, NoteDto noteDto, String token) {
		Long userId = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(userId).isPresent();

		boolean isNote = noteRepository.findByNoteId(noteId).isPresent();
		if (!(isNote && isuser)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteId(noteId).get();
			note.setUpdateTime(Utility.todayDate());
			note.setTitle(noteDto.getTitle());
			note.setDescription(noteDto.getDescription());
			//user.getNotes().add(note);
			Note note1=noteRepository.save(note);
			try {
				elastic.updateNote(note1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.update.success"));
			return response;
		}
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
		if (note.isPin() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setPin(true);
			noteRepository.save(note);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.pin"));
			return response;
		} else {
			note.setPin(false);
			noteRepository.save(note);
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
		// User user = userRepository.findById(userId).get();
		noteRepository.save(note);
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
		if (note.isTrash() == false) {
			note.setUpdateTime(Utility.todayDate());
			note.setTrash(true);
			noteRepository.save(note);
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.trash"));
			return response;
		} else {
			note.setTrash(false);
			noteRepository.save(note);
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
			Response response = ResponseUtil.getResponse(200, env.getProperty("note.archive"));
			return response;
		} else {
			note.setArchive(false);
			noteRepository.save(note);
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
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isEmailId = userRepository.findByEmailId(emailId).isPresent();
		boolean isnote = noteRepository.findByNoteIdAndUserId(noteId, uid).isPresent();
		if (!(isuser && isEmailId && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteId(noteId).get();
			note.setUpdateTime(Utility.todayDate());
			User userCol=userRepository.findByEmailId(emailId).get();
			List<User> noteCol=new ArrayList<User>();
			noteCol=note.getCollaboratedUsers();
			if(noteCol.stream().filter(u->u.getEmailId().equals(userCol.getEmailId())).findFirst().isPresent())
			{
				Response response=ResponseUtil.getResponse(204,"Already present");
				return response;
			}
			else {
				note.getCollaboratedUsers().add(userCol);
				noteRepository.save(note);
				emailSender.mailSender(emailId, "Collobarator", "Collobarator Success");
				Response response=ResponseUtil.getResponse(200,"Successfull");
				return response;
			}
			
		}
	}

	public Response removeCollaborator(long noteId, String token, String emailId) {
		long uid = TokenUtil.verifyToken(token);
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isEmailId = userRepository.findByEmailId(emailId).isPresent();
		boolean isnote = noteRepository.findByNoteIdAndUserId(noteId, uid).isPresent();
		if (!(isuser && isEmailId && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteId(noteId).get();
			note.setUpdateTime(Utility.todayDate());
			User userCol=userRepository.findByEmailId(emailId).get();
			List<User> noteCol=new ArrayList<User>();
			noteCol=note.getCollaboratedUsers();
			if(noteCol.stream().filter(u->u.getEmailId().equals(userCol.getEmailId())).findFirst().isPresent())
			{
				User noteRemove=noteCol.stream().filter(u->u.getEmailId().equals(userCol.getEmailId())).findFirst().get();
				note.getCollaboratedUsers().remove(noteRemove);
				noteRepository.save(note);
				Response response=ResponseUtil.getResponse(200,"Removed Successfull");
				return response;
			}
			else {
				

				Response response=ResponseUtil.getResponse(204,"Removed UnSuccessfully");
				return response;
			}
			
		}
	}

	@Override
	public Response addLabels(long noteId, String token, long labelId) {
		long uid = TokenUtil.verifyToken(token);
		User user = userRepository.findById(uid).get();
		boolean isuser = userRepository.findById(uid).isPresent();
		boolean isnote = noteRepository.findByNoteId(noteId).isPresent();
		boolean isLabel = labelRepository.findByLabelId(labelId).isPresent();
		if (!(isuser && isLabel && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteId(noteId).get();
			Label label = labelRepository.findByLabelId(labelId).get();
			
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
		boolean isnote = noteRepository.findByNoteId(noteId).isPresent();
		boolean isLabel = labelRepository.findByLabelId(labelId).isPresent();
		if (!(isuser && isLabel && isnote)) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		} else {
			Note note = noteRepository.findByNoteId(noteId).get();
			Label label = labelRepository.findByLabelId(labelId).get();
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
	public List<Note> searchNoteByTitle(String title) {
	
			List<Note> allNotes = new ArrayList<Note>();
			try {
				allNotes = elastic.searchByTitle(title);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return allNotes;
		}
	
	public List<User> getCollaborator(long noteId,String token) {
		long uid = TokenUtil.verifyToken(token);
		Note note=noteRepository.findByNoteIdAndUserId(noteId, uid).get();
		List<User> col=note.getCollaboratedUsers().stream().collect(Collectors.toList());
		return col;
	}
	
	public Response today(long noteId) {
		
		boolean isnote=noteRepository.findById(noteId).isPresent();
		if(!isnote) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		else {
		Note note=noteRepository.findById(noteId).get();
		note.setReminder(Utility.todayDate());
		noteRepository.save(note);
		Response response = ResponseUtil.getResponse(200, " successs");
		return response;
		}
	}
	

	public Response tomorrow(long noteId) {
		
		boolean isnote=noteRepository.findById(noteId).isPresent();
		if(!isnote) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		else {
		Note note=noteRepository.findById(noteId).get();
		
		Calendar calendar = Calendar.getInstance();
		 calendar.add(Calendar.DAY_OF_YEAR, 1);
		 Date tomorrow = calendar.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timeanddate = dateFormat.format(tomorrow);
		    note.setReminder(timeanddate);
		    
		noteRepository.save(note);
		Response response = ResponseUtil.getResponse(200, " successs");
		return response;
		}
	}
	
	public Response forWeek(long noteId) {
		
		boolean isnote=noteRepository.findById(noteId).isPresent();
		if(!isnote) {
			Response response = ResponseUtil.getResponse(204, env.getProperty("note.notfound"));
			return response;
		}
		else {
		Note note=noteRepository.findById(noteId).get();
		
		Calendar calendar = Calendar.getInstance();
		 calendar.add(Calendar.DAY_OF_YEAR, 7);
		 Date tomorrow = calendar.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timeanddate = dateFormat.format(tomorrow);
		    note.setReminder(timeanddate);
		    
		noteRepository.save(note);
		Response response = ResponseUtil.getResponse(200, " successs");
		return response;
		}
	}
	
	public String getAllreminder(String token,long noteId) {
		long id = TokenUtil.verifyToken(token);
//		User user = userRepository.findById(id).get();
		Note note=noteRepository.findByNoteIdAndUserId(noteId, id).get();
		if(note.getReminder()!=null)
		{
		String notes= note.getReminder();
		return notes;
		}
		else {
		return null;
		}
	}
}
