package com.sersol.kbtools.bvt.pages;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.sersol.common.bvt.utils.Utils;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Santiago Gonzalez
 *
 */

public class MarcImporterForm extends HomePage {
	
    @FindBy(how = How.LINK_TEXT, using = "MediaType Report")
    private WebElement mediaTypeReportLink;
	
    public MediaTypeReportForm selectMediaTypeReportLink() {
    	waitForElement(By.linkText("MediaType Report"));
    	mediaTypeReportLink.click();
        return PageFactory.initElements(driver, MediaTypeReportForm.class);
    }

    public void clickUploadButton() {
        waitForElement(By.name("action")).click();
    }

    public void setFileToImport(String absolutePath){
        waitForElement(By.name("records")).sendKeys(absolutePath);
    }

    public Boolean isImportStarted(String marcRecordFile, long timeout) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeout) {
            utils.refreshPage();
            utils.waitForPageTitle(PAGETITLE_MARCIMPORTER, 20);
            if (isMarcFileImporting(marcRecordFile)) return true;
            Utils.sleep(1000);
        }
        return false;
    }

    public Boolean isImportFinished(String marcRecordFile, long timeout) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeout) {
            utils.refreshPage();
            utils.waitForPageTitle(PAGETITLE_MARCIMPORTER, 20);
            if (!isMarcFileImporting(marcRecordFile)) return true;
            Utils.sleep(1000);
        }
        return false;
    }

    /**
     * Checks to see if "No Active Imports" appears in "Active Imports" table
     * @return
     */
    public Boolean isNoActiveImports(){
        List<WebElement> rows = getActiveTableRows();
        List<WebElement> cells = rows.get(1).findElements(By.tagName("td"));
        if(cells.size() == 1 && cells.get(0).getText().equals("No Active Imports")){
            return true;
        }

        return false;
    }

    private List<WebElement> getActiveTableRows(){
        WebElement activeImportsTable = utils.getTable(1);
        return activeImportsTable.findElements(By.cssSelector("tr"));
    }

    private Boolean isMarcFileImporting(String marcRecordFile){
        // check for "No Active Imports"
        if(isNoActiveImports()){
            return false;
        }

        // check for the import file in the table
        List<WebElement> rows = getActiveTableRows();
        for(int i = 1; i < rows.size(); i++){ // skipping the header
            List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
            if(cells.get(2).getText().contains(marcRecordFile)){
                return true;
            }
        }

        return false;
    }

    public Boolean isMarcFileinImportLogs(String marcRecordFile, int rowIndex, long timeout){
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis()-start<timeout){
            utils.refreshPage();
            utils.waitForPageTitle(PAGETITLE_MARCIMPORTER, 20);
            if (isMarcFileInImportLogs(marcRecordFile, rowIndex)) return true;
        }
        return false;
    }

    public Boolean isMarcFileInImportLogs(String marcRecordFile, int rowIndex){
        WebElement ImportLogsTable = utils.getTable(2);

        List<WebElement> rows = ImportLogsTable.findElements(By.cssSelector("tr"));
        WebElement fileCell;
        try {
                fileCell = rows.get(rowIndex).findElement(By.xpath("td[4]"));
            }catch (Exception e) {
                return false;
            }
            return fileCell.getText().contentEquals(marcRecordFile);
    }
}
