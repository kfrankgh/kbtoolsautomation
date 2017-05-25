package com.sersol.kbtools.bvt.pages;

import com.sersol.common.bvt.pages.BasePage;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CcHomePage extends BasePage {
    public CcSearchResultsPage searchByTitleEquals(String title){

		WebElement dropDown = waitForElement(By.cssSelector("#ddlECatCriteria"));
		dropDown.click();
		Select select = new Select(dropDown);
		select.selectByValue("TitleEquals");

		waitForElement(By.id("ctl00__eCat_tbECatCriteria")).click();
		waitForElement(By.id("ctl00__eCat_tbECatCriteria")).sendKeys(title);
		waitForElement(By.id("ctl00__eCat_searchButton")).click();
		return PageFactory.initElements(driver, CcSearchResultsPage.class);

	}
}
