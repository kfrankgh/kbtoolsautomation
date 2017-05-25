package com.sersol.kbtools.bvt.utils;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kfrankli on 3/11/2015.
 */
public class TestRail {
    private final static String ENVIRONMENT = System.getenv("ENVIRONMENT");
    private final static String TESTRUN = System.getenv("TESTRUN");
    private static EnvInfo env = new EnvInfo();

    private APIClient initializeApiClient() {
        APIClient client = new APIClient("http://testrail.pre.proquest.com/testrail/");
        client.setUser("ken.franklin@proquest.com");
        client.setPassword("test1234");
        return client;
    }

    public void postPassingTestRailResult(String testCaseId) throws Exception {
        try {
            APIClient client = initializeApiClient();
            Map<String, Serializable> data = new HashMap<>();
            data.put("status_id", 1);
            data.put("comment", "This test passed during an automated test run in " + ENVIRONMENT);
            data.put("version", env.getBuildInfo());
            data.put("custom_pqwsenvironment", ENVIRONMENT);
            client.sendPost("add_result_for_case/" + TESTRUN + "/" + testCaseId, data);
        } catch (APIException e) {
            System.out.println("Not posting test result to TestRail.");
        }
    }

    public void postFailingTestRailResult(String testCaseId) throws Exception {
        try {
            APIClient client = initializeApiClient();
            Map<String, java.io.Serializable> data = new HashMap<>();
            data.put("status_id", 5);
            data.put("comment", "This test failed during an automated test run in " + ENVIRONMENT);
            data.put("version", env.getBuildInfo());
            data.put("custom_pqwsenvironment", ENVIRONMENT);
            client.sendPost("add_result_for_case/" + TESTRUN + "/" + testCaseId, data);
        } catch (APIException e) {
            System.out.println("Not posting test result to TestRail.");

        }

    }
}
