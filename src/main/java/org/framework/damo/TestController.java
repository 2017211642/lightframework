package org.framework.damo;

import org.framework.annotation.Autowired;
import org.framework.annotation.Controller;
import org.framework.annotation.RequestMapping;
import org.framework.damo.test.TestService;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/test")
@Controller
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/helloword")
    public String getTest(String name, HttpServletRequest re){
        System.out.println("join helloWolrd !!!");

        return "Hello Wolrd:"+name+",request:"+re;
    }
}
