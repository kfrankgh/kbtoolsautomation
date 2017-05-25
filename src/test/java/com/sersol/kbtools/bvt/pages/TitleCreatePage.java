package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TitleCreatePage extends HomePage implements ITestConstants {

    public void addMarcRecord(String MarcRecordCode){
        WebElement marcRecordField =  waitForElement(By.cssSelector(CSSPATH_MARC_RECORD_INPUT_FIELD));
        marcRecordField.click();
        marcRecordField.sendKeys(MarcRecordCode);
    }
    public ViewTitle clickCreateTitleButton(){
        utils.clickButtonByText("Create Title");
        utils.waitForPageTitle("View Title - KBTools", 60);
        return PageFactory.initElements(driver, ViewTitle.class);
    }

    public TitleCreatePage clickPreviewChangesButton(){
        utils.clickButtonByText("Preview Changes");
        utils.waitForPageTitle("Title Create", 60);
        return PageFactory.initElements(driver, TitleCreatePage.class);
    }

    public ViewTitle clickCreateButtonAfterPreview(){
        utils.clickButtonByText("Create");
        utils.waitForPageTitle("KBTools - View Title", 60);
        return PageFactory.initElements(driver, ViewTitle.class);
    }

    public void waitForCreateTitleButton(){
        waitForElement(By.name("Action")).click();
    }

    public String getMarcRecordCode(WebElement marcRecordsTable){
        WebElement tableRowWithError = marcRecordsTable.findElements(By.cssSelector("tr")).get(1);
        List <WebElement> tableElements = tableRowWithError.findElements(By.cssSelector("td"));
        return tableElements.get(2).getText();
    }

    public String getMarcRecordErrorMsg(WebElement marcRecordsTable){
        WebElement tableRowWithError = marcRecordsTable.findElements(By.cssSelector("tr")).get(1);
        List <WebElement> tableElements = tableRowWithError.findElements(By.cssSelector("td"));
        return tableElements.get(4).getText();
    }

    public WebElement getMarcRecordsTable(){
        List<WebElement> TitleCreateTables = utils.getElements(CSSPATH_TITLECREATE_TABLES);
        return TitleCreateTables.get(3);
    }

    public Boolean marcRecordRowIsRed(WebElement marcRecordsTable){
        WebElement tableRowWithError = marcRecordsTable.findElements(By.cssSelector("tr")).get(1);
        String rowColor = tableRowWithError.getAttribute("bgcolor");
        return rowColor.contains(ROW_COLOR_RED);
    }
}
