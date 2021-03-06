
package com.bridgeit.user.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgeit.note.model.Note;
import com.bridgeit.user.dto.ForgotPasswordDto;
import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.dto.SetPasswordDto;
import com.bridgeit.user.dto.UserDto;

import com.bridgeit.user.model.User;
import com.bridgeit.user.model.UserResponse;
import com.bridgeit.user.model.UserResponseUtil;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.EmailSenderUtil;
import com.bridgeit.utility.EncryptUtil;
import com.bridgeit.utility.TokenUtil;
import com.bridgeit.utility.Utility;

@Service
@PropertySource("classpath:error.properties")
@PropertySource("classpath:message.properties")
public class UserService implements UserServiceInterface {
	@Autowired
	private UserRepositoryInterface userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EmailSenderUtil emailSender;
	@Autowired
	private EncryptUtil encryptUtil;
	@Autowired
	private Environment env;
	@Autowired
	private RedisTemplate<String, Object> redis;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	
	private static final String KEY = "user";
	
	@Override
	public UserResponse register(UserDto userDto) {

		boolean isUser = userRepository.findByEmailId(userDto.getEmailId()).isPresent();

		if (isUser) {
			UserResponse response = UserResponseUtil.getResponse(204, env.getProperty("user.exist"));
			return response;
		} else {
			User user = modelMapper.map(userDto, User.class);
			String token = TokenUtil.generateToken(user.getUserId());
			user.setPassword(encryptUtil.encryptPassword(user.getPassword()));
			user.setToken(token);
			user.setRegisterStamp(Utility.todayDate());
			user.setUpdateStamp(Utility.todayDate());
			String fileUrl = "https://"+bucketName + ".s3.ap-south-1.amazonaws.com/" + "1562847866535-default.jpg";
			user.setImage(fileUrl);
			userRepository.save(user);
			registerActivationMail(user);
			UserResponse response = UserResponseUtil.getResponse(200, token, env.getProperty("user.register.success"));
			return response;
		}

	}
	
	@Override
	public void registerActivationMail(User user) {
		String token = TokenUtil.generateToken(user.getUserId());
//		StringBuffer requestUrl = request.getRequestURL();
//		System.out.println("" + requestUrl);
		String url = "http://localhost:9090/user/activation/" + token;
//		System.out.println(url);
		emailSender.mailSender(user.getEmailId(), env.getProperty("user.email.register"), url);
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public UserResponse registerActivation(String token) {
		Long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		user.setVerified(true);
		user.setUpdateStamp(Utility.todayDate());
		userRepository.save(user);
		UserResponse response = UserResponseUtil.getResponse(200, env.getProperty("user.activate.success"));
		return response;
	}

	@Override
	public UserResponse forgotPassword(ForgotPasswordDto forgotdto) {

		String email = forgotdto.getEmailId();
		boolean isUser = userRepository.findByEmailId(email).isPresent();
		if (!isUser) {
			UserResponse response1 = UserResponseUtil.getResponse(204, env.getProperty("user.email.incorrect"));
			return response1;
		}
		User userId = userRepository.findByEmailId(email).get();
		String token = TokenUtil.generateToken(userId.getUserId());
		emailSender.mailSender(userId.getEmailId(), env.getProperty("user.email.subject"),
				"http://localhost:4200/setPassword/" + token);
		userId.setUpdateStamp(Utility.todayDate());
//		userId.setPassword(forgotdto.getPassword());
//
//		userRepository.save(userId);

		UserResponse response1 = UserResponseUtil.getResponse(200, token, env.getProperty("user.email.success"));

		return response1;
	}

	@Override
	public UserResponse login(LoginDto loginDto) {

		boolean isEmail = userRepository.findByEmailId(loginDto.getEmailId()).isPresent();
		if (!isEmail) {
			UserResponse response1 = UserResponseUtil.getResponse(204, env.getProperty("user.email.invaild"));
			return response1;
		}

		User user = userRepository.findByEmailId(loginDto.getEmailId()).get();
		boolean isPassword = encryptUtil.isPassword(loginDto, user);

		if (!(isPassword && user.isVerified())) {
			UserResponse response = UserResponseUtil.getResponse(204, env.getProperty("user.login.failed"));
			return response;
		}
		String token = TokenUtil.generateToken(user.getUserId());
//		httpResponse.addHeader("token", token);
		user.setUpdateStamp(Utility.todayDate());
		redis.opsForHash().put(KEY,token,user);
		userRepository.save(user);
		UserResponse response = UserResponseUtil.getResponse(200, token, env.getProperty("user.login.success"));
		return response;

	}


	@Override
	public UserResponse setPassword(SetPasswordDto setPassDto, String token) {

		Long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		String email = user.getEmailId();
		boolean isUser = userRepository.findByEmailId(email).isPresent();
		if (!isUser) {
			UserResponse response = UserResponseUtil.getResponse(204, env.getProperty("user.email.invaild"));
			return response;
		}
		User userId = userRepository.findByEmailId(email).get();
		userId.setPassword(encryptUtil.encryptPassword(setPassDto.getPassword()));
		userId.setUpdateStamp(Utility.todayDate());
		userRepository.save(userId);

		UserResponse response = UserResponseUtil.getResponse(200, token, env.getProperty("user.password.set"));
		return response;

	}

	@Override
	public UserResponse delete(String token) {
		Long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		userRepository.delete(user);
		UserResponse response = UserResponseUtil.getResponse(200, "user.delete.success");
		return response;

	}

	public User getRedisData(String token){
//   long userid = TokenUtil.verifyToken(token);

	    return (User) redis.opsForHash().get(KEY, token);
//	    return redisUtil.getMapAsSingleEntry(KEY, id);
	    
	}  

}
