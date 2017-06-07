import org.apache.log4j.Logger;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class Log4jTest {

    private static final Logger LOGGER = Logger.getLogger(Log4jTest.class);

    public static void main(String[] args) {
        LOGGER.info("Log4j make a info log");
    }
}
