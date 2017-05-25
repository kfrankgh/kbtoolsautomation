package com.sersol.kbtools.bvt.utils;

import com.sersol.common.bvt.pages.BasePage;
import com.sersol.kbtools.bvt.configuration.KBToolsEnvironment;
import com.sersol.kbtools.bvt.pages.HoldingSearchForm;
import com.sersol.kbtools.bvt.pages.TitleNormalizationHoldingViewPage;
import com.sersol.kbtools.bvt.pages.ViewTitle;
import com.sersol.kbtools.bvt.tests.LoginTest;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by kfrankli on 3/11/2015.
 */
public class HelperMethods extends BasePage{
    public final static int MIN_WAIT_MILLISECONDS = 1000;

    public void goToAbsoluteUrl(String url){
        driver.navigate().to(url);
    }

    public void goToPreviousPage(String pageTitle)
    {
        driver.navigate().back();
        new WebDriverWait(driver,10, 50).until(ExpectedConditions.titleIs(pageTitle));
    }

    public void goToUrl(String url) {
        driver.navigate().to(getBaseURL() + url);
    }

    public void login() {
        LoginTest loginTest = new LoginTest();
        loginTest.login();
    }

    public void quitDriver(){
        driver.quit();
    }

    /**
     * Returns a list of "tr" elements
     * @param tableXpath
     * @return
     */
    public List<WebElement> getTableRowsCollection(String tableXpath) {
        WebElement table = driver.findElement(By.xpath(tableXpath));
        List<WebElement> trCollection = table.findElements(By.xpath(tableXpath + "/tbody/tr"));

        return trCollection;
    }

    public Boolean tableExists(String tableXpath) {
        List<WebElement> trCollection = getTableRowsCollection(tableXpath);
        return !trCollection.isEmpty();
    }

    public List<WebElement> getDivsCollection(String bodyXpath) {
        WebElement body = driver.findElement(By.xpath(bodyXpath));
        List<WebElement> divCollection = body.findElements(By.xpath(bodyXpath + "/div"));

        return divCollection;
    }

    public List<WebElement> getElements(String cssPath) {
        return driver.findElements(By.cssSelector(cssPath));
    }

    /**
     * Gets a "random" string of specified length
     * @param length
     * @return
     */
    public String getRandomNumber(int length){
        String random = Long.toString(System.currentTimeMillis());
        int startIndex = random.length() - 1 - length;
        int endIndex = random.length() - 1;

        return random.substring(startIndex, endIndex);
    }

    public String getResourceFilePath(String fileDirectory) throws URISyntaxException {
        URL resource = Main.class.getResource(fileDirectory);
        String absolutePath = Paths.get(resource.toURI()).toString();
        return absolutePath;
    }

    public Boolean isLinkPresent(String linkText) {
        return driver.findElement(By.linkText(linkText)).isDisplayed();
    }

    /**
     * Clicks a link by the link text
     *
     * @param  linkText
     */
    public void clickLink(String linkText) {
        waitForElement(By.linkText(linkText)).click();
    }

    public String getValueFromInputField(String name) {
        return driver.findElement(By.name(name)).getAttribute("value");
    }

    public void clickButtonByText(String buttonText) {
        driver.findElement(By.xpath("//input[@value='" + buttonText + "']")).click();
    }

    public boolean isButtonPresentByValue(String value) {
        waitForElement(By.xpath("//input[@value='" + value + "']"));
        return driver.findElement(By.xpath("//input[@value='" + value + "']")).isDisplayed();
    }

