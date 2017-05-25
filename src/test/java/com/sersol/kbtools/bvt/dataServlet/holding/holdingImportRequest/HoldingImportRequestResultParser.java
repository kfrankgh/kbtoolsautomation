package com.sersol.kbtools.bvt.dataServlet.holding.holdingImportRequest;

import com.sersol.kbtools.bvt.dataServlet.commons.ResultParser;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportStateResult;

public class HoldingImportRequestResultParser implements ResultParser<HoldingImportRequestResult> {

    public void parse(String headerName, String value, HoldingImportRequestResult result){
        switch(headerName) {
            case "ErrorMessage":
                result.setErrorMessage(value);
                break;
        }
    }
}
