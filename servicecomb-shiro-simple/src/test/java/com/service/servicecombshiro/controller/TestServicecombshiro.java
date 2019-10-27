package com.service.servicecombshiro.controller;



import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestServicecombshiro {

        ServicecombshiroDelegate servicecombshiroDelegate = new ServicecombshiroDelegate();


    @Test
    public void testhelloworld(){

        String expactReturnValue = "hello"; // You should put the expect String type value here.

        String returnValue = servicecombshiroDelegate.helloworld("hello");

        assertEquals(expactReturnValue, returnValue);
    }

}