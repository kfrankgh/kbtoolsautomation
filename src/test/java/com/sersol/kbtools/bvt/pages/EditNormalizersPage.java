package com.sersol.kbtools.bvt.pages;

import com.sersol.common.bvt.pages.PageRegistry;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class EditNormalizersPage extends HomePage {

    public void clickSelectAllCheckbox(){
        waitForElement(By.cssSelector("#selectAllBox"), 20).click();
    }

    public ViewTitle clickTitleCodeLink(String titleCode){
        utils.clickLink(titleCode);
        return PageRegistry.get(ViewTitle.class);
    }

    public Boolean removeNormalizer(String title){
        List<WebElement> rows = driver.findElements(By.cssSelector("#tableNormalizer>tbody>tr"));
        int rowSize = rows.size();
        if (rowSize == 0) return false;

        WebElement row;
        List<WebElement> cells;
        for (int i = 1; i < rowSize-5;i++){ //last 5 <tr> elements don't contain normalizers
            row = rows.get(i);
            cells = row.findElements(By.cssSelector("td"));
            if(cells.get(0).getText().contentEquals(title)){
                cells.get(4).click();   //checkbox
                waitForElement(By.cssSelector("[value='Remove']")).click();
                break;
            }
        }

        rows = driver.findElements(By.cssSelector("#tableNormalizer>tbody>tr"));
        if (rows.size()==rowSize-1){
            return true;
        }else{
            return false;
        }
    }

    public String getTitleDetailsTitle() {
        return driver.findElement(By.xpath("//div[@id='title_detail']/table/tbody/tr/th[text()='Title']/following-sibling::td")).getText();
    }

    public String getTitleDetailsTitleCode() {
        return driver.findElement(By.xpath("//div[@id='title_detail']/table/tbody/tr/th[text()='Title Code']/following-sibling::td")).getText();
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
        return driver.findElement(By.cssSelector(cssSelector)).getText();
    }

    public String getTitleDetailsIdentifier() {
        return driver.findElement(By.cssSelector("div#identifiers>table>tbody>tr>td>a")).getText();
    }
}
