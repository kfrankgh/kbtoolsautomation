package com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding;

import com.sersol.kbtools.bvt.dataServlet.commons.ResultParser;
import com.sersol.kbtools.bvt.dataServlet.commons.ResultParserUtils;


public class RuleDatabaseHoldingResultParser implements ResultParser<RuleDatabaseHoldingResult> {
    ResultParserUtils utils = new ResultParserUtils();

    public void parse(String headerName, String value, RuleDatabaseHoldingResult result ){
        switch(headerName){
            case "RuleID":
                result.setRuleId(Integer.parseInt(value));
                break;
            case "Description":
                result.setDescription(value);
                break;
            case "Inactive":
                result.setInactive(value.equals("null") ? null : utils.formatDateString(value));
                break;
            case "Updated":
                result.setUpdated(value.equals("null") ? null : utils.formatDateString(value));
                break;
            case "AccountIdLastUpdate":
                result.setAccountIdLastUpdate(Integer.parseInt(value));
                break;
            case "EmailLastUpdate":
                result.setEmailLastUpdate(value);
                break;
            case "DatabaseCode":
                result.setDatabaseCode(value);
                break;
            case "Title":
                result.setTitle(value);
                break;
            case "RuleHoldingType":
                result.setRuleHoldingType(value);
                break;
        }
    }
}
