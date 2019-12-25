package mylogback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLog {
    private static final Logger logger  = LoggerFactory.getLogger(MyLog.class);

    public static void main(String[] args) {
        logger.info("TEST....");
        logger.info("select * from DECM_INIT");
    }
}
