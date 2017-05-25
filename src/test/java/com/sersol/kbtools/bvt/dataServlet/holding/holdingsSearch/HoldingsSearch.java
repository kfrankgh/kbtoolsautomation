package com.sersol.kbtools.bvt.dataServlet.holding.holdingsSearch;

import com.sersol.kbtools.bvt.dataServlet.commons.Databases;
import com.sersol.kbtools.bvt.dataServlet.commons.StoredProcedure;

/**
 * Represents the stored procedure "prcHoldingsSearch" in LibraryDB
 */
public class HoldingsSearch implements StoredProcedure {

    // TODO: add more parameters as needed
    private String databaseCode;
    private String titleBegins;

    public String getDatabase(){
        return Databases.LibraryDB.toString();
    }
    public String getStoredProcedure(){
        return "prcHoldingsSearch";
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getTitleBegins() {
        return titleBegins;
    }

    public void setTitleBegins(String titleBegins) {
        this.titleBegins = titleBegins;
    }
}
