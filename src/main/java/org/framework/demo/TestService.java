package org.framework.demo;

import org.framework.annotation.Service;

@Service
public class TestService implements TestSer {
    public void hello(){
        System.out.println("hello world");
    }
}
