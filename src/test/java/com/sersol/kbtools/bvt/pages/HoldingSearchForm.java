package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.tests.ITestConstants;

public class HoldingSearchForm extends HomePage implements ITestConstants {

	@FindBy(how = How.NAME, using = "dbCode")
	private WebElement dbCodeInput;

	@FindBy(how = How.NAME, using = "provider")
	private WebElement providerCodeInput;

	@FindBy(how = How.NAME, using = "titleCode")
	private WebElement titleCodeInput;

	@FindBy(how = How.NAME, using = "id")
	private WebElement issnIsbnInput;

	@FindBy(how = How.NAME, using = "title")
	private WebElement titleInput;

	@FindBy(how = How.NAME, using = "url")
	private WebElement urlInput;

	@FindBy(how = How.NAME, using = "titleSearchType")
	private WebElement titleSearchTypeSelect;

	@FindBy(how = How.LINK_TEXT, using = "Monographs")
	private WebElement monographLink;

	@FindBy(how = How.LINK_TEXT, using = "New Search")
	private WebElement newSearchLink;

	@FindBy(how = How.LINK_TEXT, using = "Serials")
	private WebElement serialsLink;
	
	@FindBy(how = How.NAME, using = "includeLSH")
	private WebElement includeLshCheckbox;
	
	@FindBy(how = How.NAME, using = "titleType")
	private WebElement titleTypeSelect;
	
	@FindBy(how = How.NAME, using = "NormalizedState")
	private WebElement normalizedSelect;
	
	@FindBy(how = How.NAME, using = "exportDirectly")
	private WebElement exportFileCheckbox;
	
	@FindBy(how = How.NAME, using = "escapeWild")
	private WebElement escapeWildcardsCheckbox;

	

	public void setDatabaseCode(String databaseCode) {
		updateField(dbCodeInput, databaseCode);
	}

	public void setProvider(String providerValue) {
		updateField(providerCodeInput, providerValue);
	}

	public void setTitleCode(String titleCodeValue) {
		updateField(titleCodeInput, titleCodeValue);
	}

	public void setId(String id) {
		updateField(issnIsbnInput, id);
	}

	public void setTitle(String titleValue) {
		updateField(titleInput, titleValue);
	}

	public void setTitleURL(String titleURLValue) {
		updateField(urlInput, titleURLValue);
	}

	public void setTitleSearchType(String searchType) {
		selectByVisibleText(titleSearchTypeSelect, searchType);
	}

	/**
	 * Gets the Title of the Holdings Search Results Table that indicates whether the holdings are of type,
	 * serial, monograph, or unknown.
	 *
	 * @return the title of the table e.g. "Serial Holdings", "Monograph Holdings, etc.
	 *
	 */
	public String getTitleOfHoldingsTable(){
		return waitForElement(By.cssSelector("tbody>tr>td>b")).getText();
	}

	public String getDatabaseCode() {
		return waitForElement(By.xpath("//a[text()='DB']/parent::th/parent::tr//following-sibling::tr/td[1]/a")).getText().trim();
	}

	public String getDatabaseName() {
		return waitForElement(By.xpath("//a[text()='Database Name']/parent::th/parent::tr//following-sibling::tr/td[2]")).getText().trim();
	}

	public String getTitleCode() {
		return waitForElement(By.xpath("//a[text()='Authority']/parent::th/parent::tr//following-sibling::tr/td[3]/a")).getText().trim();
	}

	public String getISSN(WebElement holding){
		List<WebElement> elements = holding.findElements(By.cssSelector("td"));
		if(elements.size()<4) throw new IndexOutOfBoundsException();
		return elements.get(3).getText();
	}


	public String getISBN13() {	//gets ISBN in first row of the table
		return waitForElement(By.xpath("//a[text()='ISBN13']/parent::th/parent::tr//following-sibling::tr/td[5]/a")).getText().trim();
	}

