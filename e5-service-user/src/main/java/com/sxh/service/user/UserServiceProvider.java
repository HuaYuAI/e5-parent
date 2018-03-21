package com.sxh.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by HuaYu on 2018/2/23.
 */
public class UserServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceProvider.class);
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
            //没有使用web启动，使用main方法启动IOC容器时，加载了service，mapper，以及dubbo的服务接口
            context.start();
        } catch (Exception e) {
            LOGGER.error("== DubboProvider context start error:",e);
        }
//        System.in.read();
        synchronized (UserServiceProvider.class) {
            while (true) {
                try {
                    UserServiceProvider.class.wait();
                    System.out.println("商品服务已启动成功！");
                } catch (InterruptedException e) {
                    LOGGER.error("== synchronized error:",e);
                }
            }
        }
    }
}
