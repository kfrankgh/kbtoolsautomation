package com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState;

import com.proquest.commons.pattern.IValueEnum;
import com.sersol.kbtools.bvt.dataServlet.commons.ResultParser;
import com.sersol.kbtools.bvt.dataServlet.commons.ResultParserUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class HoldingImportStateResultParser implements ResultParser<HoldingImportStateResult> {

    ResultParserUtils utils = new ResultParserUtils();

    public void parse(String headerName, String value, HoldingImportStateResult result){
        switch(headerName){
            case "HoldingImportDatabaseId":
                result.setHoldingImportDatabaseId(Integer.parseInt(value));
                break;
            case "DatabaseName":
                result.setDatabaseName(value);
                break;
            case "DatabaseCode":
                result.setDatabaseCode(value);
                break;
            case "Username":
                result.setUsername(value);
                break;
            case "UserId":
                result.setUserId(Integer.parseInt(value));
                break;
            case "Processed":
                result.setProcessed(value.equals("null") ? null : utils.formatDateString(value));
                break;
        }
    }
}
