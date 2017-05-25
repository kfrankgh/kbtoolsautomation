package com.sersol.kbtools.bvt.dataServlet;

import com.sersol.kbtools.bvt.dataServlet.account.AccountQueries;
import com.sersol.kbtools.bvt.dataServlet.commons.DataServlet;
import com.sersol.kbtools.bvt.dataServlet.holding.HoldingQueries;
import com.sersol.kbtools.bvt.dataServlet.rule.RuleQueries;

/**
 * Container for query classes representing a group of queries for a particular domain such as holdings.
 * You can just instantiate this class to access all the available query action methods.
 */
public class DataServletActions {
    private DataServlet servlet;

    public HoldingQueries holdingQueries;
    public RuleQueries ruleQueries;
    public AccountQueries accountQueries;

    public DataServletActions(){
        servlet = DataServlet.getInstance();
        init();
    }

    private void init(){
        holdingQueries = new HoldingQueries(servlet);
        ruleQueries = new RuleQueries(servlet);
        accountQueries = new AccountQueries(servlet);
    }
}
