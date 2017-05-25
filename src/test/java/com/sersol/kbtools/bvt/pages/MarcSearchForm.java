package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.support.ui.Select;


public class MarcSearchForm extends HomePage implements ITestConstants {

	@FindBy(how = How.NAME, using = "displayField")
	private WebElement displayFieldInput;
	
	@FindBy(how = How.NAME, using = "limit")
	private WebElement limitInput;
	
	@FindBy(how = How.NAME, using = "bibLevel")
	private WebElement bibLevelSelect;
	
	
    public void setDisplayField(String marcDisplayField) {
        updateField(displayFieldInput, marcDisplayField);
    }

	public void setSearchType(String searchType){
		Select dropdown = new Select(driver.findElement(By.name("searchType")));
		dropdown.selectByValue(searchType);
	}
    
    public boolean getBodyContent(String stringToSearch) {
    	return driver.findElement(By.xpath("/html/body")).getText().contains(stringToSearch);
    }
    
    public boolean isDisplayFieldInputPresent() {
    	return displayFieldInput.isDisplayed();
    }
    
    public boolean isLimitInputPresent() {
    	return limitInput.isDisplayed();
    }
    
    public boolean isBibLevelSelectPresent() {
    	return bibLevelSelect.isDisplayed();
    }
    
    public String getBibLevelSelectContent() {
        return bibLevelSelect.getText();
    }
    
    public String getRecordCode() {
        return driver.findElement(By.xpath("//tr/th[text()='Record Code']/parent::tr/following-sibling::tr/td[1]/a")).getText().trim();
    }

    public String getMarcTagContent() {
        return driver.findElement(By.xpath("//tr/th[text()='020']/parent::tr/following-sibling::tr/td[2]")).getText().trim();
    }

    public String getAuthorityId() {
        return driver.findElement(By.xpath("//tr/th[text()='Authority']/parent::tr/following-sibling::tr/td[3]/a")).getText().trim();
    }

    public String getIdentifier() {
        return driver.findElement(By.xpath("//tr/th[text()='ID']/parent::tr/following-sibling::tr/td[4]")).getText().trim();
    }

    public String getTitle(Boolean searchWithDisplayField) {
		String columnIndex;
		if (searchWithDisplayField) {
			columnIndex = "5";
		}
		else{
			columnIndex = "4";
		}
		return driver.findElement(By.xpath("//tr/th[text()='Title']/parent::tr/following-sibling::tr/td["+columnIndex
				+"]")).getText().trim();
    }

	public String getSearchTypeLabel() {
		waitForElement(By.xpath("/html/body/div[2]/form/b"));
		return driver.findElement(By.xpath("/html/body/div[2]/form/b")).getText();
	}
	
