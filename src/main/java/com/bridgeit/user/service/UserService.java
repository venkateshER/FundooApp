
package com.bridgeit.user.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgeit.user.dto.ForgotPasswordDto;
import com.bridgeit.user.dto.LoginDto;
import com.bridgeit.user.dto.SetPasswordDto;
import com.bridgeit.user.dto.UserDto;

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

	@Override
	public Response register(UserDto userDto, HttpServletRequest request) {

		boolean isUser = userRepository.findByEmailId(userDto.getEmailId()).isPresent();

		if (isUser) {
			Response response = ResponseUtil.getResponse(204,env.getProperty("user.exist"));
			return response;
		} else {
			User user = modelMapper.map(userDto, User.class);
			String token = TokenUtil.generateToken(user.getUserId());
			user.setPassword(encryptUtil.encryptPassword(user.getPassword()));
			user.setToken(token);
			user.setRegisterStamp(Utility.todayDate());
			user.setUpdateStamp(Utility.todayDate());
			userRepository.save(user);
			registerActivationMail(user, request);
			Response response = ResponseUtil.getResponse(200, token, env.getProperty("user.register.success"));
			return response;
		}

	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public Response registerActivation(String token) {
		Long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		user.setVerified(true);
		user.setUpdateStamp(Utility.todayDate());
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, env.getProperty("user.activate.success"));
		return response;
	}

	@Override
	public Response forgotPassword(ForgotPasswordDto forgotdto, HttpServletRequest request) {

		String email = forgotdto.getEmailId();
		boolean isUser = userRepository.findByEmailId(email).isPresent();
		if (!isUser) {
			Response response1 = ResponseUtil.getResponse(204,env.getProperty("user.email.incorrect"));
			return response1;
		}
		User userId = userRepository.findByEmailId(email).get();
		String token = TokenUtil.generateToken(userId.getUserId());
		emailSender.mailSender(userId.getEmailId(), env.getProperty("user.email.subject"),
				"http://localhost:9090/setPassword/" + token);
		userId.setUpdateStamp(Utility.todayDate());
		userId.setPassword(forgotdto.getPassword());

		userRepository.save(userId);

		Response response1 = ResponseUtil.getResponse(200, token, env.getProperty("user.email.success"));

		return response1;
	}

	@Override
	public Response login(LoginDto loginDto, HttpServletResponse httpResponse) {

		boolean isEmail = userRepository.findByEmailId(loginDto.getEmailId()).isPresent();
		if (!isEmail) {
			Response response1 = ResponseUtil.getResponse(204,env.getProperty("user.email.invaild"));
			return response1;
		}

		User user = userRepository.findByEmailId(loginDto.getEmailId()).get();
		boolean isPassword = encryptUtil.isPassword(loginDto, user);

		if (!(isPassword && user.isVerified())) {
			Response response = ResponseUtil.getResponse(204,env.getProperty("user.login.failed"));
			return response;
		}
		String token = TokenUtil.generateToken(user.getUserId());
		httpResponse.addHeader("token", token);
		user.setUpdateStamp(Utility.todayDate());
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, token, env.getProperty("user.login.success"));
		return response;

	}

	@Override
	public void registerActivationMail(User user, HttpServletRequest request) {
		String token = TokenUtil.generateToken(user.getUserId());
		StringBuffer requestUrl = request.getRequestURL();
		System.out.println("" + requestUrl);
		String url = requestUrl.substring(0, requestUrl.lastIndexOf("/")) + "/activation/" + token;
		System.out.println(url);
		emailSender.mailSender(user.getEmailId(), env.getProperty("user.email.register"), url);
	}

	@Override
	public Response setPassword(SetPasswordDto setPassDto, String token) {

		Long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		String email = user.getEmailId();
		boolean isUser = userRepository.findByEmailId(email).isPresent();
		if (!isUser) {
			Response response = ResponseUtil.getResponse(204,env.getProperty("user.email.invaild"));
			return response;
		}
		User userId = userRepository.findByEmailId(email).get();
		userId.setPassword(encryptUtil.encryptPassword(setPassDto.getPassword()));
		userId.setUpdateStamp(Utility.todayDate());
		userRepository.save(userId);

		Response response = ResponseUtil.getResponse(205, token, env.getProperty("user.password.set"));
		return response;

	}

	@Override
	public Response delete(String token) {
		Long id = TokenUtil.verifyToken(token);
		User user = userRepository.findById(id).get();
		userRepository.delete(user);
		Response response = ResponseUtil.getResponse(200, "user.delete.success");
		return response;

	}

}
