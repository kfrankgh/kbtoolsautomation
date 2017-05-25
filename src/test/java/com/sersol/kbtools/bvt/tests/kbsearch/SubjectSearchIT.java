package com.sersol.kbtools.bvt.tests.kbsearch;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sersol.kbtools.bvt.pages.ViewSubjectForm;
import com.sersol.kbtools.bvt.utils.TestRail;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.MainMenuForm;
import com.sersol.kbtools.bvt.pages.SubjectSearchForm;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import com.sersol.kbtools.bvt.tests.KBToolsTest;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * @author Santiago Gonzalez
 *
 */
@RunWith(JUnitParamsRunner.class)
public class SubjectSearchIT extends KBToolsTest implements ITestConstants {
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
	public void simpleSubjectSearch() {
		testCaseId = "134781";

		PageRegistry.get(HomePage.class).clickMainMenu();
		SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();
		
		subjectSearchForm.setSearchCriteria("computer security");
		
		subjectSearchForm.clickSearchButton();
		
		assertThat(subjectSearchForm.getResultsText().contains("Error"), is(false));
		assertThat(subjectSearchForm.getResultsText().contains("Exception"), is(false));
	}

    @Test
    public void verifyMeshOutboundLinks() {
        testCaseId = "138657";

        PageRegistry.get(HomePage.class).clickMainMenu();
        SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();

        subjectSearchForm.setHierarchy(SELECT_ACCESSOR_MESH);
        subjectSearchForm.setSearchType(SELECT_ACCESSOR_NAME_BEGINS_WITH);
        subjectSearchForm.setSearchCriteria(SUBJECT_SEX_DEVELOPMENT_DISORDERS);

        subjectSearchForm.clickSearchButton();
        assertThat(subjectSearchForm.utils.elementExists(By.linkText(SUBJECT_SEX_DEVELOPMENT_DISORDERS)), is(true));

        ViewSubjectForm viewSubjectForm = subjectSearchForm.clickSubjectLink(SUBJECT_SEX_DEVELOPMENT_DISORDERS);
        assertEquals(VIEW_SUBJECT_PAGE_TITLE, viewSubjectForm.getPageTitle());

        String windowHandle = viewSubjectForm.utils.getWindowHandle();

        viewSubjectForm.utils.clickLink(VIEW_SUBJECT_LINK_NLM);

        viewSubjectForm.utils.switchToNewWindow(windowHandle);

        assertEquals("http://www.nlm.nih.gov/cgi/mesh/2011/MB_cgi?term=D012734&field=uid&exact=Find+Exact+Term", viewSubjectForm.utils.getCurrentUrl());
        assertEquals(SUBJECT_SEX_DEVELOPMENT_DISORDERS, viewSubjectForm.utils.getTextWithCssPath("p>table>* tr:nth-child(1) td:nth-child(2)"));
    }

    @Test
    public void verifyPageElements() {
        testCaseId = "138961";

        PageRegistry.get(HomePage.class).clickMainMenu();
        SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();

        assertTrue(subjectSearchForm.hierarchyDropdownElementsExist());
        assertTrue(subjectSearchForm.searchTypeDropdownElementsExist());
        assertTrue(subjectSearchForm.criteriaFieldExists());
        assertTrue(subjectSearchForm.filterResultsDropdownElementsExist());
        assertTrue(subjectSearchForm.searchButtonExists());
    }

    @Test
    @Parameters
    public void MesH_LargeNodesReturnFromSearch(String subject, String searchResult) {
        testCaseId = "139147";

        PageRegistry.get(HomePage.class).clickMainMenu();
        SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();

        subjectSearchForm.setHierarchy(SELECT_ACCESSOR_MESH);
        subjectSearchForm.setSearchType(SELECT_ACCESSOR_NAME_BEGINS_WITH);
        subjectSearchForm.setSearchCriteria(subject);

        subjectSearchForm.clickSearchButton();

        List<WebElement> searchResults = subjectSearchForm.getSearchResults();

        assertThat("No results found", searchResults.size() > 0);

        for(Integer i = 0; i < searchResults.size();i++) {
           assertThat("Match not found for: " + searchResults.get(i).getText(), searchResults.get(i).getText().matches(searchResult));
        }
    }

