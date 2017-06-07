package com.shearf.learn.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiahaihu on 17/6/7.
 */
public class Slf4jTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jTest.class);

    public static void main(String[] args) {
        LOGGER.info("SLF4J make a info log");
    }
}
