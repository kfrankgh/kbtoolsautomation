package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;

public class ViewSubjectForm extends MainMenuForm {
    public String getPageTitle(){return driver.findElement(By.cssSelector(".page_header>h1")).getText();}
}
