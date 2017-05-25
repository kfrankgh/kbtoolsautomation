package com.sersol.kbtools.bvt.tests.kbsearch;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.sersol.kbtools.bvt.utils.TestRail;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.TitleSearchForm;
import com.sersol.kbtools.bvt.pages.ViewTitle;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Iterator;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class AuthorityTitleSearchIT extends KBToolsTest implements ITestConstants {
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
                e1.printStackTrace();
            }
        }
    };

    @Test
    @Parameters
    public void testSearch(String testId, String SearchType, String SearchTitleType, String Criteria,
                           String TitleCode, String TitleType, String Identifier, String Title) {
        testCaseId = testId;

        TitleSearchForm titleSearchForm = PageRegistry.get(HomePage.class).selectAuthorityTitleSearchLink();
        assertThat(titleSearchForm.getPageTitle(), is(PT_AUTH_TITLE_SEARCH));
        titleSearchForm.setTitleSearchType(SearchType);
        titleSearchForm.setTitleType(SearchTitleType);
        titleSearchForm.setSearchCriteria(Criteria);
        titleSearchForm.clickSearchButton();
        assertThat(titleSearchForm.getPageTitle(), is(PT_AUTH_TITLE_SEARCH));
        assertThat(titleSearchForm.getID(), is(TitleCode));
        assertThat(titleSearchForm.getType(), is(TitleType));
        assertThat(titleSearchForm.getIdentifier(), is(Identifier));
        assertThat(titleSearchForm.getTitle(), is(Title));
    }

    private Object[] parametersForTestSearch() {
        return new Object[]{
                new Object[]{"133784", SELECT_VALUE_TITLE_BEGINS, SELECT_VALUE_MONOGRAPH, TITLEBEGINS_BIKEBOYS,
                        TITLECODE_BIKEBOYS, TITLE_TYPE_MONOGRAPH, ID_BIKEBOYS, AUTH_TITLE_BIKEBOYS},
                new Object[]{"160409", SELECT_VALUE_TITLECODE, SELECT_VALUE_MONOGRAPH, TITLECODE_DIDDLE,
                        TITLECODE_DIDDLE, TITLE_TYPE_MONOGRAPH, ID_DIDDLE, AUTH_TITLE_DIDDLE},
                new Object[]{"159423", SELECT_VALUE_TITLE_ID, SELECT_VALUE_MONOGRAPH, TITLE_ID_ACNE,
                        TITLECODE_ACNE, TITLE_TYPE_MONOGRAPH, ID_ACNE, AUTH_TITLE_ACNE},
                new Object[]{"159424", SELECT_VALUE_IDENTIFIER, SELECT_VALUE_SERIAL, ID_WEATHER,
                        TITLECODE_WEATHER, TITLE_TYPE_SERIAL, ID_WEATHER, AUTH_TITLE_WEATHER},
                new Object[]{"159425", SELECT_VALUE_MARCRECORDCODE, SELECT_VALUE_SERIAL, MARCRECORDCODE_DARKMATTER,
                        TITLECODE_DARKMATTER, TITLE_TYPE_SERIAL, ID_DARKMATTER, AUTH_TITLE_DARKMATTER},
                new Object[]{"159426", SELECT_VALUE_SSID, SELECT_VALUE_MONOGRAPH, SSID_LIGHT,
                        TITLECODE_lIGHT, TITLE_TYPE_MONOGRAPH, ID_LIGHT, AUTH_TITLE_LIGHT},
                new Object[]{"159433", SELECT_VALUE_TITLECONTAINS, SELECT_VALUE_MONOGRAPH, "Hey there",
                        TITLECODE_HEY, TITLE_TYPE_MONOGRAPH, ID_HEY, AUTH_TITLE_HEY},
                new Object[]{"159436", SELECT_VALUE_TITLECONTAINS_AND, SELECT_VALUE_MONOGRAPH, "twister danger space",
                        TITLECODE_TWIST, TITLE_TYPE_MONOGRAPH, ID_TWIST, AUTH_TITLE_TWIST},
                new Object[]{"159440", SELECT_VALUE_TITLE_EQUALS, SELECT_VALUE_SERIAL, AUTH_TITLE_RUNNING,
                        TITLECODE_RUNNING, TITLE_TYPE_SERIAL, ID_RUNNING, AUTH_TITLE_RUNNING},
        };
    }

    @Test
    @Parameters
    public void testQuickSearch(String testId, String SearchCriteria, String ExpectedTitleCode, String ExpectedTitleType,
                                String ExpectedIdentifier, String ExpectedTitle) {
        testCaseId = testId;
        TitleSearchForm titleSearchForm = PageRegistry.get(HomePage.class).selectAuthorityTitleSearchLink();
        assertThat(titleSearchForm.getPageTitle(), is(PT_AUTH_TITLE_SEARCH));
        titleSearchForm.setAuthorityTitleCriteria(SearchCriteria);

        ViewTitle viewTitle = titleSearchForm.clickViewButton();

        assertThat(viewTitle.getPageTitle(), is(PT_VIEW_TITLE));
        assertThat(viewTitle.getTitleDetailsTitleCode(), is(ExpectedTitleCode));
        assertThat(viewTitle.getTitleDetailsTitle(), is(ExpectedTitle));
        assertThat(viewTitle.getTitleDetailsTitleType(ExpectedTitleType), is(ExpectedTitleType));
        assertThat(viewTitle.getTitleDetailsIdentifier(), is(ExpectedIdentifier));
    }

    private Object[] parametersForTestQuickSearch() {
        return new Object[]{
                new Object[]{"133785", TITLECODE_RUGBY, TITLECODE_RUGBY, TITLE_TYPE_MONOGRAPH, ID_RUGBY,
                        AUTH_TITLE_RUGBY},
                new Object[]{"160410", TITLE_ID_AXEMAN, TITLECODE_AXEMAN, TITLE_TYPE_MONOGRAPH, ID_AXEMAN,
                        AUTH_TITLE_AXEMAN},
                new Object[]{"160411", SSID_JUICE, TITLECODE_JUICE, TITLE_TYPE_SERIAL, ID_JUICE,
                        AUTH_TITLE_JUICE},
        };
    }

    @Test
    public void testMultipleSearchResults() {
        testCaseId = "225275";

        TitleSearchForm titleSearchForm = PageRegistry.get(HomePage.class).selectAuthorityTitleSearchLink();
        assertThat(titleSearchForm.getPageTitle(), is(PT_AUTH_TITLE_SEARCH));
        titleSearchForm.setTitleSearchType(SELECT_VALUE_TITLE_BEGINS);
        titleSearchForm.setLimit("101");
        titleSearchForm.setSearchCriteria("Sport");
        titleSearchForm.clickSearchButton();

        assertThat(titleSearchForm.getPageTitle(), is(PT_AUTH_TITLE_SEARCH));

        List<WebElement> rows = titleSearchForm.getSearchResults();

        assertThat(rows.size(), is(102)); //includes header row

        Iterator<WebElement> iterator = rows.iterator();
        Boolean firstRow = true;
        while (iterator.hasNext()) {
            if(firstRow){
                iterator.next();
                firstRow=false;
            }
            List<WebElement> values = iterator.next().findElements(By.cssSelector("td"));
            assertThat(values.size(), is(4));
            for (int i = 0; i < values.size(); i++) {
                String fieldValue = values.get(i).getText().trim();
                switch (i) {
                    case 0:
                        assertThat("Expected: " + REGEX_TITLECODE + " but was: " + fieldValue ,fieldValue.matches(REGEX_TITLECODE));
                        break;
                    case 1:
                        assertThat("Expected: " + REGEX_HOLDINGTYPE + " but was: " + fieldValue, fieldValue.matches(REGEX_HOLDINGTYPE));
                        break;
                    case 2:
                        assertThat("Expected: " + REGEX_ID + " but was: " + fieldValue, fieldValue.matches(REGEX_ID));
                        break;
                    case 3:
                        assertThat("Expected: " + REGEX_TITLE + " but was: " + fieldValue, fieldValue.matches(REGEX_TITLE));
                        break;
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }

        }

    }
}