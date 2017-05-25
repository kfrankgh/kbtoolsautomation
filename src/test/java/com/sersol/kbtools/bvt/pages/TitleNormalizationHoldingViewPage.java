package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class TitleNormalizationHoldingViewPage extends HomePage implements ITestConstants {

	public TitleCreatePage clickCreateButton(){
		waitForElement(By.cssSelector("b>input[value=Create]")).click();
		utils.waitForPageTitle("Title Create - KBTools", 60);
		return PageFactory.initElements(driver, TitleCreatePage.class);
	}

	public List<WebElement> getAuthTitleSearchRows(){
		return driver.findElements(By.cssSelector(CSS_PATH_AUTH_TITLE_TABLE_ELEMENTS));
	}

	public WebElement getAuthTitleSearchElement(List<WebElement> tablerows, int rowIndex, int colIndex){
		WebElement tablerow = tablerows.get(rowIndex);
		List<WebElement> rowValues = tablerow.findElements(By.cssSelector("td"));
		if (rowValues.size() != AUTH_TITLE_TABLE_COLUMNS_PER_ROW) return null;
		return rowValues.get(colIndex);

	}

	public HoldingSearchForm clickUseButton(WebElement tableRow){
		List<WebElement> rowValues =tableRow.findElements(By.cssSelector("td"));
		if (rowValues.size() != AUTH_TITLE_TABLE_COLUMNS_PER_ROW) return null;
		rowValues.get(0).click();
		return PageFactory.initElements(driver, HoldingSearchForm.class);
	}

	/**
	 * Clicks the Use Button next to the Authority Title Code textbox
	 *
	 * @return HoldingSearchForm
	 */
	public HoldingSearchForm clickUseButton(){
		waitForElement(By.cssSelector("b>input[value='Use']")).click();
		return PageFactory.initElements(driver, HoldingSearchForm.class);
	}


	public List<String> getMarcTitles(){
		List<String> marcTitlesArray = new ArrayList<String>();
		List<WebElement> trCollection =	utils.getTableRowsCollection("/html/body/blockquote[2]/table");
		WebElement trElement;
		List<WebElement> tdCollection;
		WebElement tdElement;

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) {

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("td"));
			tdElement = tdCollection.get(4);
			marcTitlesArray.add(tdElement.getText().replaceAll(" .,-?!", "").toUpperCase());
		}
		return marcTitlesArray;
	}

	/**
	 * Enters MARC Record Code in the MARC Record Code field
	 *
	 * @param marcRecordCode
	 */
	public void setMarcRecordCode(String marcRecordCode){
		WebElement input = waitForElement(By.cssSelector("b>input[name=mRecordCode]"), 2);
		input.click();
		input.sendKeys(marcRecordCode);
	}

	/**
	 * Enters title code in the Authority Title Code field
	 *
	 * @param titleCode
	 */
	public void setTitleCode(String titleCode){
		WebElement input = waitForElement(By.cssSelector("b>input[name=titleCode"), 2);
		input.click();
		input.sendKeys(titleCode);
	}






}
