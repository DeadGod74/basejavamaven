package com.webapp.util;

import java.util.logging.Logger;

public class LogUtil {
    // Статический экземпляр логгера
    private static final Logger logger = Logger.getLogger(LogUtil.class.getName());

    // Метод для логирования информации
    public static void logInfo(String message) {
        logger.info(message);
    }

    // Метод для логирования ошибок
    public static void logError(String message, Throwable throwable) {
        logger.severe(message + ": " + throwable.getMessage());
    }

    // Вы можете добавить другие методы для разных уровней логирования, например:
    public static void logWarning(String message) {
        logger.warning(message);
    }
}