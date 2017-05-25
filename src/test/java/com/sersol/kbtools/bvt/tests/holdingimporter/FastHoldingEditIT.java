package com.sersol.kbtools.bvt.tests.holdingimporter;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.common.bvt.utils.Utils;
import com.sersol.kbtools.bvt.pages.FastHoldingEditForm;
import com.sersol.kbtools.bvt.pages.HoldingImporterForm;
import com.sersol.kbtools.bvt.pages.HoldingSearchForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.ImportQueueForm;
import com.sersol.kbtools.bvt.pages.ReviewChangesForm;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FastHoldingEditIT extends BaseHoldingImporter {
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
    public void fastHoldingEdit() throws URISyntaxException {
        testCaseId = "133035";
        String dbCode = "EFU";
        String file = "EFU-1title.txt";
        String title = "Automated Holding EFU";
        String editedTitle = "Automated Holding EFU - Fast Holding edited";

        //LOAD THE FILE
        HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        holdingImporterForm.setFileToImport(file);
        holdingImporterForm.clickLoadButton();

        //WAIT FOR FILE IMPORTED.
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        ImportQueueForm importQueueForm = PageRegistry.get(HoldingImporterForm.class).selectImportsInQueueLink();
        importQueueForm.waitForLinkPresent(dbCode);

        //WAIT FOR IMPORT AND ACCEPT CHANGES
        ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dbCode);

        reviewChangesForm.clickYesLink();
        PageRegistry.get(HomePage.class).selectHoldingImporterLink();
        holdingImporterForm.selectImportsInQueueLink();
        importQueueForm.waitForDBprocessing(dbCode);
        importQueueForm.waitForTextNotPresent(dbCode);

        //Go to holding search and search for the new holding added.
        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, title);

        holdingSearchForm.clickMonographLink();
        Utils.sleep(5000);

        FastHoldingEditForm editHoldingForm = holdingSearchForm.clickEditButtonForMonograph(title);
        editHoldingForm.setFastTitle(editedTitle);
        editHoldingForm.setFastAuthor("2");
        editHoldingForm.setFastEditor("3");
        editHoldingForm.setFastPublisher("4");
        editHoldingForm.setFastEdition("5");
        editHoldingForm.setFastDatePublished("1996");
        editHoldingForm.clickUpdateFastHoldingEditButton();

        Utils.sleep(5000);
        holdingSearchForm.refresh();

        assertThat(holdingSearchForm.getTitle(0), is(editedTitle));
        assertThat(holdingSearchForm.getMonographAuthor(), is("2"));
        assertThat(holdingSearchForm.getMonographEditor(), is("3"));
        assertThat(holdingSearchForm.getPublisher(editedTitle), is("4"));
        assertThat(holdingSearchForm.getMonographEdition(), is("5"));
        assertThat(holdingSearchForm.getMonographPublished(), is("1996"));
    }

    @Test
    public void testFastHoldingEditWithDiacritics() throws URISyntaxException {
        testCaseId = "135300";
        String dbCode = "ABTED";
        String title = "Educazione alla teatralità (E. TE)";

        //Holdings search and FHE
        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, title);
        String actual = holdingSearchForm.getTitle(0);
        assertThat("Expected Serial Title: " + title, actual, is(title));

        FastHoldingEditForm fastHoldingEditForm = holdingSearchForm.clickEditButtonForSerial(title);
        fastHoldingEditForm.setFastDateStart("2000");
        fastHoldingEditForm.setFastDateEnd("2001");
        fastHoldingEditForm.clickUpdateFastHoldingEditButton();

        Utils.sleep(5000);
        holdingSearchForm.refresh();

        assertThat(holdingSearchForm.getTitle(0), is(title));
        assertThat(holdingSearchForm.getDateStart(title), is("2000"));
        assertThat(holdingSearchForm.getDateEnd(title), is("2001"));

        //Clean up
        fastHoldingEditForm = holdingSearchForm.clickEditButtonForSerial(title);
        fastHoldingEditForm.setFastDateStart("2011");
        fastHoldingEditForm.setFastDateEnd("");
        fastHoldingEditForm.setFastPublisher("");
        fastHoldingEditForm.clickUpdateFastHoldingEditButton();

    }

    @Test
    public void testSearchHoldingEditTitleWithDiacritics() throws URISyntaxException {
        testCaseId = "135301";
        String dbCode = "ABTED";
        String monographTitle = "HeiligtÃ¼mer des Konfuzianismus in KÊ¾Ã¼-fu irradiated at âˆ¼30Â°C to 1 x 10Â¹â¶ neutrons/cmÂ² in a commercial reactor cavity";
        String updatedTitle = "Nan nü";

        //Holdings search and FHE on Title
        PageRegistry.get(HomePage.class).clickMainMenu();
        HoldingSearchForm holdingSearchForm = PageRegistry.get(HomePage.class).selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(dbCode, monographTitle);
        String title = holdingSearchForm.getTitle(0);
        assertThat("Expected Monograph Title: " + monographTitle, title, is(monographTitle));

        FastHoldingEditForm fastHoldingEditForm = holdingSearchForm.clickEditButtonForMonograph(monographTitle);

        fastHoldingEditForm.setHoldingTitle(updatedTitle);
        fastHoldingEditForm.clickUpdateFastHoldingEditButton();

        Utils.sleep(5000);
        holdingSearchForm.refresh();
        holdingSearchForm.clickNewSearchLink();
        holdingSearchForm.setDatabaseCode(dbCode);
        holdingSearchForm.setTitle(updatedTitle);
        Utils.sleep(4000);
        holdingSearchForm.clickSearchButton();
        assertThat(holdingSearchForm.getTitle(0), is(updatedTitle));

        //Clean up
        fastHoldingEditForm = holdingSearchForm.clickEditButtonForMonograph(updatedTitle);
        fastHoldingEditForm.setHoldingTitle(monographTitle);
        fastHoldingEditForm.clickUpdateFastHoldingEditButton();

    }

}
