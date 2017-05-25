package com.sersol.kbtools.bvt.tests.normalizer;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.sersol.kbtools.bvt.pages.*;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Rule;
import org.junit.Test;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Santiago Gonzalez
 *
 */

public class TitleNormalization extends KBToolsTest implements ITestConstants {
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
	public void testMediaTypeReport() {
        testCaseId = "134782";

		PageRegistry.get(HomePage.class).selectMarcImporterLink();
		MediaTypeReportForm mediaTypeReportForm = PageRegistry.get(MarcImporterForm.class).selectMediaTypeReportLink();

		assertThat(mediaTypeReportForm.getTitle(), is(PT_MEDIA_TYPE_REPORT));
		assertThat(mediaTypeReportForm.getTitlesNeedingReviewText(), containsString(MEDIA_TYPE_REPORT_HEADER));
		assertThat(mediaTypeReportForm.getFormText(), containsString(MEDIA_TYPE_REPORT_FORM_1));
		assertThat(mediaTypeReportForm.getFormText(), containsString(MEDIA_TYPE_REPORT_FORM_2));

		assertThat(mediaTypeReportForm.isLimitInputPresent(), is(true));
		assertThat(mediaTypeReportForm.isHideMissingRecordsButtonPresent(), is(true));
		assertThat(mediaTypeReportForm.isRefreshButtonPresent(), is(true));

		assertThat(mediaTypeReportForm.getAuthorityColumnText(), is(AUTHORITY_TEXT));
		assertThat(mediaTypeReportForm.getTitleColumnText(), is(TITLE_TEXT));
		assertThat(mediaTypeReportForm.getIdColumnText(), is(ID_TEXT));
		assertThat(mediaTypeReportForm.getMarcRecordColumnText(), is(MARC_RECORD));
		assertThat(mediaTypeReportForm.getActionColumnText(), is(ACTION_TEXT));

		if(mediaTypeReportForm.isSecondRowDisplayed()) {
			assertThat(mediaTypeReportForm.isAuthLinkPresent(), is(true));
			assertThat(mediaTypeReportForm.isMarcRecordLinkPresent(), is(true));
			assertThat(mediaTypeReportForm.isFormatSelectPresent(), is(true));
			assertThat(mediaTypeReportForm.isAddButtonPresent(), is(true));
			assertThat(mediaTypeReportForm.isBlockButtonPresent(), is(true));
			assertThat(mediaTypeReportForm.isRemoveButtonPresent(), is(true));
		}
	}

	@Test
	public void testNormalizationLogs() {
        testCaseId = "134784";

		PageRegistry.get(HomePage.class).selectTitleNormalizationLink();
		NormalizationLogsForm normalizationLogsForm = PageRegistry.get(TitleNormalizationForm.class).selectNormalizationLogsLink();

		assertThat(normalizationLogsForm.isSearchLogsLinkPresent(), is(true));
		normalizationLogsForm.clickSearchLogsLink();
		assertThat(normalizationLogsForm.isCancelSearchLinkPresent(), is(true));

		assertThat(normalizationLogsForm.getUserLabelText(), containsString("User:"));
		assertThat(normalizationLogsForm.getIdLabelText(), containsString("ID:"));
		assertThat(normalizationLogsForm.getTitleLabelText(), containsString("Title:"));

		assertThat(normalizationLogsForm.isUserInputPresent(), is(true));
		assertThat(normalizationLogsForm.isIdInputPresent(), is(true));
		assertThat(normalizationLogsForm.isTitleInputPresent(), is(true));

		assertThat(normalizationLogsForm.isSearchButtonPresent(), is(true));

		normalizationLogsForm.clickCancelSearchLink();

		assertThat(normalizationLogsForm.getTop10UsersLabelText(), containsString("Top 10 Users"));

		normalizationLogsForm.assertNormalizationLogsTable1Data();

		assertThat(normalizationLogsForm.getMostRecentNormalizationsText(), containsString("Most Recent Normalizations"));

		normalizationLogsForm.assertNormalizationLogsTable2Data();
	}

	@Test
	public void testListIndexers() {
        testCaseId = "134785";

		TitleNormalizationForm titleNormalizationForm = PageRegistry.get(HomePage.class).selectTitleNormalizationLink();
		
		titleNormalizationForm.setDatabaseCode("5X5");
		
		titleNormalizationForm.clickCreateNewListButton();
	    titleNormalizationForm.clickMainMenu();
		PageRegistry.get(HomePage.class).selectTitleNormalizationLink();

		ListIndexersForm listIndexersForm = PageRegistry.get(TitleNormalizationForm.class).selectListIndexersLink();

		assertThat(listIndexersForm.utils.isLinkPresent("Start Refresh"), is(true));
		listIndexersForm.utils.clickLink("Start Refresh");

		listIndexersForm.assertListIndexersTableData();
	}
	
