package com.service.servicecombshiro.auth;

import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

public class HttpAuthFilter implements HttpServerFilter {

  private org.apache.shiro.mgt.SecurityManager securityManager;

  public HttpAuthFilter() {
    Realm realm = new IniRealm("classpath:shiro.ini");  //使用ini的配置方法来初始化Realm
    this.securityManager = new DefaultSecurityManager(realm);        //初始化SecurityManager
  }

  @Override
  public int getOrder() {
    return -10000;  // 确保这个Filter在一般的filter之前先执行
  }

  @Override
  public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx httpServletRequestEx) {
    SecurityUtils.setSecurityManager(securityManager);  // 因为用到了线程上下文，只支持同步编码方式
    Subject user = SecurityUtils.getSubject();
    String userInfo = httpServletRequestEx.getHeader("Authorization");
    if (userInfo == null || userInfo.isEmpty()) {
      return Response.create(401, "Unauthorized",
          "WWW-Authenticate: Basic realm=protected_docs");
    }
    if (userInfo.length() < 5 || !userInfo.startsWith("Basic")) {
      return Response.create(401, "Unauthorized",
          "Header is wrong!");
    }
    String authInfo = userInfo.substring(5).trim();
    String[] authInfos = Base64.decodeToString(authInfo).split(":");
    if (authInfos.length != 2) {
      return Response.create(401, "Unauthorized",
          "Header is wrong!");
    }
    UsernamePasswordToken token = new UsernamePasswordToken(authInfos[0], authInfos[1]); // 获取到请求的用户名和密码
    String path = httpServletRequestEx.getPathInfo();
    if (path.startsWith("/auth")) { // 只对特定的资源检测
      try {
        user.login(token);  // 登录不报异常表示成功了
      } catch (AuthenticationException e) {
        System.out.println("Has no right!");  // 异常表示身份认证失败
        return Response.create(401, "Unauthorized", e.getMessage());
      }
    }
    return null;
  }
}
