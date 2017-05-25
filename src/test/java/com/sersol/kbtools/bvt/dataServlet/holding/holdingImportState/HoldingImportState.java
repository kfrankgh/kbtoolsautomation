package com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState;

import com.sersol.kbtools.bvt.dataServlet.commons.Databases;
import com.sersol.kbtools.bvt.dataServlet.commons.StoredProcedure;

/**
 * Represents the stored procedure "prcHoldingImportState" in LibraryDB
 */
public class HoldingImportState implements StoredProcedure {

    public String getDatabase(){
        return Databases.LibraryDB.toString();
    }
    public String getStoredProcedure(){
        return "prcHoldingImportState";
    }
}
