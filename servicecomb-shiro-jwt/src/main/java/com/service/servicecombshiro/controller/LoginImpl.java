package com.service.servicecombshiro.controller;


import javax.ws.rs.core.MediaType;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.CseSpringDemoCodegen", date = "2019-09-30T00:57:56.072Z")

@RestSchema(schemaId = "login")
@RequestMapping(path = "/login", produces = MediaType.APPLICATION_JSON)
public class LoginImpl {

  @RequestMapping(value = "/login",
      produces = {"text/plain"},
      method = RequestMethod.POST)
  public String helloworld(@RequestParam(value = "name", required = true) String name) {
    return "";
  }
}
