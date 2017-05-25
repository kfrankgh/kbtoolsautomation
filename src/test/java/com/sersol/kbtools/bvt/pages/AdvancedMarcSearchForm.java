package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sersol.kbtools.bvt.tests.ITestConstants;

public class AdvancedMarcSearchForm extends HomePage implements ITestConstants {
    
	@FindBy(how = How.NAME, using = "c1Value")
	private WebElement searchTypeInput;
	
	@FindBy(how = How.NAME, using = "c1Arg1")
	private WebElement propertyConstraintSelect;
	
	@FindBy(how = How.NAME, using = "c1Arg2")
	private WebElement searchSubTypeSelect;
	
	@FindBy(how = How.NAME, using = "displayField")
	private WebElement displayFieldInput;
	
	@FindBy(how = How.NAME, using = "c2Arg1")
	private WebElement leaderConstraintSelect;
	
	@FindBy(how = How.NAME, using = "c2Value")
	private WebElement leaderValueEqualsInput;
	
	@FindBy(how = How.XPATH, using = "//input[@value='Field']")
	private WebElement fieldButton;
	
	@FindBy(how = How.NAME, using = "c1Arg1")
	private WebElement tagInput;
	
	@FindBy(how = How.NAME, using = "c1Arg2")
	private WebElement subFieldInput;
	
	@FindBy(how = How.NAME, using = "c2Value")
	private WebElement equalsInput;

    public String getAbsolutePath(String fileDirectory) throws URISyntaxException {
        URL resource = Main.class.getResource(fileDirectory);
        String absolutePath = Paths.get(resource.toURI()).toString();
        return absolutePath;
    }

    public void setSearchType(String searchType) {
        selectByVisibleText(propertyConstraintSelect, searchType);
    }

    public void setSearchSubType(String searchSubType) {
        selectByVisibleText(searchSubTypeSelect, searchSubType);
    }

    public void setSearchCriteria(String searchCriteria) {
        updateField(searchTypeInput, searchCriteria);
    }

    public void setDisplayField(String displayField) {
        updateField(displayFieldInput, displayField);
    }
    
	public void setTagField(String tagText) {
		updateField(tagInput, tagText);
	}
	
	public void setSubField(String subFieldText) {
		updateField(subFieldInput, subFieldText);
	}
	
	public void setEqualsField(String equalsFieldText) {
		updateField(equalsInput, equalsFieldText);
	}

	public void clickFieldButton() {
		fieldButton.click();
	}

    public void setLeaderPosition(String leaderPosition) {
        selectByVisibleText(leaderConstraintSelect, leaderPosition);
    }

    public void setLeaderValueEquals(String leaderValue) {
        updateField(leaderValueEqualsInput, leaderValue);
    }

    public String getRecordCode() {
        return driver.findElement(By.xpath("//tr/th[text()='Record Code']/parent::tr/following-sibling::tr[2]/td[1]/a")).getText().trim();
    }

    public String getIdentifier() {
        return driver.findElement(By.xpath("//tr/th[text()='ID']/parent::tr/following-sibling::tr[2]/td[4]")).getText().trim();
    }

    public String getAuthorityTitleCode() {
        return driver.findElement(By.xpath("//tr/th[text()='Authority']/parent::tr/following-sibling::tr[2]/td[3]/a")).getText().trim();
    }

    public String getTableLabel() {
        WebElement tableLabel = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("font[class='tableLabel']")));
        return tableLabel.getText().trim();
    }

    public String getTitle() {
        return driver.findElement(By.xpath("//tr/th[text()='Title']/parent::tr/following-sibling::tr[2]/td[5]")).getText().trim();
    }

    public String getMarcTagColumnHeader() {
        return driver.findElement(By.xpath("//tr/th[2]")).getText().trim();
    }

	public void selectSubTypeContainsAnd() {
		waitForElement(By.xpath("/html/body/div[2]/form/p[1]/select[2]/option[4]"));
		driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/select[2]/option[4]")).click();
	}
	
	public boolean isSubTypeSelectPresent() {
		return searchSubTypeSelect.isDisplayed();
	}
	
	public void selectTypeISSN() {
		driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/select[1]/option[2]")).click();
	}
	
	public void selectSubTypeBeginsWith() {
		driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/select[2]/option[1]")).click();
	}
	
	public void selectTypeTitle() {
		driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/select[1]/option[3]")).click();
	}
	
	public void extractFileInfoAndAssertTableData() throws URISyntaxException {
		String fileToParse = getAbsolutePath(PUNCTUATION_TXT_FILE_PATH);
		BufferedReader fileReader = null;

		final String DELIMITER = "\n";
		
		try {
			String line = "";
		
			fileReader = new BufferedReader(new FileReader(fileToParse));

			while ((line = fileReader.readLine()) != null) {
				
                String[] tokens = line.split(DELIMITER);
                                
                for(String token : tokens)	{
                	
                	this.selectSubTypeContainsAnd();
                	this.setSearchCriteria("Data Mining"+token+" Modelling");
                	this.clickSearchButton();
                	
                	assertThat(this.getTableLabel(), containsString("Found 2 Records"));
                	
                	assertSearchResultsTableData();
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

	public List<WebElement> getResultsTable(String xpath){
		return utils.getTableRowsCollection("/html/body/div[2]/table");
	}

	private void assertSearchResultsTableData() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		for(int rowNum=1;rowNum<3;rowNum++) { // skipping the header
			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));

			assertThat(tdCollection.get(1).getText(), containsString("TC0000070528"));
			assertThat(tdCollection.get(3).getText(), containsString("International journal of data mining, modelling and management"));
		}
	}
	
	public void assertSearchResultsTableData2() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		for(int rowNum=1;rowNum<3;rowNum++) { // skipping the header

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(3).getText(), containsString("<TAG> (Sterling, Va.)"));
		}
	}
	
	public void assertSearchResultsTableData3() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		for(int rowNum=1;rowNum<2;rowNum++) {

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(3).getText(), containsString("Trust & investments"));
		}
	}
}
