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

public class LoadingLiveDataIT extends BaseHoldingImporter {
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
    public void loadLiveData() {
        testCaseId = "133034";
        String title = "New holding for LLD test case";
        String dbCode = "EBB";

        //LOAD LIVE DATA
        HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        ImportQueueForm importQueueForm = holdingImporterForm.LldAndWaitUntilProcessed(dbCode);
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);

        //Verify that no rules were applied or evaluated
        assertThat(reviewChangesForm.getAppliedRules(), is("0"));
        assertThat(reviewChangesForm.getUnappliedRules(), is("0"));

        //Add new holding
        EditHoldingForm editHoldingForm = reviewChangesForm.clickAddHoldingLink();
        editHoldingForm.setHoldingTitleType("Serial");
        editHoldingForm.setHoldingTitle(title);
        editHoldingForm.setHoldingURL("http://www.google.com");
        editHoldingForm.clickAddHoldingButton();

        //Restart import and wait for link present.
        reviewChangesForm.restartThenGoToReviewChanges(importQueueForm, dbCode);

        //Verify that the new holding is showed.
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Add");
        reviewChangesForm.setStartWith(title);
        reviewChangesForm.clickUpdateView();
        assertThat(reviewChangesForm.getTitle(0), is(title));

        //Accept changes
        reviewChangesForm.clickYesLink();

        //Go to import queue and wait for the import completed.
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        holdingImporterForm.selectImportsInQueueLink();
        importQueueForm.waitForDBprocessing(dbCode);
        importQueueForm.waitForTextNotPresent(dbCode);

        //Go to holding search and search for the new holding added.
        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, title);

        assertThat(holdingSearchForm.getTitle(0), is(title));

        //Load live data again
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        holdingImporterForm.clickLLDButton();
        holdingImporterForm.setDBCode(dbCode);
        holdingImporterForm.clickLoadButton();
        importQueueForm.waitForLinkPresent(dbCode);
        importQueueForm.clickDBlink(dbCode);
        reviewChangesForm.setTitleType("Serial");

        //Delete Holding
        reviewChangesForm.clickEditForSerialTitle(title);

        editHoldingForm.clickDeleteHoldingButton();

        //Restart Import
        reviewChangesForm.restartThenGoToReviewChanges(importQueueForm, dbCode);

        //Verify the added holding is being deleted.
        reviewChangesForm.setFilter("Delete");
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setStartWith(title);
        reviewChangesForm.clickUpdateView();
        assertThat(reviewChangesForm.getTitle(0), is(title));

        //Accept import
        this.acceptChanges(dbCode);

        //Go to holding search and search for the new holding added.
        PageRegistry.get(HomePage.class).clickMainMenu();
        PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, title);

        //verify that the holding is not found.
        assertThat(holdingSearchForm.getNumberOfSerialsFound(), is("0"));
    }

    @Test
    public void loadLiveDataWithRuleContainingWildCards() throws URISyntaxException {
        testCaseId = "135554";
        String dbCode = "ABTED";
        String title = "Chilton's motor age (1970)";
        String anotherTitle = "Motor Age";

        cleanupImportData(dbCode, Integer.parseInt(RULE_ID), ABTED_CLEAN_IMPORT_FILE);

        //LOAD LIVE DATA
        HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        ImportQueueForm importQueueForm = holdingImporterForm.LldAndWaitUntilProcessed(dbCode);
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);

        //Verify that no rules were applied or evaluated
        assertThat(reviewChangesForm.getAppliedRules(), is("0"));

        //Restart import and wait for link present.
        reviewChangesForm.restartThenGoToReviewChanges(importQueueForm, dbCode);

        //Verify that the rule has applied
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Any Change");
        int rowIndex = reviewChangesForm.getRowIndexFor(title);
        assertThat(reviewChangesForm.getIssn(rowIndex), is("0193-7022"));
        assertThat(reviewChangesForm.getStart(rowIndex), is("01/01/1984"));
        assertThat(reviewChangesForm.getEnd(rowIndex), is("09/30/1997"));

        rowIndex = reviewChangesForm.getRowIndexFor(anotherTitle);
        assertThat(reviewChangesForm.getIssn(rowIndex), is("1520-9385"));
        assertThat(reviewChangesForm.getStart(rowIndex), is("10/01/1997"));
        assertThat(reviewChangesForm.getEnd(rowIndex), is(""));

        //Accept changes
        reviewChangesForm.clickYesLink();

        //Go to import queue and wait for the import completed.
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();
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

        //Load live data again, verify no changes, and restart
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        importQueueForm = holdingImporterForm.LldAndWaitUntilProcessed(dbCode);
        importQueueForm.clickDBlink(dbCode);

        //Verify no rules are applied
        reviewChangesForm.setTitleType("Serial");
        reviewChangesForm.setFilter("Any Change");
        assertThat(reviewChangesForm.getAppliedRules(), is("0"));

        //Restart import and wait for link present.
        reviewChangesForm.restartThenGoToReviewChanges(importQueueForm, dbCode);
        reviewChangesForm.clickRestartLink();
        importQueueForm.waitForDBprocessing(dbCode);
        importQueueForm.waitForLinkPresent(dbCode);
        assertThat("Import has a Validation Error", !(importQueueForm.getDBState(dbCode).contains(HOLDING_VALIDATION_ERRORS)));
        importQueueForm.clickDBlink(dbCode);

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

}
