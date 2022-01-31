package n11client.utils;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log {
    private static final Logger logger = LogManager.getLogger();
    public static final boolean logDetail = System.getProperty("log.detail", "false").equals("true");

    public static void log(String msg) {
        logger.info("[N11] " + msg);
    }

    public static void warn(String msg) {
        logger.warn("[N11] " + msg);
    }

    public static void warn(String msg, Throwable t) {
        logger.warn("[N11] " + msg, t);
    }

    public static void error(String msg) {
        logger.error("[N11] " + msg);
    }

    public static void error(String msg, Throwable t) {
        logger.error("[N11] " + msg, t);
    }

    public static void debug(String msg) {
        if (logDetail) {
            logger.debug("[N11] " + msg);
        }
    }
}
