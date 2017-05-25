package com.sersol.kbtools.bvt.tests.holdingimporter;

import com.sersol.common.bvt.configuration.DriverManager;
import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportStateResult;
import com.sersol.kbtools.bvt.pages.ExportLogsForm;
import com.sersol.kbtools.bvt.pages.HdiLogsForm;
import com.sersol.kbtools.bvt.pages.HoldingImporterForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.LoadQueueForm;
import com.sersol.kbtools.bvt.pages.MainMenuForm;
import com.sersol.kbtools.bvt.pages.ReviewPageForm;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VerifyUiElementsIT extends BaseHoldingImporter {
    private TestRail client = new TestRail();
    private String testCaseId;

    private WebDriver driver =  DriverManager.getInstance().getDriver();

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
    public void testPageElements() {
        testCaseId = "133040";
        HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();

        assertThat(holdingImporterForm.utils.getTextWithXpath("/html/body/div[2]/form/b").contains("File:"), is(true));
        assertThat(holdingImporterForm.utils.getTextWithXpath("/html/body/div[2]/form/p[1]/b[2]").contains("Default Title Type:"), is(true));
        assertThat(holdingImporterForm.utils.getTextWithXpath("/html/body/div[2]/form/p[1]/b[1]").contains("Augment Live Data"), is(true));
        assertThat(holdingImporterForm.utils.getTextWithXpath("/html/body/div[2]/form/div/p/b").contains("Priority:"), is(true));

        verifySystemStatisticsTable();
        verifyLoadHoldingsFileTable();
        verifyImportLogsTable();

        assertThat(holdingImporterForm.utils.isElementPresent("defaultTitleType"), is(true));
        assertThat(holdingImporterForm.utils.isElementPresent("priority"), is(true));

        assertThat(holdingImporterForm.utils.isElementPresent("holdings"), is(true));
        assertThat(holdingImporterForm.utils.isElementPresent("augment"), is(true));

        assertThat(holdingImporterForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[2]"), is(true));
        assertThat(holdingImporterForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[3]"), is(true));
        assertThat(holdingImporterForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[4]"), is(true));
    }
    private void verifySystemStatisticsTable(){
        WebElement table = getTablesFromPageContent().get(0);
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        String tableHeader = rows.get(0).findElement(By.tagName("th")).getText();
        List<WebElement> staticLabels = rows.get(2).findElements(By.tagName("th"));
        String importErrorLabel = staticLabels.get(0).getText();
        String usersLabel = staticLabels.get(2).getText();

        assertThat(tableHeader, is("System Statistics"));
        assertThat(importErrorLabel, is("Imports with Errors"));
        assertThat(usersLabel, is("Users"));
        assertThat(driver.findElement(By.linkText("Imports to Review")).isDisplayed(), is(true));
        assertThat(driver.findElement(By.linkText("Imports in Queue")).isDisplayed(), is(true));
        assertThat(driver.findElement(By.linkText("LMH Imports")).isDisplayed(), is(true));
        assertThat(driver.findElement(By.linkText("Files Loading")).isDisplayed(), is(true));
    }
    private void verifyLoadHoldingsFileTable(){
        WebElement table = getTablesFromPageContent().get(1);
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        String tableHeader = rows.get(0).findElement(By.tagName("th")).getText();

        assertThat(rows.size(), is(1));
        assertThat(tableHeader, is("Load Holdings File"));
    }
    private void verifyImportLogsTable(){
        WebElement table = getTablesFromPageContent().get(2);
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        String tableHeader = rows.get(0).findElement(By.tagName("th")).getText();

        List<WebElement> subHeaders = rows.get(1).findElements(By.tagName("th"));
        String time = subHeaders.get(0).getText();
        String user = subHeaders.get(1).getText();
        String action = subHeaders.get(2).getText();
        String note = subHeaders.get(3).getText();

        assertThat(tableHeader, is("Import Logs"));
        assertThat(time, is("Time"));
        assertThat(user, is("User"));
        assertThat(action, is("Action"));
        assertThat(note, is("Note"));
    }

    private List<WebElement> getTablesFromPageContent(){
        return driver.findElement(By.className("page_content")).findElements(By.tagName("table"));
    }

    @Test
    public void testHDILogsPageElements() {
        testCaseId = "133041";
        HdiLogsForm hdiLogsForm = PageRegistry.get(HomePage.class).selectHdiLogsLink();

        assertThat(hdiLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/b[1]"), containsString("User:"));
        assertThat(hdiLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/b[2]"), containsString("Action:"));
        assertThat(hdiLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/b[3]"), containsString("Show:"));

        //Verify Search button is present
        assertThat(hdiLogsForm.utils.isElementPresentWithXpath("/html/body/div[2]/form/input[2]"), is(true));

        assertThat(hdiLogsForm.utils.isElementPresent("usr"), is(true));
        assertThat(hdiLogsForm.utils.isElementPresent("act"), is(true));
        assertThat(hdiLogsForm.utils.isElementPresent("limit"), is(true));

        verifyHDILogsTable();
    }
    private void verifyHDILogsTable(){
        WebElement table = getTablesFromPageContent().get(0);
        List<WebElement> headers = table.findElement(By.tagName("tr")).findElements(By.tagName("th"));

        String time = headers.get(0).getText();
        String user = headers.get(1).getText();
        String action = headers.get(2).getText();
        String note = headers.get(3).getText();

        assertThat(time, is("Time"));
        assertThat(user, is("User"));
        assertThat(action, is("Action"));
        assertThat(note, is("Note"));
    }

    @Test
    public void testLMHMonitorPageElements() {
        testCaseId = "133042";

        PageRegistry.get(MainMenuForm.class).clickMainMenu();
        PageRegistry.get(MainMenuForm.class).selectLmhMonitorLink();
        verifyLMHMonitorTable();
    }
    private void verifyLMHMonitorTable(){
        WebElement table = getTablesFromPageContent().get(0);
        List<WebElement> headers = table.findElement(By.tagName("tr")).findElements(By.tagName("th"));

        String date = headers.get(0).getText();
        String databaseCode = headers.get(1).getText();
        String libraryCode = headers.get(2).getText();
        String user = headers.get(3).getText();
        String status = headers.get(4).getText();

        assertThat(date, is("Date"));
        assertThat(databaseCode, is("DataBase Code"));
        assertThat(libraryCode, is("Library Code"));
        assertThat(user, is("User"));
        assertThat(status, is("Status"));
    }

    @Test
    public void testHdiExportLogsPageElements() {
        testCaseId = "133043";
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();

        ExportLogsForm exportLogsForm = PageRegistry.get(HomePage.class).selectExportLogsLink();

        assertThat(exportLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/p[1]/b[1]"), containsString("From:"));
        assertThat(exportLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/p[1]/b[2]"), containsString("To:"));
        assertThat(exportLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/p[2]/b"), containsString("Data:"));
        assertThat(exportLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/p[2]/i[1]"), containsString("HDI Logs"));
        assertThat(exportLogsForm.utils.getTextWithXpath("/html/body/div[2]/form/p[2]/i[2]"), containsString("LMH Logs"));

        assertThat(exportLogsForm.utils.isElementPresent("startDate"), is(true));
        assertThat(exportLogsForm.utils.isElementPresent("endDate"), is(true));

        assertThat(exportLogsForm.utils.isButtonPresentByValue("Reset"), is(true));
        assertThat(exportLogsForm.utils.isButtonPresentByValue("Export"), is(true));
    }

    @Test
    public void testHdiReviewPageElements() throws URISyntaxException {
        testCaseId = "133044";

        String file = "5X5-1title.txt";
        String dbCode = "5X5";

        //load file if it is not in the import queue
        HoldingImportStateResult targetImport = getImportInQueue(dbCode);
        if(targetImport == null){
            loadFile(file);
        }

        ReviewPageForm reviewPageForm = PageRegistry.get(HomePage.class).selectReviewPageLink();
        reviewPageForm.clickStopRefreshLink();

        String text = reviewPageForm.utils.getTextWithXpath("/html/body");

        if(text.contains("Application Failures")){
            verifyApplicationFailuresTable();
        }

        verifyForReviewTable();
        assertThat(reviewPageForm.utils.isButtonPresentByValue("Reject"), is(true));
        assertThat(reviewPageForm.utils.isButtonPresentByValue("Accept"), is(true));
    }
    private void verifyApplicationFailuresTable(){
        WebElement table = getTable("Application Failures");
        List<WebElement> headers = table.findElement(By.tagName("tr")).findElements(By.tagName("th"));

        String db = headers.get(0).getText();
        String databaseName = headers.get(1).getText();
        String error = headers.get(2).getText();
        String user = headers.get(3).getText();

        assertThat(db, is("DB"));
        assertThat(databaseName, is("Database Name"));
        assertThat(error, is("Error"));
        assertThat(user, is("User"));
    }
    private void verifyForReviewTable(){
        WebElement table = getTable("For Review");
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        List<WebElement> firstHeaderRow = rows.get(0).findElements(By.tagName("th"));
        List<WebElement> secondHeaderRow = rows.get(1).findElements(By.tagName("th"));

        String live = firstHeaderRow.get(1).getText();
        String file = firstHeaderRow.get(2).getText();
        String modifications = firstHeaderRow.get(3).getText();
        assertThat(live, is("Live"));
        assertThat(file, is("File"));
        assertThat(modifications, is("Modifications"));

        String db = secondHeaderRow.get(0).getText();
        String databaseName = secondHeaderRow.get(1).getText();
        String uniqueHoldings = secondHeaderRow.get(2).getText();
        String totalHoldings = secondHeaderRow.get(3).getText();
        String normPercent = secondHeaderRow.get(4).getText();
        String uniqueHoldings2 = secondHeaderRow.get(5).getText();
        String totalHoldings2 = secondHeaderRow.get(6).getText();
        String normPercent2 = secondHeaderRow.get(7).getText();
        String add = secondHeaderRow.get(8).getText();
        String change = secondHeaderRow.get(9).getText();
        String delete = secondHeaderRow.get(10).getText();
        String unknowns = secondHeaderRow.get(11).getText();
        String users = secondHeaderRow.get(12).getText();

        assertThat(db, is("DB"));
        assertThat(databaseName, is("Database Name"));
        assertThat(uniqueHoldings, is("Unique\nHoldings"));
        assertThat(totalHoldings, is("Total\nHoldings"));
        assertThat(normPercent, is("Norm %"));
        assertThat(uniqueHoldings2, is("Unique\nHoldings"));
        assertThat(totalHoldings2, is("Total\nHoldings"));
        assertThat(normPercent2, is("Norm %"));
        assertThat(add, is("Add"));
        assertThat(change, is("Change"));
        assertThat(delete, is("Delete"));
        assertThat(unknowns, is("Unknowns"));
        assertThat(users, is("User"));
    }

    /**
     * Finds the table with the table name in Holding Importer - Review Page
     * @param tableName "Application Failures", "Modified Imports" or "For Review"
     * @return null if the table identified by the specified name is not found.
     */
    private WebElement getTable(String tableName){
        List<WebElement> tables = getTablesFromPageContent();
        List<WebElement> tableNames = driver.findElements(By.cssSelector(".page_content h3"));
        for(int i = 0; i < tables.size(); i++){
            WebElement table = tables.get(i);
            if(tableNames.get(i).getText().equals(tableName)){
                return table;
            }
        }

        return null;
    }

    @Test
    public void testHdiLoadQueuePageElements() {
        testCaseId = "133045";
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();

        LoadQueueForm loadQueueForm = PageRegistry.get(HomePage.class).selectLoadQueueLink();

        assertThat(loadQueueForm.utils.getTextWithXpath("/html/body/div[2]/div/h3"), containsString("Load Queue"));
        assertThat(loadQueueForm.utils.getTextWithXpath("/html/body/div[2]/h3"), containsString("This page only shows files/DBs that are currently loading or loads that have failed in the last 24 hours. A failed load does not prevent you from trying the load again; once a load has failed it is out of the system."));
        assertThat(loadQueueForm.utils.getTextWithXpath("/html/body"), containsString("Nothing in the load queue"));
    }
}
