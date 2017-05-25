package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class ReviewChangesForm extends HomePage {


	@FindBy(how = How.XPATH, using = "//select[@name='titleType']")
	private WebElement titleType;

	@FindBy(how = How.XPATH, using = "//select[@name='filter']")
	private WebElement filter;

	@FindBy(how = How.LINK_TEXT, using = "View All Rules")
	private WebElement viewAllRulesLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Evaluate Rules")
	private WebElement evaluateRulesLink;

	@FindBy(how = How.LINK_TEXT, using = "Add Holding")
	private WebElement addHoldingLink;

	@FindBy(how = How.LINK_TEXT, using = "Restart")
	private WebElement restartLink;

	@FindBy(how = How.LINK_TEXT, using = "Yes")
	private WebElement yesLink;

	@FindBy(how = How.LINK_TEXT, using = "No")
	private WebElement noLink;

	@FindBy(how = How.NAME, using = "startsWith")
	private WebElement startsWithInput;
	
	@FindBy(how = How.XPATH, using = "//input[@value='Update View']")
	private WebElement updateViewButton;

	private List<WebElement> holdingsTableRows;

	public EvaluateRulesForm clickEvaluateRulesLink() {
		evaluateRulesLink.click();
		return PageFactory.initElements(driver, EvaluateRulesForm.class);
	}
	
	public ViewRulesForm clickViewRulesLink() {
		viewAllRulesLink.click();
		return PageFactory.initElements(driver, ViewRulesForm.class);
	}

	public EditHoldingForm clickAddHoldingLink() {
		addHoldingLink.click();
		return PageFactory.initElements(driver, EditHoldingForm.class);
	}

	public void setTitleType(String titleType) {
		selectByValue(this.titleType, titleType);
		resetHoldingsTableRows();
		utils.pause(utils.MIN_WAIT_MILLISECONDS);
	}

	public void setFilter(String filter) {
		selectByVisibleText(this.filter, filter);
		utils.pause(utils.MIN_WAIT_MILLISECONDS);
		resetHoldingsTableRows();
	}
	
	public String getFilter() {
		Select select = new Select(driver.findElement(By.xpath("//select[@name='filter']")));
		return select.getFirstSelectedOption().getText();
	}

	public EditHoldingForm clickEditForSerialTitle(String holdingTitle) {
		driver.findElement(By.xpath("//a[text()='"+holdingTitle+"']/parent::td/parent::tr/td[9]/a[text()='Edit']")).click();   
		return PageFactory.initElements(driver, EditHoldingForm.class);
	}

	/**
	 * Clicks "Edit" in the specified row in "XXX Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public EditHoldingForm clickEdit(int rowIndex) {
		int columnIndex = getColumnIndexFor("Action");
		getHoldingsTableRows().get(rowIndex).findElements(By.xpath("./td")).get(columnIndex).click();

		utils.pause(utils.MIN_WAIT_MILLISECONDS);
		return PageFactory.initElements(driver, EditHoldingForm.class);
	}

	/**
	 * Clicks "Edit" in the row identified by the specified title in "XXX Holdings" table
	 * @param title
	 * @return
	 */
	public EditHoldingForm clickEdit(String title) {
		int rowIndex = getRowIndexFor(title);
		int actionColumnIndex = getColumnIndexFor("Action");
		List<WebElement> cells = getHoldingsTableRows().get(rowIndex).findElements(By.xpath("./td"));
		cells.get(actionColumnIndex).findElement(By.xpath("./a")).click();

		utils.pause(utils.MIN_WAIT_MILLISECONDS);
		return PageFactory.initElements(driver, EditHoldingForm.class);
	}

	/**
	 * Returns the row index identified by the specified title in "XXX Holdings" table.
	 * Note: the first title found is used. The "to-be-added" row in green will appear first.
	 * Returns -1 if not found.
	 * @param title
	 * @return
	 */
	public int getRowIndexFor(String title){
		int titleColumnIndex = getColumnIndexFor("Title");
		List<WebElement> rows = getHoldingsTableRows();
		for(int i = 0; i < rows.size(); i++){
			List<WebElement> cells = rows.get(i).findElements(By.xpath("./td"));
			WebElement row = cells.get(titleColumnIndex);
			if(row.getText().equals(title)){
				return i;
			}
		}
		return -1;
	}

	public void clickRestartLink() {
		restartLink.click();
	}

	public void clickYesLink() {
		yesLink.click();
	}

	public void clickNoLink() {
		noLink.click();
	}

	/**
	 * Gets the number of rows in "XXX Holdings" table excluding the header row
	 * @return
	 */
	public int getHoldingsTableRowCount(){
		return getHoldingsTableRows().size();
	}

	/**
	 * Returns a list of "tr" elements excluding the header row.
	 * Return an empty list when there is only the header row present.
	 * @return
	 */
	private List<WebElement> getHoldingsTableRows(){
		if(holdingsTableRows == null){
			List<WebElement> rows = utils.getTableRowsCollection("/html/body/table");
			if(rows.size() > 1){
				holdingsTableRows = new ArrayList<>(rows.subList(1, rows.size()));
			}else{
				holdingsTableRows = new ArrayList<>();
			}
		}
		return holdingsTableRows;
	}
	private WebElement getHoldingsTableHeaderRow(){
		return driver.findElement(By.xpath("/html/body/table/tbody/tr"));
	}

	/**
	 * Gets the column index identified by the header name in "XXX Holdings" table
	 * @param header
	 * @return
	 */
	private int getColumnIndexFor(String header){
		List<WebElement> headers = getHoldingsTableHeaderRow().findElements(By.tagName("th"));

		for(int i = 0; i < headers.size(); i++){
			if(headers.get(i).getText().equals(header)){
				return i;
			}
		}

		return -1;
	}

	/**
	 * Gets the cell value identified by the row index and header name.
	 * The header row is skipped.
	 * @param rowIndex
	 * @param header
	 * @return
	 */
	private String getCellValue(int rowIndex, String header){
		if(getHoldingsTableRowCount() == 0){
			return "HOLDINGS TABLE IS EMPTY";
		}

		int columnIndex = getColumnIndexFor(header);
		return columnIndex == -1 ? "SPECIFIED HEADER NOT FOUND" :
									getHoldingsTableRows().get(rowIndex).findElements(By.xpath("./td")).get(columnIndex).getText();
	}

	/**
	 * Resets the list of rows built previously by retrieving cell data.
	 * The method needs to be called when the table data changes by changing the filter or navigating away from the form
	 * and come back again.
	 */
	public void resetHoldingsTableRows(){
		holdingsTableRows = null;
	}

	/**
	 * Gets the cell value in "Title" column identified by the 0 based row index in "XXX Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getTitle(int rowIndex){
		return getCellValue(rowIndex, "Title");
	}

	/**
	 * Gets the cell value in "Authority" column identified by the 0 based row index in "XXX Holdings" table.
	 * @param rowIndex
	 * @return
	 */
	public String getAuthority(int rowIndex) {
		return getCellValue(rowIndex, "Authority");
	}

	/**
	 * Gets the cell value in "Publisher" column identified by the 0 based row index in "Serial Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getPublisher(int rowIndex) {
		return getCellValue(rowIndex, "Publisher");
	}

	/**
	 * Gets the cell value in "URL" column identified by the 0 based row index in "XXX Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getUrl(int rowIndex) {
		return getCellValue(rowIndex, "URL");
	}

	/**
	 * Gets the cell value in "ISSN" column identified by the 0 based row index in "Serial Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getIssn(int rowIndex) {
		return getCellValue(rowIndex, "ISSN");
	}

	/**
	 * Gets the cell value in "Start" column identified by the 0 based row index in "Serial Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getStart(int rowIndex) {
		return getCellValue(rowIndex, "Start");
	}

	/**
	 * Gets the cell value in "End" column identified by the 0 based row index in "Serial Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getEnd(int rowIndex) {
		return getCellValue(rowIndex, "End");
	}

	/**
	 * Gets the cell value in "ProviderTitleID" column identified by the 0 based row index in "XXX Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getProviderTitleId(int rowIndex) {
		return getCellValue(rowIndex, "ProviderTitleID");
	}

	/**
	 * Gets the cell value in "ISBN13" column identified by the 0 based row index in "Monograph Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getIsbn13(int rowIndex) {
		return getCellValue(rowIndex, "ISBN13");
	}

	/**
	 * Gets the cell value in "Published Date" column identified by the 0 based row index in "Monograph Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getPublishedDate(int rowIndex) {
		return getCellValue(rowIndex, "Published Date");
	}

	/**
	 * Gets the cell value in "Author" column identified by the 0 based row index in "Monograph Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getAuthor(int rowIndex) {
		return getCellValue(rowIndex, "Author");
	}

	/**
	 * Gets the cell value in "Error" column identified by the 0 based row index in "XXX Holdings" table
	 * @param rowIndex
	 * @return
	 */
	public String getError(int rowIndex){
		return getCellValue(rowIndex, "Error");
	}

	public String getAppliedRules() {
		String text = driver.findElement(By.xpath("//table/tbody/tr/td/b[text()='Applied Rules: ']/parent::td")).getText().trim();
		return text.substring(text.length()-1, text.length());
	}
	public String getUnappliedRules() {
		String text = driver.findElement(By.xpath("//table/tbody/tr/td/b[text()='Unapplied Rules:']/parent::td")).getText().trim();
		return text.substring(text.length()-1, text.length());
	}

    public void restartThenGoToReviewChanges(ImportQueueForm importQueueForm, String databaseCode) {
        this.clickRestartLink();
        importQueueForm.waitForDBprocessing(databaseCode);
        importQueueForm.waitForLinkPresent(databaseCode);
        importQueueForm.clickDBlink(databaseCode);
    }

	/**
	 * Checks to see if the specified title is present in "XXX Holdings" table
	 * @param title
	 * @return
	 */
	public Boolean titlePresentInTable(String title) {
		int titleColumnIndex = getColumnIndexFor("Title");
		List<WebElement> rows =  getHoldingsTableRows();
		for(WebElement row : rows){
			String value = row.findElements(By.xpath("./td")).get(titleColumnIndex).getText();
			if(title.equals(value)){
				return true;
			}
		}

		return false;
	}

	public void setStartWith(String title) {
		updateField(startsWithInput, title);
	}

	public void clickUpdateView() {
		updateViewButton.click();
		resetHoldingsTableRows();
	}
}
