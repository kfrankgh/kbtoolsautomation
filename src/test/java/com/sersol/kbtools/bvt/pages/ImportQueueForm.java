package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.common.bvt.utils.Utils;

public class ImportQueueForm extends HomePage {

	public void setDatabaseCode(String databaseCode) {
		updateField(searchButton, databaseCode);
	}

	public void waitForLinkPresent(String linkText){
		WebDriverWait wait = new WebDriverWait(driver, 500);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
	}

	public void waitForTextNotPresent(String dbCode){
		PageRegistry.get(ImportQueueForm.class).clickStopRefreshLink();
		boolean textIsPresent = true;
		int timeout = 0;
		while (textIsPresent && timeout < 5000){
			try{
				driver.findElement(By.xpath("//td[contains(.,'"+dbCode+"')]"));
				Utils.sleep(1000);
				driver.navigate().refresh();
				timeout++;
			}catch (NoSuchElementException e){
				PageRegistry.get(ImportQueueForm.class).clickStartRefreshLink();
				textIsPresent = false;
			}
		}
	}

	public ReviewChangesForm clickDBlink(String dbCode) {
		driver.findElement(By.linkText(dbCode)).click();
		return PageFactory.initElements(driver, ReviewChangesForm.class);
	}

	public void waitForDBprocessing(String dbCode) {
		WebDriverWait wait = new WebDriverWait(driver, 500);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(.,'"+dbCode+"')]")));
	}

	public boolean dbLinkIsPresent(String dbCode){
		try{
			driver.findElement(By.xpath("//a[contains(.,'"+dbCode+"')]"));
			return true;
		}catch (NoSuchElementException e){
			return false;
		}
	}
	
	public String getDBState(String dbCode) {
		return driver.findElement(By.xpath("//div[@class='page_content']/table/tbody/tr/td/a[text()='"+dbCode+"']/parent::td/parent::tr/td[4]")).getText().trim();
	}

    public void deleteDatabase(String dbCode) {
        WebElement tableRow = driver.findElement(By.xpath("//div[@class='page_content']/table/tbody/tr/td/a[text()='"+dbCode+"']/parent::td/parent::tr/td[7]"));
        tableRow.findElement(By.linkText("Delete")).click();
    }
	
	public boolean rowIsRed(String dbCode) {
		try{
			driver.findElement(By.xpath("//div[@class='page_content']/table/tbody/tr/td/a[text()='"+dbCode+"']/parent::td/parent::tr[@class='red']"));
			return true;
		}catch (NoSuchElementException e){
			return false;
		}
	}
}
