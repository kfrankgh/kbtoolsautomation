package com.sersol.kbtools.bvt.dataServlet.holding.holdingsSearch;

import com.sersol.kbtools.bvt.dataServlet.commons.ResultParser;

public class HoldingSearchResultParser implements ResultParser<HoldingsSearchResult>{

    public void parse(String headerName, String value, HoldingsSearchResult result){
        switch(headerName){
            case "TitleTypeCode":
                result.setTitleTypeCode(value);
                break;
            case "IsNormalized":
                result.setIsNormalized(value.equals("0") ? false : true);
                break;
            case "Holdings":
                result.setHoldings(Integer.parseInt(value));
                break;
        }
    }
}
