package com.shearf.learn.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class Log4j2Test {

    private static final Logger LOGGER = LogManager.getLogger(Log4j2Test.class);

    public static void main(String[] args) {
        LOGGER.info("Log4j2 make a info log");
    }
}
