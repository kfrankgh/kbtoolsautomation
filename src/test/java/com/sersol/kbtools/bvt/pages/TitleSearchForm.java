package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TitleSearchForm extends HomePage {

	@FindBy(how = How.NAME, using = "authID")
	private WebElement authorityTitleCriteria;

	@FindBy(how = How.CSS, using = "input[value='View']")
	private WebElement viewButton;

	@FindBy(how = How.NAME, using = "criteria")
	private WebElement searchCriteriaInput;

	@FindBy(how = How.NAME, using = "titleType")
	private WebElement titleTypeSelect;

	public void setAuthorityTitleCriteria(String criteria) {
		updateField(authorityTitleCriteria, criteria);
	}

	public void setTitleType(String titleType) {
		selectByVisibleText(titleTypeSelect, titleType);
	}

	public ViewTitle clickViewButton() {
		viewButton.click();
		return PageFactory.initElements(driver, ViewTitle.class);
	}

	public void setSearchCriteria(String searchCriteria) {
		updateField(searchCriteriaInput, searchCriteria);
	}

	public String getTitle() {
		return driver.findElement(By.xpath("//div[@class='page_content']/table/tbody/tr/th[text()='Title']/parent::tr/following-sibling::tr/td[4]")).getText().trim();
	}

	public String getID() {
		return waitForElement((By.xpath("//div[@class='page_content']/table/tbody/tr/th[text()='Authority']/parent::tr/following-sibling::tr/td[1]/a"))).getText().trim();
	}

	public String getType() {
		return driver.findElement(By.xpath("//div[@class='page_content']/table/tbody/tr/th[text()='Type']/parent::tr/following-sibling::tr/td[2]")).getText().trim();
	}

	public String getIdentifier() {
		return driver.findElement(By.xpath("//div[@class='page_content']/table/tbody/tr/th[text()='ID']/parent::tr/following-sibling::tr/td[3]")).getText().trim();
	}

	public String setLimit(String searchResultsLimit){
		driver.findElement(By.name("limit")).clear();
		driver.findElement(By.name("limit")).sendKeys(searchResultsLimit);
		return driver.findElement(By.name("limit")).getText();
	}

	public List<WebElement> getSearchResults(){
		return driver.findElements(By.cssSelector("table tr"));
	}
}
