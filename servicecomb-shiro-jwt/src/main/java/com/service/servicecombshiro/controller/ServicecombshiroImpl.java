package com.service.servicecombshiro.controller;


import javax.ws.rs.core.MediaType;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestSchema(schemaId = "auth")
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON)
public class ServicecombshiroImpl {

  @Autowired
  private ServicecombshiroDelegate userServicecombshiroDelegate;


  @RequestMapping(value = "/helloworld",
      produces = {"application/json"},
      method = RequestMethod.GET)
  @RequiresRoles(value = {"viewer"})
  public String helloworld(@RequestParam(value = "name", required = true) String name) {
    return userServicecombshiroDelegate.helloworld(name);
  }

  @RequestMapping(value = "/helloworld/admin",
      produces = {"application/json"},
      method = RequestMethod.POST)
  @RequiresRoles("administrator")
  public String admin(@RequestParam(value = "name", required = true) String name) {

    return "admin " + userServicecombshiroDelegate.helloworld(name);
  }
}
