package com.sersol.kbtools.bvt.tests.kbsearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;


import java.net.URISyntaxException;
import java.util.*;

import com.sersol.kbtools.bvt.pages.*;
import com.sersol.kbtools.bvt.tests.smoketests.common.HoldingData;
import com.sersol.kbtools.bvt.utils.HelperMethods;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;

public class HoldingSearchIT extends KBToolsTest implements ITestConstants {
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
	public void resetTestRun(){
		super.resetTestRun();
	}

	@Test
	public void simpleHoldingsSearch() {
        testCaseId = "78770";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setDatabaseCode(DB_CODE);
		holdingSearchForm.setProvider(PROVIDER_CODE);
		holdingSearchForm.setTitleCode(TITLE_CODE);
		holdingSearchForm.setTitle(TITLE_NAME);
		holdingSearchForm.setId(ISSN);
		holdingSearchForm.setTitleURL(TITLE_URL);

		assertThat(holdingSearchForm.isClearButtonPresent(), is(true));

		holdingSearchForm.clickSearchButton();

		assertThat(holdingSearchForm.getPageTitle(), is(PT_HOLDING_SEARCH_VIEW));
		assertThat(holdingSearchForm.getDatabaseCode(), is(DATABASE_CODE));
		assertThat(holdingSearchForm.getDatabaseName(), is(DATABASE_NAME));
		assertThat(holdingSearchForm.getTitleCode(), is(TITLE_CODE));
		assertThat(holdingSearchForm.getISSN(TITLE_NAME), is(ISSN));
		assertThat(holdingSearchForm.getTitle(0), is(TITLE_NAME));
		assertThat(holdingSearchForm.getDateStart(TITLE_NAME), is("05/01/2007"));
		assertThat(holdingSearchForm.getUrl(TITLE_NAME), is(TITLE_URL));
	}

	@Test
	public void testAuthProviderCodesLSHoff() {
        testCaseId = "78719";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractAndAssertResultsDataForLSHoff();
	}

	@Test
	public void testAuthProviderCodesLSHon() {
        testCaseId = "78721";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractAndAssertResultsDataForLSHon();
	}

	@Test
	public void testDbCode() {
        testCaseId = "78723";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractAndAssertResultsDataForDbCode();
	}

	@Test
	public void testFastHoldingSerials() {
        testCaseId = "78738";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setDatabaseCode("ONO");
		holdingSearchForm.clickSearchButton();

		holdingSearchForm.clickSerialLink();
		holdingSearchForm.utils.clickLink("Show All Columns");

		holdingSearchForm.extractInfoAndAssertDataForFastHoldingSerials();
	}

	@Test
	public void testFastHoldingMonographs() {
        testCaseId = "78742";

		ArrayList<HoldingData> monographs = getHoldingData();

		for (HoldingData monograph: monographs) {
			MainMenuForm mainMenuForm = PageRegistry.get(HomePage.class).clickMainMenu();
			HoldingSearchForm holdingSearchForm = mainMenuForm.clickHldgSearchLink();
			holdingSearchForm.setDatabaseCode("5X5");
			holdingSearchForm.clickSearchButton();
			holdingSearchForm.clickMonographLink();
			holdingSearchForm.utils.clickLink("Show All Columns");
			FastHoldingEditForm fastHldgEditForm = holdingSearchForm.clickEdit(0);
			fastHldgEditForm.editHolding(monograph);
			fastHldgEditForm.clickUpdateButton();
			verifyHolding(holdingSearchForm, monograph);
		}
	}

	private ArrayList<HoldingData> getHoldingData(){
		ArrayList <HoldingData> monographs = new ArrayList<>();
		HoldingData monograph1 = new HoldingData();

		monograph1.setTitleType(TitleType.Monograph);
		monograph1.setTitle("How to Talk to Girls " + PageRegistry.get(HelperMethods.class).getRandomNumber(4));
		monograph1.setIsbn10("0061709999");
		monograph1.setIsbn("9780061709999");
		monograph1.setAuthor("Alec Greven");
		monograph1.setEditor("None");
		monograph1.setPublisher("Collins");
		monograph1.setEdition("1st");
		monograph1.setDatePublished("2000");
		monograph1.setUrl("http://www.gutenberg.org/etext/32321");
		monographs.add(monograph1);

		HoldingData monograph2 = new HoldingData();
		monograph2.setTitleType(TitleType.Monograph);
		monograph2.setTitle("Pumpkins " + PageRegistry.get(HelperMethods.class).getRandomNumber(4));
		monograph2.setIsbn10("0312371411");
		monograph2.setIsbn("9780312371411");
		monograph2.setAuthor("Ken Robbins");
		monograph2.setEditor("None");
		monograph2.setPublisher("Square Fish");
		monograph2.setEdition("1st");
		monograph2.setDatePublished("2004");
		monograph2.setUrl("http://www.biodiversitylibrary.org/title/98094");
		monographs.add(monograph2);
		return monographs;
	}

