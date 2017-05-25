package com.sersol.kbtools.bvt.tests.kbsearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.text.Collator;
import java.util.*;

import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebElement;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.DatabaseSearchForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;

public class DatabaseSearchIT extends KBToolsTest implements ITestConstants {
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

   	@Test
	public void simpleDatabaseSearch() {
        testCaseId = "134725";
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		assertThat(databaseSearchForm.getPageTitle(), is(PT_DB_SEARCH));

		databaseSearchForm.setDatabaseCode(DS_DATABASE_CODE);
		databaseSearchForm.setDatabaseName(DS_DATABASE_NAME);
		databaseSearchForm.setTitleMin(6000);
		databaseSearchForm.setTitleMax(7000);
		databaseSearchForm.setUnNormTitleMin(300);
		databaseSearchForm.setUnNormTitleMax(500);
		databaseSearchForm.setPercNormalizedMin(90);
		databaseSearchForm.setPercNormalizedMax(99);

		databaseSearchForm.clickSearchButton();

		assertThat(databaseSearchForm.getPageTitle(), is(PT_DB_SEARCH));
		assertThat(databaseSearchForm.utils.tableExists(XPATH_TABLE_SEARCH_RESULTS), is(true));
		assertThat(databaseSearchForm.getDatabaseCode(), is(DS_DATABASE_CODE));
		assertThat(databaseSearchForm.getDatabaseName(), is(DS_DATABASE_NAME));
		assertThat(databaseSearchForm.getProviderCode(), is(DS_PROVIDER_CODE));
	}

	@Test
	public void testSearchSpecialCharacters() {
        testCaseId = "134726";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		databaseSearchForm.setDatabaseName("rkiye Bili");
		databaseSearchForm.clickSearchButton();

		List<WebElement> trCollection = databaseSearchForm.utils.getTableRowsCollection("/html/body/div[2]/table");

		assertThat(trCollection.size()==3, is(true));
	}

	@Test
	public void testNormalizationPercentageByType() {
        testCaseId = "133933";
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		databaseSearchForm.setTitleMin(2550);
		databaseSearchForm.setTitleMax(2570);

		databaseSearchForm.clickSearchButton();

		databaseSearchForm.assertNormalizationPercentageByTypeTableData();
	}

	@Test
	public void testSearchAll() {
        testCaseId = "134729";
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		String databaseCode = "5X5";

		databaseSearchForm.setDatabaseCode(databaseCode);
		databaseSearchForm.clickAllDbsRadioButton();

		databaseSearchForm.clickSearchButton();

		databaseSearchForm.assertSearchAllTableData(databaseCode);
	}

	@Test
	public void testSearchPercentNorm() {
        testCaseId = "134730";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		databaseSearchForm.setPercNormalizedMin(6);
		databaseSearchForm.setPercNormalizedMax(7);

		databaseSearchForm.clickSearchButton();

		assertThat("Unexpected Percent Normalized value",databaseSearchForm.isNormalizedTitlesPercentCorrect(6,7));
	}

	@Test
	public void testSearchNumTitles() {
        testCaseId = "134731";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		databaseSearchForm.setTitleMin(100);
		databaseSearchForm.setTitleMax(120);

		databaseSearchForm.clickSearchButton();

		databaseSearchForm.assertSearchNumTitlesTableData();
	}

	@Test
	public void testSearchName() {
        testCaseId = "134732";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		databaseSearchForm.setDatabaseName("RAND");

		databaseSearchForm.clickSearchButton();

		assertThat("Unexpected database name", databaseSearchForm.dbNamesContain("RAND"));
	}

