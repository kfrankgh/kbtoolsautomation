package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.ITestConstants;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.sersol.common.bvt.pages.BasePage;
import org.openqa.selenium.support.PageFactory;

public class EditRuleForm extends BasePage implements ITestConstants {

	
	 @FindBy(how = How.XPATH, using = "//input[@value='Change']")
	    private WebElement changeButton;
	 @FindBy(how = How.XPATH, using = "//input[@value='Add']")
	    private WebElement addButton;
	 @FindBy(how = How.XPATH, using = "//input[@value='Delete']")
	    private WebElement deleteButton;
	 @FindBy(how = How.XPATH, using = "//input[@value='Save']")
	    private WebElement saveButton;
	 @FindBy(how = How.XPATH, using = "//input[@value='Submit']")
	    private WebElement submitButton;
	 @FindBy(how = How.XPATH, using = "//input[@name='newdb']")
	    private WebElement newdb;
	@FindBy(how = How.XPATH, using = "//select[@name='MatchType']")
		private WebElement matchTitleType;
	@FindBy(how = How.XPATH, using = "//input[@name='MatchTitle']")
	 	private WebElement matchTitle;
	 @FindBy(how = How.XPATH, using = "//input[@name='MatchURL']")
		private WebElement matchURL;
	 @FindBy(how = How.XPATH, using = "//select[@name='Change1Type']")
	    private WebElement change1TitleType;
	 @FindBy(how = How.XPATH, using = "//input[@name='Change1Title']")
	    private WebElement change1Title;
	 @FindBy(how = How.XPATH, using = "//input[@name='Change1URL']")
	    private WebElement change1URL;
	 @FindBy(how = How.XPATH, using = "//textarea[@name='description']")
	    private WebElement description;
	 @FindBy(how = How.XPATH, using = "//textarea[@name='changeHistory']")
	    private WebElement note;
	 

		public void clickAddButton (){
			addButton.click();
		}
		public void clickChangeButton (){
			changeButton.click();
		}
		public void clickDeleteButton (){
			deleteButton.click();
		}
		public void setChange1TitleType(String titleType) {
			selectByVisibleText(this.change1TitleType, titleType);
		}
		public void setChange1Title(String title) {
			updateField(this.change1Title, title);
		}

		public void setChange1URL(String URL) {
			updateField(this.change1URL, URL);
		}

		public void setMatchTitleType(String titleType)		{
			selectByVisibleText(this.matchTitleType, titleType);
		}

		public void setMatchTitle(String title) {
			 updateField(this.matchTitle, title);
		}

		public void setMatchURL(String URL) {
			 updateField(this.matchURL, URL);
		}

		public void setDescription(String description) {
			 updateField(this.description, description);
		}

		public void setNote(String note) {
			 updateField(this.note, note);
		}

		public ViewRulesForm clickSaveButton() {
			saveButton.click();
			return PageFactory.initElements(driver, ViewRulesForm.class);
		}

		public void setDBAssociation(String newdb) {
			updateField(this.newdb, newdb);
		}

		public void clickSubmitButton() {
			submitButton.click();
		}
}