    private Object[] parametersForMesH_LargeNodesReturnFromSearch() {
        return new Object[]{
                new Object[]{"TH17 Cells", "MeSH -> Anatomy\\[\\d+\\] -> .+\\[\\d+\\] -> Leukocytes\\[\\d+\\] -> Leukocytes, Mononuclear\\[\\d+\\] -> Lymphocytes\\[\\d+\\] -> T-Lymphocytes\\[\\d+\\] -> CD4-Positive T-Lymphocytes\\[\\d+\\] -> T-Lymphocytes, Helper-Inducer\\[\\d+\\] -> Th17 Cells\\[\\d+\\]"},
                new Object[]{"Post-Synaptic Density", "MeSH -> Anatomy.?\\d{0,4}.? -> .+ -> Synapses.?\\d{0,4}.? -> Synaptic Membranes.?\\d{0,4}.? -> Post-Synaptic Density.?\\d{0,4}.?"},
                new Object[]{"Chromosomal Puffs","MeSH -> Anatomy.?\\d{0,4}.? -> Cells.?\\d{0,4}.? -> Cellular Structures.?\\d{0,4}.? -> Intracellular Space.?\\d{0,4}.? -> Cell Nucleus.?\\d{0,4}.? -> Cell Nucleus Structures.?\\d{0,4}.? -> Intranuclear Space.?\\d{0,4}.? -> Chromosomes.?\\d{0,4}.? -> .+ -> Chromosomal Puffs.?\\d{0,4}.?"},
                new Object[]{"Multivesicular Bodies","MeSH -> Anatomy.?\\d{0,4}.? -> Cells.?\\d{0,4}.? -> Cellular Structures.?\\d{0,4}.? -> Intracellular Space.?\\d{0,4}.? -> Cytoplasm.?\\d{0,4}.? -> Cytoplasmic Structures.?\\d{0,4}.? -> Organelles.?\\d{0,4}.? -> Cytoplasmic Vesicles.?\\d{0,4}.? -> Transport Vesicles.?\\d{0,4}.? -> Endosomes.?\\d{0,4}.? -> Multivesicular Bodies.?\\d{0,4}.?"},
                new Object[]{"Papio papio","MeSH -> Organisms.?\\d{0,4}.? -> Eukaryota.?\\d{0,4}.? -> Animals.?\\d{0,4}.? -> Chordata.?\\d{0,4}.? -> Vertebrates.?\\d{0,4}.? -> Mammals.?\\d{0,4}.? -> Primates.?\\d{0,4}.? -> Haplorhini.?\\d{0,4}.? -> Catarrhini.?\\d{0,4}.? -> Cercopithecidae.?\\d{0,4}.? -> Cercopithecinae.?\\d{0,4}.? -> Papio.?\\d{0,4}.? -> Papio papio.?\\d{0,4}.?"}
        };
    }

    @Test
    @Parameters
    public void MesH_NameChangeResults(String testId, String invalidSubject, String validSubject, String searchResult) {
        testCaseId = testId;

        PageRegistry.get(HomePage.class).clickMainMenu();
        SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();

        subjectSearchForm.setHierarchy(SELECT_ACCESSOR_MESH);
        subjectSearchForm.setSearchType(SELECT_ACCESSOR_NAME_BEGINS_WITH);
        subjectSearchForm.setSearchCriteria(invalidSubject);

        subjectSearchForm.clickSearchButton();

        List<WebElement> searchResults = subjectSearchForm.getSearchResults();

        assertThat("Results unexpectedly found", searchResults.size() == 0);

        searchResults.clear();
        subjectSearchForm.setSearchCriteria(validSubject);

        subjectSearchForm.clickSearchButton();

        searchResults = subjectSearchForm.getSearchResults();

        assertThat("Results not found", searchResults.size() > 0);

        for(Integer i = 0; i < searchResults.size();i++) {
            assertThat("Match not found for: " + searchResults.get(i).getText(), searchResults.get(i).getText().matches(searchResult));
        }
    }

