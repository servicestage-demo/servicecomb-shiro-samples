package com.service.servicecombshiro.controller;


import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.servicecomb.provider.rest.common.RestSchema;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.CseSpringDemoCodegen", date = "2019-09-30T00:57:56.072Z")

@RestSchema(schemaId = "auth")
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON)
public class ServicecombshiroImpl {

    @Autowired
    private ServicecombshiroDelegate userServicecombshiroDelegate;


    @RequestMapping(value = "/helloworld",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public String helloworld( @RequestParam(value = "name", required = true) String name){

        return userServicecombshiroDelegate.helloworld(name);
    }

}
