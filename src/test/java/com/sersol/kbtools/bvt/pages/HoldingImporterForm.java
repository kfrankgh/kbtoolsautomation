package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class HoldingImporterForm extends HomePage {

	@FindBy(how = How.NAME, using = "holdings")
	private WebElement chooseHoldingsFile;

	@FindBy(how = How.XPATH, using = "//input[@value='Load']")
	private WebElement loadButton;

	@FindBy(how = How.XPATH, using = "//input[@value='Load Live Data']")
	private WebElement loadLiveDataButton;

	@FindBy(how = How.XPATH, using = "//input[@name='augment']")
	private WebElement augmentLiveData;

	@FindBy(how = How.XPATH, using = "//input[@name='dbCode']")
	private WebElement dbCodeInput;

	@FindBy(how = How.LINK_TEXT, using = "Imports in Queue")
	private WebElement importsInQueueLink;

    public ImportQueueForm LldAndWaitUntilProcessed(String databaseCode) {
        this.clickLLDButton();
        this.setDBCode(databaseCode);
        ImportQueueForm importQueueForm = this.clickLoadButton();
        importQueueForm.waitForDBprocessing(databaseCode);
        importQueueForm.waitForLinkPresent(databaseCode);
        return importQueueForm;
    }

	/**
	 * Gets the absolute path to the specified file in test/resources and
	 * types the path to the input field, which corresponds to the area next to
	 * "File:". This will bypass using the OS functionality for selecting a file from the
	 * file system.
	 * @param fileName file name if the file is under test/resources.
	 *                 subdirectory/file name if the file is in the subfolder in test/resources.
	 * @throws URISyntaxException
	 */
	public void setFileToImport(String fileName) throws URISyntaxException {
		URL resource = ClassLoader.getSystemResource(fileName);
		String absolutePath = resource.getFile();
		this.chooseHoldingsFile.sendKeys(absolutePath);
	}

	public ImportQueueForm selectImportsInQueueLink() {

		//WORKAROUND: when the test runs in ec2, click() doesn't seem to work on this particular link element.
		// this is the only way that this link element responds and navigates to the specified href.
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", importsInQueueLink);

		return PageFactory.initElements(driver, ImportQueueForm.class);
	}
	
	public ImportQueueForm clickLoadButton() {
		loadButton.click();
		utils.pause(utils.MIN_WAIT_MILLISECONDS);
		return PageFactory.initElements(driver, ImportQueueForm.class);
	}
	
	public void setAugmentLiveData() {
		augmentLiveData.click();
	}
	public void clickLLDButton() {
		loadLiveDataButton.click();
	}

	public void setDBCode(String dbCode){
		this.dbCodeInput.sendKeys(dbCode);
	}
}