	public String getISBN13(WebElement holding) {
		List<WebElement> elements = holding.findElements(By.cssSelector("td"));
		if(elements.size()<3) throw new IndexOutOfBoundsException();
		return elements.get(2).getText();
	}

	/**
	 * Gets holding title from the specified row of the search results table
	 *
	 * @param  rowIndex
	 * @return
	 */
	public String getTitle(int rowIndex) {
		int columnIndex = getColumnIndexFor("<<     Title");
		return getHoldings( ).get(rowIndex).findElements(By.cssSelector("td")).get(columnIndex).getText().trim();
	}

	/**
	 * Checks whether a Holding of the specified title exists in the search results
	 *
	 * @param  title
	 * @return
	 */
	public Boolean titleExists(String title){
		int rowIndex = getHoldingRowIndex(title);
		return (rowIndex != -1);
	}

	/**
	 * Gets the ISSN of a holding of the specified title in the Search Results table
	 *
	 * @param  title
	 * @return
	 */
	public String getISSN(String title){
		return getHoldingValueByTitle(title, "ISSN");
	}

	/**
	 * Gets the Publisher of a holding of the specified title in the Search Results table
	 *
	 * @param  title
	 * @return
	 */
	public String getPublisher(String title){
		return getHoldingValueByTitle(title, "Publisher");
	}
	/**
	 * Gets the Start Date of a holding of the specified title in the Search Results table
	 *
	 * @param  title
	 * @return
	 */
	public String getDateStart(String title){
		return getHoldingValueByTitle(title, "Start");
	}

	/**
	 * Gets the End Date of a holding of the specified title in the Search Results table
	 *
	 * @param  title
	 * @return
	 */
	public String getDateEnd(String title){
		return getHoldingValueByTitle(title, "End");
	}

	/**
	 * Gets the URL of a holding of the specified title in the Search Results table
	 *
	 * @param  title
	 * @return
	 */
	public String getUrl(String title){
		return getHoldingValueByTitle(title, "<<     URL");
	}

	/**
	 * Gets the ISBN13 of a holding of the specified title in the Search Results table
	 *
	 * @param  title
	 * @return
	 */
	public String getISBN13(String title){
		return getHoldingValueByTitle(title, "ISBN13");
	}

	/**
	 * Gets the value of a column in the Search Results for a holding of a given title
	 *
	 * @param  title
	 * @param  header
	 * @return
	 */
	private String getHoldingValueByTitle(String title, String header){
		int rowIndex = getHoldingRowIndex(title);
		int columnIndex = getColumnIndexFor(header);

		if (rowIndex<0) return "Holding not found";
		if (columnIndex<0) return "Header not found";

		WebElement holding = getHoldings().get(rowIndex);
		return holding.findElements(By.cssSelector("td")).get(columnIndex).getText().trim();
	}

	public String getMonographEdition() {
		return waitForElement(By.xpath("//a[text()='Edition']/parent::th/parent::tr//following-sibling::tr/td[7]")).getText().trim();
	}

	/** Gets the Edition of a holding of the specified title in the Search Results table
	 *
	 * @param title
	 * @return
	 */
	public String getEdition(String title) {
		return getHoldingValueByTitle(title, "Edition");
	}

	public String getMonographAuthor() {
		return waitForElement(By.xpath("//a[contains(.,'Author')]/parent::th/parent::tr//following-sibling::tr/td[8]/a")).getText().trim();
	}

	public String getMonographEditor() {
		return waitForElement(By.xpath("//a[text()='Editor']/parent::th/parent::tr//following-sibling::tr/td[9]")).getText().trim();
	}

	/** Gets the Editor of a holding of the specified title in the Search Results table
	 *
	 * @param title
	 * @return
	 */
	public String getEditor(String title) {
		return getHoldingValueByTitle(title, "Editor");
	}

	public String getMonographPublished() {
		return waitForElement(By.xpath("//a[text()='Published']/parent::th/parent::tr//following-sibling::tr/td[11]")).getText().trim();
	}

