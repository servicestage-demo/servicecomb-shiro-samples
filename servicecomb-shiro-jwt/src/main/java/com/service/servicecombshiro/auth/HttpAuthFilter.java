package com.service.servicecombshiro.auth;

import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Const;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.engine.SwaggerProducerOperation;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;

public class HttpAuthFilter implements HttpServerFilter {

  private DefaultSecurityManager securityManager;
  private JWTRealm realm;

  public HttpAuthFilter() {
    realm = new JWTRealm("classpath:shiro.ini");  //使用ini的配置方法来初始化Realm
    this.securityManager = new DefaultSecurityManager(realm);        //初始化SecurityManager
    this.securityManager.setSubjectFactory(new JWTSubjectFactory());
    DefaultSessionManager sm = new DefaultSessionManager();
    // 关闭会话校验任务
    sm.setSessionValidationSchedulerEnabled(false);
    // 关闭会话存储，否则会报异常
    ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) this.securityManager.getSubjectDAO())
        .getSessionStorageEvaluator()).setSessionStorageEnabled(false);
    this.securityManager.setSessionManager(sm);
  }

  @Override
  public int getOrder() {
    return -10000;  // 确保这个Filter在一般的filter之前先执行
  }

  @Override
  public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx httpServletRequestEx) {
    SecurityUtils.setSecurityManager(securityManager);  // 因为用到了线程上下文，只支持同步编码方式
    String path = httpServletRequestEx.getPathInfo();
    String userInfo = httpServletRequestEx.getHeader("Authorization");
    if (userInfo == null || userInfo.isEmpty()) {
      return tryLogin(httpServletRequestEx, path);
    }
    JWTToken token = new JWTToken(userInfo);

    if (path.startsWith("/auth")) { // 只对特定的资源检测
      Subject user = null;
      try {
        user = SecurityUtils.getSubject();
        user.login(token);  // 登录不报异常表示成功了
      } catch (AuthenticationException e) {
        System.out.println("Has no right!");  // 异常表示身份认证失败
        return Response.create(401, "Unauthorized", e.getMessage());
      }
      SwaggerProducerOperation swaggerProducerOperation = invocation.getOperationMeta().getExtData(Const.PRODUCER_OPERATION);
      RequiresRoles requiresRoles = swaggerProducerOperation.getProducerMethod().getAnnotation(RequiresRoles.class);
      if (requiresRoles != null) {
        String[] roles = requiresRoles.value();
        try {
          user.checkRoles(roles);
        } catch (AuthorizationException e) {
          System.out.println("Has no required roles!");  // 异常表示权限认证失败
          return Response.create(401, "Unauthorized", e.getMessage());
        }
      }
    }
    return null;
  }

  private Response tryLogin(HttpServletRequestEx httpServletRequestEx, String path) {
    if (path.equals("/login/login")) {
      // 这里只是简单的获取用户密码，使用form表单的方式来提交
      String username = httpServletRequestEx.getParameter("username");
      String secret = httpServletRequestEx.getParameter("password");
      boolean login = realm.canLogin(username, secret);
      if (!login) {
        return Response.create(401, "Unauthorized",
            "User/Password is not right!");
      }
      String token = JWTUtils.sign(username, secret);
      return Response.createSuccess(token);
    }
    return Response.create(401, "Unauthorized",
        "JWT Token is missing, please login first!");
  }
}
