package com.sersol.kbtools.bvt.configuration;

import java.util.ResourceBundle;

public class DBEnvironment {
    public static String msSqlInstance;
    public static String msSqlUsername;
    public static String msSqlPassword;
    private static String testEnvironment = System.getenv("ENVIRONMENT");
    private static ResourceBundle envProperties;

    private DBEnvironment() {
    }

    static {
        envProperties = ResourceBundle.getBundle("environments");
        setEnvUrls();
    }

    /**
     * Sets values needed for connecting to a database
     * depending on the environment that the tests are running in.
     *
     * @return  the instance to connect to
     */
    public static String setEnvUrls() {
        switch (testEnvironment.toUpperCase()) {
            case "CI":
                msSqlInstance = envProperties.getString("ci.coredb3");
                msSqlUsername = envProperties.getString("ci.db.username");
                msSqlPassword = envProperties.getString("ci.db.password");
            case "DEVQA1":
                msSqlInstance = envProperties.getString("env1.coredb3");
                msSqlUsername = envProperties.getString("devQA.username");
                msSqlPassword = envProperties.getString("devQA.password");
                break;
            case "DEVQA2":
                msSqlInstance = envProperties.getString("env2.coredb3");
                msSqlUsername = envProperties.getString("devQA.username");
                msSqlPassword = envProperties.getString("devQA.password");
                break;
            default:
                System.out.println("No environment given");
                break;
        }
        return msSqlInstance;
    }
}
