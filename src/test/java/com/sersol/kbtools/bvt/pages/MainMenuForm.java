/**
 * 
 */
package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class MainMenuForm extends HomePage {

    @FindBy(how = How.LINK_TEXT, using = "Holding Search")
    private WebElement hldgSearchLink;
	
    @FindBy(how = How.LINK_TEXT, using = "Admin")
    private WebElement adminLink;
    
    @FindBy(how = How.LINK_TEXT, using = "Subject Search")
    private WebElement subjectSearchLink;
	
    @FindBy(how = How.LINK_TEXT, using = "Rule Search")
    private WebElement ruleSearchLink;
    
	@FindBy(how = How.LINK_TEXT, using = "LMH Monitor")
	private WebElement lmhMonitorLink;

    /**
     * Clicks Holding Search link
     * @return
     */
    public HoldingSearchForm clickHldgSearchLink(){
        hldgSearchLink.click();
        return PageFactory.initElements(driver, HoldingSearchForm.class);
    }

    /**
     * Clicks Admin link
     * @return
     */
	public AdminForm clickAdminLink() {
    	waitForElement(By.linkText("Admin"));
    	adminLink.click();
        return PageFactory.initElements(driver, AdminForm.class);
	}

    /**
     * Clicks Subject Search link
     * @return
     */
    public SubjectSearchForm selectSubjectSearchLink() {
    	subjectSearchLink.click();
        return PageFactory.initElements(driver, SubjectSearchForm.class);
    }

    /**
     * Clicks Rule Search Link
     * @return
     */
    public RuleSearchForm selectRuleSearchLink() {
    	ruleSearchLink.click();
        return PageFactory.initElements(driver, RuleSearchForm.class);
    }

    /**
     * Clicks LMH Monitor link
     * @return
     */
	public LmhMonitorForm selectLmhMonitorLink() {
		lmhMonitorLink.click();
		return PageFactory.initElements(driver, LmhMonitorForm.class);
	}
}
