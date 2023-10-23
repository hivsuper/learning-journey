package org.lxp.dubbo.main;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootStrap {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "applicationProvider.xml" });
        context.start();
        System.out.println("press any key to quit ~ ");
        System.in.read();
        context.close();
    }
}
