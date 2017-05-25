package com.sersol.kbtools.bvt.tests.smoketests;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.core.marc.MarcSearchTypes;
import com.sersol.kbtools.bvt.pages.*;
import com.sersol.kbtools.bvt.pages.dataStatistics.DataStatisticsPage;
import com.sersol.kbtools.bvt.pages.dataStatistics.dataStatisticsTables.DataStatisticsTableRow;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;
import com.sersol.kbtools.bvt.tests.smoketests.common.HoldingData;
import com.sersol.kbtools.bvt.utils.TestRail;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class SmokeTestsIT extends KBToolsTest implements ITestConstants {
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
    @Parameters
    public void normalizeHoldingToExistingAuthority(String testId, String title, TitleType type) throws URISyntaxException {
        testCaseId = testId;

        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();

        holdingSearchForm.setTitle(title);
        holdingSearchForm.clickSearchButton();
        holdingSearchForm.utils.waitForPageTitle(HSF_PAGE_TITLE, 60);
        switch (type) {
            case Monograph:
                holdingSearchForm.clickMonographLink();
                break;
            case Serial:
                holdingSearchForm.clickSerialLink();
                break;
            default:
                assertThat("Unexpected holding type: " + type, false);
        }


        String windowHandle1 = holdingSearchForm.utils.getWindowHandle();

        holdingSearchForm.utils.clickLink(HSF_LINK_AUTHORITY);
        WebElement holding = holdingSearchForm.getHoldings().get(0);
        assertThat("Holding is normalized", holdingSearchForm.isHoldingNormalized(holding), is(false));

        String dbCode = holdingSearchForm.getDatabaseCode();
        String holdingId = "";
        switch (type) {
            case Monograph:
                holdingId = holdingSearchForm.getISBN13(holding);
                break;
            case Serial:
                holdingId = holdingSearchForm.getISSN(holding);
        }

        String holdingTitle = holdingSearchForm.getTitle(0);

        holdingSearchForm.utils.clickButtonByText(HSF_BUTTON_NORMALIZE);

        Set<String> tabs = holdingSearchForm.utils.getWindowHandles();
        tabs.remove(windowHandle1);
        String windowHandle2 = tabs.iterator().next();

        TitleNormalizationHoldingViewPage titleNormalizationHldgView =
                PageRegistry.get(HomePage.class).utils.switchToTitleNormalizationHoldingView(windowHandle2);

        List<WebElement> tableRows = titleNormalizationHldgView.getAuthTitleSearchRows();
        assertThat("Expected: at least 3 rows, Actual: " + Integer.toString(tableRows.size()), tableRows.size() > 2, is(true));

        WebElement titleCodeElement = titleNormalizationHldgView.getAuthTitleSearchElement(tableRows, 1, 1); //get TitleCode in 1st row of the table
        String titleCode = "";
        if (titleCodeElement != null) {//there are titlecodes displayed to use
            titleCode = titleCodeElement.getText();
            holdingSearchForm = titleNormalizationHldgView.clickUseButton(tableRows.get(1));  //click the first Use button
        }
        else{//no titlecodes are displayed so enter one
            switch (type) {
                case Monograph:
                    titleCode= "TC0000515220";
                    break;
                case Serial:
                    titleCode = "TC0000299936";
            }
            titleNormalizationHldgView.setTitleCode(titleCode);
            titleNormalizationHldgView.clickUseButton();
        }

        holdingSearchForm.clickNewSearchLink();
        holdingSearchForm.setDatabaseCode(dbCode);
        holdingSearchForm.setTitle(holdingTitle);
        holdingSearchForm.setId(holdingId);
        holdingSearchForm.clickSearchButton();

        assertThat(holdingSearchForm.getDatabaseCode(), containsString(dbCode));
        int rowIndex = holdingSearchForm.getHoldingRowIndex(holdingTitle);
        assertTrue("Holding not found", rowIndex!=-1);
        holding = holdingSearchForm.getHoldings().get(rowIndex);
        String id = "";
        if (type.equals(TitleType.Monograph)) {
            id = holdingSearchForm.getISBN13(holding);
        } else if (type.equals(TitleType.Serial)) {
            id = holdingSearchForm.getISSN(holding);
        }
        assertThat(id, containsString(holdingId));
        assertThat(holdingSearchForm.getTitleCode(), containsString(titleCode));

        //Get the Authority Title
        holdingSearchForm.clickTitleCodeLink(titleCode);
        tabs = holdingSearchForm.utils.getWindowHandles();
        tabs.remove(windowHandle1);
        tabs.remove(windowHandle2);
        String windowHandle3 = tabs.iterator().next();
        holdingSearchForm.utils.closeWindow();
        ViewTitle viewTitle = holdingSearchForm.utils.switchToViewTitle(windowHandle3);
        String urlViewTitle = viewTitle.utils.getCurrentUrl();
        String authorityTitle = viewTitle.getTitleDetailsTitle();

        //Verify the Authorty Title can be found in Client Center by searching for the Holding Title
        CcLoginPage ccLoginPage = new CcLoginPage();
        CcHomePage ccHomePage = ccLoginPage.login(USERNAME, PASSWORD, "MLZ");
        CcSearchResultsPage ccSearchResultsPage = ccHomePage.searchByTitleEquals(holdingTitle);
        assertThat(ccSearchResultsPage.titleExists(authorityTitle), is(true));

        //Clean up
        ccSearchResultsPage.utils.goToAbsoluteUrl(urlViewTitle);
        EditNormalizersPage editNormalizersPage = viewTitle.clickNormalizerEditLink();
        editNormalizersPage.removeNormalizer(holdingTitle);
        editNormalizersPage.utils.closeWindow();
        tabs = holdingSearchForm.utils.getWindowHandles();
        tabs.remove(windowHandle2);
        PageRegistry.get(HomePage.class).utils.switchToHoldingSearchForm(windowHandle1);

    }

    private Object[] parametersForNormalizeHoldingToExistingAuthority() {
        return new Object[]{
                new Object[]{"149114", "Jazz", TitleType.Monograph},
                new Object[]{"149115", "Journal of Ma", TitleType.Serial}
        };
    }

    @Test
    @Parameters
    public void createAuthorityTitle(String testId, String title, TitleType type, String marcRecordCode) throws URISyntaxException {
        testCaseId = testId;

        //Before the test, verify the marc records to be used, are not associated with an Authority Title
        MarcSearchForm msf = PageRegistry.get(HomePage.class).selectMarcSearchLink();
        msf.setSearchType(MarcSearchTypes.MARC_RECORD_CODE_EQUALS);
        msf.setSearchCriteria(marcRecordCode);
        msf.clickSearchButton();

        WebElement searchResultsTable = msf.utils.getTable(0);
        WebElement searchResultsRow = msf.utils.getTableRow(searchResultsTable, 1);
        WebElement titleCodeCell = msf.utils.getTableCell(searchResultsRow, 1);
        assertThat("Marc Record is already used", titleCodeCell.getText(), is(""));
        HomePage homePage = PageRegistry.get(HomePage.class).selectMainLink();

        //Start the test
        HoldingSearchForm holdingSearchForm = homePage.selectHoldingSearchLink();

        holdingSearchForm.holdingSearch("", title);
        holdingSearchForm.utils.waitForPageTitle(HSF_PAGE_TITLE, 60);
        switch (type) {
            case Monograph:
                holdingSearchForm.clickMonographLink();
                break;
            case Serial:
                holdingSearchForm.clickSerialLink();
                break;
            default:
                assertThat("Unexpected holding type: " + type, false);
        }


        String windowHandle1 = holdingSearchForm.utils.getWindowHandle();

        holdingSearchForm.utils.clickLink(HSF_LINK_AUTHORITY);
        WebElement holding = holdingSearchForm.getHoldings().get(0);
        assertThat("Holding is normalized", holdingSearchForm.isHoldingNormalized(holding), is(false));

        String holdingTitle = holdingSearchForm.getTitle(0);

        holdingSearchForm.utils.clickButtonByText(HSF_BUTTON_NORMALIZE);

        Set<String> tabs = holdingSearchForm.utils.getWindowHandles();
        tabs.remove(windowHandle1);
        String windowHandle2 = tabs.iterator().next();

        TitleNormalizationHoldingViewPage titleNormalizationHldgView =
                PageRegistry.get(HomePage.class).utils.switchToTitleNormalizationHoldingView(windowHandle2);
        titleNormalizationHldgView.utils.waitForPageTitle(TNHV_PAGE_TITLE, 60);

        //Enter the MARC Record Code and click Create
        titleNormalizationHldgView.setMarcRecordCode(marcRecordCode);
        TitleCreatePage titleCreatePage = titleNormalizationHldgView.clickCreateButton();

        //Click Create on Title Create page
        ViewTitle viewTitle = titleCreatePage.clickCreateTitleButton();
        String urlViewTitle = viewTitle.utils.getCurrentUrl();

        String ssId = "";
        String id = "";
        String titleType = "";
        String authorityTitle = "";
        String publisher = "";
        String marcRecordType = "";
        String titleCode = "";

        switch (type) {
            case Serial:
                ssId = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsSerial.SSID.getIndex());
                id = viewTitle.getId(0);
                titleType = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsSerial.TITLETYPE.getIndex());
                authorityTitle = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsSerial.TITLE.getIndex());
                publisher = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsSerial.PUBLISHER.getIndex());
                marcRecordType = viewTitle.getMarcRecordFormat(1);
                titleCode = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsSerial.TITLECODE.getIndex());
                break;
            case Monograph:
                ssId = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsMonograph.SSID.getIndex());
                id = viewTitle.getId(0);
                titleType = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsMonograph.TITLETYPE.getIndex());
                authorityTitle = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsMonograph.TITLE.getIndex());
                publisher = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsMonograph.PUBLISHER.getIndex());
                marcRecordType = viewTitle.getMarcRecordFormat(1);
                titleCode = viewTitle.getTitleDetailsValue(ViewTitle.TitleDetailsTableItemsMonograph.TITLECODE.getIndex());
        }
        assertThat(titleType, is(type.toString().toLowerCase()));

        //Verify the Authorty Title can be found in Client Center by searching for the Holding Title
        CcLoginPage ccLoginPage = new CcLoginPage();
        CcHomePage ccHomePage = ccLoginPage.login(USERNAME, PASSWORD, "MLZ");
        CcSearchResultsPage ccSearchResultsPage = ccHomePage.searchByTitleEquals(holdingTitle);
        assertThat(ccSearchResultsPage.titleExists(authorityTitle), is(true));

        //Verify the Authority Title metadata
        CcTitleDetailsPage ccTitleDetailsPage = ccSearchResultsPage.clickTitleLink(authorityTitle);
        ccTitleDetailsPage.utils.clickLink("Title Metadata");
        String ccSsId = ccTitleDetailsPage.utils.getTextWithCssPath("#ctl00__dvph_SSIDProperty_ReadOnlyDisplay");
        String ccId = ccTitleDetailsPage.utils.getTextWithCssPath("#ctl00__dvph_IdentifierProperty_TextItem");
        String ccType = ccTitleDetailsPage.utils.getTextWithCssPath("#ctl00__dvph_TypeProperty_ReadOnlyDisplay");
        String ccTitle = ccTitleDetailsPage.utils.getTextWithCssPath("#ctl00__dvph_TitleProperty_ReadOnlyDisplay");
        String ccPublisher = ccTitleDetailsPage.utils.getTextWithCssPath("#ctl00__dvph_PublisherProperty_ReadOnlyDisplay");
        String ccMarcRecords = ccTitleDetailsPage.utils.getTextWithCssPath("#ctl00__dvph_MarcRecordsProperty_List");

        assertThat(ccSsId, is(ssId));
        assertThat(ccId, is(id));
        if (titleType.contentEquals("serial")) titleType = "Journal";
        if (titleType.contains("monograph")) titleType = "Book";
        assertThat(ccType, is(titleType));
        assertThat(ccTitle, is(authorityTitle));
        assertThat(ccPublisher, is(publisher));
        assertThat(ccMarcRecords, containsString(marcRecordType));

        //Clean up
        ccTitleDetailsPage.utils.goToAbsoluteUrl(urlViewTitle);
        EditTitleDetailsPage editTitleDetailsPage = viewTitle.clickTitleDetailsEditLink();
        editTitleDetailsPage.setTitle("delete me " + Long.toString(System.currentTimeMillis()) + " - " + authorityTitle);
        editTitleDetailsPage.utils.clickButtonByText(ETDP_BUTTON_SAVECHANGES);

        EditMarcRecordsPage editMarcRecordsPage = viewTitle.clickMarcRecordsEditLink();
        editMarcRecordsPage.removeMarcRecord();

        EditNormalizersPage editNormalizersPage = viewTitle.clickNormalizerEditLink();
        editNormalizersPage.clickSelectAllCheckbox();
        editNormalizersPage.utils.clickButtonByText(ENP_BUTTON_REMOVE);
        editNormalizersPage.clickTitleCodeLink(titleCode);

        viewTitle.utils.closeWindow();
        tabs = holdingSearchForm.utils.getWindowHandles();
        tabs.remove(windowHandle2);
        PageRegistry.get(HomePage.class).utils.switchToHoldingSearchForm(windowHandle1);
    }

    private Object[] parametersForCreateAuthorityTitle() {
        return new Object[]{
                new Object[]{"149116", "Rugby", TitleType.Serial, "lccnsn 94022525"},
                new Object[]{"149117", "Football", TitleType.Monograph, "lccn97126301"}
        };
    }

    @Test
    public void importMarcRecord() throws URISyntaxException{
        testCaseId = "149118";

        MarcImporterForm mif = PageRegistry.get(HomePage.class).selectMarcImporterLink();
        mif.utils.waitForPageTitle(PAGETITLE_MARCIMPORTER, 20);
        assertEquals(true, mif.isNoActiveImports());

        //start import test
        String filePath = mif.utils.getResourceFilePath("/"+MARCIMPORT_FILE);
        mif.setFileToImport(filePath);
        mif.clickUploadButton();

        //Verify marc import file appears in the Active Imports table
        assertTrue(mif.isImportStarted(MARCIMPORT_FILE, 30000));

        //Verify the for the file eventually no longer appears in the Active Imports table
        assertTrue(mif.isImportFinished(MARCIMPORT_FILE, 120000));

        //Verify the file is in the Import Logs table
        assertTrue(mif.isMarcFileinImportLogs(MARCIMPORT_FILE, 2, 30000));

        //Verify a MARC search finds the last record in the file
        mif.clickMainMenu();
        MarcSearchForm msf = mif.selectMarcSearchLink();
        msf.setSearchType(marcSearchType.MARCRECORDCODE_EQUALS.value());
        msf.setSearchCriteria("aspnASP1000662478/blfi");
        msf.clickSearchButton();
        assertEquals("aspnASP1000662478/blfi", msf.getRecordCode());
        assertEquals("Optique MARC Import Test", msf.getTitle(false));

        //Clean up: Import clean record
        msf.clickMainMenu();
        mif = msf.selectMarcImporterLink();
        filePath = mif.utils.getResourceFilePath("/"+CLEAN_MARCIMPORT_FILE);
        mif.setFileToImport(filePath);
        mif.clickUploadButton();
    }

    @Test
    public void fastHoldingEdit() {
        testCaseId = "149119";

        final String fheDatabaseCode = "FICZZ";

        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(fheDatabaseCode, "Title");
        assertThat(holdingSearchForm.getTitleOfHoldingsTable(), is("Serial Holdings"));

        FastHoldingEditForm fhe = holdingSearchForm.clickEdit(0);
        assertThat(fhe.getTitleType(), is("Serial"));

        String random = Long.toString(System.currentTimeMillis());
        random = random.substring(random.length()-5,random.length()-1); //get last 4 digits

        String title = "Title "+random;
        String issn = "0317-8471";
        String publisher = "Pub "+random;
        String dateStart = random;
        String dateEnd = random;
        String url = "http://"+random+".com";

        fhe.setFastTitle(title);
        fhe.setFastISSN(issn);
        fhe.setFastPublisher(publisher);
        fhe.setFastDateStart(dateStart);
        fhe.setFastDateEnd(dateEnd);
        fhe.setFastURL(url);
        fhe.clickUpdateFastHoldingEditButton();

        holdingSearchForm.utils.clickLink("Show All Columns");
        assertTrue(holdingSearchForm.titleExists(title));
        assertThat(holdingSearchForm.getISSN(title), is(issn));
        assertThat(holdingSearchForm.getPublisher(title), is(publisher));
        assertThat(holdingSearchForm.getDateStart(title), is(dateStart));
        assertThat(holdingSearchForm.getDateEnd(title), is(dateEnd));
        assertThat(holdingSearchForm.getUrl(title), is(url));
    }

    @Test
    public void libraryGroups(){
        testCaseId = "149120";
            String randomNum = PageRegistry.get(HomePage.class).utils.getRandomNumber(7);
            String libraryGroupCode = "GroupCode" + randomNum;
            String libraryGroupName = "TestName" + randomNum;
            String libraryGroupNameEdited = libraryGroupName + " Edited";
            String libraryCodes = "ALZ";
            String libraryCodesEditedIn = "ALZ,MLZ";
            String libraryCodesEditedOut = "MLZ, ALZ";

            LibraryGroupEditorPage lgep = PageRegistry.get(HomePage.class).selectLibraryGroupsLink();
            lgep.setGroupCode(libraryGroupCode);
            lgep.createGroupName(libraryGroupName);
            lgep.createLibraryCodes(libraryCodes);
            lgep.clickCreateGroupButton();

            lgep.utils.refreshPage();
            int rowIndex = lgep.getRowIndexFor(libraryGroupCode);
            assertThat("LIBRARY GROUP CODE NOT FOUND", rowIndex >= 0, is(true));
            assertThat(lgep.getGroupName(rowIndex), is(libraryGroupName));
            assertThat(lgep.getLibraries(rowIndex), is(libraryCodes));

            lgep.clickUpdateLink(rowIndex);
            lgep.updateLibraryCodes(libraryCodesEditedIn);
            lgep.updateGroupName(libraryGroupNameEdited);
            lgep.clickUpdateButton();

            lgep = lgep.refreshLibraryGroupEditorPage(lgep);
            rowIndex = lgep.getRowIndexFor(libraryGroupCode);
            assertThat("LIBRARY GROUP CODE NOT FOUND", rowIndex >= 0, is(true));
            assertThat(lgep.getGroupName(rowIndex), is(libraryGroupNameEdited));
            assertThat(lgep.getLibraries(rowIndex), is(libraryCodesEditedOut));

            lgep.clickDeleteLink(rowIndex);
            lgep.clickConfirmAlert();   //"Delete MARC Group?" dialog
            lgep.clickConfirmAlert();   //"MARC Group Deleted" dialog

            lgep = lgep.refreshLibraryGroupEditorPage(lgep);
            rowIndex = lgep.getRowIndexFor(libraryGroupCode);
            assertThat("LIBRARY GROUP CODE FOUND", rowIndex == -1, is(true));
    }

    @Test
    public void loadLiveData(){
        testCaseId = "149121";

        HomePage homePage = PageRegistry.get(HomePage.class);
        HoldingData serialData = new HoldingData();
        serialData.setDatabaseCode("3AL");
        serialData.setTitle("Automation serial title " + homePage.utils.getRandomNumber(4));
        serialData.setTitleType(TitleType.Serial);
        serialData.setIssn("0093-3686");
        serialData.setPublisher("Alexander Street Press");
        serialData.setDateStart("2000");
        serialData.setDateEnd("2001");
        serialData.setUrl("http://search.proquest.com/publication/105928");

        HoldingData monographData = new HoldingData();
        monographData.setDatabaseCode("3AL");
        monographData.setTitle("Automation monograph title " + homePage.utils.getRandomNumber(4));
        monographData.setTitleType(TitleType.Monograph);
        monographData.setIsbn("9780679411673");
        monographData.setEdition("1st");
        monographData.setPublisher("Wiley");
        monographData.setDatePublished("2002");
        monographData.setAuthor("Joe Bloggs");
        monographData.setEditor("Jim Brown");
        monographData.setUrl("http://naldc.nal.usda.gov/catalog/37930");

        HoldingData unknownData = new HoldingData();
        unknownData.setDatabaseCode("3AL");
        unknownData.setTitle("Automation unknown title " + homePage.utils.getRandomNumber(4));;
        unknownData.setTitleType(TitleType.Unknown);
        unknownData.setPublisher("Dr Who");
        unknownData.setUrl("http://www.pressreader.com/russia/football");

        ImportQueueForm importQueueForm = homePage.selectImportQueueLink();
        if (importQueueForm.dbLinkIsPresent(serialData.getDatabaseCode())){
            importQueueForm.deleteDatabase(serialData.getDatabaseCode());
        }

        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        importQueueForm = holdingImporterForm.LldAndWaitUntilProcessed(serialData.getDatabaseCode());
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(serialData.getDatabaseCode());

        EditHoldingForm editHoldingForm = reviewChangesForm.clickAddHoldingLink();
        editHoldingForm.editHolding(serialData);
        editHoldingForm.clickAddHoldingButton();

        editHoldingForm = reviewChangesForm.clickAddHoldingLink();
        editHoldingForm.editHolding(monographData);
        editHoldingForm.clickAddHoldingButton();

        editHoldingForm = reviewChangesForm.clickAddHoldingLink();
        editHoldingForm.editHolding(unknownData);
        editHoldingForm.clickAddHoldingButton();

        reviewChangesForm.restartThenGoToReviewChanges(importQueueForm, serialData.getDatabaseCode());
        reviewChangesForm.clickYesLink();
        importQueueForm = homePage.selectImportQueueLink();
        importQueueForm.waitForDBprocessing(serialData.getDatabaseCode());
        importQueueForm.waitForTextNotPresent(serialData.getDatabaseCode());

        verifyHoldingData(serialData);
        verifyHoldingData(monographData);
        verifyHoldingData(unknownData);
    }

    private void verifyHoldingData(HoldingData holdingData){
        HomePage homePage = PageRegistry.get(HomePage.class);
        homePage.clickMainMenu();
        HoldingSearchForm holdingSearchForm = homePage.selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(holdingData.getDatabaseCode(), holdingData.getTitle());
        if (holdingData.getTitleType().equals(TitleType.Unknown)) holdingSearchForm.clickUnknownLink();

        assertThat(holdingSearchForm.getTitle(0), is(holdingData.getTitle()));
        assertThat(holdingSearchForm.getUrl(holdingData.getTitle()), is(holdingData.getUrl()));

        switch(holdingData.getTitleType()){
            case Serial:
                assertThat(holdingSearchForm.getISSN(holdingData.getTitle()), is(holdingData.getIssn()));
                assertThat(holdingSearchForm.getDateStart(holdingData.getTitle()), is(holdingData.getDateStart()));
                assertThat(holdingSearchForm.getDateEnd(holdingData.getTitle()), is(holdingData.getDateEnd()));
                break;
            case Monograph:
                assertThat(holdingSearchForm.getISBN13(),is(holdingData.getIsbn()));
                assertThat(holdingSearchForm.getMonographEdition(), is(holdingData.getEdition()));
                assertThat(holdingSearchForm.getMonographAuthor(), is(holdingData.getAuthor()));
                assertThat(holdingSearchForm.getMonographEditor(), is(holdingData.getEditor()));
                assertThat(holdingSearchForm.getPublisher(holdingData.getTitle()), is(holdingData.getPublisher()));
                assertThat(holdingSearchForm.getPublishedDate(), is(holdingData.getDatePublished()));
                break;
            case Unknown:
                assertThat(holdingSearchForm.getPublisher(holdingData.getTitle()), is(holdingData.getPublisher()));
                break;
            default:
                assertTrue("INVALID VALUE FOR TITLE TYPE", false);
                break;
        }
    }

    private enum dbSearchType{ByDbCode,ByDbName};
    @Test
    public void reportServer(){
        testCaseId = "149122";
        String dbCode = "ABC";
        String dbName = "kentucky";

        //test Database Search
        HomePage homePage = PageRegistry.get(HomePage.class);
        homePage.clickMainMenu();
        DatabaseSearchForm dbSearchForm = homePage.selectDatabaseSearchLink();
        dbSearchForm.setDatabaseCode(dbCode);
        dbSearchForm.clickSearchButton();
        verifyDbSearchResults(dbCode, dbSearchType.ByDbCode);

        dbSearchForm.clickClearButton();
        dbSearchForm.setDatabaseName(dbName);
        dbSearchForm.clickSearchButton();
        verifyDbSearchResults(dbName, dbSearchType.ByDbName);

        //test Data Statistics
        homePage.clickMainMenu();
        DataStatisticsPage statisticsPage = homePage.clickDataStatisticsLink();

        if(statisticsPage.isDataUpdating()){
            statisticsPage.waitForDataToLoad();
        }

        Date dateTime1 = statisticsPage.getLastUpdatedDateTime();
        statisticsPage.clickTriggerUpdateLink();
        assertThat("Expected: Data is updating", statisticsPage.isDataUpdating(), is(true));
        statisticsPage.waitForDataToLoad();
        Date dateTime2 = statisticsPage.getLastUpdatedDateTime();

        assertThat("Expected: Date is later",dateTime2.after(dateTime1));

        verifyLibraryDbStatisticsTable();
        verifyTitleDbStatisticsTable();
        verifyMarcDbStatisticsTable();
    }

    private void verifyDbSearchResults(String searchString, dbSearchType dbSearchType){
        verifyDbSearchResultsHeaderAndFooter();
        verifyDbSearchResultsValues(searchString, dbSearchType);
    }

    private void verifyDbSearchResultsHeaderAndFooter(){
        List<WebElement> tableRows = getDbSearchResultsTableRows();
        List<WebElement> headerColumns = tableRows.get(0).findElements(By.tagName("th"));
        assertThat(headerColumns.size(), is(14));

        List<WebElement> footerColumns = tableRows.get(tableRows.size() - 1).findElements(By.tagName("th"));
        assertThat(footerColumns.size(), is(14));

        assertThat("Expected DB header", headerColumns.get(0).getText().contentEquals("DB"));
        assertThat("Expected: Blank column", footerColumns.get(0).getText().isEmpty());

        assertThat("Expected Database Name header", headerColumns.get(1).getText().contentEquals("Database Name"));
        assertThat("Expected: Total nnn Databases",
                footerColumns.get(1).getText().matches(String.format("Total: %d Databases", tableRows.size() - 2)));

        assertThat("Expected PRV header", headerColumns.get(2).getText().contentEquals("PRV"));
        assertThat("Expected: Blank column", footerColumns.get(2).getText().isEmpty());

        assertThat("Expected Provider Name header", headerColumns.get(3).getText().contentEquals("Provider Name"));
        assertThat("Expected: Blank column", footerColumns.get(3).getText().isEmpty());

        assertThat("Expected Titles header", headerColumns.get(4).getText().contentEquals("Titles"));
        assertThat("Expected: Integer value", footerColumns.get(4).getText().matches("\\d+"));

        assertThat("Expected Norm header", headerColumns.get(5).getText().contentEquals("Norm"));
        assertThat("Expected: Integer value", footerColumns.get(5).getText().matches("\\d+"));

        assertThat("Expected Norm % header", headerColumns.get(6).getText().contentEquals("Norm %"));
        assertThat("Expected: Percentage value", footerColumns.get(6).getText().matches("\\d+%"));

        assertThat("Expected Serials header", headerColumns.get(7).getText().contentEquals("Serials"));
        assertThat("Expected: Integer value", footerColumns.get(7).getText().matches("\\d+"));

        assertThat("Expected Norm Serials header", headerColumns.get(8).getText().contentEquals("Norm Serials"));
        assertThat("Expected: Integer value", footerColumns.get(8).getText().matches("\\d+"));

        assertThat("Expected Norm Serials % header", headerColumns.get(9).getText().contentEquals("Norm Serials %"));
        assertThat("Expected: Percentage value", footerColumns.get(9).getText().matches("\\d+%"));

        assertThat("Expected Monographs header", headerColumns.get(10).getText().contentEquals("Monographs"));
        assertThat("Expected: Integer value", footerColumns.get(10).getText().matches("\\d+"));

        assertThat("Expected Norm Monographs header", headerColumns.get(11).getText().contentEquals("Norm Monographs"));
        assertThat("Expected: Integer value", footerColumns.get(11).getText().matches("\\d+"));

        assertThat("Expected Norm Monographs % header", headerColumns.get(12).getText().contentEquals("Norm Monographs %"));
        assertThat("Expected: Percentage value", footerColumns.get(12).getText().matches("\\d+%"));

        assertThat("Expected Action header", headerColumns.get(13).getText().contentEquals("Action"));
        assertThat("Expected: Blank column", footerColumns.get(13).getText().isEmpty());
    }

    private List<WebElement> getDbSearchResultsTableRows(){
        DatabaseSearchForm dbSearchForm = PageRegistry.get(DatabaseSearchForm.class);
        return dbSearchForm.utils.getTableRowsCollection("//div[@class=\"page_content\"]/table");
    }

    private void verifyDbSearchResultsValues(String searchString, dbSearchType dbSearchType){
        List<WebElement> resultsTableRows = getDbSearchResultsTableRows();

        for(int i = 1; i < resultsTableRows.size() - 1; i++){
            List<WebElement> cells = resultsTableRows.get(i).findElements(By.tagName("td"));

            if(dbSearchType == dbSearchType.ByDbCode){
                String dbCode = cells.get(0).getText();
                assertThat(String.format("Expected dbCode: %s", searchString), dbCode.contains(searchString));
            }
            else if (dbSearchType == dbSearchType.ByDbName){
                String dbName = cells.get(1).getText();
                assertThat(String.format("Expected database Name: %s", searchString),
                        dbName.toLowerCase().contains(searchString));
            }

            assertThat("Expected text", cells.get(2).getText().matches(".+"));
            assertThat("Expected text", cells.get(3).getText().matches(".+"));
            assertThat("Expected integer value", cells.get(4).getText().matches("\\d+"));
            assertThat("Expected integer value", cells.get(5).getText().matches("\\d+"));
            assertThat("Expected integer value", cells.get(7).getText().matches("\\d+"));
            assertThat("Expected integer value", cells.get(8).getText().matches("\\d+"));
            assertThat("Expected integer value", cells.get(10).getText().matches("\\d+"));
            assertThat("Expected integer value", cells.get(11).getText().matches("\\d+"));
            assertThat("Expected percentage value", cells.get(6).getText().matches("\\w+%"));
            assertThat("Expected percentage value", cells.get(9).getText().matches("\\w+%"));
            assertThat("Expected percentage value", cells.get(12).getText().matches("\\w+%"));
            assertThat("Expected links", cells.get(13).getText().matches("Norm . HDI . HSearch"));
        }
    }

    private void verifyLibraryDbStatisticsTable(){
        DataStatisticsPage statisticsPage = PageRegistry.get(DataStatisticsPage.class);
        List<DataStatisticsTableRow> libraryDbStatsTable = statisticsPage.getStatisticsTable("Library DB Statistics");

        assertThat(libraryDbStatsTable.size(),is(12));
        assertThat(libraryDbStatsTable.get(0).header, is("Normalized Databases"));
        assertThat(libraryDbStatsTable.get(0).description, is("Authority databases we track and normalize."));
        assertThat(libraryDbStatsTable.get(1).header, is("Non-Normalized DBs"));
        assertThat(libraryDbStatsTable.get(1).description, is("Databases we do not normalize. Mostly library " +
                "specific and test databases."));
        assertThat(libraryDbStatsTable.get(2).header, is("Title Count"));
        assertThat(libraryDbStatsTable.get(2).description, is("Total number of titles in all the normalized " +
                "databases. If a title is in more then one database it will be counted multiple times."));
        assertThat(libraryDbStatsTable.get(3).header, is("Normalized Titles"));
        assertThat(libraryDbStatsTable.get(3).description, is("Number of normalized titles in all the normalized " +
                "databases."));
        assertThat(libraryDbStatsTable.get(4).header, is("Normalized Percentage"));
        assertThat(libraryDbStatsTable.get(4).description, is("Percentage of normalized titles in all the " +
                "normalized databases."));
        assertThat(libraryDbStatsTable.get(5).header, is("Serials Count"));
        assertThat(libraryDbStatsTable.get(5).description, is("Total number of serials in all the normalized" +
                " databases."));
        assertThat(libraryDbStatsTable.get(6).header, is("Normalized Serials"));
        assertThat(libraryDbStatsTable.get(6).description, is("Number of normalized serials in all the" +
                " normalized databases."));
        assertThat(libraryDbStatsTable.get(7).header, is("Normalized Serials Percentage"));
        assertThat(libraryDbStatsTable.get(7).description, is("Percentage of normalized serials in all the " +
                "normalized databases."));
        assertThat(libraryDbStatsTable.get(8).header, is("Monographs Count"));
        assertThat(libraryDbStatsTable.get(8).description, is("Total number of monographs in all the normalized" +
                " databases."));
        assertThat(libraryDbStatsTable.get(9).header, is("Normalized Monographs"));
        assertThat(libraryDbStatsTable.get(9).description, is("Number of normalized monographs in all the " +
                "normalized databases."));
        assertThat(libraryDbStatsTable.get(10).header,
                is("Normalized Monographs Percentage"));
        assertThat(libraryDbStatsTable.get(10).description, is("Percentage of normalized monographs in all the " +
                "normalized databases."));
        assertThat(libraryDbStatsTable.get(11).header, is("Normalization Average"));
        assertThat(libraryDbStatsTable.get(11).description, is("The average normalization percentage for " +
                "authority databases."));

        for(int i=0; i < libraryDbStatsTable.size(); i++){
            String count = libraryDbStatsTable.get(i).count;
            switch(i){
                case 4: case 7: case 10: case 11:
                    assertThat(String.format("Expected percentage. Actual: %s", count), count.matches("\\d+%"));
                    break;
                default:
                    assertThat(String.format("Expected digit(s). Actual: %s", count), count.matches("\\d+"));
                    break;
            }
        }
    }

    private void verifyTitleDbStatisticsTable(){
        DataStatisticsPage statisticsPage = PageRegistry.get(DataStatisticsPage.class);
        List<DataStatisticsTableRow> titleDbStatsTable = statisticsPage.getStatisticsTable("Title DB Statistics");

        assertThat(titleDbStatsTable.size(),is(3));
        assertThat(titleDbStatsTable.get(0).header, is("Unknown Titles"));
        assertThat(titleDbStatsTable.get(0).description, is("Total authority Unknown titles."));

        assertThat(titleDbStatsTable.get(1).header, is("Serial Titles"));
        assertThat(titleDbStatsTable.get(1).description, is("Total authority Serial titles."));

        assertThat(titleDbStatsTable.get(2).header, is("Monograph Titles"));
        assertThat(titleDbStatsTable.get(2).description, is("Total authority Monograph titles."));

        for(int i=0; i < titleDbStatsTable.size(); i++){
            String count = titleDbStatsTable.get(i).count;
            assertThat(String.format("Expected digit(s). ACTUAL: %s",count), count.matches("\\d+"));
        }
    }

    private void verifyMarcDbStatisticsTable(){
        verifyMarcDbTableHeadersAndDescriptions();

        DataStatisticsPage statisticsPage = PageRegistry.get(DataStatisticsPage.class);
        List<DataStatisticsTableRow> marcDbStatsTable = statisticsPage.getStatisticsTable("Title DB Statistics");

        for(DataStatisticsTableRow tableRow:marcDbStatsTable){
            String count = tableRow.count;
            assertThat(String.format("Expected digit(s). ACTUAL: %s",count), count.matches("\\d+"));
        }

    }

    private void verifyMarcDbTableHeadersAndDescriptions(){
        DataStatisticsPage statisticsPage = PageRegistry.get(DataStatisticsPage.class);
        List<DataStatisticsTableRow> marcDbStatsTable = statisticsPage.getStatisticsTable("MARC DB Statistics");

        assertThat(marcDbStatsTable.get(0).header, is("Total Records"));
        assertThat(marcDbStatsTable.get(0).description, is("The number of MARC Records in the system."));


        if(thisMonthRowExists(marcDbStatsTable)){// row sometimes does not appear, presumably if no records were added
            // this month
            assertThat(marcDbStatsTable.get(1).header, is("This Month"));
            assertThat(marcDbStatsTable.get(1).description, is("Records added this month"));
            assertThat(marcDbStatsTable.get(2).header, is("This Year"));
            assertThat(marcDbStatsTable.get(2).description, is("Records added this year"));
            assertThat(marcDbStatsTable.get(3).header, is("Has ISSN"));
            assertThat(marcDbStatsTable.get(3).description, is("Records with an ISSN."));
            assertThat(marcDbStatsTable.get(4).header, is("Has ISBN"));
            assertThat(marcDbStatsTable.get(4).description, is("Records with an ISBN."));
        } else{
            assertThat(marcDbStatsTable.get(1).header, is("This Year"));
            assertThat(marcDbStatsTable.get(1).description, is("Records added this year"));
            assertThat(marcDbStatsTable.get(2).header, is("Has ISSN"));
            assertThat(marcDbStatsTable.get(2).description, is("Records with an ISSN."));
            assertThat(marcDbStatsTable.get(3).header, is("Has ISBN"));
            assertThat(marcDbStatsTable.get(3).description, is("Records with an ISBN."));
        }

        verifyMarcRecordsByProvider();
        verifyMarcRecordsByFormat();
    }

    private void verifyMarcRecordsByProvider(){
        DataStatisticsPage statisticsPage = PageRegistry.get(DataStatisticsPage.class);
        List<DataStatisticsTableRow> marcDbStatsTable = statisticsPage.getStatisticsTable("MARC DB Statistics");
        int rowIndexIsbn;

        Boolean thisMonthRowExists = thisMonthRowExists(marcDbStatsTable);
        if(thisMonthRowExists) rowIndexIsbn = 4;
        else rowIndexIsbn = 3;

        int totalRows = marcDbStatsTable.size();
        int firstRowByFormat = totalRows - 4;

        for (int i = rowIndexIsbn + 1; i < firstRowByFormat;i++){
            assertThat("Expected 4 Letter Org Code", marcDbStatsTable.get(i).header.matches("\\w{4}"));
            assertThat("Expected: \"Records from <Provider>\"",
                    marcDbStatsTable.get(i).description.matches("Records from .+"));
        }
    }

    private void verifyMarcRecordsByFormat(){
        DataStatisticsPage statisticsPage = PageRegistry.get(DataStatisticsPage.class);
        List<DataStatisticsTableRow> marcDbStatsTable = statisticsPage.getStatisticsTable("MARC DB Statistics");
        int totalRows = marcDbStatsTable.size();
        int firstRowByFormat = totalRows - 4;

        for (int i = firstRowByFormat; i < totalRows;i++){
            if (i == firstRowByFormat){
                assertThat(marcDbStatsTable.get(i).header, is("MARC21"));
                assertThat(marcDbStatsTable.get(i).description, is("Records with MARC21 " +
                        "Standard"));
            }
            if (i == firstRowByFormat+1){
                assertThat(marcDbStatsTable.get(i).header, is("MARC_8"));
                assertThat(marcDbStatsTable.get(i).description, is("Records with MARC_8 " +
                        "Encoding Format"));
            }
            if (i == firstRowByFormat+2){
                assertThat(marcDbStatsTable.get(i).header, is("MARC-8"));
                assertThat(marcDbStatsTable.get(i).description, is("Records with MARC-8 " +
                        "Encoding Format"));
            }

            if (i == firstRowByFormat+3){
                assertThat(marcDbStatsTable.get(i).header, is("UTF_8"));
                assertThat(marcDbStatsTable.get(i).description, is("Records with UTF_8 " +
                        "Encoding Format"));
            }
        }
    }

    /** Verifies whether the row exists by finding the index of the "Has ISBN" row which is the next row below.
     * Throws error if index is not 3 or 4, or if the "Has ISBN" row is not found.
     *
     * @param marcDbStatsTable
     * @return
     */
    private Boolean thisMonthRowExists(List<DataStatisticsTableRow> marcDbStatsTable){
        for(int i = 0; i < marcDbStatsTable.size(); i++) {
            if (marcDbStatsTable.get(i).header.contentEquals("Has ISBN")) {
                if (i == 3) return false;
                if (i == 4) return true;
                assertTrue("Unexpected row index for \"Has ISBN\" header", false);
            }
        }
        assertTrue("\"Has ISBN\" header not found in MARC DB Stats table", false);
        return null; //avoids compilation error
    }
}