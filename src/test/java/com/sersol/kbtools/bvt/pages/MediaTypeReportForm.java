package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * @author Santiago Gonzalez
 *
 */

public class MediaTypeReportForm extends MarcImporterForm {
	
    @FindBy(how = How.NAME, using = "limit")
    private WebElement limitInput;
    
    @FindBy(how = How.NAME, using = "hideRed")
    private WebElement hideMissingRecordsButton;
    
    @FindBy(how = How.NAME, using = "format")
    private WebElement formatSelect;
    
	@FindBy(how = How.XPATH, using = "//input[@value='Refresh']")
    private WebElement refreshButton;
	
	@FindBy(how = How.XPATH, using = "//input[@value='Add']")
    private WebElement addButton;
	
	@FindBy(how = How.XPATH, using = "//input[@value='Block']")
    private WebElement blockButton;
	
	@FindBy(how = How.XPATH, using = "//input[@value='Remove']")
    private WebElement removeButton;
	
    public String getFormText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/form")).getText();
    }
    
    public String getTitlesNeedingReviewText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/h3")).getText();
    }
    
    public String getTitle() {
    	return driver.findElement(By.xpath("/html/body/div[1]/h1")).getText();
    }
    
    public boolean isRefreshButtonPresent() {
    	return refreshButton.isDisplayed();
	}

    public boolean isHideMissingRecordsButtonPresent() {
    	return hideMissingRecordsButton.isDisplayed();
	}
    
    public boolean isLimitInputPresent() {
    	return limitInput.isDisplayed();
	}
    
    public String getAuthorityColumnText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[1]")).getText();
	}
    public String getTitleColumnText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[2]")).getText();
	}
    public String getIdColumnText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[3]")).getText();
	}
    public String getMarcRecordColumnText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[4]")).getText();
	}
    public String getActionColumnText() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[5]")).getText();
	}
    
    public boolean isSecondRowDisplayed() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[2]")).isDisplayed();
	}
    
    public boolean isAuthLinkPresent() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[2]/td[1]/a")).isDisplayed();
	}

    public boolean isMarcRecordLinkPresent() {
    	return driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[2]/td[4]/a")).isDisplayed();
	}

    public boolean isFormatSelectPresent() {
    	return formatSelect.isDisplayed();
	}

    public boolean isAddButtonPresent() {
    	return addButton.isDisplayed();
	}

    public boolean isBlockButtonPresent() {
    	return blockButton.isDisplayed();
	}

    public boolean isRemoveButtonPresent() {
    	return removeButton.isDisplayed();
	}
}
