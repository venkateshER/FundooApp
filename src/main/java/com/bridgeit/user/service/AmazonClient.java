package com.bridgeit.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgeit.user.model.User;
import com.bridgeit.user.model.UserResponse;
import com.bridgeit.user.model.UserResponseUtil;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.TokenUtil;
import com.bridgeit.utility.Utility;


@Service
public class AmazonClient {

	private AmazonS3 s3client;

	@Autowired
	private UserRepositoryInterface userRepository;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public UserResponse uploadFile(MultipartFile multipartFile, String token) {

		long id = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(id).isPresent();
		if (!isUser) {
			UserResponse response = UserResponseUtil.getResponse(204, "user not exist");
			return response;
		}
		User user = userRepository.findById(id).get();
		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = "https://"+bucketName + ".s3.ap-south-1.amazonaws.com/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
			user.setUpdateStamp(Utility.todayDate());
			user.setImage(fileUrl);
			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserResponse response = UserResponseUtil.getResponse(200, token, "Image upload Successfully");
		return response;
	}

	public UserResponse deleteFileFromS3Bucket(String fileName, String token) {

		long id = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(id).isPresent();
		if (!isUser) {
			UserResponse response = UserResponseUtil.getResponse(204, "user not exist");
			return response;
		}
		User user = userRepository.findById(id).get();
		user.setUpdateStamp(Utility.todayDate());
		user.setImage("");
//        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		userRepository.save(user);
		UserResponse response = UserResponseUtil.getResponse(200, "Image deleted Successfully");
		return response;
	}
	
	public URL getProfile(String token)
	{
		long id = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(id).isPresent();
		if (!isUser) {
			
//			UserResponse response = UserResponseUtil.getResponse(204, "user not exist");
//			return response;
			System.out.println("Not found");
		}
		
		User user=userRepository.findById(id).get();
//		GeneratePresignedUrlRequest url= new GeneratePresignedUrlRequest(bucketName,user.getImage());
//		url.setMethod(HttpMethod.GET);
//		URL profilePic=s3client.generatePresignedUrl(url);
		String s1=user.getImage();
		URL url = null;
		try {
			url = new URL(s1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
}
