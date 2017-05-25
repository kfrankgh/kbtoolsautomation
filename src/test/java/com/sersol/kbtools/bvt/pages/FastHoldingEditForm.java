package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.smoketests.common.HoldingData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

public class FastHoldingEditForm extends HomePage {

	 @FindBy(how = How.NAME, using = "fromType")
	    private WebElement titleTypeSelect;
	 @FindBy(how = How.NAME, using = "fromTitle")
	    private WebElement titleInput;
	 @FindBy(how = How.NAME, using = "fromURL")
	    private WebElement urlInput;
	 @FindBy(how = How.NAME, using = "fromISSN")
	    private WebElement issnInput;
	 @FindBy(how = How.NAME, using = "fromDateStart")
		private WebElement dateStartInput;
	 @FindBy(how = How.NAME, using = "fromDateEnd")
		private WebElement dateEndInput;
	 @FindBy(how = How.NAME, using = "fromISBN10")
	    private WebElement isbn10Input;
	 @FindBy(how = How.NAME, using = "fromISBN13")
	    private WebElement isbn13Input;
	 @FindBy(how = How.NAME, using = "fromAuthor")
	    private WebElement authorInput;
	 @FindBy(how = How.NAME, using = "fromEditor")
	    private WebElement editorInput;
     @FindBy(how = How.NAME, using = "fromEdition")
		private WebElement editionInput;
	@FindBy(how = How.NAME, using = "fromPublisher")
	    private WebElement publisherInput;
	 @FindBy(how = How.NAME, using = "fromDatePublished")
	    private WebElement datePublishedInput;
	 
	 //BUTTONS
	 @FindBy(how = How.NAME, using = "action")
	    private WebElement updateButton;

	//GETs
	/**
	 * Gets the selected dropdown item in the Title Type dropdown
	 *
	 * @return
	 */
	public String getTitleType(){
		Select titleType = new Select(waitForElement(By.name("fromType")));
		return titleType.getFirstSelectedOption().getText();
	}
	 
	 //SETs

	/**
	 * Sets holding TitleType
	 * @param titleType
	 */
	  public void setHoldingTitleType(String titleType) {
		selectByVisibleText(titleTypeSelect, titleType);
	 }

	/**Sets holding title
	 *
	 * @param title
	 */
	 public void setHoldingTitle(String title) {
		updateField(titleInput, title);
	 }

	/**
	 * Sets holding URL
 	 * @param url
	 */
	public void setHoldingURL(String url) {
	    updateField(urlInput, url);
	 }

	/**Sets holding ISSN
	 *
	 * @param issn
	 */
	 public void setHoldingIssn(String issn) {
		 updateField(issnInput, issn);
	 }

	/**
	 * Sets holding Date Start
	 * @param dateStart
	 */
	 public void setHoldingDateStart(String dateStart) {
		updateField(dateStartInput, dateStart);
	 }

	/**
	 * Sets holding Date End
	 * @param dateEnd
	 */
	 public void setHoldingDateEnd(String dateEnd) { updateField(dateEndInput, dateEnd); }

	/**
	 * Sets holding ISBN10
	 * @param isbn10
	 */
	 public void setHoldingIsbn10(String isbn10) { updateField(isbn10Input, isbn10); }

	/**
	 * Sets holding ISBN13
	 * @param isbn13
	 */
	 public void setHoldingIsbn13(String isbn13) { updateField(isbn13Input, isbn13); }

	/**
	 * Sets holding Author
	 * @param author
	 */
	 public void setHoldingAuthor(String author) { updateField(authorInput, author); }

	/**
	 * Sets holding Editor
	 *
	 * @param editor
	 */
	 public void setHoldingEditor(String editor) { updateField(editorInput, editor); }

	/**
	 * Sets holding Edition
 	 * @param edition
	 */
	 public void setHoldingEdition(String edition) { updateField(editionInput, edition); }

	/**
	 * Sets holding publisher
	 * @param publisher
	 */
	 public void setHoldingPublisher(String publisher) { updateField(publisherInput, publisher); }

	/**
	 * Sets holding Date Published
	 * @param datePublished
	 */
	 public void setHoldingDatePublished(String datePublished) { updateField(datePublishedInput, datePublished); }
	 
	 //BUTTONS
	/**
	 * Clicks the Update button to save changes
	 */
	 public void clickUpdateButton() { updateButton.click(); }

	/**
	 * Fast Holding Edits a holding of type Monograph, Serial or Unknown
	 * @param holding
	 */
	 public void editHolding(HoldingData holding){
		setHoldingTitle(holding.getTitle());
		setHoldingPublisher(holding.getPublisher());
		setHoldingURL(holding.getUrl());

		switch(holding.getTitleType()) {
			case Monograph:
				setHoldingAuthor(holding.getAuthor());
				setHoldingIsbn13(holding.getIsbn());
				setHoldingEditor(holding.getEditor());
				setHoldingEdition(holding.getEdition());
				setHoldingDatePublished(holding.getDatePublished());
				break;
			case Serial:
				setHoldingIssn(holding.getIssn());
				setHoldingDateStart(holding.getDateStart());
				setHoldingDateEnd(holding.getDateEnd());
				break;
		}
	}
}