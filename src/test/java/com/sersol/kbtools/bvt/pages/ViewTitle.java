package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ViewTitle extends HomePage implements ITestConstants{

    public enum TitleDetailsTableItemsSerial {
        TITLEID (0),
        TITLECODE (1),
        SSID (2),
        TITLE (3),
        ALPHABETIZATION (4),
        NONFILINGCHARACTERS (5),
        PUBLISHER (6),
        LANGUAGE (7),
        TITLETYPE (8);

        private final int index;    //index of <td> element in the table
        TitleDetailsTableItemsSerial(int index){
            this.index = index;
        }

        public int getIndex(){
            return index;
        }
    }

    public enum TitleDetailsTableItemsMonograph {
        TITLEID (0),
        TITLECODE (1),
        SSID (2),
        TITLE (3),
        TITLEFULL (4),
        ALPHABETIZATION (5),
        NONFILINGCHARACTERS (6),
        EDITION (7),
        DATEPUBLISHED (8),
        PUBLISHER (9),
        LANGUAGE (10),
        TITLETYPE (11);

        private final int index;    //index of <td> element in the table
        TitleDetailsTableItemsMonograph(int index){
            this.index = index;
        }

        public int getIndex(){
            return index;
        }
    }

    public String getId(int index){
        WebElement table = waitForElement(By.cssSelector("#identifiers"));
        List<WebElement> ids = table.findElements(By.cssSelector("a"));
        if (ids.size()==0) return "";   //authority title has no identifiers

        WebElement idCell = null;
        try{
            idCell = ids.get(index);
        }
        catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return idCell.getText();
    }

    public String getMarcRecordFormat(int index){
        WebElement table = waitForElement(By.cssSelector("#media_types"));
        List<WebElement> rows = table.findElements(By.cssSelector("tr"));

        WebElement row;
        WebElement cell = null;
        try{
            row = rows.get(index);
            cell = row.findElements(By.cssSelector("td")).get(0);
        }
        catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return cell.getText();
    }

    public String getTitleDetailsTitle() {
        return waitForElement(By.xpath("//div[@id='title_detail']/table/tbody/tr/th[text()='Title']/following-sibling::td")).getText();
    }

    public String getTitleDetailsTitleCode() {
        return waitForElement(By.xpath("//div[@id='title_detail']/table/tbody/tr/th[text()='Title Code']/following-sibling::td")).getText();

    }

    public String getTitleDetailsValue(int index) {
        WebElement table = waitForElement(By.cssSelector("#title_detail"));
        List<WebElement> tableValues = table.findElements(By.cssSelector("td"));
        String value = "";

        try{
            value = tableValues.get(index).getText().trim();
        }
        catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return value;
    }

    public String getTitleDetailsTitleType(String expectedTitleType) {
        String cssSelector;
        switch(expectedTitleType){
            case "monograph":
                cssSelector = "#title_detail>table>tbody>tr:nth-child(12) td:nth-child(2)";
                break;
            case "serial":
                cssSelector = "#title_detail>table>tbody>tr:nth-child(9) td:nth-child(2)";
                break;
            default:
                return "Invalid expectedTitleType: " + expectedTitleType;
        }
        return waitForElement(By.cssSelector(cssSelector)).getText();
    }

    public String getTitleDetailsIdentifier() {
        return waitForElement(By.cssSelector("div#identifiers>table>tbody>tr>td>a")).getText();
    }

    public EditMarcRecordsPage clickMarcRecordsEditLink(){
        WebElement element = waitForElement(By.cssSelector("#media_types>h3"), 20);
        element.findElement(By.linkText("Edit")).click();
        return PageFactory.initElements(driver, EditMarcRecordsPage.class);
    }

    public EditNormalizersPage clickNormalizerEditLink(){
        WebElement element = waitForElement(By.cssSelector("#normalizers>h3"), 10);
        element.findElement(By.linkText("Edit")).click();
        return PageFactory.initElements(driver, EditNormalizersPage.class);
    }

    public EditTitleDetailsPage clickTitleDetailsEditLink(){
        WebElement element = waitForElement(By.cssSelector("#title_detail>h3"));
        element.findElement(By.linkText("Edit")).click();
        return PageFactory.initElements(driver, EditTitleDetailsPage.class);
    }

}
