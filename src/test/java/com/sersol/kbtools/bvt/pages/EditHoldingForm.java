package com.sersol.kbtools.bvt.pages;

import com.sersol.kbtools.bvt.tests.smoketests.common.HoldingData;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class EditHoldingForm extends HomePage {

	@FindBy(how = How.NAME, using = "titleTypeCode")
		private WebElement titleTypeSelect;
	@FindBy(how = How.NAME, using = "title")
		private WebElement titleField;
	@FindBy(how = How.NAME, using = "url")
		private WebElement urlField;
	@FindBy(how = How.NAME, using = "issn")
	    private WebElement issnField;
	@FindBy(how = How.NAME, using = "isbn10")
	    private WebElement isbn10Field;
	@FindBy(how = How.NAME, using = "isbn13")
	    private WebElement isbn13Field;
	@FindBy(how = How.NAME, using = "providerTitleId")
	    private WebElement providerTitleIdField;
	@FindBy(how = How.NAME, using = "author")
	    private WebElement authorField;
	@FindBy(how = How.NAME, using = "editor")
	    private WebElement editorField;
	@FindBy(how = How.NAME, using = "publisher")
	    private WebElement publisherField;
	@FindBy(how = How.NAME, using = "datePublished")
	    private WebElement datePublishedField;
	@FindBy(how = How.NAME, using = "dateStart")
		private WebElement dateStartField;
	@FindBy(how = How.NAME, using = "dateEnd")
		private WebElement dateEndField;
	@FindBy(how = How.NAME, using = "edition")
		private WebElement editionField;
	 
	 //BUTTONS
	@FindBy(how = How.NAME, using = "create")
	    private WebElement addHoldingButton;
	@FindBy(how = How.NAME, using = "update")
	    private WebElement updateButton;
	@FindBy(how = How.NAME, using = "delete")
	    private WebElement deleteButton;

	 
	 
	 //SETs

	/**
	 * Selects the title type from the Title Type dropdown
	 * @param titleType
	 */
	public void setHoldingTitleType(String titleType) {
		selectByVisibleText(titleTypeSelect, titleType);
	}

	/**
	 * Sets the title in the Title field
	 * @param title
	 */
	public void setHoldingTitle(String title) {
		updateField(titleField, title);
	 }

	/**
	 * Sets the URL in the URL field
	 * @param url
	 */
	public void setHoldingURL(String url) {
	    updateField(urlField, url);
	 }

	/**
	 * Sets the ISSN in the ISSN field (for serials only)
	 * @param issn
	 */
	public void setHoldingIssn(String issn) {
		 updateField(issnField, issn);
	 }

	/**
	 * Sets the ISBN10 in ISBN10 field (for monographs only)
	 * @param isbn10
	 */
	public void setHoldingIsbn10(String isbn10) {
		 updateField(isbn10Field, isbn10);
	 }

	/**
	 * Sets the ISBN13 in the ISBN13 field (for monographs only)
	 * @param isbn13
	 */

	public void setHoldingIsbn13(String isbn13) {
		 updateField(isbn13Field, isbn13);
	 }

	/**
	 * Sets the ProviderTitleID in the ProviderTitleID field
	 * @param providerTitleId
	 */
	public void setHoldingProviderTitleId(String providerTitleId) {
		 updateField(providerTitleIdField, providerTitleId);
	 }

	/**
	 * Sets the Author in the Author field (for monographs only)
	 * @param author
	 */
	public void setHoldingAuthor(String author) {
		 updateField(authorField, author);
	 }

	/**
	 * Sets the Editor in the Editor field (for monographs only)
	 * @param editor
	 */
	public void setHoldingEditor(String editor) {
		 updateField(editorField, editor);
	 }

	/**
	 * Sets the Publisher in the Publisher field
	 * @param publisher
	 */
	public void setHoldingPublisher(String publisher) {
		 updateField(publisherField, publisher);
	 }

	/**
	 * Sets the Date Published in the Date Published field (for monographs only)
	 * @param datePublished
	 */
	public void setHoldingDatePublished(String datePublished) {
		 updateField(datePublishedField, datePublished);
	 }

	/**
	 * Sets the Start Date in the Date Start field (for serials only)
	 * @param dateStart
	 */
	public void setDateStart(String dateStart) {
		updateField(dateStartField, dateStart);
	}

	/**
	 * Sets the End Date in the Date End field (for serials only)
	 * @param dateEnd
	 */
	public void setDateEnd(String dateEnd) {
		updateField(dateEndField, dateEnd);
	}

	/**
	 * Sets the Edition in the Edition field (for monographs only)
	 * @param edition
	 */
	public void setHoldingEdition(String edition) {
		updateField(editionField, edition);
	}
	 
	 //BUTTONS

	/**
	 * Clicks the Add Holding button when adding a holding
	 */
	 public void clickAddHoldingButton() {
	     addHoldingButton.click();
	 }

	/**
	 * Clicks the Save Changes button when editing a holding
	 */
	 public void clickSaveChangesButton() {
		 updateButton.click();
	 }

	/**
	 * Clicks the Delete Holding Button when editing a holding
	 */
	 public void clickDeleteHoldingButton() {
		 deleteButton.click();
	 }

	/**
	 * Enters holding values for Serials, Monographs, or Unknown title types.
	 * @param holdingData
	 */
	public void editHolding(HoldingData holdingData){
		switch (holdingData.getTitleType()){
			case Serial:
				setHoldingTitleType(holdingData.getTitleType().toString());
				setHoldingTitle(holdingData.getTitle());
				setHoldingIssn(holdingData.getIssn());
				setHoldingPublisher(holdingData.getPublisher());
				setDateStart(holdingData.getDateStart());
				setDateEnd(holdingData.getDateEnd());
				setHoldingURL(holdingData.getUrl());
				break;
			case Monograph:
				setHoldingTitleType(holdingData.getTitleType().toString());
				setHoldingTitle(holdingData.getTitle());
				setHoldingIsbn13(holdingData.getIsbn());
				setHoldingPublisher(holdingData.getPublisher());
				setHoldingAuthor(holdingData.getAuthor());
				setHoldingEditor(holdingData.getEditor());
				setHoldingPublisher(holdingData.getPublisher());
				setHoldingEdition(holdingData.getEdition());
				setHoldingDatePublished(holdingData.getDatePublished());
				setHoldingURL(holdingData.getUrl());
				break;
			case Unknown:
				setHoldingTitleType(holdingData.getTitleType().toString());
				setHoldingTitle(holdingData.getTitle());
				setHoldingPublisher(holdingData.getPublisher());
				setHoldingURL(holdingData.getUrl());
				break;
			default: System.out.println("editHolding(): INVALID VALUE FOR TITLE TYPE");
		}
	}


}