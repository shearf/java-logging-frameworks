package com.shearf.learn.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class CommonsLogLog4j2Test {

    private static final Log LOG = LogFactory.getLog(CommonsLogLog4j2Test.class);

    public static void main(String[] args) {

        LOG.info("commons-log with log4j2 make a info log");
    }
}
