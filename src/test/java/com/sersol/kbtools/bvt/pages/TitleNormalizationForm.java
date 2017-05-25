package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Santiago Gonzalez
 *
 */
public class TitleNormalizationForm extends HomePage {

    @FindBy(how = How.NAME, using = "dbCode")
    private WebElement dbCodeInput;

    @FindBy(how = How.NAME, using = "listIndex")
    private WebElement listIndexInput;

    @FindBy(how = How.NAME, using = "words")
    private WebElement wordsInput;

    @FindBy(how = How.NAME, using = "searchCap")
    private WebElement searchCapInput;

    @FindBy(how = How.NAME, using = "listName")
    private WebElement listNameInput;

    @FindBy(how = How.NAME, using = "titleDBMatch")
    private WebElement titleDbMatchCheckbox;

    @FindBy(how = How.NAME, using = "marcDBMatch")
    private WebElement marcDbMatchCheckbox;

    @FindBy(how = How.NAME, using = "conser")
    private WebElement conserCheckbox;

    @FindBy(how = How.NAME, using = "noncons")
    private WebElement nonConsCheckbox;

    @FindBy(how = How.NAME, using = "nonnorm")
    private WebElement nonNormCheckbox;

    @FindBy(how = How.ID, using = "serialId")
    private WebElement serialRadioButton;

    @FindBy(how = How.ID, using = "monographId")
    private WebElement monographRadioButton;

    @FindBy(how = How.ID, using = "unknownId")
    private WebElement unknownRadioButton;

    @FindBy(how = How.ID, using = "upfile")
    private WebElement chooseFileRadioButton;

    @FindBy(how = How.LINK_TEXT, using = "List Indexers")
    private WebElement listIndexersLink;

    @FindBy(how = How.LINK_TEXT, using = "Normalization Logs")
    private WebElement normalizationLogsLink;

    public ListIndexersForm selectListIndexersLink() {
        waitForElement(By.linkText("List Indexers"));
        listIndexersLink.click();
        return PageFactory.initElements(driver, ListIndexersForm.class);
    }

    public NormalizationLogsForm selectNormalizationLogsLink() {
        waitForElement(By.linkText("Normalization Logs"));
        normalizationLogsLink.click();
        return PageFactory.initElements(driver, NormalizationLogsForm.class);
    }

    public String getTitleNormalizationHeaderText() {
        return driver.findElement(By.xpath("/html/body/div[1]/h1")).getText();
    }

