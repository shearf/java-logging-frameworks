import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by xiahaihu on 17/6/6.
 */
public class CommonsLogTest {

    private static final Log LOG = LogFactory.getLog(CommonsLogTest.class);

    public static void main(String[] args) {
        LOG.info("Commons-log make a info log");
    }
}