	@Test
	public void testSearchClear() {
        testCaseId = "134733";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		databaseSearchForm.setDatabaseName("blackwell");
		databaseSearchForm.clickClearButton();

		assertThat(databaseSearchForm.getDatabaseNameInput(), is(""));

		databaseSearchForm.setTitleMin(2);
		databaseSearchForm.setTitleMax(100);
		databaseSearchForm.setUnNormTitleMin(4);
		databaseSearchForm.setUnNormTitleMax(50);
		databaseSearchForm.setPercNormalizedMin(50);
		databaseSearchForm.setPercNormalizedMax(50);

		databaseSearchForm.clickClearButton();

		assertThat(databaseSearchForm.getTitleMinInput(), is(""));
		assertThat(databaseSearchForm.getTitleMaxInput(), is(""));
		assertThat(databaseSearchForm.getUnNormMinInput(), is(""));
		assertThat(databaseSearchForm.getUnNormMaxInput(), is(""));
		assertThat(databaseSearchForm.getNormPercMinInput(), is(""));
		assertThat(databaseSearchForm.getNormPercMaxInput(), is(""));

		databaseSearchForm.clickAllDbsRadioButton();

		databaseSearchForm.clickClearButton();

		assertThat(databaseSearchForm.isNormalizedDbsRadioButtonChecked(), is("true"));
	}

	@Test
	public void testSearchUnnormalized() {
        testCaseId = "134734";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		int unNormTitleMin = 100;
		int unNormTitleMax = 110;

		databaseSearchForm.setUnNormTitleMin(unNormTitleMin);
		databaseSearchForm.setUnNormTitleMax(unNormTitleMax);

		databaseSearchForm.clickSearchButton();

		assertThat("Unnormalized titles verification failed", databaseSearchForm.assertUnnormalizedTableData(unNormTitleMin, unNormTitleMax));
	}

