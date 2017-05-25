package com.sersol.kbtools.bvt.dataServlet.holding.hodlingImportFile;

import com.sersol.kbtools.bvt.dataServlet.commons.Databases;
import com.sersol.kbtools.bvt.dataServlet.commons.StoredProcedure;

/**
 * Represents the stored procedure "prcHoldingImportFile" in LibraryDB
 */
public class HoldingImportFile implements StoredProcedure {

    private String source;
    private Integer accountId;


    public String getDatabase(){
        return Databases.LibraryDB.toString();
    }

    public String getStoredProcedure(){
        return "prcHoldingImportFile";
    }

    public String getSource() {
        return source;
    }

    /**
     * The absolute path to the input file
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