	public String getAuthor() {
		return waitForElement(By.xpath("//a[text()='Author']/parent::th/parent::tr//following-sibling::tr/td[8]")).getText().trim();
	}

	/** Gets the Author of a holding of the specified title in the Search Results table
	 *
	 * @param title
	 * @return
	 */
	public String getAuthor(String title) {
		return getHoldingValueByTitle(title, "Author");
	}

	public String getPublishedDate() {
		return waitForElement(By.xpath("//a[text()='Published']/parent::th/parent::tr//following-sibling::tr/td[11]")).getText().trim();
	}

	/** Gets the Date Published of a holding of the specified title in the Search Results table
	 *
	 * @param title
	 * @return
	 */
	public String getDatePublished(String title){
			return getHoldingValueByTitle(title, "Published");
	}

	/**
	 * Clicks the Edit button for a holding in a specified row of the Search Results table.
	 * Clicking the button renders the Fast Holding Edit page
	 *
	 * @param  rowIndex 0-based
	 * @return
	 */
	public FastHoldingEditForm clickEdit(int rowIndex){
		int columnIndex = getColumnIndexFor("Action");
		getHoldings().get(rowIndex).findElements(By.xpath("./td")).get(columnIndex).click();
		waitForElement(By.linkText("CJK Sorting Metadata"));
		return PageFactory.initElements(driver, FastHoldingEditForm.class);
	}

	/**
	 * Clicks Monographs link to display holdings of that type
	 */
	public void clickMonographLink() {
		waitForElement(By.linkText("Monographs")).click();
	}

	public void clickNewSearchLink() {
		newSearchLink.click();
	}

	/**
	 * Clicks Serials link to display holdings of that type
	 */
	public void clickSerialLink() {
		waitForElement(By.linkText("Serials")).click();
	}

	/**
	 * Clicks Unknown link to display holdings of that type
	 */
	public void clickUnknownLink() {driver.findElement(By.linkText("Unknowns")).click();}

	public ViewTitle clickTitleCodeLink(String titleCode) {
		utils.clickLink(titleCode);
		return PageFactory.initElements(driver, ViewTitle.class);
	}
	
	public void clickIncludeLshCheckbox() {
		includeLshCheckbox.click();
	}
	
	public String getResultsTableTitle() {
		return waitForElement(By.xpath("/html/body/div[2]/table[2]/tbody/tr/td[1]/b")).getText();
	}
	
	public String getTableResultsText() {
		return waitForElement(By.xpath("/html/body/div[2]")).getText();
	}

	public String getNumberOfSerialsFound() {
		return waitForElement(By.xpath("//a[contains(.,'Serials')]/parent::th//following-sibling::td")).getText().trim();
	}

	public FastHoldingEditForm clickEditButtonForMonograph(String title) {
		waitForElement(By.xpath("//a[text()='" + title +"']/parent::td/parent::tr/td[13]/input[@value='Edit']")).click();
		return PageFactory.initElements(driver, FastHoldingEditForm.class);
	}

    public FastHoldingEditForm clickEditButtonForSerial(String title) {
        waitForElement(By.xpath("//a[text()='" + title +"']/parent::td/parent::tr/td[10]/input[@value='Edit']")).click();
        return PageFactory.initElements(driver, FastHoldingEditForm.class);
    }

	public void saveFile(){
		WebElement webE = waitForElement(By.xpath("//a[contains(.,'Export To File')]"));
		String url = webE.getAttribute("href");
		//Download the file
		driver.navigate().to(url);
	}

	public void refresh() {
		driver.navigate().refresh();	
	}

	public String getSearchCriteraText() {
		return waitForElement(By.xpath("/html/body/div[2]/p[1]")).getText();
	}
	
