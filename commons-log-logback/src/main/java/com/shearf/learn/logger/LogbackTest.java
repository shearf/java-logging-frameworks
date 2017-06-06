package com.shearf.learn.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class LogbackTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackTest.class);

    public static void main(String[] args) {

        LOGGER.info("Logback make a info log");
    }
}
