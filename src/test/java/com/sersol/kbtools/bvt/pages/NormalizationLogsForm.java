package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * @author Santiago Gonzalez
 *
 */
public class NormalizationLogsForm extends TitleNormalizationForm {
	
    @FindBy(how = How.LINK_TEXT, using = "Search Logs")
    private WebElement searchLogsLink;
    
    @FindBy(how = How.LINK_TEXT, using = "Cancel Search")
    private WebElement cancelSearchLink;
    
    @FindBy(how = How.NAME, using = "user")
    private WebElement userInput;
    
    @FindBy(how = How.NAME, using = "id")
    private WebElement idInput;
    
    @FindBy(how = How.NAME, using = "title")
    private WebElement titleInput;
	

    
    public Boolean isSearchLogsLinkPresent() {
		return searchLogsLink.isDisplayed();
	}
	
	public void clickSearchLogsLink() {
		searchLogsLink.click();
	}
	
	public boolean isCancelSearchLinkPresent() {
		return cancelSearchLink.isDisplayed();
	}
	
	public String getUserLabelText() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/b[1]")).getText();		
	}
	
	public String getIdLabelText() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/b[2]")).getText();		
	}
	
	public String getTitleLabelText() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/b[3]")).getText();		
	}
	
	public boolean isUserInputPresent() {
		return userInput.isDisplayed();
	}
	
	public boolean isIdInputPresent() {
		return idInput.isDisplayed();
	}
			
	public boolean isTitleInputPresent() {
		return titleInput.isDisplayed();
	}
	
	public void clickCancelSearchLink() {
		cancelSearchLink.click();
	}
	
	public String getTop10UsersLabelText() {
		return driver.findElement(By.xpath("/html/body/div[2]/p[2]/font")).getText();
	}
	
	public String getMostRecentNormalizationsText() {
		return driver.findElement(By.xpath("/html/body/div[2]/p[3]/font")).getText();		
	}
	
	/**
	 * @param normalizationLogsForm
	 */
	public void assertNormalizationLogsTable1Data() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table[1]");

		assertThat(trCollection.size() <= 11, is(true));

		int rowNum,colNum;
		rowNum = 1;

		for(WebElement trElement : trCollection) {

			List<WebElement> tdCollection = trElement.findElements(By.xpath("td"));
			colNum = 1;

			for(WebElement tdElement : tdCollection) {

				if(rowNum==1) {
					if(colNum==1)
						assertThat(tdElement.getText(), containsString("Rank"));
					else if(colNum==2) {
						assertThat(tdElement.getText(), containsString("User"));
					} else {
						assertThat(tdElement.getText(), containsString("Normalized"));
					}
				} else {
					if(colNum==1)
						assertThat(Integer.parseInt(tdElement.getText())==rowNum-1, is(true));
					else if(colNum==2)
						assertThat(utils.isLinkPresent(tdElement.getText()), is(true));
				}
				colNum++;
			}
			rowNum++;
		}
	}
	
	/**
	 * @param normalizationLogsForm
	 */
	public void assertNormalizationLogsTable2Data() {
		List<WebElement> trCollection2 = utils.getTableRowsCollection("/html/body/div[2]/table[2]");

		assertThat(trCollection2.size() > 1, is(true));

		int colNum2;

		WebElement tdElement2;
		WebElement trElement2;
		List<WebElement> tdCollection2;

		for(int rowNum2=0;rowNum2<2;rowNum2++) {

			trElement2 = trCollection2.get(rowNum2);
			tdCollection2 = trElement2.findElements(By.xpath("td"));
			colNum2 = 0;

			for(colNum2=0; colNum2<tdCollection2.size();colNum2++) {
				tdElement2 = tdCollection2.get(colNum2);
				if(rowNum2==0) {
					if(colNum2==0)
						assertThat(tdElement2.getText(), containsString("User"));
					else if(colNum2==1) {
						assertThat(tdElement2.getText(), containsString("Authority"));
					} else if(colNum2==2) {
						assertThat(tdElement2.getText(), containsString("ID"));
					} else if(colNum2==3) {
						assertThat(tdElement2.getText(), containsString("Title"));
					} else if(colNum2==4) {
						assertThat(tdElement2.getText(), containsString("Normalized"));
					}
				} else {
					if(rowNum2==1) {
						if(colNum2==0)
							assertThat(utils.isLinkPresent(tdElement2.getText()), is(true));
						else if(colNum2==1) {
							assertThat(utils.isLinkPresent(tdElement2.getText()), is(true));
						}
					}
				}
			}
		}
	}
}
