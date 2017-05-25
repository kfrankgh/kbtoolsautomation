package com.sersol.kbtools.bvt.dataServlet.account;

import com.sersol.kbtools.bvt.dataServlet.account.account.Account;
import com.sersol.kbtools.bvt.dataServlet.account.account.AccountResult;
import com.sersol.kbtools.bvt.dataServlet.account.account.AccountResultParser;
import com.sersol.kbtools.bvt.dataServlet.commons.DataServlet;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Container for query methods related to rules
 */
public class AccountQueries {

    private DataServlet servlet;

    public AccountQueries(DataServlet servlet){
        this.servlet = servlet;
    }

    /**
     * Gets the basic account information such as accountId, first name and last name.
     * @param email
     * @return returns an empty AccountResult if the account was not found.
     */
    public AccountResult getAccount(String email){
        String parameters = servlet.addStringParameter(email, "Email");
        Account data = new Account();

        Document doc = servlet.execute(parameters, data.getStoredProcedure(), data.getDatabase());
        List<AccountResult> accountList = servlet.parseResult(doc, new AccountResultParser(), AccountResult.class);
        return accountList.size() != 1 ? new AccountResult() : accountList.get(0);
    }

}
