package com.service.servicecombshiro.auth;

import org.apache.shiro.mgt.DefaultSubjectFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

public class JWTSubjectFactory extends DefaultSubjectFactory {
  @Override
  public Subject createSubject(SubjectContext context) {
    context.setSessionCreationEnabled(false);  // 不创建会话
    return super.createSubject(context);
  }
}
