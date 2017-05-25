package com.sersol.kbtools.bvt.dataServlet.account.account;

import com.sersol.kbtools.bvt.dataServlet.commons.ResultParser;

public class AccountResultParser implements ResultParser<AccountResult> {

    public void parse(String headerName, String value, AccountResult result){
        switch(headerName){
            case "AccountId":
                result.setAccountId(Integer.parseInt(value));
                break;
            case "NameFirst":
                result.setNameFirst(value);
                break;
            case "NameLast":
                result.setNameLast(value);
                break;
        }
    }
}
