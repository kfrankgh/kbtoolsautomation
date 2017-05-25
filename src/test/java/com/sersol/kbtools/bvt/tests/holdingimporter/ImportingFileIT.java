package com.sersol.kbtools.bvt.tests.holdingimporter;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.EditHoldingForm;
import com.sersol.kbtools.bvt.pages.EditRuleForm;
import com.sersol.kbtools.bvt.pages.EvaluateRulesForm;
import com.sersol.kbtools.bvt.pages.HoldingImporterForm;
import com.sersol.kbtools.bvt.pages.HoldingSearchForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.ImportQueueForm;
import com.sersol.kbtools.bvt.pages.ReviewChangesForm;
import com.sersol.kbtools.bvt.pages.RuleSearchForm;
import com.sersol.kbtools.bvt.pages.ViewRulesForm;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImportingFileIT extends BaseHoldingImporter {
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
    public void fileImport() throws URISyntaxException {
        testCaseId = "132962";

        String file = "5X5-1title.txt";
        String dbCode = "5X5";

        String titleBefore = "Serial title for automation";
        String dbCodeToAssociate = "3AL";
        String ruleDescription = "Rule added by automated test case";
        String titleAfter = "Holding edited by Automated Test Case";

        cancelImport(dbCode);

        //LOAD THE FILE
        ImportQueueForm importQueueForm = loadFile(file, dbCode, false);

        //GO TO REVIEW CHANGES PAGE
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);
        ViewRulesForm viewRulesForm = reviewChangesForm.clickViewRulesLink();
        EditRuleForm editRuleForm = viewRulesForm.clickAddNewRule();
        editRuleForm.clickAddButton();

        //FILL FIELDS TO ADD RULE
        editRuleForm.setChange1TitleType("serial");
        editRuleForm.setChange1Title("Automated Rule");
        editRuleForm.setChange1URL("http://www.google.com");
        editRuleForm.setDescription(ruleDescription);
        editRuleForm.setNote("Note for new rule");
        editRuleForm.clickSaveButton();

        //EDIT RULE CREATED
        viewRulesForm.clickEditLink(ruleDescription);
        editRuleForm.setChange1Title("Automated Rule EDITED");
        editRuleForm.setNote("RULE EDITED BY AUTOMATED TEST CASE");
        editRuleForm.clickSaveButton();

        //ASSOCIATE DB TO RULE
        viewRulesForm.clickEditLink(ruleDescription);
        editRuleForm.setDBAssociation(dbCodeToAssociate);
        editRuleForm.clickSubmitButton();

        //REMOVE DB ASSOCIATION TO RULE
        editRuleForm.setDBAssociation(dbCodeToAssociate);
        editRuleForm.clickSubmitButton();
        editRuleForm.setNote("Db associated and removed");
        editRuleForm.clickSaveButton();

        //DEACTIVATE RULE
        viewRulesForm.clickDeactivateLink(ruleDescription);

        //ACTIVATE RULE
        viewRulesForm.clickActivateLink(ruleDescription);

        //DELETE RULE
        viewRulesForm.clickDeleteLink(ruleDescription);
        viewRulesForm.clickConfirmAlert();
        viewRulesForm.clickViewHDILink();

        //ADD NEW HOLDING
        EditHoldingForm editHoldingForm = reviewChangesForm.clickAddHoldingLink();
        editHoldingForm.setHoldingTitleType("Serial");
        editHoldingForm.setHoldingTitle(titleBefore);
        editHoldingForm.setHoldingURL("http://www.google.com");
        editHoldingForm.clickAddHoldingButton();

        //EDIT MONOGRAPH ADDED
        reviewChangesForm.setTitleType("Monograph");
        reviewChangesForm.setFilter("All");
        reviewChangesForm.clickEdit("Automated Holding");
        editHoldingForm.setHoldingTitle(titleAfter);
        editHoldingForm.clickSaveChangesButton();
        reviewChangesForm.restartThenGoToReviewChanges(importQueueForm, dbCode);

        //DELETE SERIAL ADDED
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("All");
        reviewChangesForm.clickEdit(titleBefore);
        editHoldingForm.clickDeleteHoldingButton();
        reviewChangesForm.clickRestartLink();
        importQueueForm.waitForLinkPresent(dbCode);
        this.acceptChanges(dbCode);

        //SEARCH FOR TITLE ADDED
        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, titleAfter);
        holdingSearchForm.clickMonographLink();
        assertThat(holdingSearchForm.getTitle(0), is(titleAfter));
    }

    /**
     * This test was developed to verify Classic-1672
     * @throws URISyntaxException
     */
    @Test
    public void verifyZeroLengthTitleErrorWhileImporting() throws URISyntaxException {
        testCaseId = "133037";
        String dbCode = "5X5";
        String file = "TwoTitlesSpaces.txt";

        cancelImport(dbCode);

        //LOAD THE FILE
        ImportQueueForm importQueueForm = loadFile(file, dbCode, false);
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);
        reviewChangesForm.setTitleType("Monograph");
        reviewChangesForm.setFilter("Errors Only");

        //VERIFY ERROR MESSAGE FOR ZERO LENGHT TITLE IS PRESENT.
        assertThat(reviewChangesForm.getError(0), is("Zero-length Title"));
        reviewChangesForm.clickNoLink();
    }

    /**
     * This test was developed to verify Classic-1672
     * @throws URISyntaxException
     */
    @Test
    public void verifyEmptyTitleErrorWhileImporting() throws URISyntaxException {
        testCaseId = "133038";
        String dbCode = "5X5";
        String file = "TwoTitlesEmpty.txt";

        cancelImport(dbCode);

        //LOAD THE FILE
        ImportQueueForm importQueueForm = loadFile(file, dbCode, false);
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);
        reviewChangesForm.setTitleType("Monograph");
        reviewChangesForm.setFilter("Errors Only");

        //VERIFY ERROR MESSAGE FOR EMPTY TITLE IS PRESENT
        assertThat(reviewChangesForm.getError(0), is("Not even a minimal holding"));
        reviewChangesForm.clickNoLink();
    }


    /**
     * This test was developed to verify Classic-1622
     * @throws URISyntaxException
     */
    @Test
    public void importValidationErrorsWhenApplyingRulesWithErrors() throws URISyntaxException {
        testCaseId = "133039";

        String dbCode = "5X5";
        String errorFile = "1title5x5-error.txt";

        cancelImport(dbCode);

        //LOAD THE FILE
        ImportQueueForm importQueueForm = loadFile(errorFile, dbCode, true);

        //VERIFY IMPORT IS IN ERROR STATE, LINK IS AVAILABLE TO CLICK AND ROW IS RED.
        assertThat(importQueueForm.getDBState(dbCode), is(HOLDING_VALIDATION_ERRORS));
        assertThat(importQueueForm.dbLinkIsPresent(dbCode), is(true));
        assertThat(importQueueForm.rowIsRed(dbCode), is(true));

        //GO TO REVIEW CHANGES PAGE
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);

        //VERIFY FILTER IS IN "ERRORS ONLY" OPTION AND THE ERROR IS OK
        assertThat(reviewChangesForm.getFilter(), is("Errors Only"));
        assertThat(reviewChangesForm.getError(0), is("ISSN Structure is invalid"));

        //VERIFY EVALUATE RULES PAGE IS AVAILABLE AND WORKING.
        EvaluateRulesForm evaluateRulesForm = reviewChangesForm.clickEvaluateRulesLink();
        evaluateRulesForm.selectReviewChangesLink();

        //THIS TEST CASE NEEDS TO BE FINISHED AFTER THE FIX, MEANWHILE AT THE END CANCEL THE IMPORT
        reviewChangesForm.clickNoLink();
    }

    @Test
    public void importFileWithRuleContainingWildCards() throws URISyntaxException {
        testCaseId = "136013";
        String dbCode = "ABTED";
        String updateFile = "ABTED-Update.txt";
        String title = "Chilton's motor age (1970)";
        String anotherTitle = "Motor Age";

        cleanupImportData(dbCode, Integer.parseInt(RULE_ID), ABTED_CLEAN_IMPORT_FILE);

        ImportQueueForm importQueueForm = loadFile(ABTED_CLEAN_IMPORT_FILE, dbCode, false);
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);

        //Verify that the rule has applied
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Any Change");
        int rowIndex = reviewChangesForm.getRowIndexFor(title);
        assertThat(reviewChangesForm.getIssn(rowIndex), is("0193-7022"));
        assertThat(reviewChangesForm.getStart(rowIndex), is("01/01/1984"));
        assertThat(reviewChangesForm.getEnd(rowIndex), is("09/30/1997"));

        rowIndex = reviewChangesForm.getRowIndexFor("Motor Age");
        assertThat(reviewChangesForm.getIssn(rowIndex), is("1520-9385"));
        assertThat(reviewChangesForm.getStart(rowIndex), is("10/01/1997"));

        //Accept changes
        reviewChangesForm.clickYesLink();

        //Go to import queue and wait for the import completed.
        HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        holdingImporterForm.selectImportsInQueueLink();
        importQueueForm.waitForDBprocessing(dbCode);
        importQueueForm.waitForTextNotPresent(dbCode);

        //Verify changed holdings.
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).searchForHolding(dbCode, title);

        assertThat(holdingSearchForm.getTitle(0), is(title));
        assertThat(holdingSearchForm.getISSN(title), is("0193-7022"));
        assertThat(holdingSearchForm.getDateStart(title), is("01/01/1984"));
        assertThat(holdingSearchForm.getDateEnd(title), is("09/30/1997"));

        holdingSearchForm = PageRegistry.get(HomePage.class).searchForHolding(dbCode, anotherTitle);

        assertThat(holdingSearchForm.getTitle(0), is(anotherTitle));
        assertThat(holdingSearchForm.getISSN(anotherTitle), is("1520-9385"));
        assertThat(holdingSearchForm.getDateStart(anotherTitle), is("10/01/1997"));
        assertThat(holdingSearchForm.getDateEnd(anotherTitle), is(""));

        //Since the rule has run we need to edit it if we want it to run again.
        RuleSearchForm ruleSearchForm = PageRegistry.get(HomePage.class).searchForRule(RULE_ID);
        ViewRulesForm viewRulesForm = ruleSearchForm.clickRuleIdLink(RULE_ID);
        EditRuleForm editRuleForm = viewRulesForm.clickEditLink(RULE_DESCRIPTION);
        editRuleForm.setNote("Updated");
        editRuleForm.clickSaveButton();

        //Load file again and verify that there is no validation error
        importQueueForm = loadFile(updateFile, dbCode, false);
        assertThat("Import has a Validation Error", !(importQueueForm.getDBState(dbCode).contains(HOLDING_VALIDATION_ERRORS)));
        reviewChangesForm = importQueueForm.clickDBlink(dbCode);

        //Verify that holdings are not updated by the rule since they already match the change logic of the rule
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Any Change");
        reviewChangesForm.setStartWith(title);
        assertThat("Holding is updated", !reviewChangesForm.titlePresentInTable(title));
        reviewChangesForm.setStartWith(anotherTitle);
        assertThat("Holding is updated", !reviewChangesForm.titlePresentInTable(anotherTitle));

        //Verify that "Holding Conflict" appears on the Evaluate Rules page
        EvaluateRulesForm evaluateRulesForm = reviewChangesForm.clickEvaluateRulesLink();
        assertThat("Rule not found", evaluateRulesForm.getFirstRuleId().contains(RULE_ID));
        assertThat("Rule was not evaluated as \"Holding Conflict\"", evaluateRulesForm.getFirstRuleEvaluation().contains("Holding Conflict"));

        evaluateRulesForm.utils.goToPreviousPage("Review Changes - Holdings Importer - KBTools");

        //Reject import
        reviewChangesForm.clickNoLink();
    }

    @Test
    public void augmentData() throws URISyntaxException {
        testCaseId = "133033";

        String dbCode = "3AL";
        String file = "3AL-2titles.txt";
        String anotherFile = "3AL-DifferentFile.txt";

        // reset test data
        cancelImport(dbCode);
        loadFile(anotherFile, dbCode, false);
        acceptChanges(dbCode);

        //LOAD AUGMENT DATA FILE
        ImportQueueForm importQueueForm = loadFile(file, dbCode, true);

        //VERIFY NO SERIALS ARE DELETED
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Delete");

        String serialTitle = "automated serial for augment data";
        String monographTitle = "automated monograph for augment data";
        String url = "http://site.ebrary.com/lib/sssssssssss/Doc?id=10221094";

        int rowCount = reviewChangesForm.getHoldingsTableRowCount();
        assertThat(rowCount, is(0));

        //VERIFY NO MONOGRAPHS ARE DELETED
        reviewChangesForm.setTitleType("Monograph");

        rowCount = reviewChangesForm.getHoldingsTableRowCount();
        assertThat(rowCount, is(0));

        //VERIFY 1 SERIAL IS ADDED
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Add");
        reviewChangesForm.setStartWith(serialTitle);
        reviewChangesForm.clickUpdateView();

        assertThat(reviewChangesForm.getTitle(0), is(serialTitle));
        assertThat(reviewChangesForm.getUrl(0), is(url));

        //VERIFY 1 MONOGRAPH IS ADDED
        reviewChangesForm.setTitleType("Monograph");
        reviewChangesForm.setStartWith(monographTitle);
        reviewChangesForm.clickUpdateView();

        assertThat(reviewChangesForm.getTitle(0), is(monographTitle));
        assertThat(reviewChangesForm.getAuthor(0), is("monograph author"));
        assertThat(reviewChangesForm.getPublisher(0), is("monograph publisher"));
        assertThat(reviewChangesForm.getPublishedDate(0), is("1996"));
        assertThat(reviewChangesForm.getUrl(0), is(url));

        //ACCEPT CHANGES
        this.acceptChanges(dbCode);

        //SEARCH FOR BOTH TITLES ADDED
        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, monographTitle);

        holdingSearchForm.clickMonographLink();
        assertThat(holdingSearchForm.getTitle(0), is(monographTitle));
        assertThat(holdingSearchForm.getUrl(monographTitle), is(url));
        assertThat(holdingSearchForm.getISBN13(), is("9780920474891"));
        assertThat(holdingSearchForm.getMonographAuthor(), is("monograph author"));
        assertThat(holdingSearchForm.getPublisher(monographTitle), is("monograph publisher"));
        assertThat(holdingSearchForm.getPublishedDate(), is("1996"));
        holdingSearchForm.clickNewSearchLink();
        holdingSearchForm.holdingSearch(dbCode, serialTitle);
        holdingSearchForm.clickSerialLink();
        assertThat(holdingSearchForm.getTitle(0), is(serialTitle));
    }

}
