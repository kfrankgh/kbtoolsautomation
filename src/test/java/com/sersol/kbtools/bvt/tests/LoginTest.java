package com.sersol.kbtools.bvt.tests;

import com.sersol.common.bvt.automation.BaseTest;
import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.LoginPage;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LoginTest extends BaseTest implements ITestConstants{

    private TestRail client = new TestRail();
    private String testCaseId;

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            try {
                client.postFailingTestRailResult(testCaseId);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        protected void succeeded(Description description) {
            try {
                client.postPassingTestRailResult(testCaseId);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };

    private static final Logger LOG = LoggerFactory.getLogger(LoginTest.class);
    
    @Before
    public void login() {
        sleep(1000);
        try
        {
            LoginPage loginPage = new LoginPage();
            loginPage.login(USERNAME, PASSWORD);
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
    
    @Test
    public void verifyLoginTest() {
        testCaseId = "134788";
        assertTrue(PageRegistry.isRegistered(LoginPage.class));
        assertTrue(PageRegistry.get(LoginPage.class).isLoggedIn());
    }
}
