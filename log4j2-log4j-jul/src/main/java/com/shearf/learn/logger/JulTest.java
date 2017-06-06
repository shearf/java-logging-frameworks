package com.shearf.learn.logger;

import java.util.logging.Logger;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class JulTest {

    private static final Logger LOGGER = Logger.getLogger(JulTest.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Jul make a info log");
    }
}