	@Test
	public void testTitleNormalization() {
        testCaseId = "134786";

		TitleNormalizationForm titleNormalizationForm = PageRegistry.get(HomePage.class).selectTitleNormalizationLink();
		
		assertThat(titleNormalizationForm.getTitleNormalizationHeaderText(), containsString(PT_TITLE_NORMALIZATION));
		assertThat(titleNormalizationForm.getDatabaseCodeLabelText(), containsString("Database Code:"));
		assertThat(titleNormalizationForm.getTypeLabelText(), containsString("Type"));
		assertThat(titleNormalizationForm.getIndexLabelText(), containsString("Index:"));
		assertThat(titleNormalizationForm.getSearchWordsLabelText(), containsString("Search Words:"));
		assertThat(titleNormalizationForm.getSearchLimitLabelText(), containsString("Search Limit"));
		assertThat(titleNormalizationForm.getListNameLabelText(), containsString("List Name:"));
		assertThat(titleNormalizationForm.getHoldingsLabelText(), containsString("Only holdings that match the following criteria:"));
		assertThat(titleNormalizationForm.getTitleDBLabelText(), containsString("An ID match is found in the TitleDB"));
		assertThat(titleNormalizationForm.getMarcDBLabelText(), containsString("An ID match is found in the MarcDB"));
		assertThat(titleNormalizationForm.getYesConserLabelText(), containsString("Is normalized to a CONSER record"));
		assertThat(titleNormalizationForm.getNoConserLabelText(), containsString("Is normalized, not to a CONSER record"));
		assertThat(titleNormalizationForm.getNotYetNormalizedLabelText(), containsString("Not yet normalized to any record"));
		
		assertThat(titleNormalizationForm.isDatabaseCodeInputPresent(), is(true));
		assertThat(titleNormalizationForm.isListIndexInputPresent(), is(true));
		assertThat(titleNormalizationForm.isWordsInputPresent(), is(true));
		assertThat(titleNormalizationForm.isSearchCapInputPresent(), is(true));
		assertThat(titleNormalizationForm.isListNameInputPresent(), is(true));
		
		assertThat(titleNormalizationForm.isCheckBoxTitleDbMatchPresent(), is(true));
		assertThat(titleNormalizationForm.isCheckBoxMarcDbMatchPresent(), is(true));
		assertThat(titleNormalizationForm.isCheckBoxConserPresent(), is(true));
		assertThat(titleNormalizationForm.isCheckBoxNonConsPresent(), is(true));
		assertThat(titleNormalizationForm.isCheckBoxNonNormPresent(), is(true));
		
		assertThat(titleNormalizationForm.isCreateNewListButtonPresent(), is(true));
		assertThat(titleNormalizationForm.isResetFormFieldsButtonPresent(), is(true));
		assertThat(titleNormalizationForm.isUploadFileButtonPresent(), is(true));
		
		assertThat(titleNormalizationForm.isSerialRadioButtonPresent(), is(true));
		assertThat(titleNormalizationForm.isMonographRadioButtonPresent(), is(true));
		assertThat(titleNormalizationForm.isUnknownRadioButtonPresent(), is(true));
		
		titleNormalizationForm.clickUploadFileButton();
		
		assertThat(titleNormalizationForm.getFileNameLabelText(), containsString("Filename:"));
		assertThat(titleNormalizationForm.isChooseFileButtonPresent(), is(true));
		assertThat(titleNormalizationForm.isFileHelpLinkPresent(), is(true));
		
		titleNormalizationForm.clickCancelUploadButton();
		
		assertThat(titleNormalizationForm.getDatabaseCodeLabelText(), containsString("Database Code:"));
		assertThat(titleNormalizationForm.isDatabaseCodeInputPresent(), is(true));
		
		titleNormalizationForm.assertTitleNormalizationTableData();
		
		titleNormalizationForm.clickCreateNewListButton();
		
		assertThat(titleNormalizationForm.getDatabaseErrorMsgText(), containsString("Invalid Database Code"));
		
		titleNormalizationForm.goToPreviousPage();
	}

    @Test   //CLASSIC-1729
    public void testSaveAndReloadNormalizationFile() {
        testCaseId = "134787";

		String dbCode = "5X5";
		String listName = "Karen's A&I Fulltext Test Database (5X5)";
		String fileName = "CLASSIC_1729";

        //Go to TitleNormalization page from Home page
        TitleNormalizationForm titleNormalizationForm = PageRegistry.get(HomePage.class).selectTitleNormalizationLink();

        //delete normalization list if it is already there
        titleNormalizationForm.deleteNormalizationList(listName);

        //Create a Normalization List and verify it
        titleNormalizationForm.setDatabaseCode(dbCode);
        titleNormalizationForm.clickCreateNewListButton();
        titleNormalizationForm.assertNormalizationList(listName, "serial", "Non-normalized and non-CONSER");

        //Go to Admin Page and save Normalization List
        MainMenuForm mainMenuForm = PageRegistry.get(HomePage.class).clickMainMenu();
        AdminForm adminForm = mainMenuForm.clickAdminLink();
        adminForm.writeNormalizationList(fileName);

        //Delete and Reload Normalization List, then verify
        titleNormalizationForm.deleteNormalizationList(listName);
        titleNormalizationForm.goToMainMenu();
        mainMenuForm.clickAdminLink();
        adminForm.loadNormalizationList(fileName);
        titleNormalizationForm.assertNormalizationList(listName, "serial", "Non-normalized and non-CONSER");

        //Clean up
        titleNormalizationForm.deleteNormalizationList(listName);
    }
}
