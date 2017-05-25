package com.sersol.kbtools.bvt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class EditMarcRecordsPage extends HomePage {

    public void removeMarcRecord(){
        utils.clickButtonByText("Remove");  //removes first MARC record in table;
    }
}
