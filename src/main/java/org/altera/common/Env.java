package org.altera.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Optional;

public class Env {

    /**
     * resources내에 있는 default conf를 반환
     * @return
     */
    public static Config getConfig() {
        return getConfig("application.conf");
    }

    public static Config getConfig(String file) {
        Environment env = getEnv();
        return ConfigFactory.load(file).getConfig(env.toString().toLowerCase());
    }

    /**
     * 현재 설정된 환경반환
     * @return
     */
    public static Environment getEnv() {
        String appMode = Optional.ofNullable(System.getenv("APP_MODE"))
            .orElse("local")
            .toUpperCase();
        return Environment.valueOf(appMode);
    }

    private enum Environment {
        LOCAL,
        DEV,
        PROD;
    }
}
