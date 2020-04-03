package org.framework.demo;

import org.framework.annotation.Autowired;
import org.framework.annotation.Controller;
import org.framework.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/test")
@Controller
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/helloworld")
    public String getTest(String name, HttpServletRequest re){
        System.out.println("join helloWolrd !!!");
        testService.hello();
        return "Hello Wolrd:"+name+",request:"+re;
    }
}
