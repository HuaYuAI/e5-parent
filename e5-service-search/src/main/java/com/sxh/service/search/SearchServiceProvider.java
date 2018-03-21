package com.sxh.service.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * Created by HuaYu on 2018/2/23.
 */
public class SearchServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceProvider.class);
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-mq-consumer.xml");

            context.start();
        } catch (Exception e) {
            LOGGER.error("== DubboProvider context start error:",e);
        }
        synchronized (SearchServiceProvider.class) {
            while (true) {
                try {
                    SearchServiceProvider.class.wait();

                } catch (InterruptedException e) {
                    LOGGER.error("== synchronized error:",e);
                }
            }
        }
    }
}
