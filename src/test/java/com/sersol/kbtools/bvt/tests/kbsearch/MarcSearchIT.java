package com.sersol.kbtools.bvt.tests.kbsearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.AdvancedMarcSearchForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.MarcSearchForm;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class MarcSearchIT extends KBToolsTest implements ITestConstants {
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
	public void clickMainMenu() {
		PageRegistry.get(HomePage.class).clickMainMenu();
	}

    @Test
    public void simpleMarcSearch() {
    	testCaseId = "134767";
        MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();
        assertThat(marcSearchForm.getPageTitle(), is(PT_MARC_SEARCH));

        marcSearchForm.setTitleSearchType("Title Begins with");
        marcSearchForm.setSearchCriteria(MARC_TITLE_NAME);
        marcSearchForm.setDisplayField(MARC_DISPLAY_FIELD);
        marcSearchForm.clickSearchButton();

        assertThat(marcSearchForm.getPageTitle(), is(PT_MARC_SEARCH));
        assertThat(marcSearchForm.getRecordCode(), is("lccn2008274058"));
        assertThat(marcSearchForm.getMarcTagContent(), is("020 \u001Fa9780061535659"));
        assertThat(marcSearchForm.getAuthorityId(), is("TC0000186579"));
        assertThat(marcSearchForm.getIdentifier(), is("9780061535659"));
        assertThat(marcSearchForm.getTitle(true), is(MARC_TITLE_NAME));
    }

    @Test
    public void simpleAdvancedMarcSearch() {
        testCaseId = "134768";
        AdvancedMarcSearchForm advancedMarcSearchForm = PageRegistry.get(HomePage.class).selectAdvancedMarcSearchLink();
        assertThat(advancedMarcSearchForm.getPageTitle(), is(PT_ADV_MARC_SEARCH));

        advancedMarcSearchForm.setSearchType("Title");
        advancedMarcSearchForm.setSearchSubType("BeginsWith");
        advancedMarcSearchForm.setSearchCriteria(ADVANCED_MARC_TITLE_NAME);
        advancedMarcSearchForm.setLeaderPosition("07 - Bibliographic level");
        advancedMarcSearchForm.setLeaderValueEquals("s");
        advancedMarcSearchForm.setDisplayField(ADVANCED_MARC_DISPLAY_FIELD);
        advancedMarcSearchForm.clickSearchButton();

        assertThat(advancedMarcSearchForm.getTableLabel(), containsString("Found"));
        assertThat(advancedMarcSearchForm.getPageTitle(), is(PT_ADV_MARC_SEARCH));
        assertTrue(advancedMarcSearchForm.getRecordCode().contains("lccn74645022"));
        assertThat(advancedMarcSearchForm.getMarcTagColumnHeader(), is(ADVANCED_MARC_DISPLAY_FIELD));
        assertTrue(advancedMarcSearchForm.getAuthorityTitleCode().matches("KNITI|"));
        assertThat(advancedMarcSearchForm.getIdentifier(), is("0023-2300"));
        assertThat(advancedMarcSearchForm.getTitle(), is(ADVANCED_MARC_TITLE_NAME));
    }
	
	@Test
	public void testPageElements() {
        testCaseId = "134769";
		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		assertThat(marcSearchForm.getSearchTypeLabel(), containsString("Search Type:"));
		assertThat(marcSearchForm.getDisplayFieldLabel(), containsString("Display Field:"));
		assertThat(marcSearchForm.getBibliographicLabel(), containsString("Bibliographic Level:"));
		assertThat(marcSearchForm.getLimitLabel(), containsString("Limit"));
		assertThat(marcSearchForm.getTipsContent(), containsString("(Using Display Field greatly increases search times)"));
		assertThat(marcSearchForm.getTipsContent(), containsString("(Bibliographic level only applies to Title-based searches)"));;

		assertThat(marcSearchForm.isSearchCriteriaInputPresent(), is(true));
		assertThat(marcSearchForm.isDisplayFieldInputPresent(), is(true));
		assertThat(marcSearchForm.isLimitInputPresent(), is(true));

		assertThat(marcSearchForm.isSearchTypeSelectPresent(), is(true));

		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("Title Equals"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("MARCRecordCode Equals"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("ISBN Equals"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("ISSN Equals"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("Title Begins with"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("LCCN Equals"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("Title Contains"));
		assertThat(marcSearchForm.getSearchTypeSelectContent(), containsString("Title and Word Search"));

		assertThat(marcSearchForm.isBibLevelSelectPresent(), is(true));
		assertThat(marcSearchForm.getBibLevelSelectContent(), containsString("s"));
		assertThat(marcSearchForm.getBibLevelSelectContent(), containsString("m"));

		assertThat(marcSearchForm.isSearchButtonPresent(), is(true));
		assertThat(marcSearchForm.isClearButtonPresent(), is(true));
		assertThat(marcSearchForm.isResetButtonPresent(), is(true));
	}

	@Test
	public void testSerialFieldSearch() {
        testCaseId = "134770";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		marcSearchForm.extractCsvInfoAndAssertTableData();
	}

	@Test
	public void testSearchTypeEquals() {
        testCaseId = "134771";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String stringToSearch = "canine";
		
		marcSearchForm.setTitleSearchType("Title Equals");
		marcSearchForm.setSearchCriteria(stringToSearch);
		marcSearchForm.clickSearchButton();

		marcSearchForm.assertMarcSearchTableData(stringToSearch, 3);
	}
	
	@Test
	public void testSearchTypeMarcRecordCodeEquals() {
        testCaseId = "134772";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String stringToSearch = "lccn2001269231";
		
		marcSearchForm.setTitleSearchType("MARCRecordCode Equals");
		marcSearchForm.setSearchCriteria(stringToSearch);
		marcSearchForm.clickSearchButton();

		marcSearchForm.assertMarcSearchTableData(stringToSearch, 0);
	}

	@Test
	public void testSearchTypeISBNEquals() {
        testCaseId = "134773";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String searchCritera = "9780966677638";
		
		marcSearchForm.setTitleSearchType("ISBN Equals");
		marcSearchForm.setSearchCriteria(searchCritera);
		
		marcSearchForm.clickSearchButton();
		
		marcSearchForm.assertMarcSearchTableData(searchCritera, 2);
	}
	
	@Test
	public void testSearchTypeISSNEquals() {
        testCaseId = "134774";
		
		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String searchCritera = "1057-6622";
		
		marcSearchForm.setTitleSearchType("ISSN Equals");
		marcSearchForm.setSearchCriteria(searchCritera);
		
		marcSearchForm.clickSearchButton();
		
		marcSearchForm.assertMarcSearchTableData(searchCritera, 2);
	}

	@Test
	public void testSearchTypeTitleBegins() {
        testCaseId = "134775";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String stringToSearch = "canine";
		
		marcSearchForm.setTitleSearchType("Title Begins with");
		marcSearchForm.setSearchCriteria(stringToSearch);
		marcSearchForm.clickSearchButton();

		marcSearchForm.assertMarcSearchTableData(stringToSearch, 3);
	}

	@Test
	public void testSearchTypeLccnEquals() {
        testCaseId = "134776";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String stringToSearch = "2001269231";

		marcSearchForm.setTitleSearchType("LCCN Equals");
		marcSearchForm.setSearchCriteria(stringToSearch);
		marcSearchForm.clickSearchButton();

		marcSearchForm.assertMarcSearchTableData(stringToSearch, 0);
	}

	@Test
	public void testSearchTypeTitleContains() {
        testCaseId = "134777";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String stringToSearch = "canine";

		marcSearchForm.setTitleSearchType("Title Contains");
		marcSearchForm.setSearchCriteria(stringToSearch);
		marcSearchForm.clickSearchButton();

		marcSearchForm.assertMarcSearchTableData(stringToSearch, 3);
	}

	@Test
	public void testSearchTypeTitleAndWordSearch() {
        testCaseId = "134778";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		String stringToSearch = "canine";

		marcSearchForm.setTitleSearchType("Title and Word Search");
		marcSearchForm.setSearchCriteria(stringToSearch);
		marcSearchForm.clickSearchButton();

		marcSearchForm.assertMarcSearchTableData(stringToSearch, 3);
	}

	@Test
	public void testMonographFieldSearch() {
        testCaseId = "134779";

		MarcSearchForm marcSearchForm = PageRegistry.get(HomePage.class).selectMarcSearchLink();

		marcSearchForm.extractLccnMonographCsvAndAssertTableData();
	}
}
