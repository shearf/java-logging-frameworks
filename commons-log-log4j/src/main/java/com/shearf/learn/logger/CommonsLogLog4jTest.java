package com.shearf.learn.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class CommonsLogLog4jTest {

    private static final Log LOG = LogFactory.getLog(CommonsLogLog4jTest.class);

    public static void main(String[] args) {
        LOG.info("Commons-log with log4j make info log");
    }
}
