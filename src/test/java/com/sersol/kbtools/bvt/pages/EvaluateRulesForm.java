package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class EvaluateRulesForm extends HomePage {

	@FindBy(how = How.XPATH, using = "//a[text()='Review Changes']")
	private WebElement reviewChanges;

    @FindBy(how = How.CSS, using = "table:nth-of-type(4)>tbody>tr>td>a>b")
    private WebElement firstRuleID;

    @FindBy(how = How.CSS, using = "table:nth-of-type(4)>tbody>tr>td:nth-of-type(2)")
    private WebElement firstRuleEvaluation;


    public void selectReviewChangesLink() {
		reviewChanges.click();
	}

    public String getFirstRuleId(){
        String ruleIdLink = firstRuleID.getText();
        return ruleIdLink.substring(9); //trim "Rule ID: " from string to get just the RuleId
    }

    public String getFirstRuleEvaluation(){
        return firstRuleEvaluation.getText();
    }
}
