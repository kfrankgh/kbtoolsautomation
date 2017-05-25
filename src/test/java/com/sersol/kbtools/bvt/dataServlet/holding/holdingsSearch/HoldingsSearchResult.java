package com.sersol.kbtools.bvt.dataServlet.holding.holdingsSearch;

public class HoldingsSearchResult {

    private String titleTypeCode;
    private Boolean isNormalized;
    private Integer holdings;

    public String getTitleTypeCode() {
        return titleTypeCode;
    }

    public void setTitleTypeCode(String titleTypeCode) {
        this.titleTypeCode = titleTypeCode;
    }

    public Boolean getIsNormalized() {
        return isNormalized;
    }

    public void setIsNormalized(Boolean isNormalized) {
        this.isNormalized = isNormalized;
    }

    public Integer getHoldings() {
        return holdings;
    }

    public void setHoldings(Integer holdings) {
        this.holdings = holdings;
    }
}
