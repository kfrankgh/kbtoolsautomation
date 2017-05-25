package com.sersol.kbtools.bvt.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.sersol.kbtools.bvt.utils.HelperMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class DatabaseSearchForm extends HomePage {

	
	@FindBy(how = How.NAME, using = "dbName")
	private WebElement dbNameInput;
	
	@FindBy(how = How.NAME, using = "titleMin")
	private WebElement titleMinInput;
	
	@FindBy(how = How.NAME, using = "titleMax")
	private WebElement titleMaxInput;
	
	@FindBy(how = How.NAME, using = "unNormMin")
	private WebElement unNormMinInput;
	
	@FindBy(how = How.NAME, using = "unNormMax")
	private WebElement unNormMaxInput;
	
	@FindBy(how = How.NAME, using = "normPercMin")
	private WebElement normPercMinInput;
	
	@FindBy(how = How.NAME, using = "normPercMax")
	private WebElement normPercMaxInput;
	
	@FindBy(how = How.XPATH, using = "/html/body/div[2]/form/p[4]/input[1]")
	private WebElement normDbsRadioButton;
	
	@FindBy(how = How.XPATH, using = "/html/body/div[2]/form/p[4]/input[2]")
	private WebElement nonNormDbsRadioButton;
	
	@FindBy(how = How.XPATH, using = "/html/body/div[2]/form/p[4]/input[3]")
	private WebElement allDbsRadioButton;
	
    
    public void setDatabaseName(String dbName) {
        updateField(dbNameInput, dbName);
    }
    
    public void setTitleMin(int titleMin) {
        updateField(titleMinInput, String.valueOf(titleMin));
    }
    
    public void setTitleMax(int titleMax) {
        updateField(titleMaxInput, String.valueOf(titleMax));
    }
    
    public void setUnNormTitleMin(int unNormMin) {
        updateField(unNormMinInput, String.valueOf(unNormMin));
    }
    
    public void setUnNormTitleMax(int unNormMax) {
        updateField(unNormMaxInput, String.valueOf(unNormMax));
    }
    
    public void setPercNormalizedMin(int normPercMin) {
        updateField(normPercMinInput, String.valueOf(normPercMin));
    }
    
    public void setPercNormalizedMax(int normPercMax) {
        updateField(normPercMaxInput, String.valueOf(normPercMax));
    }

    public String getDatabaseNameInput() {
    	return dbNameInput.getText().trim();
    }
    
    public String getTitleMinInput() {
    	return	titleMinInput.getText().trim();
    }

    public String getTitleMaxInput() {
    	return titleMaxInput.getText().trim();
    }

    public String getUnNormMinInput() {
    	return unNormMinInput.getText().trim();
    }
    
    public String getUnNormMaxInput() {
    	return unNormMaxInput.getText().trim();
    }

    public String getNormPercMinInput() {
    	return normPercMinInput.getText().trim();
    }
	
    public String getNormPercMaxInput() {
    	return normPercMaxInput.getText().trim();
    }
    
    public String isNormalizedDbsRadioButtonChecked() {
    	return normDbsRadioButton.getAttribute("checked");
    }
    
    public String getDatabaseCode() {
        return driver.findElement(By.xpath("//a[text()='DB']/parent::th/parent::tr//following-sibling::tr/td[1]/a")).getText().trim();
    }
    
    public String getDatabaseName() {
        return driver.findElement(By.xpath("//a[text()='Database Name']/parent::th/parent::tr//following-sibling::tr/td[2]")).getText().trim();
    }
    
    public String getProviderCode() {
        return driver.findElement(By.xpath("//a[text()='PRV']/parent::th/parent::tr//following-sibling::tr/td[3]")).getText().trim();
    }

    public String getProviderName() {
        return driver.findElement(By.xpath("//a[text()='PRV']/parent::th/parent::tr//following-sibling::tr/td[4]")).getText().trim();
    }
    public void clickAllDbsRadioButton() {
    	driver.findElement(By.xpath("/html/body/div[2]/form/p[4]/input[3]")).click();
    }
    
	public void clickNonNormDbsRadioButton() {
		driver.findElement(By.xpath("/html/body/div[2]/form/p[4]/input[2]")).click();
	}
	
	public void clickNormDbsRadioButton() {
		normDbsRadioButton.click();
	}
    
    public void assertNormalizationPercentageByTypeTableData() {
    	
		boolean serialsComplete = false;
		boolean monographsComplete = false;
		boolean mixedComplete = false;
		int serialsComputedPercentage;
		int serialsReportedPercentage;
		int monographsComputedPercentage;
		int monographsReportedPercentage;
		int mixedComputedPercentage;
		int mixedReportedPercentage;

		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) { // skipping the header and footer

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));

			int serials = Integer.parseInt(tdCollection.get(7).getText().trim());
			float normSerials = Float.parseFloat(tdCollection.get(8).getText().trim());
			int monographs = Integer.parseInt(tdCollection.get(10).getText().trim());
			float normMonographs = Float.parseFloat(tdCollection.get(11).getText().trim());

			if(serials > 0 && monographs == 0) {

				serialsComputedPercentage = Math.round(normSerials / (float)serials *100);
				serialsReportedPercentage = Integer.parseInt(tdCollection.get(9).getText().trim().replace("%", ""));

				serialsComplete = true;
				assertThat(serialsComputedPercentage == serialsReportedPercentage, is(true));

			} else if(serials == 0 && monographs > 0) {

				monographsComputedPercentage = Math.round(normMonographs / (float)monographs * 100);
				monographsReportedPercentage = Integer.parseInt(tdCollection.get(12).getText().trim().replace("%", ""));

				monographsComplete = true;
				assertThat(monographsComputedPercentage == monographsReportedPercentage, is(true));

			} else if(serials > 0 && monographs > 0) {
				float titles = Float.parseFloat(tdCollection.get(4).getText().trim());
				float norm = Float.parseFloat(tdCollection.get(5).getText().trim());

				mixedComputedPercentage = Math.round(norm / titles* 100);
				mixedReportedPercentage = Integer.parseInt(tdCollection.get(6).getText().trim().replace("%", ""));

				mixedComplete = true;
				assertThat(mixedComputedPercentage == mixedReportedPercentage, is(true));
			}
		}
		assertThat(serialsComplete && monographsComplete && mixedComplete, is(true));
	}
    
    
	/**
	 * @param databaseCode
	 */
	public void assertSearchAllTableData(String databaseCode) {
		
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
		assertThat(trCollection.size()==3, is(true));

		for(int rowNum=1; rowNum<trCollection.size() - 1; rowNum++) { // skipping the header and footer
			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			assertThat(tdCollection.get(0).getText(), is(databaseCode));
		}
	}
	
	public Boolean isNormalizedTitlesPercentCorrect(Integer MinPercentNorm, Integer MaxPercentNorm) {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		int errorCount = 0;
		for(int rowNum=1; rowNum<trCollection.size() - 1; rowNum++) { // skipping the header and footer
			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(6);
			int actualPercent = Integer.parseInt(tdElement.getText().replace("%", "").trim());
			if( !(actualPercent >= MinPercentNorm && actualPercent <= MaxPercentNorm) ){
				errorCount++;
			}
		}
        return errorCount > 0 ? false : true;
	}
	
	public void assertSearchNumTitlesTableData() {
		
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		for(int rowNum=1; rowNum<trCollection.size()-1; rowNum++) { // skipping the header and footer

			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(4);
			assertThat((Integer.parseInt(tdElement.getText())<=120) && (Integer.parseInt(tdElement.getText())>=100), is(true));
		}
	}

	/**
	 * Iterates the table and check the "DB Name" column to see if the string contains the specified parameter.
	 * If any of them does, then it returns false.
	 * @param dbName
	 * @return
	 */
	public Boolean dbNamesContain(String dbName) {
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		WebElement trElement;
		List<WebElement> tdCollection;
		int failCounter = 0;
		String patternToMatch = String.format("(?i:.*%s.*)", dbName); // case-insensitive matching

		for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) { // skipping the header row
			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));
			if(!tdCollection.get(1).getText().matches(patternToMatch)){
				failCounter++;
			}
		}

        return failCounter > 0 ? false : true;
	}

    public Boolean resultIncludesDBCodeWithSpecialChars (){
        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

        for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) { // skipping the header and footer

            WebElement trElement = trCollection.get(rowNum);
            List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(0);
			String dbCode = tdElement.getText().toLowerCase();
			if (dbCode.contains(".") || dbCode.contains("~") || dbCode.contains("_") || dbCode.contains("-")) {
				return true;
			}
        }
        return false;
    }
	/**
	 * @param unNormTitleMin
	 * @param unNormTitleMax
	 */
	public boolean assertUnnormalizedTableData(int unNormTitleMin, int unNormTitleMax) {
		
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		WebElement trElement;
		List<WebElement> tdCollection;

		int notNorm;
		
		for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) {

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));

			notNorm =  Integer.parseInt(tdCollection.get(4).getText()) - Integer.parseInt(tdCollection.get(5).getText());
			if (notNorm < unNormTitleMin || notNorm > unNormTitleMax)return false;
		}
        return true;
	}

    public  Boolean searchResultExists(String searchString, int colToSearch) {
        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
		WebElement trElement = trCollection.get(1);
		List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
		WebElement tdElement = tdCollection.get(colToSearch);
		return tdElement.getText().contains(searchString);
    }

	public void clickNamesColumnSortLink() {
		driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[2]/a")).click();
	}

	public void clickTitlesColumnSortLink() {
		driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[5]/a")).click();
	}
	
	public void clickNormColumnSortLink() {
		driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[6]/a")).click();
	}
	
	public void clickNormPercentageColumnSortLink() {
        driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/th[7]/a")).click();
    }


    /**
     * @param Array2
      @param columnNum
     */
    public void fillArrayWithTableNumericalData(List<Integer> Array2, Integer columnNum) {

        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

        for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) { // skipping the header and footer
            WebElement trElement = trCollection.get(rowNum);
            List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(columnNum);
			Array2.add(Integer.parseInt(tdElement.getText().replace("%", "")));
        }
    }

	private void getTableData(List<? super Object> data, int columnNum, boolean isIntegerData){
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) {  // skipping the header and footer
			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(columnNum);
			String cellValue = tdElement.getText();
			if(isIntegerData){
				data.add(Integer.parseInt(cellValue));
			}else{
				data.add(cellValue);
			}
		}
	}

	/**
	 * @param Array2
	 * @param columnNum
	 */
	public void fillArrayWithTableStringData(List<? super Object> Array2, Integer columnNum) {
		getTableData(Array2, columnNum, false);
	}

	/**
	 * @param normArray2
	 */
	public void fillNormArrayWithTableData(List<? super Object> normArray2) {
		getTableData(normArray2, 5, true);
	}

	/**
	 * @param titlesArray2
	 */
	public void fillTitlesArrayWithTableData(List<? super Object> titlesArray2) {
		getTableData(titlesArray2, 4, true);
	}

    /**
     * @param dbArray2
     */
    public void fillDbArrayWithTableData(List<? super Object> dbArray2) {
		getTableData(dbArray2, 0, false);
    }

	/**
	 * @param namesArray2
	 */
	public void fillNamesArrayWithTableData(List<? super Object> namesArray2) {
		getTableData(namesArray2, 1, false);
	}

	/**
     * @param dbArray
	 * @param namesArray
     * @param providerCodeArray
     * @param providerNameArray
	 * @param titlesArray
	 * @param normArray
	 * @param percentageArray
     * @param serialTitlesArray
     * @param normSerialsArray
     * @param percentageSerialsArray
     * @param monoTitlesArray
     * @param normMonosArray
     * @param percentageMonosArray
	 */
	public void fillArraysWithTableData(List<String> dbArray, List<String> namesArray, List<String> providerCodeArray, List<String> providerNameArray, List<Integer> titlesArray,
			List<Integer> normArray, List<Integer> percentageArray, List<Integer> serialTitlesArray, List<Integer> normSerialsArray, List<Integer> percentageSerialsArray,
            List<Integer> monoTitlesArray, List<Integer> normMonosArray, List<Integer> percentageMonosArray) {
		
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

		WebElement trElement;
		WebElement tdElement;
		List<WebElement> tdCollection;

		for (int rowNum = 1; rowNum < trCollection.size() - 1; rowNum++) { // skipping the header row

			trElement = trCollection.get(rowNum);
			tdCollection = trElement.findElements(By.xpath("./td"));
			for (int colNum = 0; colNum < tdCollection.size(); colNum++) {
				tdElement = tdCollection.get(colNum);

				switch (colNum) {
					case 0:
						dbArray.add(tdElement.getText().trim());
						break;
					case 1:
						namesArray.add(tdElement.getText().trim());
						break;
					case 2:
						providerCodeArray.add(tdElement.getText().trim());
						break;
					case 3:
						providerNameArray.add(tdElement.getText().trim());
						break;
					case 4:
						titlesArray.add(Integer.parseInt(tdElement.getText()));
						break;
					case 5:
						normArray.add(Integer.parseInt(tdElement.getText()));
						break;
					case 6:
						percentageArray.add(Integer.parseInt(tdElement.getText().replace("%", "")));
						break;
					case 7:
						serialTitlesArray.add(Integer.parseInt(tdElement.getText()));
						break;
					case 8:
						normSerialsArray.add(Integer.parseInt(tdElement.getText()));
						break;
					case 9:
						percentageSerialsArray.add(Integer.parseInt(tdElement.getText().replace("%", "")));
						break;
					case 10:
						monoTitlesArray.add(Integer.parseInt(tdElement.getText()));
						break;
					case 11:
						normMonosArray.add(Integer.parseInt(tdElement.getText()));
						break;
					case 12:
						percentageMonosArray.add(Integer.parseInt(tdElement.getText().replace("%", "")));
						break;
				}
			}
		}
	}
	
	/**
	 * @param databaseCode
	 */
	public void assertSearchNonNormTable(String databaseCode) {
		
		List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
		assertThat(trCollection.size()==3, is(true));

		for(int rowNum=1;rowNum<trCollection.size()-1;rowNum++) { // skipping the header and footer
			WebElement trElement = trCollection.get(rowNum);
			List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
			WebElement tdElement = tdCollection.get(0);
			assertThat(tdElement.getText(), containsString(databaseCode));
		}
	}

    public Boolean isNormalizedSerialsPercentCorrect(String dbCode) {

        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
        assertThat(trCollection.size() == 3, is(true));

        for (int rowNum = 1; rowNum < trCollection.size() - 1; rowNum++) {
            WebElement trElement = trCollection.get(rowNum);
            List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
            if (!tdCollection.get(0).getText().contains(dbCode)) continue;
            double percentNormalizedSerialsCalc = Double.parseDouble(tdCollection.get(8).getText()) / Double.parseDouble(tdCollection.get(7).getText()) * 100;
            percentNormalizedSerialsCalc = Math.round(percentNormalizedSerialsCalc);
            return percentNormalizedSerialsCalc == Double.parseDouble(tdCollection.get(9).getText().replace("%",""));
        }
        System.out.println("Database was not found");
        return false;
    }

    public Boolean isNormalizedMonographsPercentCorrect(String dbCode) {

        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
        assertThat(trCollection.size() == 3, is(true));

        for (int rowNum = 1; rowNum < trCollection.size() - 1; rowNum++) {
            WebElement trElement = trCollection.get(rowNum);
            List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
            if (!tdCollection.get(0).getText().contains(dbCode)) continue;
            double percentNormalizedMonographsCalc = Double.parseDouble(tdCollection.get(11).getText()) / Double.parseDouble(tdCollection.get(10).getText()) * 100;
            percentNormalizedMonographsCalc = Math.round(percentNormalizedMonographsCalc);
            return percentNormalizedMonographsCalc == Double.parseDouble(tdCollection.get(12).getText().replace("%",""));
        }
        System.out.println("Database was not found");
        return false;
    }

    public Boolean isNormalizedTitlesPercentCorrect(String dbCode) {    //serials and monographs

        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");
        assertThat(trCollection.size() == 3, is(true));

        for (int rowNum = 1; rowNum < trCollection.size() - 1; rowNum++) {
            WebElement trElement = trCollection.get(rowNum);
            List<WebElement> tdCollection = trElement.findElements(By.xpath("./td"));
            if (!tdCollection.get(0).getText().contains(dbCode)) continue;
            double normalizedTitlesCalc = Double.parseDouble(tdCollection.get(8).getText()) + Double.parseDouble(tdCollection.get(11).getText());
            double titlesCalc = Double.parseDouble(tdCollection.get(7).getText()) + Double.parseDouble(tdCollection.get(10).getText());
            double percentNormalizedTitlesCalc = normalizedTitlesCalc / titlesCalc * 100;
            percentNormalizedTitlesCalc = Math.round(percentNormalizedTitlesCalc);
            return percentNormalizedTitlesCalc == Double.parseDouble(tdCollection.get(6).getText().replace("%",""));
        }
        System.out.println("Database was not found");
        return false;
    }

    public Integer getSummaryCounts(Integer colNum) {

        List<WebElement> trCollection = utils.getTableRowsCollection("/html/body/div[2]/table");

        WebElement trElement;
        List<WebElement> thCollection;

        Integer lastRow = trCollection.size()-1;
        Integer colVal;

        trElement = trCollection.get(lastRow);
        thCollection = trElement.findElements(By.xpath("./th"));
        if (colNum != 1) {
            colVal = Integer.parseInt(thCollection.get(colNum).getText().replace("%", ""));
        }else{
            String colString = thCollection.get(colNum).getText();
            colString = colString.replace("Total: ", "");
            colString = colString.replace(" Databases", "");
            colVal = Integer.parseInt(colString);
        }
        return colVal;
    }



	public String getDatabaseCodeLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/b[1]")).getText();
	}

	public String getDatabaseNameLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/b[2]")).getText();
	}

	public String getNumberOfTitlesLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[1]/b")).getText();
	}

	public String getUnNormTitlesLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[2]/b")).getText();		
	}

	public String getPercentageNormLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[3]/b")).getText();
	}

	public String getRadioButtonsLabel() {
		return driver.findElement(By.xpath("/html/body/div[2]/form/p[4]")).getText();
	}

	public String getAtLeastInputSize() {
		return normPercMinInput.getAttribute("size");
	}

	public String getButNoMoreThanInputSize() {
		return normPercMaxInput.getAttribute("size");
	}

	public boolean isDbNameInputPresent() {
		return dbNameInput.isDisplayed();
	}

	public boolean isTitleMinInputPresent() {
		return titleMinInput.isDisplayed();
	}

	public boolean isTitleMaxInputPresent() {
		return titleMaxInput.isDisplayed();
	}

	public boolean isUnNormMinInputPresent() {
		return unNormMinInput.isDisplayed();
	}

	public boolean isUnNormMaxInputPresent() {
		return unNormMaxInput.isDisplayed();
	}

	public boolean isNormPercMinInputPresent() {
		return normPercMinInput.isDisplayed();
	}

	public boolean isNormPercMaxInputPresent() {
		return normPercMaxInput.isDisplayed();
	}

	public boolean isNormDbsRadioButtonPresent() {
		return normDbsRadioButton.isDisplayed();
	}

	public boolean isNonNormDbsRadioButtonPresent() {
		return nonNormDbsRadioButton.isDisplayed();
	}

	public boolean isAllDbsRadioButtonPresent() {
		return allDbsRadioButton.isDisplayed();
	}
}