    public boolean isButtonEnabled(String value) {
        WebDriverWait wait;
        wait = new WebDriverWait(driver, 60, 2000);
        WebElement Button = driver.findElement(By.cssSelector("input[value=\"" + value + "\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(Button));
        return Button.isEnabled();
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public TitleNormalizationHoldingViewPage switchToTitleNormalizationHoldingView(String windowHandle) {
        driver.switchTo().window(windowHandle);
        return PageFactory.initElements(driver, TitleNormalizationHoldingViewPage.class);
    }

    public ViewTitle switchToViewTitle(String windowHandle) {
        driver.switchTo().window(windowHandle);
        return PageFactory.initElements(driver, ViewTitle.class);
    }

    public HoldingSearchForm switchToHoldingSearchForm(String windowHandle) {
        driver.switchTo().window(windowHandle);
        return PageFactory.initElements(driver, HoldingSearchForm.class);
    }

    public void closeWindow() {
        driver.close();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public int getInputTextMaxLength(String name) {
        return Integer.parseInt(driver.findElement(By.name(name)).getAttribute("size"));
    }

    public String getTextWithXpath(String xpath) {
        return waitForElement(By.xpath(xpath)).getText();
    }

    public String getTextWithId(String id) {
        return driver.findElement(By.id(id)).getText();
    }

    public boolean isElementPresent(String elementName) {
        return driver.findElements(By.name(elementName)).size() > 0;
    }

    public boolean isElementPresentWithXpath(String xpath) {
        return driver.findElements(By.xpath(xpath)).size() > 0;
    }

    public boolean isCheckboxChecked(String name) {
        return Boolean.parseBoolean(driver.findElement(By.name(name)).getAttribute("checked"));
    }

    public boolean elementExists(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException elementNotFoundException) {
            return false;
        }
    }

    public void switchToNewWindow(String mainWindow) {
        Set windowHandles = this.getWindowHandles();

        Iterator ite = windowHandles.iterator();

        while (ite.hasNext()) {
            String newWindow = ite.next().toString();
            if (!newWindow.contains(mainWindow)) {
                driver.switchTo().window(newWindow);
            }

        }
    }

    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    public String getTextWithCssPath(String CssPath){
        return waitForElement(By.cssSelector(CssPath), 60).getText();
    }

    public WebElement getTableRow(WebElement table, String cssPathTableRows, int rowIndex){
        List<WebElement> rows = table.findElements(By.cssSelector(cssPathTableRows));
        WebElement row = null;
        try {
            row = rows.get(rowIndex);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("IndexOutOfBoundsException: " + e.getMessage());

        }
        return row;
    }

    public String getTableCellText(WebElement tableRow, int tableCellIndex){
        String text = "";
        try{
            text = tableRow.findElements(By.cssSelector("td")).get(tableCellIndex).getText();
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("IndexOutOfBoundsException: " + e.getMessage());

        }
        return text;
    }

    public WebElement getTable(int tableIndex) {
        waitForElement(By.cssSelector("table"));    //wait for any table
        List<WebElement> tables = driver.findElements(By.cssSelector("table"));
        WebElement table = null;
        try {
            table = tables.get(tableIndex);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("IndexOutOfBoundsException: " + e.getMessage());

        }
        return table;
    }

    public WebElement getTableRow(WebElement table, int index) {
        return getChildElement(table, index, "tr");
    }

    public WebElement getTableCell(WebElement tableRow, int index){
        return getChildElement(tableRow, index, "td");
    }

    /**
     * Finds the table with the table name
     * @param tableName
     * @return null if the table identified by the specified name is not found.
     */
    public WebElement getTable(String tableName){
        List<WebElement> tables = getTablesFromPageContent();
        List<WebElement> tableNames = driver.findElements(By.cssSelector(".page_content h3"));

        for(int i = 0; i < tables.size(); i++){
            WebElement table = tables.get(i);

            if(tableNames.get(i).getText().equals(tableName)){
                return table;
            }
        }

        return null;
    }

    private List<WebElement> getTablesFromPageContent(){
        return driver.findElement(By.className("page_content")).findElements(By.tagName("table"));
    }

    private WebElement getChildElement(WebElement parentElement, int index, String tag) {
        List<WebElement> children = parentElement.findElements(By.cssSelector(tag));
        WebElement child = null;
        try {
            child = children.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException: " + e.getMessage());

        }
        return child;
    }

    public void waitForPageTitle(String pageTitle, long timeout){
        WebDriverWait wait;
        wait = new WebDriverWait(driver, timeout, 2000);
        wait.until(ExpectedConditions.titleIs(pageTitle));
    }

    public void refreshPage(){
        driver.navigate().refresh();
    }

    /**
     * Pauses the execution of the test for the specified milliseconds
     * @param milliseconds
     */
    public void pause(long milliseconds){
        try{
            Thread.sleep(milliseconds);
        }catch(InterruptedException e){
            System.out.print(e.getMessage());
        }
    }
}
