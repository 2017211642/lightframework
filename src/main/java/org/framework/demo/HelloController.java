package org.framework.demo;

import org.framework.annotation.Controller;
import org.framework.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    @RequestMapping("")
    public String hello(){
        return "Hello Coder!";
    }
}
