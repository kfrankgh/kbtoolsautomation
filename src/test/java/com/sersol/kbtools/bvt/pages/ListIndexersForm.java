package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Santiago Gonzalez
 *
 */
public class ListIndexersForm extends TitleNormalizationForm {


	public void assertListIndexersTableData() {

		List<WebElement> trCollection3 = utils.getTableRowsCollection("/html/body/div[2]/table");

		int colNum3;

		WebElement tdElement3;
		WebElement trElement3;
		List<WebElement> tdCollection3;

		for(int rowNum3=0;rowNum3<2;rowNum3++) {

			trElement3 = trCollection3.get(rowNum3);
			tdCollection3 = trElement3.findElements(By.xpath("td"));
			colNum3 = 0;

			for(colNum3=0; colNum3<tdCollection3.size();colNum3++) {
				tdElement3 = tdCollection3.get(colNum3);
				if(rowNum3==0) {
					if(colNum3==0)
						assertThat(tdElement3.getText(), containsString("Name"));
					else if(colNum3==1) {
						assertThat(tdElement3.getText(), containsString("DB Code"));
					} else if(colNum3==2) {
						assertThat(tdElement3.getText(), containsString("Progress"));
					} else if(colNum3==3) {
						assertThat(tdElement3.getText(), containsString("Errors"));
					} else if(colNum3==4) {
						assertThat(tdElement3.getText(), containsString("Duration"));
					} else if(colNum3==5) {
						assertThat(tdElement3.getText(), containsString("MS/Index"));
					} else {
						assertThat(tdElement3.getText(), containsString("Action"));
					}
				} else {
					if(rowNum3==1) {
						if(colNum3==6) {
							assertThat(utils.isLinkPresent(tdElement3.getText()), is(true));
						}
					}
				}
			}
		}
	}
}
