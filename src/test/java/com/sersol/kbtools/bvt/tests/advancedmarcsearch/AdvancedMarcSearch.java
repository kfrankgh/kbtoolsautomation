package com.sersol.kbtools.bvt.tests.advancedmarcsearch;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.AdvancedMarcSearchForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.MainMenuForm;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;


/**
 * @author Santiago Gonzalez
 *
 */
public class AdvancedMarcSearch extends KBToolsTest implements ITestConstants {
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

	@Before
	public void goToMainMenu() {
	     PageRegistry.get(HomePage.class).clickMainMenu();
	}

	@Test
	public void testPunctuation() throws URISyntaxException {
        testCaseId = "75326";
		AdvancedMarcSearchForm advancedMarcSearchForm = PageRegistry.get(MainMenuForm.class).selectAdvancedMarcSearchLink();

		advancedMarcSearchForm.extractFileInfoAndAssertTableData();
	}
	
	@Test
	public void testHtmlTitles() {
	    testCaseId = "75344";
		AdvancedMarcSearchForm advancedMarcSearchForm = PageRegistry.get(MainMenuForm.class).selectAdvancedMarcSearchLink();
		
		advancedMarcSearchForm.selectSubTypeContainsAnd();
		
		assertThat(advancedMarcSearchForm.isSubTypeSelectPresent(), is(true));
		
		advancedMarcSearchForm.selectTypeISSN();
		advancedMarcSearchForm.selectSubTypeBeginsWith();
		
		advancedMarcSearchForm.setSearchCriteria("1067-9197");
		
		advancedMarcSearchForm.clickSearchButton();
		
		advancedMarcSearchForm.assertSearchResultsTableData2();
		
		goToMainMenu();
		PageRegistry.get(MainMenuForm.class).selectAdvancedMarcSearchLink();
		
		assertThat(advancedMarcSearchForm.isSubTypeSelectPresent(), is(true));
		
		advancedMarcSearchForm.selectTypeTitle();
		advancedMarcSearchForm.selectSubTypeBeginsWith();
		
		advancedMarcSearchForm.setSearchCriteria("<Tag>");
		
		advancedMarcSearchForm.clickSearchButton();
		
		advancedMarcSearchForm.assertSearchResultsTableData2();
		
		goToMainMenu();
		PageRegistry.get(MainMenuForm.class).selectAdvancedMarcSearchLink();
		
		assertThat(advancedMarcSearchForm.isSubTypeSelectPresent(), is(true));
		
		advancedMarcSearchForm.selectTypeISSN();
		advancedMarcSearchForm.selectSubTypeBeginsWith();
		
		advancedMarcSearchForm.setSearchCriteria("1098-3759");
		
		advancedMarcSearchForm.clickSearchButton();
		
		advancedMarcSearchForm.assertSearchResultsTableData3();
	}
	
	@Test
	public void testDiacritics() {
        testCaseId = "75343";
		AdvancedMarcSearchForm advancedMarcSearchForm = PageRegistry.get(MainMenuForm.class).selectAdvancedMarcSearchLink();

		advancedMarcSearchForm.selectSubTypeContainsAnd();
		advancedMarcSearchForm.setSearchCriteria("des sciences de St");
		advancedMarcSearchForm.clickSearchButton();

		List<WebElement> trCollection = advancedMarcSearchForm.getResultsTable("/html/body/div[2]/table");

		assertThat(trCollection.size() >= 31, is(true));
	}
	
	@Test
	public void testSearchOnSubFields() {
        testCaseId = "75345";
		AdvancedMarcSearchForm advancedMarcSearchForm = PageRegistry.get(MainMenuForm.class).selectAdvancedMarcSearchLink();

		advancedMarcSearchForm.clickFieldButton();
		
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "650", "a", "plato", "m");
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "650", "a", "plato", "s");
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "650", "a", "shakespeare", "m");
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "710", "a", "lasers and electro-optics society", "m");
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "710", "a", "lasers and electro-optics society", "s");
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "040", "d", "nu", "m");
		this.assertAdvancedMarcSearchRecordData(advancedMarcSearchForm, "040", "d", "nu", "s");
	}

	/**
	 * @param tag
	 * @param subField
	 * @param searchCriteria
	 * @param equalsField
	 */
	private void assertAdvancedMarcSearchRecordData(AdvancedMarcSearchForm advancedMarcSearchForm, String tag, String subField, String searchCriteria, String equalsField) {
		advancedMarcSearchForm.setTagField(tag);
		advancedMarcSearchForm.setSubField(subField);
		advancedMarcSearchForm.setSearchCriteria(searchCriteria);
		advancedMarcSearchForm.setEqualsField(equalsField);

		advancedMarcSearchForm.clickSearchButton();

		this.assertSearchResultsTableAndClickMarcRecordLink(advancedMarcSearchForm);

		String windowHandle = advancedMarcSearchForm.utils.getWindowHandle();

		Set<String> tabs = advancedMarcSearchForm.utils.getWindowHandles();
		assertTrue(tabs.size() > 1);
		tabs.remove(windowHandle);

		advancedMarcSearchForm.utils.switchToWindow(tabs.iterator().next());

		List<WebElement> divCollection = advancedMarcSearchForm.utils.getDivsCollection("/html/body");

		assertThat(divCollection.get(0).getText().contains(equalsField), is(true));

		boolean tagFound = false;
		boolean subFieldFound = false;

		for(int i=0;i<divCollection.size();i++) {
			//System.out.print("Looking for tag: "+ tag + " in subField: " + divCollection.get(i).getText()+"\n");
			if(divCollection.get(i).getText().contains(tag)) {
				tagFound = true;
				if(divCollection.get(i).getText().toLowerCase().contains(searchCriteria)) {
					subFieldFound = true;
					break;
				}
			}
		}
		assertThat("Tag not found in MARC Record for: tag = " + tag + ", SubField = " + subField + "," +
				" Search Term = " + searchCriteria + ", Record Type = " + equalsField, tagFound);
		assertThat("Search term not found in MARC Record subField for:  tag = " + tag + ", SubField = " + subField +
				", Search Term = " + searchCriteria + ", Record Type = " + equalsField, subFieldFound);

		advancedMarcSearchForm.utils.closeWindow();

		advancedMarcSearchForm.utils.switchToWindow(windowHandle);
	}

	public void assertSearchResultsTableAndClickMarcRecordLink(AdvancedMarcSearchForm advancedMarcSearchForm) {
		List<WebElement> trCollection = advancedMarcSearchForm.utils.getTableRowsCollection("/html/body/div[2]/table");
		assertThat(trCollection.size()>1, is(true));

		WebElement tdElement;
		WebElement trElement;
		List<WebElement> tdCollection;

		trElement = trCollection.get(1);
		tdCollection = trElement.findElements(By.xpath("td"));
		assertThat(tdCollection.size()>1, is(true));

		tdElement = tdCollection.get(0);
		advancedMarcSearchForm.utils.clickLink(tdElement.getText());
	}
}