    private Object[] parametersForMesH_NameChangeResults() {
        return new Object[]{
                new Object[]{"139158", "Sex Differentiation Disorders", "Disorders of Sex Development", "MeSH -> .+ -> Disorders of Sex Development.?\\d{0,4}.?"},
                new Object[]{"139194", "Cookery", "Cooking", "MeSH -> Technology, Industry, Agriculture.?\\d{0,4}.? -> Technology, Industry, and Agriculture.?\\d{0,4}.? -> .+ -> Cooking and Eating Utensils.?\\d{0,4}.?|" +
                                                    "MeSH -> Technology, Industry, Agriculture.?\\d{0,4}.? -> Technology, Industry, and Agriculture.?\\d{0,4}.? -> .+ -> Cooking.?\\d{0,4}.?"}
        };
    }

    @Test
    @Parameters
    public void testSearchFunctionality(String testId, String hierarchy, String searchType, String searchString, String searchResult) {
        testCaseId = testId;

        PageRegistry.get(HomePage.class).clickMainMenu();
        SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();

        subjectSearchForm.setHierarchy(hierarchy);
        subjectSearchForm.setSearchType(searchType);
        subjectSearchForm.setSearchCriteria(searchString);

        subjectSearchForm.clickSearchButton();

        List<WebElement> searchResults = subjectSearchForm.getSearchResults();

        assertThat("No results found", searchResults.size() > 0);

        for(Integer i = 0; i < searchResults.size();i++) {
            String result = searchResults.get(i).getText();
            assertThat("Match not found for: " + result, result.matches(searchResult));
        }
    }

