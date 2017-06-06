package com.shearf.learn.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class ConsoleFormatter extends Formatter {
    public String format(LogRecord record) {
        return "Jul [" + record.getThreadID() + " " + record.getLevel() + " ] " + record.getMessage();
    }
}
