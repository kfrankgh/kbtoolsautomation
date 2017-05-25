package com.sersol.kbtools.bvt.configuration;

import com.sersol.common.bvt.configuration.AbstractBVTEnvironment;
import com.sersol.common.bvt.configuration.TestEnvironment;

public class ClientCenterEnvironment extends AbstractBVTEnvironment {

    @Override
    public String getProductName() {return "KBTools Tests in Client Center";}

    @Override
    public String getBaseUrl() {
        switch (TestEnvironment.fromEnvironment()) {
            case DEVQA1:
                return "http://clientcenter.env1.devqa.sersol.il.pqe/CC/Login/";
            case DEVQA2:
                return "http://clientcenter.env2.devqa.sersol.il.pqe/CC/Login/";
            case PRODUCTION:
                return "https://clientcenter.serialssolutions.com/CC/Login/";
            case STAGING:
                return "https://clientcenter.set1.stage.sersol.lib/CC/Login/";
            case CI:
                return "http://libtools-cc-ci.aws.devqa.sersol.il.pqe/CC/Login/";
            default:
                System.out.println("No environment given");
                return "";
        }
    }



}
