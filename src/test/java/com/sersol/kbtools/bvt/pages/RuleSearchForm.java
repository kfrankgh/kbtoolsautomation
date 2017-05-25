/**
 * 
 */
package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * @author Santiago Gonzalez
 *
 */
public class RuleSearchForm  extends MainMenuForm implements ITestConstants {

	@FindBy(how = How.NAME, using = "ruleID")
	private WebElement ruleIdInput;
	
	@FindBy(how = How.NAME, using = "dbCode")
	private WebElement databaseCodeInput;
	
	
	public void setRuleId(String ruleId) {
		updateField(ruleIdInput, ruleId);
	}
	
	public void setDatabaseCode(String dbCode) {
		updateField(databaseCodeInput, dbCode);
	}

	public void setId(String id){
		updateField(By.name("id"), id);
	}

	public void setProvider(String providerCode){
		updateField(By.name("provider"), providerCode);
	}

	public void setTitle(String title){
		updateField(By.name("title"), title);
	}

	public void setTitleCode(String titleCode){
		updateField(By.name("titleCode"), titleCode);
	}

	public void setUrl(String url){
		updateField(By.name("url"), url);
	}

    public ViewRulesForm clickRuleIdLink(String linkText) {
        driver.findElement(By.linkText(linkText)).click();
        return PageFactory.initElements(driver, ViewRulesForm.class);
    }

    public void ruleSearch(String ruleId) {
        this.setRuleId(ruleId);
        this.clickSearchButton();

    }

	public EditRuleForm clickAddRuleLink() {
		driver.findElement(By.linkText(LINK_PLUS)).click();
		return PageFactory.initElements(driver, EditRuleForm.class);
	}

	public boolean serialsTableLabelExists(){
		List<WebElement> tableLabels = driver.findElements(By.cssSelector(CSSPATH_TABLE_LABELS));
		if (tableLabels.size()==0){return false;		}
		return tableLabels.get(0).getText().contains(TABLE_LABEL_SERIALS);
	}

	public boolean monographsTableLabelExists(int index){
		List<WebElement> tableLabels = driver.findElements(By.cssSelector(CSSPATH_TABLE_LABELS));
		if (tableLabels.size()==0){return false;		}
		return tableLabels.get(index).getText().contains(TABLE_LABEL_MONOGRAPHS);
	}
}