	@Test
	public void testSearchSorting() {
        testCaseId = "134735";

		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setUnNormTitleMin(100);
        databaseSearchForm.setUnNormTitleMax(110);

		databaseSearchForm.clickSearchButton();

        List<String> dbArray = new ArrayList<String>();
		List<String> namesArray = new ArrayList<String>();
        List<String> providerCodesArray = new ArrayList<String>();
        List<String> providerNamesArray = new ArrayList<String>();
		List<Integer> titlesArray = new ArrayList<Integer>();
		List<Integer> normArray = new ArrayList<Integer>();
		List<Integer> percentageArray = new ArrayList<Integer>();
        List<Integer> serialTitlesArray = new ArrayList<Integer>();
        List<Integer> normSerialsArray = new ArrayList<Integer>();
        List<Integer> percentageSerialsArray = new ArrayList<Integer>();
        List<Integer> monoTitlesArray = new ArrayList<Integer>();
        List<Integer> normMonosArray = new ArrayList<Integer>();
        List<Integer> percentageMonosArray = new ArrayList<Integer>();

		List<? super Object> dbArray2 = new ArrayList<>();
		List<? super Object> namesArray2 = new ArrayList<>();
		List<? super Object> providerCodesArray2 = new ArrayList<>();
		List<? super Object> providerNamesArray2 = new ArrayList<>();
		List<? super Object> titlesArray2 = new ArrayList<>();
		List<? super Object> normArray2 = new ArrayList<>();
		List<Integer> percentageArray2 = new ArrayList<Integer>();
        List<Integer> serialTitlesArray2 = new ArrayList<Integer>();
        List<Integer> normSerialsArray2 = new ArrayList<Integer>();
        List<Integer> percentageSerialsArray2 = new ArrayList<Integer>();
        List<Integer> monoTitlesArray2 = new ArrayList<Integer>();
        List<Integer> normMonosArray2 = new ArrayList<Integer>();
        List<Integer> percentageMonosArray2 = new ArrayList<Integer>();

		databaseSearchForm.fillArraysWithTableData(dbArray, namesArray, providerCodesArray, providerNamesArray, titlesArray, normArray, percentageArray,
                serialTitlesArray, normSerialsArray, percentageSerialsArray, monoTitlesArray, normMonosArray, percentageMonosArray);

        databaseSearchForm.utils.clickLink("DB");
        databaseSearchForm.fillDbArrayWithTableData(dbArray2);

        databaseSearchForm.clickNamesColumnSortLink();
		databaseSearchForm.fillNamesArrayWithTableData(namesArray2);

        databaseSearchForm.utils.clickLink("PRV");
        databaseSearchForm.fillArrayWithTableStringData(providerCodesArray2, 2);

        databaseSearchForm.utils.clickLink("Provider Name");
        databaseSearchForm.fillArrayWithTableStringData(providerNamesArray2, 3);

        databaseSearchForm.clickTitlesColumnSortLink();
		databaseSearchForm.fillTitlesArrayWithTableData(titlesArray2);

		databaseSearchForm.clickNormColumnSortLink();
		databaseSearchForm.fillNormArrayWithTableData(normArray2);

		databaseSearchForm.clickNormPercentageColumnSortLink();
		databaseSearchForm.fillArrayWithTableNumericalData(percentageArray2, 6);

        databaseSearchForm.utils.clickLink("Serials");
        databaseSearchForm.fillArrayWithTableNumericalData(serialTitlesArray2, 7);

        databaseSearchForm.utils.clickLink("Norm Serials");
        databaseSearchForm.getPageTitle();
        databaseSearchForm.fillArrayWithTableNumericalData(normSerialsArray2, 8);

        databaseSearchForm.utils.clickLink("Norm Serials %");
        databaseSearchForm.fillArrayWithTableNumericalData(percentageSerialsArray2, 9);

        databaseSearchForm.utils.clickLink("Monographs");
        databaseSearchForm.fillArrayWithTableNumericalData(monoTitlesArray2, 10);

        databaseSearchForm.utils.clickLink("Norm Monographs");
        databaseSearchForm.fillArrayWithTableNumericalData(normMonosArray2, 11);

        databaseSearchForm.utils.clickLink("Norm Monographs %");
        databaseSearchForm.fillArrayWithTableNumericalData(percentageMonosArray2, 12);

        assertThat(dbArray, containsInAnyOrder(dbArray2.toArray()));
        assertThat(namesArray, containsInAnyOrder(namesArray2.toArray()));
        assertThat(providerCodesArray, containsInAnyOrder(providerCodesArray2.toArray()));
		assertThat(titlesArray, containsInAnyOrder(titlesArray2.toArray()));
		assertThat(normArray, containsInAnyOrder(normArray2.toArray()));
		assertThat(percentageArray, containsInAnyOrder(percentageArray2.toArray()));
        assertThat(serialTitlesArray, containsInAnyOrder(serialTitlesArray2.toArray()));
        assertThat(normSerialsArray, containsInAnyOrder(normSerialsArray2.toArray()));
        assertThat(percentageSerialsArray, containsInAnyOrder(percentageSerialsArray2.toArray()));
        assertThat(monoTitlesArray, containsInAnyOrder(monoTitlesArray2.toArray()));
        assertThat(normMonosArray, containsInAnyOrder(normMonosArray2.toArray()));
        assertThat(percentageMonosArray, containsInAnyOrder(percentageMonosArray2.toArray()));
		assertThat(providerNamesArray, containsInAnyOrder(providerNamesArray2.toArray()));
    }

	@Test
	public void testSearchNonNorm() {
        testCaseId = "134736";
	
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

		String databaseCode = "4I9";

		databaseSearchForm.setDatabaseCode(databaseCode);

		databaseSearchForm.clickNonNormDbsRadioButton();

		databaseSearchForm.clickSearchButton();

		databaseSearchForm.assertSearchNonNormTable(databaseCode);
	}

	@Test
	public void testSearchNorm() {
        testCaseId = "134737";
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();
		
		databaseSearchForm.setDatabaseCode("ZCF");
		
		databaseSearchForm.clickNormDbsRadioButton();
		
		databaseSearchForm.clickSearchButton();
		
		List<WebElement> trCollection = databaseSearchForm.utils.getTableRowsCollection("/html/body/div[2]/table");
		
		assertThat(trCollection.size()==2, is(true));
	}

	@Test
	public void testSearchNormPercentSerials() {
        testCaseId = "134738";
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();
		
		databaseSearchForm.setDatabaseCode("RP1");
		
		databaseSearchForm.clickAllDbsRadioButton();
		
		databaseSearchForm.clickSearchButton();
		
		assertThat("Normalized Percentage does not match", databaseSearchForm.isNormalizedSerialsPercentCorrect("RP1"));
	}
	
