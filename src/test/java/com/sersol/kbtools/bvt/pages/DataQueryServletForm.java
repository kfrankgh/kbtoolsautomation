/**
 * 
 */
package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;

/**
 * @author Santiago Gonzalez
 *
 */
public class DataQueryServletForm extends AdminForm {

	public String getDataQueryServletHeaderText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/h1")).getText();
	}
	
	public String getDataQueryServletText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td")).getText();
	}
	
	public String getFormatTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/h3[1]")).getText();
	}
	
	public String getFormatText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[1]/pre/code")).getText();
	}
	
	public String getParametersTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/h3[2]")).getText();
	}
	
	public String getMimeTypeTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]")).getText();
	}
	
	public String getTextHtmlLi() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[1]/li[1]")).getText();
	}
	
	public String getXmlText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[1]/li[2]")).getText();
	}
	
	public String getTsvText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[1]/li[3]")).getText();
	}
	
	public String getCsvText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[1]/li[4]")).getText();
	}
	
	public String getDatabasePoolTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]")).getText();
	}
	
	public String getHdidbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[1]")).getText();
	}
	
	public String getJToolsDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[2]")).getText();
	}
	
	public String getMarcRecordDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[3]")).getText();
	}
	
	public String getTitleDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[4]")).getText();
	}
	
	public String getLibraryDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[5]")).getText();
	}
	
	public String getLibraryFileDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[6]")).getText();
	}
	
	public String getSearchCacheText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[7]")).getText();
	}
	
	public String getMarcDiffDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[8]")).getText();
	}
	
	public String getMarcClientConfigDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[9]")).getText();
	}
	
	public String getMarcClientConfigAdminDbText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/ul[2]/li[10]")).getText();
	}
	
	public String getSqlQueryTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]")).getText();
	}
	
	public String getSqlQueryText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/blockquote[1]")).getText();
	}
	
	public String getHtmlTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/i[1]")).getText();
	}
	
	public String getHtmlText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/blockquote[2]")).getText();
	}
	
	public String getFileNameTitle() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/i[2]")).getText();
	}
	
	public String getFileNameText() {
		return driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td/blockquote[2]/blockquote[3]")).getText();
	}

	public String getSystemStatusText() {
		waitForElement(By.xpath("/html/body"));
		return driver.findElement(By.xpath("/html/body")).getText();
	}

	public String getErrorCodeText() {
		return driver.findElement(By.xpath("/html/body/h1")).getText();
	}

	public String getBodyText() {
		return driver.findElement(By.xpath("/html/body/div")).getText();
	}
}
