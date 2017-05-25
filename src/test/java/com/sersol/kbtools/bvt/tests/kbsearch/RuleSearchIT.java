package com.sersol.kbtools.bvt.tests.kbsearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import com.sersol.kbtools.bvt.pages.*;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;

/**
 * @author Santiago Gonzalez
 *
 */
public class RuleSearchIT extends KBToolsTest implements ITestConstants {
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
    public void simpleRuleSearch() {
        testCaseId = "134780";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setRuleId("27114");
        ruleSearchForm.setDatabaseCode("A3C");

        ruleSearchForm.clickSearchButton();

        List<WebElement> trCollection = ruleSearchForm.utils.getTableRowsCollection("/html/body/div[2]/table");

        for (WebElement trElement : trCollection) {

            List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));

            for (WebElement tdElement : tdCollection) {

                assertThat(tdElement.getText().contains("Error"), is(false));
                assertThat(tdElement.getText().contains("Exception"), is(false));

            }
        }
    }

    @Test
    public void createRuleFromRuleSearch() {
        testCaseId = "149101";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setDatabaseCode(RULE_SEARCH_DB_CODE);

        ruleSearchForm.clickSearchButton();

        EditRuleForm editRuleForm = ruleSearchForm.clickAddRuleLink();

        assertThat(editRuleForm.getPageTitle(), containsString(PAGETITLE_EDIT_RULE));

        editRuleForm.setMatchTitle(RULE_MATCH_TITLE_SHIPS);
        editRuleForm.setChange1Title(RULE_CHANGE_TITLE_BANANAS);
        editRuleForm.setDescription(RULE_DESCRIPTION1);
        editRuleForm.setNote(RULE_CHANGE_NOTE1);
        ViewRulesForm viewRulesForm = editRuleForm.clickSaveButton();

        int ruleIndex = viewRulesForm.getRuleIndex(RULE_DESCRIPTION1);
        assertThat("Rule not found on View Rules page", ruleIndex > -1, is(true));
        assertThat(viewRulesForm.getMatchTitle(ruleIndex), containsString(RULE_MATCH_TITLE_SHIPS));
        assertThat(viewRulesForm.getChangeTitle(ruleIndex), containsString(RULE_CHANGE_TITLE_BANANAS));
        assertThat(viewRulesForm.getLatestChangeNote(ruleIndex), containsString(RULE_CHANGE_NOTE1));

        //Clean up
        viewRulesForm.clickDeleteLink(RULE_DESCRIPTION1);
        viewRulesForm.clickConfirmAlert();
    }

    @Test
    public void createRuleFromViewRules() {
        testCaseId = "149102";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setDatabaseCode(RULE_SEARCH_DB_CODE2);

        ruleSearchForm.clickSearchButton();

        EditRuleForm editRuleForm = ruleSearchForm.clickAddRuleLink();

        assertThat(editRuleForm.getPageTitle(), containsString(PAGETITLE_EDIT_RULE));
        ViewRulesForm viewRulesForm = createChangeRule(editRuleForm, RULE_MATCH_TITLE2, RULE_CHANGE_TITLE2, RULE_DESCRIPTION2, RULE_CHANGE_NOTE2);

        int ruleIndex = viewRulesForm.getRuleIndex(RULE_DESCRIPTION2);
        assertThat("Rule not found on View Rules page", ruleIndex > -1, is(true));
        assertThat(viewRulesForm.getMatchTitle(ruleIndex), containsString(RULE_MATCH_TITLE2));
        assertThat(viewRulesForm.getChangeTitle(ruleIndex), containsString(RULE_CHANGE_TITLE2));
        assertThat(viewRulesForm.getLatestChangeNote(ruleIndex), containsString(RULE_CHANGE_NOTE2));

        editRuleForm = viewRulesForm.clickAddNewRule();
        viewRulesForm = createChangeRule(editRuleForm, TITLE_TYPE_MONOGRAPH, RULE_MATCH_TITLE3, TITLE_TYPE_MONOGRAPH, RULE_CHANGE_TITLE3, RULE_DESCRIPTION3, RULE_CHANGE_NOTE3);

        ruleIndex = viewRulesForm.getRuleIndex(RULE_DESCRIPTION3);
        assertThat("Rule not found on View Rules page", ruleIndex > -1, is(true));
        assertThat(viewRulesForm.getMatchTitle(ruleIndex), containsString(RULE_MATCH_TITLE3));
        assertThat(viewRulesForm.getChangeTitle(ruleIndex), containsString(RULE_CHANGE_TITLE3));
        assertThat(viewRulesForm.getLatestChangeNote(ruleIndex), containsString(RULE_CHANGE_NOTE3));

        PageRegistry.get(HomePage.class).clickMainMenu();
        ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();
        ruleSearchForm.setDatabaseCode(RULE_SEARCH_DB_CODE2);
        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());
        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(1));

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        int serialRuleRowIndex = serialsTable.findElements(By.cssSelector(CSSPATH_RULES)).size() - 1;    //get last rule
        WebElement serialRuleRow = ruleSearchForm.utils.getTableRow(serialsTable, CSSPATH_RULES, serialRuleRowIndex);
        assertThat("Rule not found in Serials Table", serialRuleRow != null, is(true));

        String title = ruleSearchForm.utils.getTableCellText(serialRuleRow, INDEX_RULE_TITLE);
        assertThat(title, containsString(RULE_MATCH_TITLE2));

        String type = ruleSearchForm.utils.getTableCellText(serialRuleRow, INDEX_RULE_TYPE);
        assertThat(type, containsString(RULE_TYPE_CHANGE));

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE_AFTER_SERIALS);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        int monographRuleRowIndex = monographsTable.findElements(By.cssSelector(CSSPATH_RULES)).size() - 1;    //get last rule
        WebElement monographRuleRow = ruleSearchForm.utils.getTableRow(monographsTable, CSSPATH_RULES, monographRuleRowIndex);
        assertThat("Rule not found in Mongraphs Table", monographRuleRow != null, is(true));

        title = ruleSearchForm.utils.getTableCellText(monographRuleRow, INDEX_RULE_TITLE);
        assertThat(title, containsString(RULE_MATCH_TITLE3));

        type = ruleSearchForm.utils.getTableCellText(monographRuleRow, INDEX_RULE_TYPE);
        assertThat(type, containsString(RULE_TYPE_CHANGE));

        //Clean up
        String ruleId = ruleSearchForm.utils.getTableCellText(serialRuleRow, INDEX_RULE_ID);
        viewRulesForm = ruleSearchForm.clickRuleIdLink(ruleId);
        viewRulesForm.clickDeleteLink(RULE_DESCRIPTION2);
        viewRulesForm.clickConfirmAlert();
        viewRulesForm.clickDeleteLink(RULE_DESCRIPTION3);
        viewRulesForm.clickConfirmAlert();
    }

    @Test
    public void ruleSummaryAndViewRules() {
        testCaseId = "149103";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setDatabaseCode(RULE_SEARCH_DB_CODE3);

        ruleSearchForm.clickSearchButton();
        int totalRulesBefore = ruleSearchForm.utils.getElements(CSSPATH_RULES).size();

        //Create serial change rule
        EditRuleForm editRuleForm = ruleSearchForm.clickAddRuleLink();
        assertThat(editRuleForm.getPageTitle(), containsString(PAGETITLE_EDIT_RULE));
        ViewRulesForm viewRulesForm = createChangeRule(editRuleForm, RULE_MATCH_TITLE4, RULE_CHANGE_TITLE4, RULE_DESCRIPTION4, RULE_CHANGE_NOTE4);

        int ruleIndex = viewRulesForm.getRuleIndex(RULE_DESCRIPTION4);
        assertThat("Rule not found on View Rules page", ruleIndex > -1, is(true));

        //Create monograph change rule
        editRuleForm = viewRulesForm.clickAddNewRule();
        viewRulesForm = createChangeRule(editRuleForm, TITLE_TYPE_MONOGRAPH, RULE_MATCH_TITLE5, TITLE_TYPE_MONOGRAPH, RULE_CHANGE_TITLE5, RULE_DESCRIPTION5, RULE_CHANGE_NOTE5);

        ruleIndex = viewRulesForm.getRuleIndex(RULE_DESCRIPTION5);
        assertThat("Rule not found on View Rules page", ruleIndex > -1, is(true));

        PageRegistry.get(HomePage.class).clickMainMenu();
        ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();
        ruleSearchForm.setDatabaseCode(RULE_SEARCH_DB_CODE3);
        ruleSearchForm.clickSearchButton();

        //Verify rule count is increased by 2 on Rule Search page
        int totalRulesAfter = ruleSearchForm.utils.getElements(CSSPATH_RULES).size();
        assertThat("Expected rule count: " + Integer.toString(totalRulesBefore + 2) + ", Actual: " + Integer.toString(totalRulesAfter), totalRulesAfter, is(totalRulesBefore + 2));

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        //Click the ruleID of one of the rules
        int serialRuleRowIndex = serialsTable.findElements(By.cssSelector(CSSPATH_RULES)).size() - 1;    //get last rule
        WebElement serialRuleRow = ruleSearchForm.utils.getTableRow(serialsTable, CSSPATH_RULES, serialRuleRowIndex);
        String ruleId = ruleSearchForm.utils.getTableCellText(serialRuleRow, INDEX_RULE_ID);
        viewRulesForm = ruleSearchForm.clickRuleIdLink(ruleId);

        //Verify the total rules count is correct on View Rules page
        String totalRulesLinkText = viewRulesForm.getRuleCountInLinkText();
        assertThat(Integer.parseInt(totalRulesLinkText), is(totalRulesAfter));

        //Click the <DBcode> has <X> rules link, and verify the page displays all rules
        viewRulesForm.utils.clickLink(totalRulesLinkText);
        int totalRules = viewRulesForm.getTotalRules();
        assertThat(totalRules, is(totalRulesAfter));

        //Click a ruleID, and verify the page displays just one rule
        viewRulesForm.utils.clickLink(ruleId);
        totalRules = viewRulesForm.getTotalRules();
        assertThat(totalRules, is(1));

        //Clean up
        viewRulesForm.utils.clickLink(totalRulesLinkText);
        viewRulesForm.clickDeleteLink(RULE_DESCRIPTION4);
        viewRulesForm.clickConfirmAlert();
        viewRulesForm.clickDeleteLink(RULE_DESCRIPTION5);
        viewRulesForm.clickConfirmAlert();
    }

    @Test
    public void searchOnDatabaseCode() {
        testCaseId = "149104";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setDatabaseCode(RULE_SEARCH_DB_CODE_7XB); //this database should contain both serial and monograph rules

        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());
        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(1));

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() >= 9, is(true));  //There should be at least one row of 9 headers
        assertThat(serialRuleHeaders.size() % 9, is(0));   //Every rule in the table has a row of 9 headers above it

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() >= 9, is(true));  //There should be at least one rule with 9 values
        assertThat(serialRuleValues.size() % 9, is(0));   //Every rule in the table has 9 values

        verifySerialRuleTableValues(serialRuleValues, "byDbCode");

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE_AFTER_SERIALS);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        List<WebElement> monographRuleHeaders = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(monographRuleHeaders.size() >= 10, is(true));  //There should be at least one row of 10 headers
        assertThat(monographRuleHeaders.size() % 10, is(0));   //Every rule in the table has a row of 10 headers above it

        verifyMonographRulesTableHeaders(monographRuleHeaders);

        List<WebElement> monographRuleValues = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(monographRuleValues.size() >= 10, is(true));  //There should be at least one rule with 10 values
        assertThat(monographRuleValues.size() % 10, is(0));   //Every rule in the table has 10 values

        verifyMonographRuleTableValues(monographRuleValues, "byDbCode");
    }

    @Test
    public void searchOnID() {
        testCaseId = "149105";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setId(RULE_SEARCH_ISSN1);

        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() >= 9, is(true));  //There should be at least one row of 9 headers
        assertThat(serialRuleHeaders.size() % 9, is(0));   //Every rule in the table has a row of 9 headers above it

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() >= 10, is(true));
        assertThat(serialRuleValues.size() % 10, is(0));

        verifySerialRuleTableValues(serialRuleValues, "byId");

        //for every ten cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(serialRuleValues, 10);

        //for every ten cells in the table there should be one cell that spans 8 columns and contains database links
        verifyDatabaseRow(serialRuleValues, 10, 8);

        //Search for monograph rule by ISBN
        ruleSearchForm.setId(RULE_SEARCH_ISBN1);
        ruleSearchForm.clickSearchButton();
        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(0));

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        List<WebElement> monographRuleHeaders = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(monographRuleHeaders.size() >= 10, is(true));  //There should be at least one row of 10 headers
        assertThat(monographRuleHeaders.size() % 10, is(0));   //Every rule in the table has a row of 10 headers above it

        verifyMonographRulesTableHeaders(monographRuleHeaders);

        List<WebElement> monographRuleValues = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(monographRuleValues.size() >= 11, is(true));  //There should be at least 1 rule with 11 values
        assertThat(monographRuleValues.size() % 11, is(0));   //Every rule in the table has 11 values

        verifyMonographRuleTableValues(monographRuleValues, "byId");

        //for every eleven cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(monographRuleValues, 11);

        //for every eleven cells in the table there should be one cell that spans 9 columns and contain database links
        verifyDatabaseRow(monographRuleValues, 11, 9);
    }

    @Test
    public void searchOnProviderCode() {
        testCaseId = "149106";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setProvider(RULE_SEARCH_PROVIDER1);

        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() >= 9, is(true));  //There should be at least one row of 9 headers
        assertThat(serialRuleHeaders.size() % 9, is(0));   //Every rule in the table has a row of 9 headers above it

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() >= 10, is(true));
        assertThat(serialRuleValues.size() % 10, is(0));

        verifySerialRuleTableValues(serialRuleValues, "byProviderCode");

        //for every ten cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(serialRuleValues, 10);

        //for every ten cells in the table there should be one cell that spans 8 columns and contain database links
        verifyDatabaseRow(serialRuleValues, 10, 8);


        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(1));

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE_AFTER_SERIALS);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        List<WebElement> monographRuleHeaders = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(monographRuleHeaders.size() >= 10, is(true));  //There should be at least one row of 9 headers
        assertThat(monographRuleHeaders.size() % 10, is(0));   //Every rule in the table has a row of 9 headers above it

        verifyMonographRulesTableHeaders(monographRuleHeaders);

        List<WebElement> monographRuleValues = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(monographRuleValues.size() >= 11, is(true));  //There should be at least 1 rule with 11 values
        assertThat(monographRuleValues.size() % 11, is(0));   //Every rule in the table has 11 values

        verifyMonographRuleTableValues(monographRuleValues, "byProviderCode");

        //for every eleven cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(monographRuleValues, 11);

        //for every eleven cells in the table there should be one cell that spans 9 columns and contains database links
        verifyDatabaseRow(monographRuleValues, 11, 9);
    }

    @Test
    public void searchOnTitle() {
        testCaseId = "149107";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setTitle(RULE_SEARCH_SERIAL_TITLE1);
        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() == 9, is(true));  //There should be one row of 9 headers

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() == 10, is(true));

        verifySerialRuleTableValues(serialRuleValues, "byTitle");

        //for every ten cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(serialRuleValues, 10);

        //for every ten cells in the table there should be one cell that spans 8 columns and contain database links
        verifyDatabaseRow(serialRuleValues, 10, 8);

        //Search for monograph title
        ruleSearchForm.setTitle(RULE_SEARCH_MONOGRAPH_TITLE1);
        ruleSearchForm.clickSearchButton();

        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(0));

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        List<WebElement> monographRuleHeaders = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(monographRuleHeaders.size() == 10, is(true));  //There should one row of 9 headers

        verifyMonographRulesTableHeaders(monographRuleHeaders);

        List<WebElement> monographRuleValues = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(monographRuleValues.size() == 11, is(true));  //There should be 1 rule with 11 values

        verifyMonographRuleTableValues(monographRuleValues,"byTitle");

        //for every eleven cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(monographRuleValues, 11);

        //for every eleven cells in the table there should be one cell that spans 9 columns and contains database links
        verifyDatabaseRow(monographRuleValues, 11, 9);
    }

    @Test
    public void searchOnTitleCode() {
        testCaseId = "149108";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setTitleCode(RULE_SEARCH_SERIAL_TITLECODE1);
        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() >= 9, is(true));  //There should be at least one row of 9 headers
        assertThat(serialRuleHeaders.size() % 9, is(0));   //Every rule in the table has a row of 9 headers above it

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() >= 10, is(true));
        assertThat(serialRuleValues.size() % 10, is(0));

        verifySerialRuleTableValues(serialRuleValues, "byTitleCode");

        //for every ten cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(serialRuleValues, 10);

        //for every ten cells in the table there should be one cell that spans 8 columns and contain database links
        verifyDatabaseRow(serialRuleValues, 10, 8);

        //Search for monograph title
        ruleSearchForm.setTitleCode(RULE_SEARCH_MONOGRAPH_TITLECODE1);
        ruleSearchForm.clickSearchButton();

        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(0));

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        List<WebElement> monographRuleHeaders = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(monographRuleHeaders.size() >= 10, is(true));  //There should at least one row of 9 headers
        assertThat(monographRuleHeaders.size() % 10, is(0));   //Every rule in the table has a row of 10 headers above it

        verifyMonographRulesTableHeaders(monographRuleHeaders);

        List<WebElement> monographRuleValues = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(monographRuleValues.size() >= 11, is(true));  //There should be at at least 1 rule with 11 values
        assertThat(monographRuleValues.size() % 11, is(0));   //Every rule in the table has 11 values

        verifyMonographRuleTableValues(monographRuleValues, "byTitleCode");

        //for every eleven cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(monographRuleValues, 11);

        //for every eleven cells in the table there should be one cell that spans 9 columns and contains database links
        verifyDatabaseRow(monographRuleValues, 11, 9);
    }

    @Test
    public void searchOnUrl() {
        testCaseId = "149109";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setTitle(RULE_SEARCH_TITLE1);
        ruleSearchForm.setUrl(RULE_SEARCH_URL1);
        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() >= 9, is(true));  //There should be at least one row of 9 headers
        assertThat(serialRuleHeaders.size() % 9, is(0));   //Every rule in the table has a row of 9 headers above it

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() >= 10, is(true));
        assertThat(serialRuleValues.size() % 10, is(0));

        verifySerialRuleTableValues(serialRuleValues, "byUrl");

        //for every ten cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(serialRuleValues, 10);

        //for every ten cells in the table there should be one cell that spans 8 columns and contain database links
        verifyDatabaseRow(serialRuleValues, 10, 8);

        //Search for monograph title
        ruleSearchForm.setUrl(RULE_SEARCH_URL2);
        ruleSearchForm.clickSearchButton();

        assertThat("Monographs table label not found", ruleSearchForm.monographsTableLabelExists(0));

        WebElement monographsTable = ruleSearchForm.utils.getTable(INDEX_MONOGRAPHS_TABLE);
        assertThat("Monographs table not found", monographsTable != null, is(true));

        List<WebElement> monographRuleHeaders = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(monographRuleHeaders.size() >= 10, is(true));  //There should at least one row of 9 headers
        assertThat(monographRuleHeaders.size() % 10, is(0));   //Every rule in the table has a row of 10 headers above it

        verifyMonographRulesTableHeaders(monographRuleHeaders);

        List<WebElement> monographRuleValues = monographsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(monographRuleValues.size() >= 11, is(true));  //There should be at at least 1 rule with 11 values
        assertThat(monographRuleValues.size() % 11, is(0));   //Every rule in the table has 11 values

        verifyMonographRuleTableValues(monographRuleValues, "byUrl");

        //for every eleven cells in the table there should be one ID cell that spans two rows
        IdCellSpansRows(monographRuleValues, 11);

        //for every eleven cells in the table there should be one cell that spans 9 columns and contains database links
        verifyDatabaseRow(monographRuleValues, 11, 9);
    }

    @Test
    public void searchOnAllFields() {
        testCaseId = "149110";

        PageRegistry.get(HomePage.class).clickMainMenu();
        RuleSearchForm ruleSearchForm = PageRegistry.get(MainMenuForm.class).selectRuleSearchLink();

        ruleSearchForm.setRuleId(RULE_SEARCH_RULE_ID1);
        ruleSearchForm.setDatabaseCode("AE0");
        ruleSearchForm.setProvider("PRVCDH");
        ruleSearchForm.setTitleCode("ACADANDLITLO");
        ruleSearchForm.setId(RULE_SEARCH_ISSN2);
        ruleSearchForm.setTitle(RULE_SEARCH_TITLE2);
        ruleSearchForm.setUrl(RULE_SEARCH_URL3);
        ruleSearchForm.clickSearchButton();

        assertThat("Serials table label not found", ruleSearchForm.serialsTableLabelExists());

        WebElement serialsTable = ruleSearchForm.utils.getTable(INDEX_SERIALS_TABLE);
        assertThat("Serials table not found", serialsTable != null, is(true));

        List<WebElement> serialRuleHeaders = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_HEADERS));
        assertThat(serialRuleHeaders.size() == 9, is(true));  //There should be one row of 9 headers

        verifySerialRuleTableHeaders(serialRuleHeaders);

        List<WebElement> serialRuleValues = serialsTable.findElements(By.cssSelector(CSSPATH_RULE_VALUES));
        assertThat(serialRuleValues.size() == 9, is(true));

        verifySerialRuleTableValues(serialRuleValues, "byAll");
    }

    private void verifySerialRuleTableValues(List<WebElement> serialRuleValues, String searchType) {
        int valuesPerRule = 10;
        if (searchType.contains("byDbCode")||searchType.contains("byAll")){valuesPerRule = 9;}

        for (int i = 0; i<serialRuleValues.size();i++){
            String cellText = serialRuleValues.get(i).getText();
            switch(i%valuesPerRule){
                case 0: if(searchType.contains("byAll")){
                        assertThat("Unexpected RuleID: " + cellText, cellText.contains(RULE_SEARCH_RULE_ID1));
                    }else {
                        assertThat("Unexpected RuleID: " + cellText, cellText.matches("\\d{1,5}"));
                    }
                    break;
                case 1: assertThat("Unexpected Type: " + cellText, cellText.matches("Change|Add|Delete"));
                    break;
                case 2: if(searchType.contains("byId")) {
                        assertThat("Unexpected ISSN: " + cellText, cellText.contains(RULE_SEARCH_ISSN1));
                    }else if(searchType.contains("byAll")){
                    assertThat("Unexpected ISSN: " + cellText, cellText.contains(RULE_SEARCH_ISSN2));
                    }
                    else{
                        assertThat("Unexpected ISSN: " + cellText, cellText.matches("(\\d{4}-\\d{4})|(\\d{4}-\\d{3}\\w)|[*]") | cellText.isEmpty());
                    }
                    break;
                case 3: if(searchType.equalsIgnoreCase("byTitle")) {
                        assertThat("Unexpected Title: " + cellText, cellText.contains(RULE_SEARCH_SERIAL_TITLE1));
                    } else if(searchType.contains("byAll")){
                        assertThat("Unexpected Title: " + cellText, cellText.contains(RULE_SEARCH_TITLE2));
                    }
                    else{
                        assertThat("Unexpected Title: " + cellText, cellText.matches(".+"));
                    }
                    break;
                case 4: assertThat("Unexpected Start: " + cellText, cellText.matches(".*"));//\d{1,2}/\d{1,2}/\d{4}|
                    break;
                case 5: assertThat("Unexpected Start Text: " + cellText, cellText.matches(".*"));
                    break;
                case 6: assertThat("Unexpected End: " + cellText, cellText.matches(".*"));
                    break;
                case 7: assertThat("Unexpected End Text: " + cellText, cellText.matches(".*"));
                    break;
                case 8: if(searchType.contains("byURL")){
                        assertThat("Unexpected URL: " + cellText, cellText.contains(RULE_SEARCH_URL3));
                    }else {
                        assertThat("Unexpected URL: " + cellText, cellText.matches(".*"));
                    }
                    break;
            }
            if (i==899) break;  //test up to 100 rules to limit running time
        }
    }

    private void verifyMonographRuleTableValues(List<WebElement> monographRuleValues, String searchType) {
        int valuesPerRule = 11;
        if (searchType.contains("byDbCode"))valuesPerRule=10;
        for (int i = 0; i<monographRuleValues.size();i++){
            String cellText = monographRuleValues.get(i).getText();
            switch(i%valuesPerRule){
                case 0: assertThat("Unexpected RuleID: " + cellText, cellText.matches("\\d{1,5}"));
                    break;
                case 1: assertThat("Unexpected Type: " + cellText, cellText.matches("Change|Add|Delete"));
                    break;
                case 2: if(searchType.contains("byId")) {
                        assertThat("Unexpected ISBN: " + cellText, cellText.contains(RULE_SEARCH_ISBN1));
                    }
                    else {
                        assertThat("Unexpected ISBN: " + cellText, cellText.matches("978\\d{10}|[*]|978\\-\\d\\-\\d{6}\\-\\d{2}\\-\\d") | cellText.isEmpty());
                    }
                    break;
                case 3: if(searchType.equalsIgnoreCase("byTitle")) {
                        assertThat("Unexpected Title: " + cellText, cellText.contains(RULE_SEARCH_MONOGRAPH_TITLE1));
                    }   else {
                        assertThat("Unexpected Title: " + cellText, cellText.matches(".+"));
                    }
                    break;
                case 4: assertThat("Unexpected Author: " + cellText, cellText.matches(".*"));//\d{1,2}/\d{1,2}/\d{4}|
                    break;
                case 5: assertThat("Unexpected Editor: " + cellText, cellText.matches(".*"));
                    break;
                case 6: assertThat("Unexpected Publisher: " + cellText, cellText.matches(".*"));
                    break;
                case 7: assertThat("Unexpected Edition: " + cellText, cellText.matches(".*"));
                    break;
                case 8: assertThat("Unexpected Date: " + cellText, cellText.matches(".*"));
                    break;
                case 9: assertThat("Unexpected URL: " + cellText, cellText.matches(".*"));
                    break;
            }
            if (i==999) break;  //test up to 100 rules to limit running time
        }
    }

    private void IdCellSpansRows(List<WebElement> ruleTableValues, int valuesPerRule) {
        for (int i = 0; i<ruleTableValues.size();i++ ){
            if(i%valuesPerRule==0){
                String attribute = ruleTableValues.get(i).getAttribute("rowspan");
                assertThat("ID cell does not span two rows",attribute, containsString("2"));
            }
        }
    }

    private void verifyDatabaseRow(List<WebElement> ruleTableValues, int valuesPerRule, int columnsSpanned) {
        for (int i = 1; i<=ruleTableValues.size();i++ ){
            if(i%valuesPerRule==0){
                WebElement cell = ruleTableValues.get(i - 1);
                String attribute = cell.getAttribute("colspan");
                assertThat("Cell with DBCodes does not span "+Integer.toString(columnsSpanned)+" columns", attribute, containsString(Integer.toString(columnsSpanned)));

                //cell should contain databases associated with the rule
                List<WebElement> cellContents = cell.findElements(By.cssSelector("a"));
                for (int j = 0; j<cellContents.size();j++){
                    attribute = cellContents.get(j).getAttribute("href");
                    assertThat("Cell with DBCodes does not contain database links", attribute,
                            containsString("viewRules.jsp?dbCode="));
                }
            }

        }
    }

    private void verifySerialRuleTableHeaders(List<WebElement> serialRuleHeaders) {
        for (int i = 0; i<serialRuleHeaders.size();i++){
            String headerText = serialRuleHeaders.get(i).getText();
            switch(i%9){
                case 0: assertThat(headerText, containsString("ID"));
                    break;
                case 1: assertThat(headerText, containsString("Type"));
                    break;
                case 2: assertThat(headerText, containsString("ISSN"));
                    break;
                case 3: assertThat(headerText, containsString("Title"));
                    break;
                case 4: assertThat(headerText, containsString("Start"));
                    break;
                case 5: assertThat(headerText, containsString("Start Text"));
                    break;
                case 6: assertThat(headerText, containsString("End"));
                    break;
                case 7: assertThat(headerText, containsString("End Text"));
                    break;
                case 8: assertThat(headerText, containsString("URL"));
                    break;
            }
            if (i==899) break;  //test up to 100 rules to limit running time
        }
    }

    private void verifyMonographRulesTableHeaders(List<WebElement> monographRuleHeaders) {
        for (int i = 0; i<monographRuleHeaders.size();i++){
            String headerText = monographRuleHeaders.get(i).getText();
            switch(i%10){
                case 0: assertThat(headerText, containsString("ID"));
                    break;
                case 1: assertThat(headerText, containsString("Type"));
                    break;
                case 2: assertThat(headerText, containsString("ISBN13"));
                    break;
                case 3: assertThat(headerText, containsString("Title"));
                    break;
                case 4: assertThat(headerText, containsString("Author"));
                    break;
                case 5: assertThat(headerText, containsString("Editor"));
                    break;
                case 6: assertThat(headerText, containsString("Publisher"));
                    break;
                case 7: assertThat(headerText, containsString("Edition"));
                    break;
                case 8: assertThat(headerText, containsString("Date"));
                    break;
                case 9: assertThat(headerText, containsString("URL"));
                    break;
            }
            if (i==999) break;  //test up to 100 rules to limit running time
        }
    }

    private ViewRulesForm createChangeRule(EditRuleForm editRuleForm, String matchTitleType, String matchTitle, String change1TitleType, String change1Title, String description, String changeNote){
        if (!matchTitleType.isEmpty()) {editRuleForm.setMatchTitleType(matchTitleType);}
        editRuleForm.setMatchTitle(matchTitle);
        if (!change1TitleType.isEmpty()) {editRuleForm.setChange1TitleType(change1TitleType);}
        editRuleForm.setChange1Title(change1Title);
        editRuleForm.setDescription(description);
        editRuleForm.setNote(changeNote);
        return editRuleForm.clickSaveButton();
    }

    private ViewRulesForm createChangeRule(EditRuleForm editRuleForm, String matchTitle, String change1Title, String description, String changeNote){
        return this.createChangeRule(editRuleForm, "", matchTitle, "", change1Title, description, changeNote);
    }
}