    private Object[] parametersForTestSearchFunctionality() {
        return new Object[]{
                new Object[]{"139978", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_NAME_BEGINS_WITH, "So", "HILCC -> [^>]+\\[\\d+\\] ->.+So[^>]+\\[\\d+\\]"},
                new Object[]{"140555", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_NAME_CONTAINS, "Medical &", "HILCC -> .+\\[\\d{1,4}\\].+ -> Medical &[^>]+\\[\\d{1,4}\\]"},
                new Object[]{"140556", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_NAME_EQUALS, "Medical Care Plans",  "HILCC -> .+\\[\\d+\\] -> .+ -> Medical Care Plans\\[\\d+\\]"},
                new Object[]{"140557", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_TITLECODE_EQUALS, "TC0000527039",  "HILCC -> .+\\[\\d+\\] -> .+ -> Medical Care Plans\\[\\d+\\]"},
                new Object[]{"140558", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_PARENT_ID_EQUALS, "74",  "HILCC -> .+\\[\\d+\\] -> Public Health\\[\\d+\\] -> [^>]+\\[\\d+\\]"},
                new Object[]{"140559", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_SUBJECT_ID_EQUALS, "265", "HILCC -> Health & Biological Sciences\\[\\d{1,4}\\] -> Medicine\\[\\d{1,4}\\] -> Radiology, MRI, Ultrasonography & Medical Physics\\[\\d{1,4}\\]"},
                new Object[]{"140560", SELECT_ACCESSOR_HILCC, SELECT_ACCESSOR_CONTAINS_CALL_NUMBER, "KF 1357",  "HILCC -> Law, Politics & Government\\[\\d+\\] -> Law - U.S.\\[\\d+\\] -> Law - U.S. - General\\[\\d+\\]"},
                new Object[]{"140561", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_MESH_UNIQUE_ID, "D058504",  "MeSH -> Anatomy\\[\\d+\\] -> .+\\[\\d+\\] -> Leukocytes\\[\\d+\\] -> Leukocytes, Mononuclear\\[\\d+\\] -> Lymphocytes\\[\\d+\\] -> T-Lymphocytes\\[\\d+\\] -> CD4-Positive T-Lymphocytes\\[\\d+\\] -> T-Lymphocytes, Helper-Inducer\\[\\d+\\] -> Th17 Cells\\[\\d+\\]"},
                new Object[]{"140562", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_MESH_PATH_ID, "A15.145.229.637.555.567.569.200.400.770", "MeSH -> Anatomy\\[\\d+\\] -> Hemic and Immune Systems\\[\\d+\\] -> Blood\\[\\d+\\] -> Blood Cells\\[\\d+\\] -> Leukocytes\\[\\d+\\] -> Leukocytes, Mononuclear\\[\\d+\\] -> Lymphocytes\\[\\d+\\] -> T-Lymphocytes\\[\\d+\\] -> CD4-Positive T-Lymphocytes\\[\\d+\\] -> T-Lymphocytes, Helper-Inducer\\[\\d+\\] -> Th17 Cells\\[\\d+\\]"},
                new Object[]{"142495", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_NAME_CONTAINS, "g Cells", "MeSH -> .+\\[\\d{1,4}\\].+ -> [^>]+g Cells[^>]*\\[\\d{1,4}\\]"},
                new Object[]{"142496", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_NAME_EQUALS, "Leydig Cells",  "MeSH -> .+\\[\\d+\\] -> .* -> Leydig Cells\\[\\d+\\]"},
                new Object[]{"142517", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_TITLECODE_EQUALS, "TC0000798328",  "MeSH -> .+\\[\\d+\\] -> .+\\[\\d+\\] -> Testis\\[\\d+\\]"},
                new Object[]{"142518", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_PARENT_ID_EQUALS, "71765",  "MeSH -> .+\\[\\d+\\] -> Gonads\\[\\d+\\] -> .+ -> [^>]+\\[\\d+\\]"},
                new Object[]{"142519", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_SUBJECT_ID_EQUALS, "67008", "MeSH -> Anatomy\\[\\d{1,4}\\] -> Endocrine System\\[\\d{1,4}\\] -> Endocrine Glands\\[\\d{1,4}\\]"},
                new Object[]{"142566", SELECT_ACCESSOR_MESH, SELECT_ACCESSOR_CONTAINS_CALL_NUMBER, "QR",  "MeSH -> Health & Biological Sciences\\[\\d+\\] -> Biology\\[\\d+\\] -> Microbiology & Immunology\\[\\d+\\]"},
                new Object[]{"142567", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_NAME_BEGINS_WITH, "Met", "Ulrichs -> [^>]+\\[\\d+\\] -> .*Met[^>]+\\[\\d+\\]"},
                new Object[]{"142568", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_NAME_CONTAINS, "n Studies", "Ulrichs -> .+\\[\\d{1,4}\\] -> .+[^>]+n Studies[^>]*\\[\\d{1,4}\\]"},
                new Object[]{"142569", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_NAME_EQUALS, "Protestant",  "Ulrichs -> .+\\[\\d+\\] -> .* -> Protestant\\[\\d+\\]"},
                new Object[]{"142570", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_TITLECODE_EQUALS, "BUDDSTU",  "Ulrichs -> Religion and Philosophy\\[\\d+\\] -> Religions and Theology\\[\\d+\\] -> Buddhist\\[\\d+\\]"},
                new Object[]{"142571", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_PARENT_ID_EQUALS, "637",  "Ulrichs -> .+\\[\\d+\\] -> Religions and Theology\\[\\d+\\] .*-> [^>]+\\[\\d+\\]"},
                new Object[]{"142572", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_SUBJECT_ID_EQUALS, "667", "Ulrichs -> Sports, Leisure, and Hobbies\\[\\d{1,4}\\] -> Sports and Games\\[\\d{1,4}\\]"},
                new Object[]{"142573", SELECT_ACCESSOR_ULRICH, SELECT_ACCESSOR_CONTAINS_CALL_NUMBER, "QP",  "Ulrichs -> Health & Biological Sciences\\[\\d+\\] -> Human Anatomy & Physiology\\[\\d+\\] -> Physiology\\[\\d+\\]"},
                new Object[]{"142579", SELECT_ACCESSOR_ALL, SELECT_ACCESSOR_NAME_BEGINS_WITH, "Medical S", "(Ulrich's)??(HILCC)??(MeSH)?? -> [^>]+\\[\\d+\\] ->.+Medical S[^>]+\\[\\d+\\]",
                }
        };
    }

