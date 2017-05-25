package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Santiago Gonzalez
 *
 */
public class SubjectSearchForm extends MainMenuForm implements ITestConstants{

    @FindBy(how = How.CLASS_NAME, using = "page_content")
    private WebElement resultsContent;

    @FindBy(how = How.NAME, using = "subjectGroupId")
    private WebElement hierarchyDropdown;

    @FindBy(how = How.NAME, using = "searchType")
    private WebElement searchTypeDropdown;

    @FindBy(how = How.NAME, using = "searchFilter")
    private WebElement searchFilterDropdown;

    @FindBy(how = How.CSS, using = "input[type='submit']")
    private WebElement searchButton;

    @FindBy(how = How.NAME, using = "criteria")
    private WebElement searchCriteriaField;

    public String getResultsText() {
        return resultsContent.getText();
    }

    public void setHierarchy(String value) {
        Select select = new Select(hierarchyDropdown);
        select.selectByValue(value);
    }

    public void setSearchType(String value) {
        Select select = new Select(searchTypeDropdown);
        select.selectByValue(value);
    }

    public void setSearchFilterDropdown(String value){
        Select select = new Select(searchFilterDropdown);
        select.selectByValue(value);
    }


    public void clickSearchButton() {
        searchButton.click();
    }

    public List<WebElement> getSearchResults(){
        List<WebElement> searchResults = driver.findElements(By.cssSelector(".page_content>div"));
        List<WebElement> updatedResults = new ArrayList<>();
        if (searchResults.size() > 0){
            for(Integer i = 0; i < searchResults.size();i++) {//discard div elements that don't contain search results
                if (i%2 == 0){
                    updatedResults.add(searchResults.get(i));
                }
            }
        }
        return updatedResults;
    }

    public Boolean hierarchyDropdownElementsExist() {
        String [] expectedDropdownOptions = {SELECT_VALUE_HILCC, SELECT_VALUE_MESH, SELECT_VALUE_ULRICH, SELECT_VALUE_ALL};
        return dropdownElementsExist(hierarchyDropdown, expectedDropdownOptions, HIERARCHY_DROPDOWN_SIZE);
    }

    public Boolean searchTypeDropdownElementsExist() {
        String [] expectedDropdownOptions = {SELECT_VALUE_SUBJECT_ID_EQUALS, SELECT_VALUE_NAME_EQUALS, SELECT_VALUE_NAME_CONTAINS,
                SELECT_VALUE_NAME_BEGINS_WITH, SELECT_VALUE_NAME_CONTAINS_CALL_NUMBER, SELECT_VALUE_MESH_UNIQUE_ID,
                SELECT_VALUE_MESH_PATH_ID, SELECT_VALUE_TITLECODE_EQUALS, SELECT_VALUE_PARENT_ID_EQUALS};
        return dropdownElementsExist(searchTypeDropdown, expectedDropdownOptions, SEARCHTYPE_DROPDOWN_SIZE);
    }

    public Boolean criteriaFieldExists()  {return searchCriteriaField.isEnabled(); }

    public Boolean filterResultsDropdownElementsExist() {
        String [] expectedDropdownOptions = {SELECT_VALUE_SUBJECT_NOT_SET_TO_DISPLAY, SELECT_VALUE_SUBJECT_SET_TO_DISPLAY,
                SELECT_VALUE_SUBJECT_ASSOCIATED_WITH_TITLES, SELECT_VALUE_ALL_SUBJECTS,
                SELECT_VALUE_SUBJECT_NOT_ASSOCIATED_WITH_TITLES};
        return dropdownElementsExist(searchFilterDropdown, expectedDropdownOptions, FILTER_RESULTS_DROPDOWN_SIZE);
    }

    public Boolean searchButtonExists() {return searchButton.isEnabled(); }

    private Boolean dropdownElementsExist(WebElement dropdown,  String[] expectedDropdownOptions, Integer expectedDropdownSize) {
        Select select = new Select(dropdown);
        List<WebElement> dropdownOptions = select.getOptions();

        if (dropdownOptions.size() != expectedDropdownSize) return false;
        for (Integer i = 0; i < expectedDropdownSize; i++) {
            if (!dropdownOptions.get(i).getText().contains(expectedDropdownOptions[i])) {
                System.out.print("Expected: " + dropdownOptions.get(i).getText() + ", Actual: " + expectedDropdownOptions[i] + "\n");
                return false;
            }
        }
        return true;
    }
}