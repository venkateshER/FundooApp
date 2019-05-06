package com.bridgeit.utility;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

@Component
public class TokenUtil {
	private static String TOKEN_SECRET = "appfundoo123";

	public static String generateToken(Long id) {
		Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
		String token = JWT.create().withClaim("venkatesh", id).sign(algorithm);
		return token;

	}

	public static Long verifyToken(String token) {

		Long id;
		Verification verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		JWTVerifier jwtVerifier = verification.build();
		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		Claim claim = decodedJWT.getClaim("venkatesh");
		id = claim.asLong();
		return id;
	}
	
}
