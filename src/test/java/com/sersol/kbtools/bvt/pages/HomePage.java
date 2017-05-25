package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.pages.dataStatistics.DataStatisticsPage;
import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.sersol.common.bvt.pages.BasePage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.sersol.kbtools.bvt.utils.HelperMethods;



public class HomePage extends BasePage implements ITestConstants {
    @Before
    public void setup() {
		WebDriverWait wait = new WebDriverWait(driver,50);}

	public HelperMethods utils = new HelperMethods();

	@FindBy(how = How.LINK_TEXT, using = "Main")
	private WebElement mainLink;

	@FindBy(how = How.LINK_TEXT, using = "Holdings Importer")
	private WebElement holdingsImporterLink;

	@FindBy(how = How.LINK_TEXT, using = "Holding Search")
	private WebElement holdingsSearchLink;

    @FindBy(how = How.LINK_TEXT, using = "Rule Search")
    private WebElement ruleSearchLink;

	@FindBy(how = How.LINK_TEXT, using = "Authority Title Search")
	private WebElement authorityTitleSearchLink;

	@FindBy(how = How.LINK_TEXT, using = "MARC Search")
	private WebElement marcSearchLink;

    @FindBy(how = How.LINK_TEXT, using = "Advanced MARC Search")
	private WebElement advancedMarcSearchLink;

	@FindBy(how = How.LINK_TEXT, using = "Database Search")
	private WebElement databaseSearchLink;

    @FindBy(how = How.LINK_TEXT, using = "Import Queue")
    private WebElement importQueueLink;

	@FindBy(how = How.LINK_TEXT, using = "MARC Importer")
	private WebElement marcImporterLink;

	@FindBy(how = How.LINK_TEXT, using = "Title Normalization")
	private WebElement titleNormalizationLink;

	@FindBy(how = How.LINK_TEXT, using = "Data Statistics")
	private WebElement dataStatisticsLink;

	@FindBy(how = How.NAME, using = "dbCode")
	private WebElement dbCodeInput;

	@FindBy(how = How.LINK_TEXT, using = "[ Start Refresh ]")
	private WebElement startRefreshLink;

	@FindBy(how = How.LINK_TEXT, using = "[ Stop Refresh ]")
	private WebElement stopRefreshLink;

	@FindBy(how = How.CSS, using = "input[value='Search']")
	protected WebElement searchButton;

	@FindBy(how = How.CSS, using = "input[value='Clear']")
	private WebElement clearButton;

	@FindBy(how = How.CSS, using = "input[value='Reset']")
	private WebElement resetButton;

	@FindBy(how = How.NAME, using = "searchType")
	private WebElement searchTypeSelect;

	@FindBy(how = How.NAME, using = "criteria")
	private WebElement 	searchCriteriaInput;

	@FindBy(how = How.NAME, using = "fromType")
	private WebElement titleTypeFastHEdit;

	@FindBy(how = How.NAME, using = "fromTitle")
	private WebElement titleFastHEdit;

	@FindBy(how = How.NAME, using = "fromURL")
	private WebElement urlFastHEdit;

	@FindBy(how = How.NAME, using = "fromAuthor")
	private WebElement authorFastHEdit;

	@FindBy(how = How.NAME, using = "fromPublisher")
	private WebElement publisherFastHEdit;

	@FindBy(how = How.NAME, using = "fromEditor")
	private WebElement editorFastHEdit;

	@FindBy(how = How.NAME, using = "fromEdition")
	private WebElement editionFastHEdit;

	@FindBy(how = How.NAME, using = "fromDatePublished")
	private WebElement datePublishedFastHEdit;

	@FindBy(how = How.NAME, using = "fromDateStart")
	private WebElement dateStartFastHEdit;

	@FindBy(how = How.NAME, using = "fromDateStartText")
	private WebElement dateStartTextFastHEdit;

	@FindBy(how = How.NAME, using = "fromDateEnd")
	private WebElement dateEndFastHEdit;

	@FindBy(how = How.NAME, using = "fromDateEndText")
	private WebElement dateEndTextFastHEdit;

	@FindBy(how = How.NAME, using = "fromISSN")
	private WebElement issnFastHEdit;

	@FindBy(how = How.NAME, using = "fromISBN10")
	private WebElement isbn10FastHEdit;

	@FindBy(how = How.NAME, using = "fromISBN13")
	private WebElement isbn13FastHEdit;
	
	@FindBy(how = How.NAME, using = "providerTitleId")
	private WebElement providerTitleIdFastHEdit;

