package com.bridgeit.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgeit.user.model.User;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.Response;
import com.bridgeit.utility.ResponseUtil;
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

	// convert to file
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	// to remove the spaces

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public Response uploadFile(MultipartFile multipartFile, String token) {

		long id = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(id).isPresent();
		if (!isUser) {
			Response response = ResponseUtil.getResponse(204, "user not exist");
			return response;
		}
		User user = userRepository.findById(id).get();
		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
			user.setUpdateStamp(Utility.todayDate());
			user.setImage(fileUrl);
			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Response response = ResponseUtil.getResponse(200, token, "Image upload Successfully");
		return response;
	}

	public Response deleteFileFromS3Bucket(String fileName, String token) {

		long id = TokenUtil.verifyToken(token);
		boolean isUser = userRepository.findById(id).isPresent();
		if (!isUser) {
			Response response = ResponseUtil.getResponse(204, "user not exist");
			return response;
		}
		User user = userRepository.findById(id).get();
		user.setUpdateStamp(Utility.todayDate());
		user.setImage("");
//        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		userRepository.save(user);
		Response response = ResponseUtil.getResponse(200, "Image deleted Successfully");
		return response;
	}
}
