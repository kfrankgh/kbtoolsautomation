package com.sersol.kbtools.bvt.tests;

import com.sersol.common.bvt.configuration.DriverManager;
import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.HomePage;
import org.junit.AfterClass;
import org.junit.Before;

import com.sersol.common.bvt.automation.BaseTest;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

import java.sql.Driver;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Base test that every test class should inherit from.
 * It manages logging in and closing the browser per test class.
 * Each test method always begins in the main page.
 */
public class KBToolsTest extends BaseTest {
	private static final int MAX_PAGE_LOAD_TIMEOUT_IN_MINUTES = 5;
	private static final int MAX_WAIT_TIMEOUT_IN_SECONDS = 40;

	private static void setDefaultTimeout(){
		WebDriver driver = DriverManager.getInstance().getDriver();
		driver.manage().timeouts().pageLoadTimeout(MAX_PAGE_LOAD_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
	}

	@BeforeClass
	public static void login() {
		setDefaultTimeout();

		LoginTest loginTest = new LoginTest();
		loginTest.login();
		loginTest.verifyLoginTest();
	}

	@Before
	public void goToMainMenu() {
		PageRegistry.get(HomePage.class).clickMainMenu();
	}

	@AfterClass
	public static void closeAllBrowserSessions() {
		try {
			DriverManager.getInstance().terminateSession();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes all the browser instances if there are multiple.
	 * This is useful to reset the run state when the previous test which
	 * opens multiple browsers fails and doesn't run the cleanup logic.
	 * Call this inside of @Before within the test class.
	 */
	public void resetTestRun(){
		Set<String> browsers = DriverManager.getInstance().getDriver().getWindowHandles();
		if(browsers.size() > 1){
			closeAllBrowserSessions();
			login();
		}
	}
}