package com.service.servicecombshiro;

import org.apache.servicecomb.transport.rest.vertx.AbstractVertxHttpDispatcher;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;

public class AuthVertxHttpDispatcher extends AbstractVertxHttpDispatcher {

  @Override
  public boolean enabled() {
    return true;
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public void init(Router router) {

  }
}
