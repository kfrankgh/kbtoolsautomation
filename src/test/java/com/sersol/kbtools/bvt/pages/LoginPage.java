package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import com.sersol.common.bvt.pages.BasePage;

public class LoginPage extends BasePage{

    public LoginPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    public HomePage login(String username, String password) {
        driver.navigate().to(getBaseURL() + "/login.jsp");
        
        waitForElement(By.name("j_username"));
        
        updateField(driver.findElement(By.name("j_username")), username);
        updateField(driver.findElement(By.name("j_password")), password);

        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        return PageFactory.initElements(driver, HomePage.class);
    }
    
    public boolean isLoggedIn() {
        try {
            return driver.findElement(By.className("tool")).isDisplayed();
        }
        catch (org.openqa.selenium.NoSuchElementException elementNotFoundException) {
            return false;

        }
    }
    
}