	private void verifyHolding(HoldingSearchForm hldgSearchForm, HoldingData holding) {
		assertThat(hldgSearchForm.titleExists(holding.getTitle()),is(true));
		assertThat(hldgSearchForm.getISBN13(holding.getTitle()),is(holding.getIsbn()));
		assertThat(hldgSearchForm.getAuthor(holding.getTitle()),is(holding.getAuthor()));
		assertThat(hldgSearchForm.getEditor(holding.getTitle()), is(holding.getEditor()));
		assertThat(hldgSearchForm.getPublisher(holding.getTitle()), is(holding.getPublisher()));
		assertThat(hldgSearchForm.getEdition(holding.getTitle()), is(holding.getEdition()));
		assertThat(hldgSearchForm.getDatePublished(holding.getTitle()), is(holding.getDatePublished()));
		assertThat(hldgSearchForm.getUrl(holding.getTitle()), is(holding.getUrl()));
	}

	@Test
	public void testISBNs() {
        testCaseId = "78743";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractInfoAndAssertDataForISBNs();
	}

	@Test
	public void testISSNs() {
        testCaseId = "78744";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractInfoAndAssertDataForISNNs();
	}

	@Test
	public void testLMHProviderCodesLSHoff() {
        testCaseId = "78745";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setProvider("PRVLSH");
		holdingSearchForm.setTitle("Management Decision");
		holdingSearchForm.clickSearchButton();

		holdingSearchForm.clickSerialLink();

		assertThat(holdingSearchForm.getResultsTableTitle(), containsString("Serial Holdings"));
		assertThat(holdingSearchForm.getTableResultsText(), containsString("No results found"));
	}

	@Test
	public void testLMHProviderCodesLSHon() {
        testCaseId = "78746";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setProvider("PRVLSH");
		holdingSearchForm.clickIncludeLshCheckbox();
		holdingSearchForm.setTitle("Management Decision");
		holdingSearchForm.clickSearchButton();

		holdingSearchForm.clickSerialLink();

		assertThat(holdingSearchForm.getResultsTableTitle(), containsString("Serial Holdings"));
		assertThat(holdingSearchForm.getTableResultsText().contains("No results found"), is(false));
	}

	@Test
	public void testPageElements() {
        testCaseId = "78747";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		assertThat(holdingSearchForm.getDatabaseCodeLabel(), containsString("Database Code:"));
		assertThat(holdingSearchForm.getProviderCodeLabel(), containsString("Provider Code:"));
		assertThat(holdingSearchForm.getTitleCodeLabel(), containsString("Title Code:"));
		assertThat(holdingSearchForm.getProviderTitleIdLabel(), containsString("Provider Title ID:"));
		assertThat(holdingSearchForm.getIssnIsbnLabel(), containsString("ISSN/ISBN:"));
		assertThat(holdingSearchForm.getUrlLabel(), containsString("URL (contains):"));

		assertThat(holdingSearchForm.getTitleLabel(), containsString("Title:"));
		assertThat(holdingSearchForm.getSelectsAndCheckboxLabels(), containsString("Is Normalized:"));
		assertThat(holdingSearchForm.getSelectsAndCheckboxLabels(), containsString("Include Library Specific Holdings"));
		assertThat(holdingSearchForm.getExportFileLabel(), containsString("Export Directly to File"));

		assertThat(holdingSearchForm.isDatabaseCodeInputPresent(), is(true));
		assertThat(holdingSearchForm.isProviderInputPresent(), is(true));
		assertThat(holdingSearchForm.isTitleCodeInputPresent(), is(true));
		assertThat(holdingSearchForm.isProviderTitleInputPresent(), is(true));
		assertThat(holdingSearchForm.isIssnIsbnInputPresent(), is(true));
		assertThat(holdingSearchForm.isTitleInputPresent(), is(true));
		assertThat(holdingSearchForm.isUrlInputPresent(), is(true));

		assertThat(holdingSearchForm.isTitleSearchTypeSelectPresent(), is(true));
		assertThat(holdingSearchForm.isTitleTypeSelectPresent(), is(true));
		assertThat(holdingSearchForm.isNormalizedSelectPresent(), is(true));

		assertThat(holdingSearchForm.isExportFileCheckboxPresent(), is(true));
		assertThat(holdingSearchForm.isIncludeLshCheckboxPresent(), is(true));
		assertThat(holdingSearchForm.isEscapeWildcardsCheckboxPresent(), is(true));

		assertThat(holdingSearchForm.isSearchButtonPresent(), is(true));
		assertThat(holdingSearchForm.isClearButtonPresent(), is(true));
		assertThat(holdingSearchForm.isResetButtonPresent(), is(true));
	}

