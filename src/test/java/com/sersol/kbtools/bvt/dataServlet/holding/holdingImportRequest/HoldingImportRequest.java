package com.sersol.kbtools.bvt.dataServlet.holding.holdingImportRequest;

import com.sersol.kbtools.bvt.dataServlet.commons.Databases;
import com.sersol.kbtools.bvt.dataServlet.commons.StoredProcedure;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportState;


/**
 * Represents the stored procedure "prcHoldingImportRequest" in LibraryDB
 */
public class HoldingImportRequest implements StoredProcedure {

    private Integer holdingImportDatabaseId;
    private String action;
    private Integer accountId;


    public String getDatabase(){
        return Databases.LibraryDB.toString();
    }
    public String getStoredProcedure(){
        return "prcHoldingImportRequest";
    }

    public Integer getHoldingImportDatabaseId() {
        return holdingImportDatabaseId;
    }

    /**
     * Call the action method to obtain the id
     * {@link com.sersol.kbtools.bvt.dataServlet.holding.HoldingQueries#getImportQueue(HoldingImportState)}
     * @param holdingImportDatabaseId
     */
    public void setHoldingImportDatabaseId(Integer holdingImportDatabaseId) {
        this.holdingImportDatabaseId = holdingImportDatabaseId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getAccountId() {
        return accountId;
    }

    /**
     * Call the action method to obtain the id
     * {@link com.sersol.kbtools.bvt.dataServlet.holding.HoldingQueries#getImportQueue(HoldingImportState)}
     * @param accountId
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
