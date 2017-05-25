package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class ViewRulesForm extends MainMenuForm implements ITestConstants {

	public EditRuleForm clickAddNewRule() {
		 waitForElement(By.linkText("+")).click();
	     return PageFactory.initElements(driver, EditRuleForm.class);
	}

	public EditRuleForm clickEditLink(String ruleDescription) {
		waitForElement(By.xpath("//pre[text()='" + ruleDescription + "']/parent::td/parent::tr//following-sibling::tr//following-sibling::tr/td[5]/a[text()='Edit']")).click();
	     return PageFactory.initElements(driver, EditRuleForm.class);
	}

	public EditRuleForm clickDeactivateLink(String ruleDescription) {
		if(driver.findElements(By.xpath("//pre[text()='"+ruleDescription+"']/parent::td/parent::tr//following-sibling::tr//following-sibling::tr/td[5]/a[text()='Deactivate']")).size()!=0)
			waitForElement(By.xpath("//pre[text()='"+ruleDescription+"']/parent::td/parent::tr//following-sibling::tr//following-sibling::tr/td[5]/a[text()='Deactivate']")).click();
	     return PageFactory.initElements(driver, EditRuleForm.class);
		
	}

	public EditRuleForm clickActivateLink(String ruleDescription) {
		if(driver.findElements(By.xpath("//pre[text()='"+ruleDescription+"']/parent::td/parent::tr//following-sibling::tr//following-sibling::tr/td[5]/a[text()='Activate']")).size()!=0)
			waitForElement(By.xpath("//pre[text()='"+ruleDescription+"']/parent::td/parent::tr//following-sibling::tr//following-sibling::tr/td[5]/a[text()='Activate']")).click();
	    return PageFactory.initElements(driver, EditRuleForm.class);
	}

	public void clickDeleteLink(String ruleDescription) {
		waitForElement(By.xpath("//pre[text()='" + ruleDescription + "']/parent::td/parent::tr//following-sibling::tr//following-sibling::tr/td[5]/a[text()='Delete']")).click();
	}

	public ReviewChangesForm clickViewHDILink() {
		waitForElement(By.linkText("View HDI")).click();
	     return PageFactory.initElements(driver, ReviewChangesForm.class);
	}

    public void changeRuleActiveStatus(String ruleDescription, Boolean deactivate) {
        if (deactivate) {
            this.clickDeactivateLink(ruleDescription);
        }
        else {
            this.clickActivateLink(ruleDescription);
        }
    }

	public int getRuleIndex(String ruleDescription){	//gets most recent rule matching the Description
		List<WebElement> rules = driver.findElements(By.cssSelector("pre"));
		int ruleIndex = -1;
		for (int i =0; i < rules.size(); i++){
			if (rules.get(i).getText().contains(ruleDescription)){ruleIndex = i;}
		}
		return ruleIndex;
	}

	public String getMatchTitle(int ruleIndex){
		List<WebElement> tables = driver.findElements(By.cssSelector("table"));
		int titlesTableIndex = (ruleIndex+1)*4-2;	//each rule has 4 tables. Titles table is the 3rd table
		WebElement titlesTable = tables.get(titlesTableIndex);
		List<WebElement> titles = titlesTable.findElements(By.cssSelector("#title_0"));
		String matchTitle = titles.get(0).getText();
		return matchTitle;
	}

	public String getChangeTitle(int ruleIndex){
		List<WebElement> tables = driver.findElements(By.cssSelector("table"));
		int titlesTableIndex = (ruleIndex+1)*4-2;	//each rule has 4 tables. Titles table is the 3rd table
		WebElement titlesTable = tables.get(titlesTableIndex);
		List<WebElement> titles = titlesTable.findElements(By.cssSelector("#title_0"));
		String changeTitle = titles.get(1).getText();
		return changeTitle;
	}

	public String getLatestChangeNote(int ruleIndex){
		List<WebElement> changeHistories = driver.findElements(By.cssSelector("blockquote"));
		WebElement changeNote = changeHistories.get(ruleIndex);
		return changeNote.getText();
	}

	public String getRuleCountInLinkText(){
		return utils.getTextWithCssPath(CSSPATH_DB_HAS_X_RULES).trim();
	}

	public int getTotalRules() {
		return driver.findElements(By.cssSelector("pre")).size();
	}
}
