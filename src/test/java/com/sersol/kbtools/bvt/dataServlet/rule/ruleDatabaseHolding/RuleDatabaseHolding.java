package com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding;

import com.sersol.kbtools.bvt.dataServlet.commons.Databases;
import com.sersol.kbtools.bvt.dataServlet.commons.StoredProcedure;

/**
 * Represents the stored procedure "prcRuleDatabaseHolding_r" in LibraryDB
 */
public class RuleDatabaseHolding implements StoredProcedure {
    private Integer ruleId;
    private String databaseCode;

    public String getDatabase(){
        return Databases.LibraryDB.toString();
    }
    public String getStoredProcedure(){
        return "prcRuleDatabaseHolding_r";
    }


    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }
}
