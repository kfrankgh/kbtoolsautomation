package com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState;

import org.joda.time.DateTime;

public class HoldingImportStateResult {

    private Integer holdingImportDatabaseId;
    private String databaseName;
    private String databaseCode;
    private String username;
    private Integer userId;
    private DateTime processed;

    public Integer getHoldingImportDatabaseId() {
        return holdingImportDatabaseId;
    }

    public void setHoldingImportDatabaseId(Integer holdingImportDatabaseId) {
        this.holdingImportDatabaseId = holdingImportDatabaseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DateTime getProcessed() {
        return processed;
    }

    public void setProcessed(DateTime processed) {
        this.processed = processed;
    }
}
