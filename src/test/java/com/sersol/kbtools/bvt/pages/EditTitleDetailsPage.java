package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class EditTitleDetailsPage extends HomePage {

    public String getTitle(){
        return waitForElement(By.name("title")).getText();
    }

    public Boolean setTitle(String title){
        WebElement input = waitForElement(By.name("title"));
        input.click();
        input.clear();
        input.sendKeys(title);
        if (getTitle().contentEquals(title)) return true;
        return false;
    }
}
