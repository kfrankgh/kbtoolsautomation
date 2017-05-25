package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

public class CcSearchResultsPage extends HomePage implements ITestConstants {

    /**
     * Checks whether the first title displayed in the search results matches the expected title
     *
     * @param title
     * @return Boolean
     */
    public Boolean titleExists(String title){
        try {
            String actual = waitForElement(By.cssSelector(".GridView>td>a"),30).getText();
            return actual.compareToIgnoreCase(title)==0;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    /**
     * Clicks the first title link in the e-Catalog search results
     *
     * @param title
     * @return CcTitleDetailsPage
     */
    public CcTitleDetailsPage clickTitleLink(String title){
        String titleLinkText = getTitleLinkText(title);
        utils.clickLink(titleLinkText);
        waitForElement(By.cssSelector("#ctl00_Treeview1__treeViewt0")); //wait for Title Details page to load
        return PageFactory.initElements(driver, CcTitleDetailsPage.class);
    }

    /**
     * Gets actual link text of the title. Sometimes the link text is different from the input title in terms of
     * case sensitivity
     *
     * @param title
     * @return exact link text
     */
	private String getTitleLinkText(String title){
        String actual = waitForElement(By.cssSelector(".GridView>td>a"),30).getText();
        String titleLinkText = actual.compareToIgnoreCase(title)==0?actual:"Title not found";
        return titleLinkText;
    }
	


}
