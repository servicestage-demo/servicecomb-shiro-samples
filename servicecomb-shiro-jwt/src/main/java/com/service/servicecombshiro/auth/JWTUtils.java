package com.service.servicecombshiro.auth;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JWTUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);

  private static final int TOKEN_VALID_TIME = 30 * 60 * 1000;

  public static boolean verify(String username, String secret, String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm)
          .withClaim("username", username)
          .build();
      DecodedJWT decodedJWT = verifier.verify(token);
      System.out.println(decodedJWT.getExpiresAt());
      return true;
    } catch (JWTVerificationException exception) {
      return false;
    }
  }

  public static String sign(String username, String secret) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      String token = JWT.create().withClaim("username", username)
          .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
          .sign(algorithm);
      return token;
    } catch (JWTCreationException exception) {
      return null;
    }
  }

  public static String decodeToken(String token) {

    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getClaim("username").asString();
    } catch (JWTDecodeException e) {
      return token;
    }
  }
}
