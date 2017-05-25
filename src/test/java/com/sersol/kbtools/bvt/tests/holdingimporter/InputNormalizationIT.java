package com.sersol.kbtools.bvt.tests.holdingimporter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.sersol.kbtools.bvt.pages.*;
import com.sersol.kbtools.bvt.utils.MSSQLServer;
import com.sersol.kbtools.bvt.utils.TestRail;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.common.bvt.utils.Utils;
import com.sersol.kbtools.bvt.tests.ITestConstants;

public class InputNormalizationIT extends BaseHoldingImporter implements ITestConstants {
    private TestRail client = new TestRail();
    private String testCaseId;

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            try {
                client.postFailingTestRailResult(testCaseId);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        protected void succeeded(Description description) {
            try {
                client.postPassingTestRailResult(testCaseId);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };

    @Test
    public void dateNormalizationTest() {
        testCaseId = "324712";

        Integer errorCount=0;
        Integer testCount=0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream
                (RESOURCE_FILES_PATH + "/DateNormalization.txt"), "UTF-16"));)
        {
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                String dates[] = currentLine.split(",");
                String dateUnnormalized = dates[0];
                String expectedDate = dates[1];
                String dateNormalized = getNormalizedDates(dateUnnormalized);
                if(!dateNormalized.contentEquals(expectedDate)){
                    System.out.println("Unexpected Normalized Date: "+dateNormalized+". Expected: "+expectedDate);
                    errorCount++;
                };
                testCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Integer.toString(errorCount)+" failed out of "+Integer.toString(testCount));
        assertThat(errorCount, is(equalTo(0)));
    }
    /**
     * Queries the database and returns a Normalized date i.e. a date that
     * is converted to an acceptable format.
     *
     * @param  inputDate  the date that should be converted
     * @return  the date that the inputDate is converted to
     */
    private String getNormalizedDates(String inputDate) {
        MSSQLServer MSSQLServer = new MSSQLServer();
        ResultSet rs = null;
        String normalizedDate = "";

        String sqlQuery = ("DECLARE @DateId int DECLARE @DateDisplay nvarchar(50) SET @DateDisplay = N'" + inputDate +
                "' EXEC dbo.prcDate_c2 @DateDisplay=@DateDisplay,@DateId=@DateId OUTPUT SELECT * FROM tblDate WHERE DateId=@DateId");

        MSSQLServer.setConnection("LibraryDB");

        rs = MSSQLServer.executeQuery(sqlQuery);
        try {
            while (rs.next()) {
                normalizedDate = (rs.getString("DateDisplay"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MSSQLServer.closeConnection();
            return normalizedDate;
        }
    }
}