	@Test
	public void testPageElements() {
        testCaseId = "133934";
		DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();
		
		assertThat(databaseSearchForm.getDatabaseCodeLabel(), containsString("Database Code:"));
		assertThat(databaseSearchForm.getDatabaseNameLabel(), containsString("Database Name:"));
		assertThat(databaseSearchForm.getNumberOfTitlesLabel(), containsString("Number of Titles:"));
		assertThat(databaseSearchForm.getUnNormTitlesLabel(), containsString("Unnormalized Titles:"));
		assertThat(databaseSearchForm.getPercentageNormLabel(), containsString("Percentage Normalized:"));
		assertThat(databaseSearchForm.getRadioButtonsLabel(), containsString("Normalized DBs"));
		assertThat(databaseSearchForm.getRadioButtonsLabel(), containsString("Non-Normalized DBs"));
		assertThat(databaseSearchForm.getRadioButtonsLabel(), containsString("All DBs"));
		assertThat(databaseSearchForm.getAtLeastInputSize(), is("3"));
		assertThat(databaseSearchForm.getButNoMoreThanInputSize(),is("3"));
		
		assertThat(databaseSearchForm.isDatabaseCodeInputPresent(), is(true));
		assertThat(databaseSearchForm.isDbNameInputPresent(), is(true));
		assertThat(databaseSearchForm.isTitleMinInputPresent(), is(true));
		assertThat(databaseSearchForm.isTitleMaxInputPresent(), is(true));
		assertThat(databaseSearchForm.isUnNormMinInputPresent(), is(true));
		assertThat(databaseSearchForm.isUnNormMaxInputPresent(), is(true));
		assertThat(databaseSearchForm.isNormPercMinInputPresent(), is(true));
		assertThat(databaseSearchForm.isNormPercMaxInputPresent(), is(true));
		
		assertThat(databaseSearchForm.isNormDbsRadioButtonPresent(), is(true));
		assertThat(databaseSearchForm.isNonNormDbsRadioButtonPresent(), is(true));
		assertThat(databaseSearchForm.isAllDbsRadioButtonPresent(), is(true));
		
		assertThat(databaseSearchForm.isSearchButtonPresent(), is(true));
		assertThat(databaseSearchForm.isClearButtonPresent(), is(true));
	}

    @Test
    public void testSearchHtmlTagInDatabaseName() {
        testCaseId = "137031";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.clickAllDbsRadioButton();
        databaseSearchForm.setDatabaseName("<TAG>");
        databaseSearchForm.clickSearchButton();

        assertThat("Search result not found", databaseSearchForm.searchResultExists("<TAG>", 1));   //column 1 contains Database Name
    }

    @Test
    public void testSearchHtmlInDatabaseName() {
        testCaseId = "137032";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.clickAllDbsRadioButton();
        databaseSearchForm.setDatabaseName("Something &amp;");
        databaseSearchForm.clickSearchButton();

        assertThat("Search result not found", databaseSearchForm.searchResultExists("Something &amp;", 1));   //column 1 contains Database Name
    }

    @Test
    public void testSearchNormPercentMonographs() {
        testCaseId = "137136";
        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode("M85");

        databaseSearchForm.clickAllDbsRadioButton();

        databaseSearchForm.clickSearchButton();

        assertThat("Normalized Percentage does not match", databaseSearchForm.isNormalizedMonographsPercentCorrect("M85"));
    }

    @Test
    public void testSearchNormPercentSerialsAndMonographs() {
        testCaseId = "137137";
        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode("BPVOX");

        databaseSearchForm.clickAllDbsRadioButton();

        databaseSearchForm.clickSearchButton();

        assertThat("Normalized Percentage does not match", databaseSearchForm.isNormalizedTitlesPercentCorrect("BPVOX"));
    }