    @Test
    @Parameters
    public void testFilterResultsBy(String testId, String hierarchy, String searchType, String filterResultsBy, String searchString, String searchResult) {
        testCaseId = testId;

        PageRegistry.get(HomePage.class).clickMainMenu();
        SubjectSearchForm subjectSearchForm = PageRegistry.get(MainMenuForm.class).selectSubjectSearchLink();

        subjectSearchForm.setHierarchy(hierarchy);
        subjectSearchForm.setSearchType(searchType);
        subjectSearchForm.setSearchCriteria(searchString);
        subjectSearchForm.setSearchFilterDropdown(filterResultsBy);

        subjectSearchForm.clickSearchButton();

        List<WebElement> searchResults = subjectSearchForm.getSearchResults();

        if (filterResultsBy.contains(SELECT_ACCESSOR_SUBJECT_NOT_SET_TO_DISPLAY)){
            assertThat("Results unexpectedly found", searchResults.size() == 0);
        }
        else{
            assertThat("No results found", searchResults.size() > 0);
        }


        for(Integer i = 0; i < searchResults.size();i++) {
            String result = searchResults.get(i).getText();
            assertThat("Match not found for: " + result, result.matches(searchResult));
        }

        switch(filterResultsBy){
            case SELECT_ACCESSOR_SUBJECT_ASSOCIATED_WITH_TITLES:
                assertThat("Unexpected Number of subjects associated with titles", searchResults.size()==15);
                break;
            case SELECT_ACCESSOR_SUBJECT_NOT_ASSOCIATED_WITH_TITLES:
                assertThat("Unexpected Number of subjects not associated with titles:", searchResults.size()==1);
                break;
        }
    }

    private Object[] parametersForTestFilterResultsBy() {
        return new Object[]{
                new Object[]{"142581", SELECT_ACCESSOR_ALL, SELECT_ACCESSOR_NAME_BEGINS_WITH, SELECT_ACCESSOR_SUBJECT_NOT_SET_TO_DISPLAY, "A", ""},
                new Object[]{"142642", SELECT_ACCESSOR_ALL, SELECT_ACCESSOR_NAME_BEGINS_WITH, SELECT_ACCESSOR_SUBJECT_SET_TO_DISPLAY, "Medical S", "(Ulrich's)??(HILCC)??(MeSH)?? -> [^>]+\\[\\d+\\] ->.+Medical S[^>]+\\[\\d+\\]"},
                new Object[]{"142643", SELECT_ACCESSOR_ALL, SELECT_ACCESSOR_NAME_BEGINS_WITH, SELECT_ACCESSOR_SUBJECT_ASSOCIATED_WITH_TITLES, "Medical S", "(Ulrich's)??(HILCC)??(MeSH)?? -> [^>]+\\[\\d+\\] ->.+Medical S[^>]+\\[0{0}\\d+\\]"},
                new Object[]{"142644", SELECT_ACCESSOR_ALL, SELECT_ACCESSOR_NAME_BEGINS_WITH, SELECT_ACCESSOR_SUBJECT_NOT_ASSOCIATED_WITH_TITLES, "Medical S", "(Ulrich's)??(HILCC)??(MeSH)?? -> [^>]+\\[\\d+\\] ->.+Medical S[^>]+\\[0\\]"},
                new Object[]{"142645", SELECT_ACCESSOR_ALL, SELECT_ACCESSOR_NAME_BEGINS_WITH, SELECT_ACCESSOR_ALL_SUBJECTS, "Medical S", "(Ulrich's)??(HILCC)??(MeSH)?? -> [^>]+\\[\\d+\\] ->.+Medical S[^>]+\\[\\d+\\]"
                }
        };
    }

    @AfterClass
    public static void cleanUp() {
        HomePage homePage = PageRegistry.get(HomePage.class);
        //homePage.quitDriver();
    }

}
