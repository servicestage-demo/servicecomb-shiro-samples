package com.service.servicecombshiro.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class JWTRealm extends IniRealm {

  public JWTRealm(String resourcePath) {
    super(resourcePath);
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token != null && token instanceof JWTToken;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String username = JWTUtils.decodeToken(principals.toString());
    USERS_LOCK.readLock().lock();
    try {
      return this.users.get(username);
    } finally {
      USERS_LOCK.readLock().unlock();
    }
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    JWTToken jwtToken = (JWTToken) token;
    String username = JWTUtils.decodeToken(jwtToken.getCredentials().toString()); //解token，获取用户名信息
    SimpleAccount account = getUser(username);
    if (account != null) {
      if (account.isLocked()) {
        throw new LockedAccountException("Account [" + account + "] is locked.");
      }
      if (account.isCredentialsExpired()) {
        String msg = "The credentials for account [" + account + "] are expired";
        throw new ExpiredCredentialsException(msg);
      }
    }
    // token校验，根据用户、密码和token，验证token是否有效
    if (!JWTUtils.verify(username, account.getCredentials().toString(), jwtToken.getCredentials().toString())) {
      throw new AuthenticationException("the token is error, please renew one!");
    }
    // 校验成功，返回认证完的身份信息
    SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username,
        jwtToken.getCredentials(), getName());
    return simpleAuthenticationInfo;
  }

  public boolean canLogin(String username, String password) {
    SimpleAccount account = getUser(username);
    if (account == null) {
      return false;
    }
    if (account.getCredentials().toString().equals(password)) {
      return true;
    }
    return false;
  }
}
