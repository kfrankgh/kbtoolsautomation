package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Santiago Gonzalez
 *
 */
public class AdminForm extends MainMenuForm {

    @FindBy(how = How.LINK_TEXT, using = "Data Query Servlet")
    private WebElement dataQueryServletLink;
	
    @FindBy(how = How.LINK_TEXT, using = "Logout")
    private WebElement logoutLink;
    
    @FindBy(how = How.LINK_TEXT, using = "DataSource Test")
    private WebElement dataSourceTestLink;

    @FindBy(how = How.XPATH, using = "/html/body/div/form[1]/input[2]")
    private WebElement writeNormalizationListField;

    @FindBy(how = How.XPATH, using = "/html/body/div/form[2]/input[2]")
    private WebElement loadNormalizationListField;

    
    
    public DataQueryServletForm clickDataQueryServletLink() {
    	waitForElement(By.linkText("Data Query Servlet"));
    	dataQueryServletLink.click();
        return PageFactory.initElements(driver, DataQueryServletForm.class);
    }
    
	public DataQueryServletForm clickLogoutLink() {
    	waitForElement(By.linkText("Logout"));
    	logoutLink.click();
        return PageFactory.initElements(driver, DataQueryServletForm.class);
	}

	public AdminForm clickDataSourceTestLink() {
		dataSourceTestLink.click();
        return PageFactory.initElements(driver, AdminForm.class);
	}

    public AdminForm writeNormalizationList(String fileName) {
        updateField(writeNormalizationListField, fileName);
        clickWriteButton();
        //Wait for Title Normalization page
        waitForElement(By.linkText("Refresh Table"));
        return PageFactory.initElements(driver, AdminForm.class);
    }

    public AdminForm loadNormalizationList(String fileName) {
        updateField(loadNormalizationListField, fileName);
        clickLoadButton();
        //Wait for Title Normalization page
        waitForElement(By.linkText("Refresh Table"));
        return PageFactory.initElements(driver, AdminForm.class);
    }

    public void clickWriteButton() {
        driver.findElement(By.xpath("/html/body/div/form[1]/input[3]")).click();
    }

    public void clickLoadButton() {
        driver.findElement(By.xpath("/html/body/div/form[2]/input[3]")).click();
    }


	public void assertDataSourcesTableData() {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div/table");
		assertThat(trCollection.size() >= 6, is(true));

		String[] expected = {"HDIDB", "JToolsDB", "LibraryDB", "MarcRecordDB", "TitleDB"};

		for(int i = 1; i < trCollection.size(); i++) { // skipping the header
			WebElement trElement = trCollection.get(i);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(1).getText(), containsString(expected[i - 1]));
			assertThat(tdCollection.get(4).getText(), containsString("Good"));
		}

	}
}
