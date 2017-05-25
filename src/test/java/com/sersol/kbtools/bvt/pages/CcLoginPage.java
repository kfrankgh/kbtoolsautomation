package com.sersol.kbtools.bvt.pages;

import com.sersol.common.bvt.pages.BasePage;
import com.sersol.kbtools.bvt.configuration.ClientCenterEnvironment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by kfrankli on 3/22/2016.
 */
public class CcLoginPage extends BasePage {

    private static ClientCenterEnvironment env = new ClientCenterEnvironment();

    public CcLoginPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    public CcHomePage login(String username, String password, String libraryCode) {
        driver.navigate().to(env.getBaseUrl());

        //CC login
        updateField(waitForElement(By.name("_login$_login$UserName")), username);
        updateField(waitForElement(By.name("_login$_login$Password")), password);
        waitForElement(By.cssSelector("input[type=\"submit\"]")).click();

        //Log into Profile
        WebElement searchBySelector = waitForElement(By.id("ctl00_cphCCMain__container__filter__search__detailsView_LibrarySearchSearchNode_Data"));
        Select menuOption = new Select(searchBySelector);
        menuOption.selectByValue("LibraryCode");
        waitForElement(By.id("ctl00_cphCCMain__container__filter__search__detailsView_LibrarySearchSearchNode_LibraryCodeSearchNode_Data")).clear();
        waitForElement(By.id("ctl00_cphCCMain__container__filter__search__detailsView_LibrarySearchSearchNode_LibraryCodeSearchNode_Data"))
                .sendKeys(libraryCode);
        waitForElement(By.cssSelector("input[value='Search']")).click();
        return PageFactory.initElements(driver, CcHomePage.class);
    }






}
