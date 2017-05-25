package com.sersol.kbtools.bvt.configuration;

import com.sersol.common.bvt.configuration.AbstractBVTEnvironment;
import com.sersol.common.bvt.configuration.TestEnvironment;

import java.util.ResourceBundle;

public class KBToolsEnvironment extends AbstractBVTEnvironment {

    private static final String CI_DEFAULT = "lnx64-hs2";
    private static final String CI_FMT_STR = "http://%s.devqa.sersol.il.pqe:%d/jtools";
    private static ResourceBundle envProperties;

    private static String smbShare, smbUsername, smbPassword;

    static {
        envProperties = ResourceBundle.getBundle("environments");
        setTestProperties();
    }

    /**
     * Sets any test properties here.
     */
    private static void setTestProperties(){
        switch(TestEnvironment.fromEnvironment()){
            case DEVQA1:
                smbShare = envProperties.getString("env1.smb.share");
                smbUsername = envProperties.getString("env1.smb.username");
                smbPassword = envProperties.getString("env1.smb.password");
                break;
            case DEVQA2:
                smbShare = envProperties.getString("env2.smb.share");
                smbUsername = envProperties.getString("env2.smb.username");
                smbPassword = envProperties.getString("env2.smb.password");
                break;
            case CI:
                smbShare = envProperties.getString("ci.smb.share");
                smbUsername = envProperties.getString("ci.smb.username");
                smbPassword = envProperties.getString("ci.smb.password");
                break;
            default:
                smbShare = "NOT SUPPORTED";
                smbUsername = "NOT SUPPORTED";
                smbPassword = "NOT SUPPORTED";
        }
    }

    /**
     * Samba server + folder.
     * Note: The input file must exist on the share
     * when DataServlet is used to import the file.
     * @return
     */
    public static String getSmbShare() {
        return smbShare;
    }

    /**
     * Username to connect to the samba share
     * @return
     */
    public static String getSmbUsername() {
        return smbUsername;
    }

    /**
     * Password for the samba share
     * @return
     */
    public static String getSmbPassword() {
        return smbPassword;
    }


    @Override
    public String getProductName() {
        return "KBTools Tests";
    }

    @Override
    public String getBaseUrl() {
        switch (TestEnvironment.fromEnvironment()) {
            case DEVQA1:
                return "http://kbtools001.env1.devqa.sersol.il.pqe:8080/jtools";
            case DEVQA2:
                return "http://kbtools001.env2.devqa.sersol.il.pqe:8080/jtools";
            case PRODUCTION:
                return "http://kbtools.sea.sersol.lib:8080/jtools";
            case STAGING:
                return "http://kbtools001.set1.stage.sersol.lib:8080/jtools";
            case CI:
                return "http://kbtools-ci.devqa.sersol.il.pqe:8080/jtools";
            default:
                int port = Integer.getInteger("cargo.port", 8080);

                if (Boolean.getBoolean("dev"))
                    return String.format("http://localhost:%d/jtools", port);

                String node = System.getenv("NODE_NAME");
                return String.format(CI_FMT_STR, node == null ? CI_DEFAULT : node, port);
        }
    }
}