	@Test
	public void testMonographTitleCodes() {
        testCaseId = "78748";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractMonographsTitleCodesToVerify();
	}

	@Test
	public void testSerialTitleCodes() {
        testCaseId = "78749";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.extractSerialsTitleCodesToVerify();
	}

	@Test
	public void testSearchCache() {
        testCaseId = "78754";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		for(int i=0;i<6;i++) {

			holdingSearchForm.setDatabaseCode("ONO");
			holdingSearchForm.clickSearchButton();
			holdingSearchForm.utils.login();
			PageRegistry.get(HomePage.class).selectHoldingSearchLink();
		}

		for(int i=0;i<6;i++) {

			holdingSearchForm.utils.login();
			PageRegistry.get(HomePage.class).selectHoldingSearchLink();
			holdingSearchForm.setDatabaseCode("5X5");
			holdingSearchForm.clickSearchButton();
		}

		holdingSearchForm.utils.login();
		holdingSearchForm.utils.goToUrl("/Holding/viewSearchCache.jsp");

		holdingSearchForm.assertViewSearchCacheTablaData();
	}

	@Test
	public void testMonographsMarcSearchOrder() {
        testCaseId = "78762";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setTitle("Canterbury Tales");
		holdingSearchForm.clickSearchButton();
		holdingSearchForm.utils.clickLink("Authority");
		holdingSearchForm.utils.clickButtonByText("Normalize");
		this.assertTableData(holdingSearchForm, "\\D++\\d{2,3}\\D++");
	}

	@Test
	public void testSerialsMarcSearchOrder() {
		testCaseId = "149082";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setTitle(TITLE_CRITICAL_PSYCHOLOGY);
		holdingSearchForm.setId(ISSN_CRITICAL_PSYCHOLOGY);
		holdingSearchForm.clickSearchButton();
		holdingSearchForm.utils.clickLink("Authority");
		holdingSearchForm.utils.clickButtonByText("Normalize");
		this.assertTableData(holdingSearchForm, "\\D++1000\\+.++");
	}

	private void assertTableData(HoldingSearchForm holdingSearchForm, String marcRecordCount){
		String windowHandle = holdingSearchForm.utils.getWindowHandle();

		Set<String> tabs = holdingSearchForm.utils.getWindowHandles();
		tabs.remove(windowHandle);
		TitleNormalizationHoldingViewPage titleNormalizationHoldingViewPage =
				holdingSearchForm.utils.switchToTitleNormalizationHoldingView(tabs.iterator().next());

		titleNormalizationHoldingViewPage.utils.waitForPageTitle(PT_TITLE_NORMALIZATION_HOLDING_VIEW, 60);

		List<String> marcTitlesArray = new ArrayList<String>();
		List<String> sortedArray = new ArrayList<String>();

		marcTitlesArray = titleNormalizationHoldingViewPage.getMarcTitles();
		sortedArray = marcTitlesArray;
		Collections.sort(sortedArray);

		assertThat(sortedArray.equals(marcTitlesArray), is(true));
		assertThat(marcTitlesArray.size() == 50, is(true));

		assertThat(titleNormalizationHoldingViewPage.utils.elementExists(By.xpath(XPATH_MARC_TABLE_TEXT_DISPLAY_50)), is(true));
		assertThat(titleNormalizationHoldingViewPage.utils.getTextWithXpath(XPATH_MARC_TABLE_TEXT_DISPLAY_50).contains("Result set too large, only" +
				" displaying 50. Please refine your search."), is(true));
		assertThat(titleNormalizationHoldingViewPage.utils.elementExists(By.xpath(XPATH_MARC_COUNT_TEXT)), is(true));
		assertThat(titleNormalizationHoldingViewPage.utils.getTextWithXpath(XPATH_MARC_COUNT_TEXT).matches(marcRecordCount), is(true));

		titleNormalizationHoldingViewPage.utils.closeWindow();
		holdingSearchForm.utils.switchToWindow(windowHandle);

	}

