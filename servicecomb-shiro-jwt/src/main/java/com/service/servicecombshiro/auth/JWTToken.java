package com.service.servicecombshiro.auth;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {
  private String token;

  public JWTToken(String token) {
    this.token = token;
  }

  @Override
  public Object getPrincipal() {
    return JWTUtils.decodeToken(token);
  }

  @Override
  public Object getCredentials() {
    return token;
  }
}