	@FindBy(how = How.XPATH, using = "//input[@value='Update']")
	private WebElement updateFHEButton;

	@FindBy(how = How.LINK_TEXT, using = "HDI Logs")
	private WebElement hdiLogsLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Log Export")
	private WebElement logExportLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Review Page")
	private WebElement reviewPageLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Load Queue")
	private WebElement loadQueueLink;
	
	
	
	public MainMenuForm clickMainMenu() {
		WebDriverWait wait;
		wait = new WebDriverWait(driver, 60, 2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Main"))); //wait sometimes needed for TitleNormalization page
		mainLink.click();
		return PageFactory.initElements(driver, MainMenuForm.class);
	}

	public HoldingImporterForm selectHoldingImporterLink() {
		holdingsImporterLink.click();
		return PageFactory.initElements(driver, HoldingImporterForm.class);
	}

	public HoldingSearchForm selectHoldingSearchLink() {
		holdingsSearchLink.click();
		return PageFactory.initElements(driver, HoldingSearchForm.class);
	}

    public RuleSearchForm selectRuleSearchLink() {
        ruleSearchLink.click();
        return PageFactory.initElements(driver, RuleSearchForm.class);
    }

    public TitleSearchForm selectAuthorityTitleSearchLink() {
		authorityTitleSearchLink.click();
		return PageFactory.initElements(driver, TitleSearchForm.class);
	}

	public MarcSearchForm selectMarcSearchLink() {
		marcSearchLink.click();
		return PageFactory.initElements(driver, MarcSearchForm.class);
	}

	public AdvancedMarcSearchForm selectAdvancedMarcSearchLink() {
		advancedMarcSearchLink.click();
		return PageFactory.initElements(driver, AdvancedMarcSearchForm.class);
	}

	public DatabaseSearchForm selectDatabaseSearchLink() {
		databaseSearchLink.click();
		return PageFactory.initElements(driver, DatabaseSearchForm.class);
	}

    public ImportQueueForm selectImportQueueLink() {
        Actions action = new Actions(driver);
        action.moveToElement(holdingsImporterLink).build().perform();
        importQueueLink.click();
		return PageFactory.initElements(driver, ImportQueueForm.class);
    }

	public MarcImporterForm selectMarcImporterLink() {
		marcImporterLink.click();
		return PageFactory.initElements(driver, MarcImporterForm.class);
	}
	
	public LoadQueueForm selectLoadQueueLink() {
		loadQueueLink.click();
		return PageFactory.initElements(driver, LoadQueueForm.class);
	}

	/**
	 * Clicks the "Library Groups" link to open "Library Group Editor" page
	 * @return
	 */
	public LibraryGroupEditorPage selectLibraryGroupsLink(){
		driver.findElement(By.linkText("Library Groups")).click();
		return PageFactory.initElements(driver, LibraryGroupEditorPage.class);
	}

	public HomePage selectMainLink(){
		mainLink.click();
		return PageFactory.initElements(driver, HomePage.class);
	}
	
	public TitleNormalizationForm selectTitleNormalizationLink() {
		titleNormalizationLink.click();
		return PageFactory.initElements(driver, TitleNormalizationForm.class);
	}
	
	public HdiLogsForm selectHdiLogsLink() {
		hdiLogsLink.click();
		return PageFactory.initElements(driver, HdiLogsForm.class);
	}
	
	public ReviewPageForm selectReviewPageLink() {
		reviewPageLink.click();
		return PageFactory.initElements(driver, ReviewPageForm.class);
	}
	
	public ExportLogsForm selectExportLogsLink() {
		logExportLink.click();
		return PageFactory.initElements(driver, ExportLogsForm.class);
	}

    public ViewSubjectForm clickSubjectLink(String subject) {
        waitForElement(By.linkText(subject)).click();
        return PageFactory.initElements(driver, ViewSubjectForm.class);
    }

	public DataStatisticsPage clickDataStatisticsLink(){
		dataStatisticsLink.click();
		return PageFactory.initElements(driver, DataStatisticsPage.class);
	}

	public void clickStartRefreshLink() {
		startRefreshLink.click();
	}

	public void clickStopRefreshLink() {
		stopRefreshLink.click();
	}
	
	public void goToPreviousPage() {
		driver.navigate().back();
	}

    public void setSearchCriteria(String searchCriteria) {
		updateField(searchCriteriaInput, searchCriteria);
	}

	public boolean isSearchCriteriaInputPresent() {
		return searchCriteriaInput.isDisplayed();
	}

	public void clickSearchButton() {
		driver.findElement(By.cssSelector("input[value='Search']")).click();
	}

	public void setDatabaseCode(String dbCode) {
		updateField(dbCodeInput, dbCode);
	}

	public boolean isDatabaseCodeInputPresent() {
		return dbCodeInput.isDisplayed();
	}

	public String getDatabaseCodeInput() {
		return dbCodeInput.getText().trim();
	}

	public boolean isClearButtonPresent() {
		return clearButton.isDisplayed();
	}

	public Boolean isSearchButtonPresent() {
		return searchButton.isDisplayed();
	}

	public void clickClearButton() {
		clearButton.click();
	}

	public boolean isResetButtonPresent() {
		return resetButton.isDisplayed();
	}

	//FAST HOLDING EDIT
	/**
	 * Enters a title in the Title field of the fastHoldingsEdit page
	 *
	 * @param  title
	 */
	public void setFastTitle(String title) {
		updateField(this.titleFastHEdit, title);
	}

	/**
	 * Enters a URL in the URL field of the fastHoldingsEdit page
	 *
	 * @param  URL
	 */
	public void setFastURL(String URL) {
		updateField(this.urlFastHEdit, URL);
	}

	public void setFastAuthor(String author) {
		updateField(this.authorFastHEdit, author);
	}

	/**
	 * Enters a publisher in the Publisher field of the fastHoldingsEdit page
	 *	 * @param  title
	 */
	public void setFastPublisher(String publisher) {
		updateField(this.publisherFastHEdit, publisher);
	}

	public void setFastEditor(String editor) {
		updateField(this.editorFastHEdit, editor);
	}

	public void setFastEdition(String edition) {
		updateField(this.editionFastHEdit, edition);
	}

	public void setFastDatePublished(String datePublished) {
		updateField(this.datePublishedFastHEdit, datePublished);
	}

	/**
	 * Enters a date in the Date Start field of the fastHoldingsEdit page
	 *
	 * @param  dateStart
	 */
	public void setFastDateStart(String dateStart) {
		updateField(this.dateStartFastHEdit, dateStart);
	}

	public void setFastDateStartText(String dateStartText) {
		updateField(this.dateStartTextFastHEdit, dateStartText);
	}

	/**
	 * Enters a date in the Date End field of the fastHoldingsEdit page
	 *
	 * @param  dateEnd
	 */
	public void setFastDateEnd(String dateEnd) {
		updateField(this.dateEndFastHEdit, dateEnd);
	}

	public void setFastDateEndText(String dateEndText) {
		updateField(this.dateEndTextFastHEdit, dateEndText);
	}

	/**
	 * Enters an ISSN in the ISSN field of the fastHoldingsEdit page
	 *
	 * @param  issn
	 */
	public void setFastISSN(String issn) {
		updateField(this.issnFastHEdit, issn);
	}

	public void setFastISBN10(String isbn10) {
		updateField(this.isbn10FastHEdit, isbn10);
	}

	public void setFastISBN13(String isbn13) {
		updateField(this.isbn13FastHEdit, isbn13);
	}
	
	public void setFastProviderTitleId(String providerTitleId) {
		updateField(this.providerTitleIdFastHEdit, providerTitleId);
	}

	/**
	 * Clicks the Update button to save changes and return to the Holdings Search page
	 */
	public void clickUpdateFastHoldingEditButton() {
		updateFHEButton.click();
		waitForElement(By.linkText("Export To File"));
	}
	
	public void setTitleSearchType(String searchType) {
        selectByVisibleText(searchTypeSelect, searchType);
    }

	public boolean isSearchTypeSelectPresent() {
    	return searchTypeSelect.isDisplayed();
    }
    
    public String getSearchTypeSelectContent() {
        return searchTypeSelect.getText();
    }
    

    public HoldingSearchForm searchForHolding(String databaseCode, String title) {
        this.clickMainMenu();
        HoldingSearchForm holdingSearchForm = this.selectHoldingSearchLink();
        holdingSearchForm.holdingSearch(databaseCode, title);
        return holdingSearchForm;
    }

    public RuleSearchForm searchForRule(String ruleID) {
        this.clickMainMenu();
        RuleSearchForm ruleSearchForm = this.selectRuleSearchLink();
        ruleSearchForm.ruleSearch(ruleID);
        return ruleSearchForm;
    }
}
