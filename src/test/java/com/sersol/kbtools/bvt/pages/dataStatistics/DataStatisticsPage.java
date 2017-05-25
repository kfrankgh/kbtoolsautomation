package com.sersol.kbtools.bvt.pages.dataStatistics;

import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.dataStatistics.dataStatisticsTables.DataStatisticsTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataStatisticsPage extends HomePage {
    /**
     * waits for a given time for the data in the tables to be loadeed
     */
    public void waitForDataToLoad(){
        long start = System.currentTimeMillis();
        if (isDataUpdating()){
            do{
                utils.pause(1000);
            }while(isDataUpdating() && (System.currentTimeMillis() - start) < 60000);
        }
    }

    /**
     * Verifies whether page data is updating.
     * @return
     */
    public Boolean isDataUpdating(){
        if(isLibraryDbStatsUpdating() || isTitleDbStatsUpdating() || isMarcDbStatsUpdating()) return true;
        if(!isTriggerUpdateLinkPresent()){
            System.out.println("Trigger Update Link is not present yet");
            return true;
        }
        return false;
    }

    private Boolean isLibraryDbStatsUpdating(){
        try {
            if (utils.getTable("Library DB Statistics").findElements(By.tagName("tr")).size() == 1) return true;
        }catch(StaleElementReferenceException e){
            utils.refreshPage();
            if (utils.getTable("Library DB Statistics").findElements(By.tagName("tr")).size() == 1) return true;
        }
        return false;
    }

    private Boolean isTitleDbStatsUpdating(){
        if (utils.getTable("Title DB Statistics").findElements(By.tagName("tr")).size() == 1) return true;
        return false;
    }

    private Boolean isMarcDbStatsUpdating(){
        if (utils.getTable("MARC DB Statistics").findElements(By.tagName("tr")).size() == 1) return true;
        return false;
    }

    private Boolean isTriggerUpdateLinkPresent(){
        try {
            waitForElement(By.linkText("Trigger Update"), 1);
        } catch(TimeoutException e){
            return false;
        }
        return true;
    }

    /**
     * gets the last Updated date and time of the data on the page
     * @return
     */
    public Date getLastUpdatedDateTime(){
        Date dateTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            dateTime = sdf.parse(getDateSubString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private String getDateSubString(){
        String cssPath_Text_LastUpdated = ".page_content>div>font";
        String lastUpdated = driver.findElement(By.cssSelector(cssPath_Text_LastUpdated)).getText();
        return lastUpdated.substring(getDateSubStringIndex(lastUpdated));
    }

    private int getDateSubStringIndex(String textContainingDate){
        return textContainingDate.indexOf("Last updated: ") + "Last updated: ".length();
    }

    /**
     * Clicks Trigger Update link
     */
    public void clickTriggerUpdateLink(){
        driver.findElement(By.linkText("Trigger Update")).click();
    }

    /**
     * Parses each row from the table into a DataStatisticsTableRow class and adds it to an Array list of
     * DataStatisticsTableRows
     *
     * @param tableName
     * @return
     */
    public List<DataStatisticsTableRow> getStatisticsTable(String tableName){
        DataStatisticsTableRow dataStatisticsTableRow;

        List<WebElement> tableRows = utils.getTable(tableName).findElements(By.tagName("tr"));
        List<DataStatisticsTableRow> dataStatsTableRows = new ArrayList<>();

        for(int i = 0; i < tableRows.size();i++){
            dataStatisticsTableRow = new DataStatisticsTableRow();
            ArrayList<String> tableRow = getRowValues(tableRows.get(i));
            dataStatisticsTableRow.header = tableRow.get(0);
            dataStatisticsTableRow.count = tableRow.get(1);
            dataStatisticsTableRow.description = tableRow.get(2);
            dataStatsTableRows.add(dataStatisticsTableRow);
        }
        return dataStatsTableRows;
    }

    private ArrayList<String> getRowValues(WebElement tableRow){
        WebElement headerCell = tableRow.findElement(By.tagName("th"));
        List<WebElement> dataCells = tableRow.findElements(By.tagName("td"));
        String header = headerCell.getText().trim();
        String count = dataCells.get(0).getText().trim();
        String description = dataCells.get(1).getText().trim();

        ArrayList<String> rowValues = new ArrayList<>();
        rowValues.add(header);
        rowValues.add(count);
        rowValues.add(description);
        return rowValues;
    }
}