	@Test
	public void testMonoAuthCreateVerify() {
        testCaseId = "78763";

		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setTitle("Moby Dick");
		holdingSearchForm.clickSearchButton();

		holdingSearchForm.clickMonographLink();

		String windowHandle = holdingSearchForm.utils.getWindowHandle();

		holdingSearchForm.utils.clickLink("Authority");
		holdingSearchForm.utils.clickButtonByText("Normalize");

		Set<String> tabs = holdingSearchForm.utils.getWindowHandles();
		assertTrue(tabs.size()>0);
		tabs.remove(windowHandle);

		holdingSearchForm.utils.switchToWindow(tabs.iterator().next());

		holdingSearchForm.utils.clickButtonByText("Create");

		assertThat(holdingSearchForm.utils.getTextWithXpath("/html/body/div[2]/form/h3[1]"), containsString("Title Details"));

		holdingSearchForm.assertTitleDetailsTableData();

		assertThat(holdingSearchForm.utils.getTextWithId("identifiers"), containsString("Identifiers"));
		
		assertThat(holdingSearchForm.utils.getTextWithXpath("/html/body/div[2]/form/h3[2]"), containsString("Title MARC Records"));
		
		holdingSearchForm.assertTitleMarcRecordsTable();

		holdingSearchForm.assertNormalizersTable();
		
		holdingSearchForm.utils.isLinkPresent("Global Anti-Normalizers");
		
		//Verify Preview Changes button is present
		holdingSearchForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[8]");
		
		//Verify Cancel button is present
		holdingSearchForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[9]");
		
		//Verify Reset Form button is present
		holdingSearchForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[10]");
		
		holdingSearchForm.utils.closeWindow();

		holdingSearchForm.utils.switchToWindow(windowHandle);
	}

	@Test
	public void testSearchIsbn() {
        testCaseId = "78764";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
		
		String isbn = "9780071415149";
		
		holdingSearchForm.setId(isbn);
		holdingSearchForm.clickSearchButton();
		holdingSearchForm.clickMonographLink();
		
		holdingSearchForm.assertResultsSearchWithIsbn(isbn);
	}
	
	@Test
	public void testSearchProvider() {
        testCaseId = "78765";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setProvider("PRVONE");
		holdingSearchForm.clickSearchButton();
		
		List<WebElement> trCollection = holdingSearchForm.utils.getTableRowsCollection("/html/body/div[2]/table[3]");

		assertThat(trCollection.size() > 10, is(true));
	}
	
	@Test
	public void testSearchIssn() {
        testCaseId = "78766";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
		
		String issn = "1947-1548";
		
		holdingSearchForm.setId(issn);
		holdingSearchForm.clickSearchButton();
		
		holdingSearchForm.assertResultsSearchWithIssn(issn);
	}
	
	@Test
	public void testNewSearch() {
        testCaseId = "78767";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
		
		holdingSearchForm.setTitle("marathon");
		holdingSearchForm.clickSearchButton();
		holdingSearchForm.utils.clickLink("New Search");
		assertThat(holdingSearchForm.utils.getValueFromInputField("title"), is(""));
	}
	
	@Test
	public void testRefineSearch() {
        testCaseId = "78768";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
		
		String title = "marathon";
		
		holdingSearchForm.setTitle(title);
		holdingSearchForm.clickSearchButton();
		holdingSearchForm.utils.clickLink("Refine Search");
		assertThat(holdingSearchForm.utils.getValueFromInputField("title"), is(title));
	}