    @Test
    public void testSearchWithDatabaseCodeWithSpecialCharacters() {
        testCaseId = "138044";
        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode("-~6");
        databaseSearchForm.clickSearchButton();
        AssertDatabaseSearchResultIsCorrect(databaseSearchForm, "-~6","2009 Cambridge Journals Digital Archive - CJDA Full Collection","PRVAEN","Cambridge University Press");

        databaseSearchForm.setDatabaseCode(".FH");
        databaseSearchForm.clickSearchButton();
        AssertDatabaseSearchResultIsCorrect(databaseSearchForm, ".FH","2012 Cambridge Journals Full Package Standard UK","PRVAEN","Cambridge University Press");

        databaseSearchForm.setDatabaseCode("_50");
        databaseSearchForm.setDatabaseName("Springer "); //Since '_' is a wildcard, specify DB Name to get one search result
        databaseSearchForm.clickSearchButton();
        AssertDatabaseSearchResultIsCorrect(databaseSearchForm, "_50","Springer for R&D Archive - Engineering","PRVAVX","Springer-Verlag");

    }

    private void AssertDatabaseSearchResultIsCorrect(DatabaseSearchForm databaseSearchForm, String dbCode, String dbName, String providerCode, String providerName) {
        assertThat("Database Code is not correct", databaseSearchForm.getDatabaseCode().contains(dbCode));
        assertThat("Database Name is not correct", databaseSearchForm.getDatabaseName().contains(dbName));
        assertThat("Provider Code is not correct", databaseSearchForm.getProviderCode().contains(providerCode));
        assertThat("Provider Name is not correct", databaseSearchForm.getProviderName().contains(providerName));
    }

    @Test
    public void testTotalCounts() {
        testCaseId = "138412";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setUnNormTitleMin(100);
        databaseSearchForm.setUnNormTitleMax(105);

        databaseSearchForm.clickSearchButton();

        List<String> dbArray = new ArrayList<String>();
        List<String> namesArray = new ArrayList<String>();
        List<String> providerCodesArray = new ArrayList<String>();
        List<String> providerNamesArray = new ArrayList<String>();
        List<Integer> titlesArray = new ArrayList<Integer>();
        List<Integer> normArray = new ArrayList<Integer>();
        List<Integer> percentageArray = new ArrayList<Integer>();
        List<Integer> serialTitlesArray = new ArrayList<Integer>();
        List<Integer> normSerialsArray = new ArrayList<Integer>();
        List<Integer> percentageSerialsArray = new ArrayList<Integer>();
        List<Integer> monoTitlesArray = new ArrayList<Integer>();
        List<Integer> normMonosArray = new ArrayList<Integer>();
        List<Integer> percentageMonosArray = new ArrayList<Integer>();

        databaseSearchForm.fillArraysWithTableData(dbArray, namesArray, providerCodesArray, providerNamesArray, titlesArray, normArray, percentageArray,
                serialTitlesArray, normSerialsArray, percentageSerialsArray, monoTitlesArray, normMonosArray, percentageMonosArray);

        Integer totalDbs = databaseSearchForm.getSummaryCounts(1);
        Integer totalTitles = databaseSearchForm.getSummaryCounts(4);
        Integer normTitles = databaseSearchForm.getSummaryCounts(5);
        Integer normPercent = databaseSearchForm.getSummaryCounts(6);
        Integer totalSerials = databaseSearchForm.getSummaryCounts(7);
        Integer normSerials = databaseSearchForm.getSummaryCounts(8);
        Integer normSerialsPercent = databaseSearchForm.getSummaryCounts(9);
        Integer totalMonographs = databaseSearchForm.getSummaryCounts(10);
        Integer normMonographs = databaseSearchForm.getSummaryCounts(11);
        Integer normMonographsPercent = databaseSearchForm.getSummaryCounts(12);


        assertThat("Database count does not match", dbArray.size() == totalDbs);
        assertThat("Titles count does not match", calculateTotalCount(titlesArray) == totalTitles);
        assertThat("Normalized Titles count does not match", calculateTotalCount(normArray) == normTitles);
        assertThat("Normalized Titles Percentage does not match", calculateNormalizedPercentage(normTitles, totalTitles) == normPercent);
        assertThat("Total Serials count does not match", calculateTotalCount(serialTitlesArray) == totalSerials);
        assertThat("Normalized Serials count does not match", calculateTotalCount(normSerialsArray) == normSerials);
        assertThat("Normalized Serials Percentage does not match", calculateNormalizedPercentage(normSerials, totalSerials) == normSerialsPercent);
        assertThat("Total Monographs count does not match", calculateTotalCount(monoTitlesArray) == totalMonographs);
        assertThat("Normalized Monographs count does not match", calculateTotalCount(normMonosArray) == normMonographs);
        assertThat("Normalized Monographs Percentage does not match", calculateNormalizedPercentage(normMonographs, totalMonographs) == normMonographsPercent);
    }