	public String getDisplayFieldLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/b[1]")).getText();
	}
	
	public String getBibliographicLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/b[2]")).getText();
	}
	
	public String getLimitLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/b[3]")).getText();
	}
	
	public String getTipsContent() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/font/i")).getText();
	}
	
	public String getTableData() {
		return driver.findElement(By.xpath("/html/body/div[2]/table")).getText();
	}
	
	public void extractCsvInfoAndAssertTableData() {
		File file = new File(RESOURCE_FILES_PATH + LCCN_EXAMPLE_CSV_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = ",";

		try {
			String line = "";

			fileReader = new BufferedReader(new FileReader(fileToParse));

			String[] valuesToSearch = {"JAMA (Chicago, Ill.)", "lccn82643544", "0098-7484", "82643544", null, null};
			
			String titleName = valuesToSearch[0];
			String displayFieldValue = valuesToSearch[4];
			String resultValue = valuesToSearch[5];

			int i=0;
			int displayValueCounter = 0;
			int resultValueCounter = 1;

			while ((line = fileReader.readLine()) != null) {

				String[] tokens = line.split(DELIMITER, 2);

				for(String token : tokens)	{

					if(i==displayValueCounter) {
						displayFieldValue = token;
						displayValueCounter = displayValueCounter + 2;
					} else if(i==resultValueCounter) {
						resultValue = token;
						resultValueCounter = resultValueCounter + 2;

						//NOTE: for debugging purposes
						if(displayFieldValue.startsWith("#")){
							i++;
							System.out.println(String.format("Skipped the display value: %s", displayFieldValue.replace("#", "").trim()));
							continue;
						}

						this.setSearchCriteria(titleName);
						this.setDisplayField(displayFieldValue);
						this.clickSearchButton();

						assertThat(this.getBodyContent("Error"), is(false));
						assertThat(this.getBodyContent("Exception"), is(false));

						valuesToSearch[4] = displayFieldValue;
						valuesToSearch[5] = resultValue;
						this.assertTableData(valuesToSearch);
					}
					i++;
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

	private void assertTableData(String[] valuesToSearch) {

		String titleName = valuesToSearch[0];
		String recordCode = valuesToSearch[1];
		String isbn = valuesToSearch[2];
		String lccn = valuesToSearch[3];
		String displayFieldValue = valuesToSearch[4];
		String resultValue = valuesToSearch[5];

		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
		int colNum;

		WebElement tdElement;
		WebElement trElement;
		List<WebElement> tdCollection;

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header row

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));

			for(colNum=0; colNum<tdCollection.size();colNum++) {
				tdElement = tdCollection.get(colNum);

				switch (colNum){
					case 0:
						assertThat(tdElement.getText().trim(), containsString(recordCode.trim()));
						assertThat(tdElement.getText().trim(), containsString(lccn.trim()));
						break;
					case 1:
						assertThat("Expected: " + resultValue + " but was: "+ tdElement.getText(),tdElement.getText().trim().matches(resultValue.trim()));
						break;
					case 3:
						assertThat(tdElement.getText().trim(), containsString(isbn.trim()));
						break;
					case 4:
						assertThat(tdElement.getText().trim(), containsString(titleName.trim()));
						assertThat(tdElement.getText(), containsString("JAMA"));
						break;
				}
			}
		}
	}

	public void assertTableDataWithAuthority(String[] valuesToSearch) {

		String titleName = valuesToSearch[0];
		String recordCode = valuesToSearch[1];
		String isbn = valuesToSearch[2];
		String lccn = valuesToSearch[3];
		String authority = valuesToSearch[4];
		String displayFieldValue = valuesToSearch[5];
		String resultValue = valuesToSearch[6];

		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
		int colNum;

		WebElement tdElement;
		WebElement trElement;
		List<WebElement> tdCollection;

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header row

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));

			for(colNum=0; colNum<tdCollection.size();colNum++) {
				tdElement = tdCollection.get(colNum);

				switch(colNum){
					case 0:
						assertThat(tdElement.getText().trim(), containsString(recordCode.trim()));
						assertThat(tdElement.getText().trim(), containsString(lccn.trim()));
						break;
					case 1:
						assertThat(tdElement.getText().trim(), containsString(displayFieldValue.trim()));
						assertThat(tdElement.getText().trim(), containsString(resultValue.trim()));
						break;
					case 2:
						assertThat(tdElement.getText().trim(), containsString(authority.trim()));
						break;
					case 3:
						assertThat(tdElement.getText().trim(), containsString(isbn.trim()));
						break;
					case 4:
						assertThat(tdElement.getText().trim(), containsString(titleName.trim()));
						break;
				}
			}
		}
	}

	/**
	 * @param stringToSearch
	 * @param columnToLook
	 */
	public void assertMarcSearchTableData(String stringToSearch, int columnToLook) {

		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		WebElement tdElement;
		WebElement trElement;
		List<WebElement> tdCollection;

		for(int rowNum=1;rowNum<trCollection.size();rowNum++) { // skipping the header row

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));

			for(int colNum=0; colNum<tdCollection.size();colNum++) {
				tdElement = tdCollection.get(colNum);

				if(colNum==columnToLook) {
					assertThat(tdElement.getText().toLowerCase(), containsString(stringToSearch));
					break;
				}
			}
		}
	}

	public void extractLccnMonographCsvAndAssertTableData() {
		
		File file = new File(RESOURCE_FILES_PATH + LCCN_MONOGRAPH_EXAMPLE_CSV_FILE_PATH);
		String fileToParse = file.getAbsolutePath(); 
		BufferedReader fileReader = null;

		final String DELIMITER = ",";

		try {
			String line = "";

			fileReader = new BufferedReader(new FileReader(fileToParse));

			String[] valuesToSearch = {"Jane Austen & Charles Darwin : naturalists and novelists", "lccn2007020236", "9780754658511", "2007020236", "TC0000184948", null, null};

			String titleName = valuesToSearch[0];
			String displayFieldValue = valuesToSearch[5];
			String resultValue = valuesToSearch[6];
			
			int i=0;
			int displayValueCounter = 0;
			int resultValueCounter = 1;

			while ((line = fileReader.readLine()) != null) {

				String[] tokens = line.split(DELIMITER, 2);

				for(String token : tokens)	{

					if(i==displayValueCounter) {
						displayFieldValue = token;
						displayValueCounter = displayValueCounter + 2;
					} else if(i==resultValueCounter) {
						resultValue = token;
						resultValueCounter = resultValueCounter + 2;

						this.setSearchCriteria(titleName);
						this.setDisplayField(displayFieldValue);
						this.clickSearchButton();

						assertThat(this.getBodyContent("Error"), is(false));
						assertThat(this.getBodyContent("Exception"), is(false));

						valuesToSearch[5] = displayFieldValue;
						valuesToSearch[6] = resultValue;
						this.assertTableDataWithAuthority(valuesToSearch);
					}
					i++;
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