    @Test
    public void holdingsSearchCountsForMonographsAndSerials() {
        testCaseId = "75828";
        int titles;
        int norm;
        String normPercentString;
        int normPercent;
        int serials;
        int normSerials;
        String normSerialsPercentString;
        int normSerialsPercent;
        int monographs;
        int normMonographs;
        String normMonographsPercentString;
        int normMonographsPercent;

        int holdingsSearchTotalHoldings;
        int holdingsSearchNormalized;
        int holdingsSearchNormalizedPercent;
        int holdingsSearchSerials;
        int holdingsSearchNormalizedSerials;
        int holdingsSearchNormalizedSerialsPercent;
        int holdingsSearchMonographs;
        int holdingsSearchNormalizedMonographs;
        int holdingsSearchNormalizedMonographsPercent;
        String[] normalizedCountAndPercentage = new String[2];

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode(DB_CODE_MONOGRAPHS_AND_SERIALS);
        databaseSearchForm.clickSearchButton();
        assertThat(databaseSearchForm.getDatabaseCode(), is(DB_CODE_MONOGRAPHS_AND_SERIALS));

        //Get Holdings counts from the DB Search page
        titles = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[5]"));
        norm = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[6]"));
        normPercentString = databaseSearchForm.utils.getTextWithXpath("//td[7]");
        normPercent = Integer.parseInt(normPercentString.substring(0, normPercentString.indexOf("%")));

        serials = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[8]"));
		normSerials = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[9]"));
        normSerialsPercentString = (databaseSearchForm.utils.getTextWithXpath("//td[10]"));
        normSerialsPercent = Integer.parseInt(normSerialsPercentString.substring(0, normSerialsPercentString.indexOf("%")));
        monographs = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[11]"));
        normMonographs = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[12]"));
        normMonographsPercentString = databaseSearchForm.utils.getTextWithXpath("//td[13]");
        normMonographsPercent = Integer.parseInt(normMonographsPercentString.substring(0, normMonographsPercentString.indexOf("%")));

        assertThat("Serials count is zero", serials > 0);
        assertThat("Monographs count is zero", monographs > 0);

        databaseSearchForm.clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

        holdingSearchForm.setDatabaseCode(DB_CODE_MONOGRAPHS_AND_SERIALS);
        holdingSearchForm.clickSearchButton();
        assertThat(holdingSearchForm.getPageTitle(), is(PT_HOLDING_SEARCH_VIEW));
        assertThat(holdingSearchForm.getDatabaseCode(), is(DB_CODE_MONOGRAPHS_AND_SERIALS));
        assertThat(holdingSearchForm.utils.getTextWithXpath("//p[5]/b/font"), is(FIRST_10000_HOLDINGS));

        //Get counts from the Holdings Search page
        holdingsSearchTotalHoldings = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[1]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[1]/td[2]");
        holdingsSearchNormalized = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedPercent = Integer.parseInt(normalizedCountAndPercentage[1]);

        holdingsSearchSerials = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[2]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[2]/td[2]");
        holdingsSearchNormalizedSerials = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedSerialsPercent = Integer.parseInt(normalizedCountAndPercentage[1]);
        holdingsSearchMonographs = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[3]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[3]/td[2]");
        holdingsSearchNormalizedMonographs = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedMonographsPercent = Integer.parseInt(normalizedCountAndPercentage[1]);


        assertThat("Total Holdings is not more than 10000", holdingsSearchTotalHoldings>10000);
        assertThat("Total Holdings is not within 2% of Total Holdings on DB Search page", holdingsSearchTotalHoldings > titles*.98 && holdingsSearchTotalHoldings < titles*1.02);
        assertThat("Normalized Holdings is not within 2% of that on the DB Search page",holdingsSearchNormalized > norm*.98 && holdingsSearchNormalized < norm*1.02);
        assertThat("Normalized Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedPercent >= normPercent-1 && holdingsSearchNormalizedPercent <= normPercent+1);
        assertThat("Serials Holdings is not within 2% of that on the DB Search page", holdingsSearchSerials > serials*.98 && holdingsSearchSerials < serials*1.02);
        assertThat("Normalized Serials is not within 2% of that on the DB Search page",holdingsSearchNormalizedSerials > normSerials*.98 && holdingsSearchNormalizedSerials < normSerials*1.02);
        assertThat("Normalized Serials Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedSerialsPercent >= normSerialsPercent-1 && holdingsSearchNormalizedSerialsPercent <= normSerialsPercent+1);
        assertThat("Monograph Holdings is not within 2% of that on the DB Search page", holdingsSearchMonographs > monographs*.98 && holdingsSearchMonographs < monographs*1.02);
        assertThat("Normalized Monograph Holdings is not within 2% of that on the DB Search page", holdingsSearchNormalizedMonographs > normMonographs*.98 && holdingsSearchNormalizedMonographs < normMonographs*1.02);
        assertThat("Normalized Monograph Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedMonographsPercent >= normMonographsPercent-1 && holdingsSearchNormalizedMonographsPercent <= normMonographsPercent+1);

    }

    @Test
    public void holdingsSearchCountsForMonographs() {
        testCaseId = "75829";
        int titles;
        int norm;
        String normPercentString;
        int normPercent;
        int serials;
        int monographs;
        int normMonographs;
        String normMonographsPercentString;
        int normMonographsPercent;

        int holdingsSearchTotalHoldings;
        int holdingsSearchNormalized;
        int holdingsSearchNormalizedPercent;
        int holdingsSearchSerials;
        int holdingsSearchNormalizedSerials;
        int holdingsSearchNormalizedSerialsPercent;
        int holdingsSearchMonographs;
        int holdingsSearchNormalizedMonographs;
        int holdingsSearchNormalizedMonographsPercent;
        String[] normalizedCountAndPercentage = new String[2];

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode(DB_CODE_MONOGRAPHS);
        databaseSearchForm.clickSearchButton();
        assertThat(databaseSearchForm.getDatabaseCode(), is(DB_CODE_MONOGRAPHS));

        //Get Holdings counts from the DB Search page
        titles = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[5]"));
        norm = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[6]"));
        normPercentString = databaseSearchForm.utils.getTextWithXpath("//td[7]");
        normPercent = Integer.parseInt(normPercentString.substring(0, normPercentString.indexOf("%")));

        serials = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[8]"));
        monographs = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[11]"));
        normMonographs = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[12]"));
        normMonographsPercentString = databaseSearchForm.utils.getTextWithXpath("//td[13]");
        normMonographsPercent = Integer.parseInt(normMonographsPercentString.substring(0, normMonographsPercentString.indexOf("%")));

        assertThat("Serials count is not zero", serials == 0);
        assertThat("Monographs count is zero", monographs > 0);

        databaseSearchForm.clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

        holdingSearchForm.setDatabaseCode(DB_CODE_MONOGRAPHS);
        holdingSearchForm.clickSearchButton();
        assertThat(holdingSearchForm.getPageTitle(), is(PT_HOLDING_SEARCH_VIEW));
        assertThat(holdingSearchForm.getDatabaseCode(), is(DB_CODE_MONOGRAPHS));
        assertThat(holdingSearchForm.utils.getTextWithXpath("//p[5]/b/font"), is(FIRST_10000_HOLDINGS));

        //Get counts from the Holdings Search page
        holdingsSearchTotalHoldings = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[1]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[1]/td[2]");
        holdingsSearchNormalized = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedPercent = Integer.parseInt(normalizedCountAndPercentage[1]);

        holdingsSearchSerials = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[2]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[2]/td[2]");
        holdingsSearchNormalizedSerials = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedSerialsPercent = Integer.parseInt(normalizedCountAndPercentage[1]);
        holdingsSearchMonographs = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[3]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[3]/td[2]");
        holdingsSearchNormalizedMonographs = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedMonographsPercent = Integer.parseInt(normalizedCountAndPercentage[1]);


        assertThat("Total Holdings is not more than 10000", holdingsSearchTotalHoldings>10000);
        assertThat("Total Holdings is not within 2% of Total Holdings on DB Search page", holdingsSearchTotalHoldings > titles*.98 && holdingsSearchTotalHoldings < titles*1.02);
        assertThat("Normalized Holdings is not within 2% of that on the DB Search page",holdingsSearchNormalized > norm*.98 && holdingsSearchNormalized < norm*1.02);
        assertThat("Normalized Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedPercent >= normPercent-1 && holdingsSearchNormalizedPercent <= normPercent+1);
        assertThat("Serials Holdings is not zero", holdingsSearchSerials == 0);
        assertThat("Normalized Serials is not zero",holdingsSearchNormalizedSerials == 0);
        assertThat("Normalized Serials Percent is not 0%", holdingsSearchNormalizedSerialsPercent == 0);
        assertThat("Monograph Holdings is not within 2% of that on the DB Search page", holdingsSearchMonographs > monographs*.98 && holdingsSearchMonographs < monographs*1.02);
        assertThat("Normalized Monograph Holdings is not within 2% of that on the DB Search page", holdingsSearchNormalizedMonographs > normMonographs*.98 && holdingsSearchNormalizedMonographs < normMonographs*1.02);
        assertThat("Normalized Monograph Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedMonographsPercent >= normMonographsPercent-1 && holdingsSearchNormalizedMonographsPercent <= normMonographsPercent+1);

    }

    @Test
    public void holdingsSearchCountsForSerials() {
        testCaseId = "75830";
        int titles;
        int norm;
        String normPercentString;
        int normPercent;
        int serials;
        int normSerials;
        String normSerialsPercentString;
        int normSerialsPercent;
        int monographs;

        int holdingsSearchTotalHoldings;
        int holdingsSearchNormalized;
        int holdingsSearchNormalizedPercent;
        int holdingsSearchSerials;
        int holdingsSearchNormalizedSerials;
        int holdingsSearchNormalizedSerialsPercent;
        int holdingsSearchMonographs;
        int holdingsSearchNormalizedMonographs;
        int holdingsSearchNormalizedMonographsPercent;
        String[] normalizedCountAndPercentage = new String[2];

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode(DB_CODE_SERIALS);
        databaseSearchForm.clickSearchButton();
        assertThat(databaseSearchForm.getDatabaseCode(), is(DB_CODE_SERIALS));

        //Get Holdings counts from the DB Search page
        titles = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[5]"));
        norm = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[6]"));
        normPercentString = databaseSearchForm.utils.getTextWithXpath("//td[7]");
        normPercent = Integer.parseInt(normPercentString.substring(0, normPercentString.indexOf("%")));

        serials = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[8]"));
        normSerials = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[9]"));
        normSerialsPercentString = (databaseSearchForm.utils.getTextWithXpath("//td[10]"));
        normSerialsPercent = Integer.parseInt(normSerialsPercentString.substring(0, normSerialsPercentString.indexOf("%")));
        monographs = Integer.parseInt(databaseSearchForm.utils.getTextWithXpath("//td[11]"));

        assertThat("Serials count is zero", serials > 0);
        assertThat("Monographs count is zero", monographs > 0);

        databaseSearchForm.clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

        holdingSearchForm.setDatabaseCode(DB_CODE_SERIALS);
        holdingSearchForm.clickSearchButton();
        assertThat(holdingSearchForm.getPageTitle(), is(PT_HOLDING_SEARCH_VIEW));
        assertThat(holdingSearchForm.getDatabaseCode(), is(DB_CODE_SERIALS));
        assertThat(holdingSearchForm.utils.getTextWithXpath("//p[5]/b/font"), is(FIRST_10000_HOLDINGS));

        //Get counts from the Holdings Search page
        holdingsSearchTotalHoldings = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[1]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[1]/td[2]");
        holdingsSearchNormalized = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedPercent = Integer.parseInt(normalizedCountAndPercentage[1]);

        holdingsSearchSerials = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[2]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[2]/td[2]");
        holdingsSearchNormalizedSerials = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedSerialsPercent = Integer.parseInt(normalizedCountAndPercentage[1]);
        holdingsSearchMonographs = Integer.parseInt(holdingSearchForm.utils.getTextWithXpath("//table[1]/tbody/tr[3]/td[1]"));
        normalizedCountAndPercentage = getNormalizedCountAndPercentage(holdingSearchForm, "//table[1]/tbody/tr[3]/td[2]");
        holdingsSearchNormalizedMonographs = Integer.parseInt(normalizedCountAndPercentage[0]);
        holdingsSearchNormalizedMonographsPercent = Integer.parseInt(normalizedCountAndPercentage[1]);


        assertThat("Total Holdings is not more than 10000", holdingsSearchTotalHoldings>10000);
        assertThat("Total Holdings is not within 2% of Total Holdings on DB Search page", holdingsSearchTotalHoldings > titles*.98 && holdingsSearchTotalHoldings < titles*1.02);
        assertThat("Normalized Holdings is not within 2% of that on the DB Search page",holdingsSearchNormalized > norm*.98 && holdingsSearchNormalized < norm*1.02);
        assertThat("Normalized Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedPercent >= normPercent-1 && holdingsSearchNormalizedPercent <= normPercent+1);
        assertThat("Serials Holdings is not within 2% of that on the DB Search page", holdingsSearchSerials > serials*.98 && holdingsSearchSerials < serials*1.02);
        assertThat("Normalized Serials is not within 2% of that on the DB Search page",holdingsSearchNormalizedSerials > normSerials*.98 && holdingsSearchNormalizedSerials < normSerials*1.02);
        assertThat("Normalized Serials Percent is not within 2% of that on the DB Search page", holdingsSearchNormalizedSerialsPercent >= normSerialsPercent-1 && holdingsSearchNormalizedSerialsPercent <= normSerialsPercent+1);
        assertThat("Monograph Holdings is zero", holdingsSearchMonographs > 0);
        assertThat("Normalized Monograph Holdings is not zero", holdingsSearchNormalizedMonographs == 0);
        assertThat("Normalized Monograph Percent is not 0%", holdingsSearchNormalizedMonographsPercent == 0);
    }


    public String[] getNormalizedCountAndPercentage(HoldingSearchForm hsf, String xPath){
        String[] normalizedCountAndPercentage = new String[2];
        String normalizedCountAndPercent;

        normalizedCountAndPercent = hsf.utils.getTextWithXpath(xPath);
        normalizedCountAndPercentage[0] = normalizedCountAndPercent.substring(0, normalizedCountAndPercent.indexOf(" "));
        normalizedCountAndPercentage[1] = normalizedCountAndPercent.substring(normalizedCountAndPercent.indexOf("(")+1,normalizedCountAndPercent.indexOf("%"));
        return normalizedCountAndPercentage;
    }

	@Test
	public void testAddSerialToMonographAuthority() {
		testCaseId = "149083";
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

		holdingSearchForm.setTitle("Jane Eyre");
		holdingSearchForm.clickSearchButton();

		holdingSearchForm.clickMonographLink();

		String windowHandle = holdingSearchForm.utils.getWindowHandle();

		holdingSearchForm.utils.clickLink("Authority");
		holdingSearchForm.utils.clickButtonByText("Normalize");

		Set<String> tabs = holdingSearchForm.utils.getWindowHandles();
		assertTrue(tabs.size() > 0);
		tabs.remove(windowHandle);

		TitleNormalizationHoldingViewPage normalizationHoldingView = holdingSearchForm.utils.switchToTitleNormalizationHoldingView(tabs.iterator().next());

		TitleCreatePage titleCreatePage = normalizationHoldingView.clickCreateButton();

		assertThat(titleCreatePage.utils.getTextWithCssPath(CSSPATH_TITLE_DETAILS), containsString(TABLE_TITLE_TITLE_DETAILS));

		//Add serial MARC record to the Authority Title
		titleCreatePage.addMarcRecord(SERIAL_MARC_RECORD);

		//Verify Preview Changes button is enabled, then click it
		assertThat(titleCreatePage.utils.isButtonEnabled(BUTTON_PREVIEW_CHANGES), is(true));
		titleCreatePage.utils.clickButtonByText(BUTTON_PREVIEW_CHANGES);

		assertThat(titleCreatePage.utils.getTextWithCssPath(CSSPATH_MARC_RECORD_CODE_IN_TABLE), containsString(SERIAL_MARC_RECORD));

		WebElement marcRecordsTable = titleCreatePage.getMarcRecordsTable();

		//Verify serial MARC record is in the Title Marc Records table, error message is present, and the row color is red
		String marcRecord = titleCreatePage.getMarcRecordCode(marcRecordsTable);
		assertThat(marcRecord, containsString(SERIAL_MARC_RECORD));


		String actualError = titleCreatePage.getMarcRecordErrorMsg(marcRecordsTable);
		assertThat(actualError,containsString(SERIAL_MARC_RECORD_INVALID_ERROR));

		assertThat(titleCreatePage.marcRecordRowIsRed(marcRecordsTable), is(true));

		titleCreatePage.utils.closeWindow();

		holdingSearchForm.utils.switchToWindow(windowHandle);
	}

	@Test
	public void testSearchHoldingWithDiacritics() throws URISyntaxException {
		testCaseId = "75678";
		String dbCode = "ABTED";
		String title = "!кандинавскаяРDилологияР= Scandinavica";

		//Go to holding search and search for holding.
		PageRegistry.get(HomePage.class).clickMainMenu();
		HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
		holdingSearchForm.holdingSearch(dbCode, title);
		String actual = holdingSearchForm.getTitle(0);
		assertThat("Expected Serial Title: " + title, actual, is(title));
	}
}