	public String getDatabaseCodeLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[1]/b[1]")).getText();
	}
	
	public String getProviderCodeLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[1]/b[2]")).getText();
	}

	public String getTitleCodeLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[1]/b[3]")).getText();
	}

	public String getProviderTitleIdLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[1]/b[4]")).getText();
	}

	public String getIssnIsbnLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[2]/b[1]")).getText();
	}

	public String getTitleLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[2]/b[2]")).getText();
	}

	public String getUrlLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[3]/b")).getText();
	}

	public String getSelectsAndCheckboxLabels() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[4]")).getText();
	}
	
	public String getExportFileLabel() {
		return waitForElement(By.xpath("/html/body/div[3]/form/p[5]")).getText();
	}

	/**
	 * Gets the column index identified by the header name in "XXX Holdings" table
	 * @param header
	 * @return
	 */
	private int getColumnIndexFor(String header){
		List<WebElement> headers = getSearchResultsTableHeaderRow().findElements(By.tagName("th"));

		for(int i = 0; i < headers.size(); i++){

			if(headers.get(i).getText().contentEquals(header)){
				return i;
			}
		}

		return -1;
	}

	private WebElement getSearchResultsTableHeaderRow(){
		return getHoldingSearchResultsTable().findElement(By.cssSelector("tr"));
	}

	/**
	 * Does a Holdings Search by database code and title
	 *
	 * @param  databaseCode
	 * @param  title
	 */
    public void holdingSearch(String databaseCode, String title) {
        this.setDatabaseCode(databaseCode);
		this.setTitle(title);
		titleInput.sendKeys(Keys.TAB);	//replaced sleep
        this.clickSearchButton();
    }

    public Boolean isNoHoldingsFound() {
        int size = driver.findElements(By.cssSelector("div.page_content")).size();
        assertThat("Expected page content", size > 0);
        String pageText = waitForElement(By.cssSelector("div.page_content")).getText();
        return pageText.contains("No Results Found");
    }
	
	public boolean isProviderInputPresent() {
		return providerCodeInput.isDisplayed();
	}
	
	public boolean isTitleCodeInputPresent() {
		return titleCodeInput.isDisplayed();
	}
	
	public boolean isProviderTitleInputPresent() {
		return providerCodeInput.isDisplayed();
	}
	
	public boolean isIssnIsbnInputPresent() {
		return issnIsbnInput.isDisplayed();
	}
	
	public boolean isTitleInputPresent() {
		return titleInput.isDisplayed();
	}
	
	public boolean isUrlInputPresent() {
		return urlInput.isDisplayed();
	}
	
	public boolean isTitleSearchTypeSelectPresent() {
		return titleSearchTypeSelect.isDisplayed();
	}
	
	public boolean isTitleTypeSelectPresent() {
		return titleTypeSelect.isDisplayed();
	}
	
	public boolean isNormalizedSelectPresent() {
		return normalizedSelect.isDisplayed();
	}
	
	public boolean isExportFileCheckboxPresent() {
		return exportFileCheckbox.isDisplayed();
	}
	
	public boolean isIncludeLshCheckboxPresent() {
		return includeLshCheckbox.isDisplayed();
	}
	
	public boolean isEscapeWildcardsCheckboxPresent() {
		return escapeWildcardsCheckbox.isDisplayed();
	}
	
	public void extractAndAssertResultsDataForLSHoff() {

		File file = new File(RESOURCE_FILES_PATH + AUTH_PROV_CODES_TXT_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = "\n";

		try {
			String line = "";

			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {

				String[] tokens = line.split(DELIMITER);

				for(String token : tokens)	{

					this.setProvider(token);
					this.clickSearchButton();

					assertThat(this.getSearchCriteraText(), containsString("Provider="+token));
					utils.login();
					PageRegistry.get(HomePage.class).selectHoldingSearchLink();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void extractAndAssertResultsDataForLSHon() {

		File file = new File(RESOURCE_FILES_PATH + AUTH_PROV_CODES_TXT_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = "\n";

		try {
			String line = "";

			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {

				String[] tokens = line.split(DELIMITER);

				for(String token : tokens)	{

					this.setProvider(token);
					this.clickIncludeLshCheckbox();
					this.clickSearchButton();

					assertThat(this.getSearchCriteraText(), containsString("Provider="+token));
					utils.login();
					PageRegistry.get(HomePage.class).selectHoldingSearchLink();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void extractAndAssertResultsDataForDbCode() {
		File file = new File(RESOURCE_FILES_PATH + DB_CODES_TXT_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = "\n";

		try {
			String line = "";

			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {

				String[] tokens = line.split(DELIMITER);

				for(String token : tokens)	{

					this.setDatabaseCode(token);
					this.clickSearchButton();
			    	
			    	assertThat(this.getSearchCriteraText(),containsString("Database="+token));
			    	
			    	utils.login();
					PageRegistry.get(HomePage.class).selectHoldingSearchLink();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void extractInfoAndAssertDataForFastHoldingSerials() {
		File file = new File(RESOURCE_FILES_PATH + FAST_HOLDING_EDIT_SERIALS_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = ",";

		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table[3]");

		int colNum;

		WebElement trElement;
		List<WebElement> tdCollection;

		for(int rowNum=0;rowNum<trCollection.size();rowNum++) {

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));
			colNum = 0;

			for(colNum=0; colNum<tdCollection.size();colNum++) {
				if(rowNum!=0) {
					if(colNum==14) {
						utils.clickLink("Edit");

						try {
							String line = "";

							fileReader = new BufferedReader(new FileReader(fileToParse));

							int a = 0;
							int b = 0;
							int c = 1;
							int d = 6;
							int e = 9;
							int f = 10;
							int g = 11;
							int h = 12;
							int i = 13;
							
							String title = null;
							String issn = null;
							String publisher = null;
							String dateStart = null;
							String dateStartText = null;
							String dateEnd = null;
							String dateEndText = null;
							String url = null;

							while ((line = fileReader.readLine()) != null) {

								String[] tokens = line.split(DELIMITER);
								
								for(String token : tokens)	{

									if(a==b) {
										title = token;
										this.setFastTitle(title);
										b = b + 13;
									} else if(a==c) {
										issn = token;
										this.setFastISSN(issn);
										c = c + 13;
									} else if(a==d) {
										publisher = token;
										this.setFastPublisher(publisher);
										d = d + 13;
									} else if(a==e) {
										dateStart = token;
										this.setFastDateStart(dateStart);
										e = e + 13;
									} else if(a==f) {
										dateStartText = token;
										this.setFastDateStartText(dateStartText);
										f = f + 13;
									} else if(a==g) {
										dateEnd = token;
										this.setFastDateEnd(dateEnd);
										g = g + 13;
									} else if(a==h) {
										dateEndText = token;
										this.setFastDateEndText(dateEndText);
										h = h +13;
									} else if(a==i) {
										url = token;
										this.setFastURL(url);
										i = i + 13;
									}
									
									this.clickUpdateFastHoldingEditButton();

									assertTableForFastHoldingSerials(rowNum,
											title, issn, publisher, dateStart,
											dateStartText, dateEnd,
											dateEndText, url);

									a++;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								fileReader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param rowNum
	 * @param title
	 * @param issn
	 * @param publisher
	 * @param dateStart
	 * @param dateStartText
	 * @param dateEnd
	 * @param dateEndText
	 * @param url
	 */
	private void assertTableForFastHoldingSerials(int rowNum, String title,
			String issn, String publisher, String dateStart,
			String dateStartText, String dateEnd, String dateEndText, String url) {
		
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table[3]");

		int colNum;

		WebElement tdElement;
		WebElement trElement;
		List<WebElement> tdCollection;


		trElement = trCollection.get(rowNum);
		tdCollection = trElement.findElements(By.xpath("./td"));
		colNum = 0;

		for(colNum=0; colNum<tdCollection.size();colNum++) {
			tdElement = tdCollection.get(colNum);
			if(colNum==4)
				assertThat(tdElement.getText(), containsString(issn));
			else if(colNum==5)
				assertThat(tdElement.getText(), containsString(title));
			else if(colNum==6)
				assertThat(tdElement.getText(), containsString(dateStart));
			else if(colNum==7)
				assertThat(tdElement.getText(), containsString(dateStartText));
			else if(colNum==8)
				assertThat(tdElement.getText(), containsString(dateEnd));
			else if(colNum==9)
				assertThat(tdElement.getText(), containsString(dateEndText));
			else if(colNum==11)
				assertThat(tdElement.getText(), containsString(publisher));
			else if(colNum==12)
				assertThat(tdElement.getText(), containsString(url));
		}
	}

	public void extractInfoAndAssertDataForISBNs() {
		
		File file = new File(RESOURCE_FILES_PATH + ISBN_SAMPLE_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = ",";
		
		try {
			String line = "\n";
		
			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {
				
                String[] tokens = line.split(DELIMITER);
                                
                for(String token : tokens)	{
                	
                	this.setId(token);
            		this.clickSearchButton();
            		this.clickMonographLink();
            		
            		assertThat(this.getResultsTableTitle(), containsString("Monograph Holdings"));
            		assertThat(this.getResultsTableTitle().contains("No results found"), is(false));
            		
            		utils.login();
            		PageRegistry.get(HomePage.class).selectHoldingSearchLink();
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void extractInfoAndAssertDataForISNNs() {
		
		File file = new File(RESOURCE_FILES_PATH + ISNN_SAMPLE_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = ",";
		
		try {
			String line = "\n";
		
			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {
				
                String[] tokens = line.split(DELIMITER);
                                
                for(String token : tokens)	{
                	
                	this.setId(token);
            		this.clickSearchButton();
            		this.clickSerialLink();
            		
            		assertThat(this.getResultsTableTitle(), containsString("Serial Holdings"));
            		assertThat(this.getResultsTableTitle().contains("No results found"), is(false));
            		
            		utils.login();
            		PageRegistry.get(HomePage.class).selectHoldingSearchLink();
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param titleCode
	 */
	public void assertTitleCodesTable(String titleCode) {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table[3]");

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(2).getText().trim(), containsString(titleCode.trim()));
		}
	}
	
	public void extractSerialsTitleCodesToVerify() {
		File file = new File(RESOURCE_FILES_PATH + SERIALS_TITLES_CODES_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = "\n";

		try {
			String line = "";
		
			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(DELIMITER);

                for(String token : tokens)	{

            		this.setTitleCode(token);
            		this.clickSearchButton();
            		this.clickMonographLink();

            		this.assertTitleCodesTable(token);
            		
            		utils.login();
            		PageRegistry.get(HomePage.class).selectHoldingSearchLink();
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void extractMonographsTitleCodesToVerify() {
		File file = new File(RESOURCE_FILES_PATH + MONOGRAPHS_TITLES_CODES_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = "\n";

		try {
			String line = "";
		
			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(DELIMITER);

                for(String token : tokens)	{

            		this.setTitleCode(token);
            		this.clickSearchButton();
            		this.clickMonographLink();

            		this.assertTitleCodesTable(token);
            		
            		utils.login();
            		PageRegistry.get(HomePage.class).selectHoldingSearchLink();
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void assertViewSearchCacheTablaData() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		int onoCount = 0;
		int xFiveCount = 0;

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(0);
			if(tdElement.getText().contains("ONO")) {
				onoCount = onoCount + 1;
			}else if(tdElement.getText().contains("5X5")) {
				xFiveCount = xFiveCount + 1;
			}

		}
		
		assertThat(onoCount>=6, is(true));
		assertThat(xFiveCount>=6, is(true));
	}
	
	private void assertMarcSearchTableText(String RegexMarcRecordCount) {
		assertThat(utils.elementExists(By.xpath(XPATH_MARC_TABLE_TEXT_DISPLAY_50)), is(true));
		assertThat(utils.getTextWithXpath(XPATH_MARC_TABLE_TEXT_DISPLAY_50).contains("Result set too large, only" +
				" displaying 50. Please refine your search."), is(true));
		assertThat(utils.elementExists(By.xpath(XPATH_MARC_COUNT_TEXT)), is(true));
		assertThat(utils.getTextWithXpath(XPATH_MARC_COUNT_TEXT).matches(RegexMarcRecordCount), is(true));

	}
	
	public void assertTitleDetailsTableData() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/form/table[1]");

		int colNum;

		WebElement thElement;
		WebElement trElement;
		List<WebElement> thCollection;

		for(int rowNum=0;rowNum<trCollection.size();rowNum++) {

			trElement = trCollection.get(rowNum);
			thCollection = trElement.findElements(By.xpath("th"));
			colNum = 0;

			for(colNum=0; colNum<thCollection.size();colNum++) {
				thElement = thCollection.get(colNum);

				if(rowNum==0) {
					assertThat(thElement.getText(), containsString("Title Display"));
					assertThat(utils.isElementPresent("TitleDisplay"), is(false));
				} else if (rowNum==1) {
					assertThat(thElement.getText(), containsString("Title"));
					assertThat(utils.getInputTextMaxLength("Title") <= 256, is(true));
					assertThat(utils.isElementPresent("Title"), is(true));
				} else if(rowNum==2) {
					assertThat(thElement.getText(), containsString("Alphabetization"));
					assertThat(utils.getInputTextMaxLength("Alphabetization") <= 256, is(true));
					assertThat(utils.isElementPresent("Alphabetization"), is(true));
				} else if(rowNum==3) {
					assertThat(thElement.getText(), containsString("Non Filing Characters"));
					assertThat(utils.getInputTextMaxLength("NonFilingChars") <= 256, is(true));
					assertThat(utils.isElementPresent("NonFilingChars"), is(true));
				} else if(rowNum==4) {
					assertThat(thElement.getText(), containsString("Edition"));
					assertThat(utils.getInputTextMaxLength("Edition") <= 256, is(true));
					assertThat(utils.isElementPresent("Edition"), is(true));
				} else if(rowNum==5) {
					assertThat(thElement.getText(), containsString("Date Published"));
					assertThat(utils.getInputTextMaxLength("DatePublished") <= 256, is(true));
					assertThat(utils.isElementPresent("DatePublished"), is(true));
				} else if(rowNum==6) {
					assertThat(thElement.getText(), containsString("Publisher"));
					assertThat(utils.getInputTextMaxLength("Publisher") <= 256, is(true));
					assertThat(utils.isElementPresent("Publisher"), is(true));
				} else if(rowNum==7) {
					assertThat(thElement.getText(), containsString("Language"));
					assertThat(utils.getInputTextMaxLength("Language") <= 256, is(true));
					assertThat(utils.isElementPresent("Language"), is(true));
				} else if(rowNum==8) {
					assertThat(thElement.getText(), containsString("Title Type"));
					assertThat(utils.isElementPresent("TitleType"), is(false));
				}
			}
		}
	}
	
	/**
	 * @param isbn
	 */
	public void assertResultsSearchWithIsbn(String isbn) {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table[3]");

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(4).getText(), containsString(isbn));
		}
	}
	
	/**
	 * @param issn
	 */
	public void assertResultsSearchWithIssn(String issn) {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table[3]");

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(4).getText().contains(issn), is(true));
			assertThat(tdCollection.get(5).getText().toLowerCase().contains("insight"), is(true));
		}
	}
	
	public void assertTitleMarcRecordsTable() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/form/table[2]");

		int colNum;
		
		WebElement thElement;
		WebElement tdElement;
		WebElement trElement;
		List<WebElement> thCollection;
		List<WebElement> tdCollection;

		for(int rowNum=0;rowNum<trCollection.size();rowNum++) {

			trElement = trCollection.get(rowNum);
			thCollection = trElement.findElements(By.xpath("./th"));
			tdCollection = trElement.findElements(By.xpath("./td"));

			for(colNum=0; colNum<thCollection.size();colNum++) {
				thElement = thCollection.get(colNum);
				
				if(rowNum==0) {
					if(colNum==0)
						assertThat(thElement.getText(), containsString("Use"));
					else if(colNum==1)
						assertThat(thElement.getText(), containsString("Type"));
					else if(colNum==2)
						assertThat(thElement.getText(), containsString("MARC Record Code"));
					else if(colNum==3)
						assertThat(thElement.getText(), containsString("Identifier"));
				} else if(rowNum==1) {
					if(colNum==0) {
						assertThat(utils.isElementPresent("mCheck1"), is(true));
						assertThat(utils.isCheckboxChecked("mCheck1"), is(true));
					} else if(colNum==2) {
						tdElement = tdCollection.get(colNum);
						assertThat(tdElement.getText().contains(""), is(false));
					}
				}
			}
		}
	}
	
	public void assertNormalizersTable() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/form/table[3]");

		int colNum;
		
		WebElement thElement;
		WebElement tdElement;
		WebElement trElement;
		List<WebElement> thCollection;
		List<WebElement> tdCollection;

		for(int rowNum=0;rowNum<trCollection.size();rowNum++) {

			trElement = trCollection.get(rowNum);
			thCollection = trElement.findElements(By.xpath("th"));
			tdCollection = trElement.findElements(By.xpath("td"));

			for(colNum=0; colNum<thCollection.size();colNum++) {
				thElement = thCollection.get(colNum);

				if(rowNum==0) {
					if(colNum==0)
						assertThat(thElement.getText(), containsString("Use"));
					else if(colNum==1)
						assertThat(thElement.getText(), containsString("Authority"));
					else if(colNum==2)
						assertThat(thElement.getText(), containsString("Title"));
					else if(colNum==3)
						assertThat(thElement.getText(), containsString("ISBN"));
					else if(colNum==4)
						assertThat(thElement.getText(), containsString("Source"));
				} else if(rowNum==1) {
					if(colNum==0) {
						assertThat(utils.isElementPresent("mCheck1"), is(true));
						assertThat(utils.isCheckboxChecked("mCheck1"), is(true));
					} else if(colNum==2) {
						tdElement = tdCollection.get(colNum);
						assertThat(tdElement.getText().contains(""), is(false));
					}
				}
			}
		}
	}


	/**
	 * Returns row elements representing the holding row. The list excludes the header row.
	 * @return
	 */
	public List<WebElement> getHoldings(){
		WebElement table = this.getHoldingSearchResultsTable();
		List<WebElement> holdings = table.findElements(By.cssSelector("tr"));
		return holdings.subList(1, holdings.size());
	}

	/**
	 * Gets the row index for a holding of the given title
	 *
	 * @param  title
	 * @return
	 */
	public int getHoldingRowIndex(String title){
		int columnIndex = getColumnIndexFor("<<     Title");
		List<WebElement> holdings = getHoldings();

		for (int i = 0;i<holdings.size();i++){
			if (holdings.get(i).findElements(By.xpath("td")).get(columnIndex).getText().trim().contentEquals(title)){
				return i;
			}						}
		return -1;
	}

	//TODO: ask dev to put id to this table element
	private WebElement getHoldingSearchResultsTable(){
		List<WebElement> tables = driver.findElements(By.cssSelector("table"));
		if (tables.size()<3) throw new IndexOutOfBoundsException();	//search results table is the 3rd table on the page
		return tables.get(2);
	}

	public Boolean isHoldingNormalized(WebElement holding){
		List<WebElement> cells = holding.findElements(By.cssSelector("td"));
		if (cells.size()<3) throw new IndexOutOfBoundsException(); //Authority cell is the 3rd cell in the row
		if (cells.get(2).getText().matches("\\w+")) {	//returns Titlecode if normalized, or "" if not normalized.
			return true;
		}
		return false;
	}
}