    private int calculateTotalCount(List<Integer> array){
        int total = 0;
        for (int i : array) {
            total += i;
        }
        return total;
    }

    private int calculateNormalizedPercentage(Integer normalizedTitles, Integer totalTitles){
       double normalizedPercent = (double) normalizedTitles/ (double) totalTitles * 100;
       return (int) normalizedPercent;
    }

    @Test
    public void testUnnormalizedDbCodeWithSpecialCharacters() {
        testCaseId = "138413";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseCode("~.-");
        databaseSearchForm.clickNonNormDbsRadioButton();
        databaseSearchForm.clickSearchButton();

        assertThat("Number of results != 1", databaseSearchForm.getSummaryCounts(1)==1);
        assertThat("Unexpected dbCode", databaseSearchForm.getDatabaseCode().contains("~.-"));

        databaseSearchForm.clickNormDbsRadioButton();
        databaseSearchForm.clickSearchButton();

        assertThat("Number of results != 0", databaseSearchForm.getSummaryCounts(1)==0);
    }

    @Test
    public void testSearchNameForSpecialDbCodes() {
        testCaseId = "138448";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseName("Jeremy");
        databaseSearchForm.setTitleMin(1);
        databaseSearchForm.setTitleMax(50);
        databaseSearchForm.clickAllDbsRadioButton();

        databaseSearchForm.clickSearchButton();

        assertThat("Unexpected database name", databaseSearchForm.dbNamesContain("Jeremy"));
        assertThat("Search Result does not contain any dbCodes with special characters", databaseSearchForm.resultIncludesDBCodeWithSpecialChars());
    }

    @Test
    public void testSearchPercentNormalizedForSpecialDbCodes() {
        testCaseId = "138473";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseName("Jeremy");
        databaseSearchForm.setPercNormalizedMin(8);
        databaseSearchForm.setPercNormalizedMax(50);
        databaseSearchForm.clickAllDbsRadioButton();

        databaseSearchForm.clickSearchButton();

        assertThat("Unexpected database name",databaseSearchForm.dbNamesContain("Jeremy"));
        assertThat("Search Result does not contain any dbCodes with special characters", databaseSearchForm.resultIncludesDBCodeWithSpecialChars());
        assertThat("Unexpected Normalized Percent value", databaseSearchForm.isNormalizedTitlesPercentCorrect(8,50));
    }

    @Test
    public void testSearchUnnormalizedTitlesForSpecialDbCodes() {
        testCaseId = "138635";

        DatabaseSearchForm databaseSearchForm = PageRegistry.get(HomePage.class).selectDatabaseSearchLink();

        databaseSearchForm.setDatabaseName("blo");
        databaseSearchForm.setUnNormTitleMin(0);
        databaseSearchForm.setUnNormTitleMax(1);
        databaseSearchForm.clickAllDbsRadioButton();

        databaseSearchForm.clickSearchButton();

        assertThat("Unexpected database name",databaseSearchForm.dbNamesContain("blo"));
        assertThat("Search Result does not contain any dbCodes with special characters", databaseSearchForm.resultIncludesDBCodeWithSpecialChars());
        assertThat("Unexpected Normalized Percent value", databaseSearchForm.assertUnnormalizedTableData(0,1));
    }

}