    public String getDatabaseCodeLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/p[1]/b[1]")).getText();
    }

    public String getTypeLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/p[1]/b[2]")).getText();
    }

    public String getIndexLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[1]/tbody/tr/td[1]/b")).getText();
    }

    public String getSearchWordsLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[1]/tbody/tr/td[3]/b")).getText();
    }

    public String getSearchLimitLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[1]/tbody/tr/td[5]/b")).getText();
    }

    public String getListNameLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/p[2]/b")).getText();
    }

    public String getHoldingsLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/b")).getText();
    }

    public String getTitleDBLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[2]/tbody/tr[1]/td[2]")).getText();
    }

    public String getMarcDBLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[2]/tbody/tr[2]/td[2]")).getText();
    }

    public String getYesConserLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[2]/tbody/tr[3]/td[2]")).getText();
    }

    public String getNoConserLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[2]/tbody/tr[4]/td[2]")).getText();
    }

    public String getNotYetNormalizedLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/table[2]/tbody/tr[5]/td[2]")).getText();
    }

    public Boolean isListIndexInputPresent() {
        return listIndexInput.isDisplayed();
    }

    public Boolean isWordsInputPresent() {
        return wordsInput.isDisplayed();
    }

    public Boolean isSearchCapInputPresent() {
        return searchCapInput.isDisplayed();
    }

    public Boolean isListNameInputPresent() {
        return listNameInput.isDisplayed();
    }

    public Boolean isCheckBoxTitleDbMatchPresent() {
        return titleDbMatchCheckbox.isDisplayed();
    }

    public Boolean isCheckBoxMarcDbMatchPresent() {
        return marcDbMatchCheckbox.isDisplayed();
    }

    public Boolean isCheckBoxConserPresent() {
        return conserCheckbox.isDisplayed();
    }

    public Boolean isCheckBoxNonConsPresent() {
        return nonConsCheckbox.isDisplayed();
    }

    public Boolean isCheckBoxNonNormPresent() {
        return nonNormCheckbox.isDisplayed();
    }

    public Boolean isCreateNewListButtonPresent() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/input[3]")).isDisplayed();
    }

    public Boolean isResetFormFieldsButtonPresent() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/input[4]")).isDisplayed();
    }

    public Boolean isUploadFileButtonPresent() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/input[5]")).isDisplayed();
    }

    public Boolean isSerialRadioButtonPresent() {
        return serialRadioButton.isDisplayed();
    }

    public Boolean isMonographRadioButtonPresent() {
        return monographRadioButton.isDisplayed();
    }

    public Boolean isUnknownRadioButtonPresent() {
        return unknownRadioButton.isDisplayed();
    }

    public Boolean isNormalizationListPresent(String listName){
        return utils.elementExists(By.linkText(listName));
    }

    public void goToMainMenu(){
        clickMainMenu();
    }
    public void clickUploadFileButton() {
        driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/input[5]")).click();
    }

    public String getFileNameLabelText() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/p[1]/b[1]")).getText();
    }

    public boolean isChooseFileButtonPresent() {
        return driver.findElement(By.name("upfile")).isDisplayed();
    }

    public boolean isFileHelpLinkPresent() {
        return driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/p[1]/a")).isDisplayed();
    }

    public void clickCancelUploadButton() {
        driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/input[5]")).click();
    }

    public void clickCreateNewListButton() {
        driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr/td/input[3]")).click();
    }

    public String getDatabaseErrorMsgText() {
        return driver.findElement(By.xpath("/html/body/font")).getText();
    }

    public void assertTitleNormalizationTableData() {
        List<WebElement> trCollection4 = utils.getTableRowsCollection("/html/body/div[2]/table");

        int colNum4 = 0;

        WebElement tdElement4;
        WebElement trElement4;
        List<WebElement> tdCollection4;
        List<WebElement> tdCollectionLastRow;

        int lastRow;
        int rowNum4 = 0;

        trElement4 = trCollection4.get(rowNum4);
        tdCollection4 = trElement4.findElements(By.xpath("td"));
        lastRow = trCollection4.size() - 1;

        tdCollectionLastRow = trCollection4.get(lastRow).findElements(By.xpath("td"));

        for (colNum4 = 0; colNum4 < tdCollection4.size(); colNum4++) {
            tdElement4 = tdCollection4.get(colNum4);
            if (rowNum4 == 0) {
                if (colNum4 == 0)
                    assertThat(tdElement4.getText(), containsString("List Name"));
                else if (colNum4 == 1) {
                    assertThat(tdElement4.getText(), containsString("Type"));
                } else if (colNum4 == 2) {
                    assertThat(tdElement4.getText(), containsString("Options"));
                } else if (colNum4 == 3) {
                    assertThat(tdElement4.getText(), containsString("Holdings"));
                } else if (colNum4 == 4) {
                    assertThat(tdElement4.getText(), containsString("Current\nIndex"));
                } else if (colNum4 == 5) {
                    assertThat(tdElement4.getText(), containsString("Created By"));
                } else if (colNum4 == 6) {
                    assertThat(tdElement4.getText(), containsString("Last Modified"));
                } else {
                    assertThat(tdElement4.getText(), containsString("Action"));
                }
            }
        }

        for (colNum4 = 0; colNum4 < tdCollection4.size(); colNum4++) {

            tdElement4 = tdCollectionLastRow.get(colNum4);

            if (colNum4 == 0)
                assertThat(tdElement4.getText(), containsString("Lists"));
            else if (colNum4 == 2)
                assertThat(tdElement4.getText(), containsString("Holdings"));
            else if (colNum4 == 3)
                assertThat(tdElement4.getText(), containsString("Refresh Table"));
        }
    }

    public void setDatabaseCode(String databaseCode) {
        updateField(dbCodeInput, databaseCode);
    }

    public void assertNormalizationList(String listName, String holdingType, String options) {
        waitForElement(By.xpath("/html/body/div[2]/table"));
        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

        boolean listVerified = false;
        int colNum = 0;

        WebElement trElement;
        WebElement tdElement;
        List<WebElement> tdCollection;

        int lastRow;
        int rowNum;
        String actualName = "";

        lastRow = trCollection.size() - 2;

        for (rowNum = 1; rowNum <= lastRow && !listVerified; rowNum++) {
            trElement = trCollection.get(rowNum);
            tdCollection = trElement.findElements(By.xpath("td"));
            tdElement = tdCollection.get(colNum);
            actualName = tdElement.getText();
            if (actualName.equals(listName)){
                tdElement = tdCollection.get(colNum+1);
                assertThat(tdElement.getText(), containsString(holdingType));
                tdElement = tdCollection.get(colNum+2);
                assertThat(tdElement.getText(), containsString(options));  //value in Options column
                listVerified = true;
            }
        }
        assert(listVerified == true);
    }

    public void deleteNormalizationList(String listName) {  //deletes all instances of the List

        waitForElement(By.xpath("/html/body/div[2]/table"));

        //if one-off lists link exists - click it.
        if (utils.elementExists(By.partialLinkText("one-off lists")))
            driver.findElement(By.partialLinkText("one-off lists")).click();

        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

        int colNum = 0;

        WebElement trElement;
        WebElement tdElement;
        List<WebElement> tdCollection;

        int lastRow = trCollection.size() - 2;
        int rowNum = 0;

        String databaseName = listName.substring(0,listName.indexOf("(")-1);
        String listNum = getNumberOfLists();

        for (rowNum = 1; rowNum <= lastRow; rowNum++) {
            trElement = trCollection.get(rowNum);
            tdCollection = trElement.findElements(By.xpath("td"));
            tdElement = tdCollection.get(colNum);

            if (tdElement.getText().equals(listName)){
                tdCollection.get(colNum+8).findElement(By.linkText("Delete")).click();
                clickConfirmAlert();
                waitForElement(By.xpath("/html/body/div[2]/table"));
                trCollection = utils.getTableRowsCollection("/html/body/div[2]/table"); //to avoid StaleElementReferenceException
                lastRow = trCollection.size() - 2;
                rowNum = 1;
            }
        }
        assert(isNormalizationListPresent(databaseName) == false);
    }
    public String getNumberOfLists(){
        waitForElement(By.xpath("/html/body/div[2]/table"));
        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

        int colNum = 0;
        String listNum;

        WebElement trElement;
        WebElement tdElement;
        List<WebElement> tdCollection;

        int lastRow;
        int rowNum = 0;
        lastRow = trCollection.size() - 1;

        trElement = trCollection.get(lastRow);
        tdCollection = trElement.findElements(By.xpath("td"));
        tdElement = tdCollection.get(0);    //Last Row, First Column
        listNum = tdElement.getText();
        listNum = listNum.substring(0,listNum.indexOf(" ")); //get just the number
        return listNum;
    }
}
