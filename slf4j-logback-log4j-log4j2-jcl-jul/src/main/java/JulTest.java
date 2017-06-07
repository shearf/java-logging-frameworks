import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Logger;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class JulTest {

    private static final Logger LOGGER = Logger.getLogger(JulTest.class.getName());

    static {
        SLF4JBridgeHandler.install();
    }

    public static void main(String[] args) {

        LOGGER.info("Jul make a info log");
    }
}
