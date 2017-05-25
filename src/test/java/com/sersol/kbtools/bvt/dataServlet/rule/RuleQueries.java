package com.sersol.kbtools.bvt.dataServlet.rule;

import com.sersol.kbtools.bvt.dataServlet.commons.DataServlet;
import com.sersol.kbtools.bvt.dataServlet.rule.rule.Rule;
import com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding.RuleDatabaseHolding;
import com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding.RuleDatabaseHoldingResult;
import com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding.RuleDatabaseHoldingResultParser;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Container for query methods related to rules
 */
public class RuleQueries {
    private DataServlet servlet;

    public RuleQueries(DataServlet servlet){
        this.servlet = servlet;
    }

    /**
     * Searches rules. It's similar to Main | Rule Search in UI.
     * @param data
     * @return
     */
    public List<RuleDatabaseHoldingResult> searchRules(RuleDatabaseHolding data){
        String parameters = "";

        if(data.getRuleId() != null){
            parameters = servlet.addNumericParameter(data.getRuleId(), "RuleId");
        }

        if(data.getDatabaseCode() != null){
            parameters += servlet.addStringParameter(data.getDatabaseCode(), "DatabaseCode");
        }

        // TODO: implement the rest of parameters

        Document doc = servlet.execute(parameters, data.getStoredProcedure(), data.getDatabase());
        return servlet.parseResult(doc, new RuleDatabaseHoldingResultParser(), RuleDatabaseHoldingResult.class);
    }

    /**
     * Activates the existing rule
     * @param ruleId
     * @param accountId
     * @return
     */
    public boolean activateRule(int ruleId, int accountId){
        Rule data = new Rule();
        data.setRuleId(ruleId);
        data.setAccountIdLastUpdate(accountId);
        data.setInactive(false);
        return editRule(data);
    }

    /**
     * Deactivate the existing rule
     * @param ruleId
     * @param accountId
     * @return
     */
    public boolean deactivateRule(int ruleId, int accountId){
        Rule data = new Rule();
        data.setRuleId(ruleId);
        data.setAccountIdLastUpdate(accountId);
        data.setInactive(true);
        return editRule(data);
    }


    //NOTE: this will likely change later as I add more support for editing rules
    private boolean editRule(Rule data){

        String parameters = servlet.addNumericParameter(data.getRuleId(), "RuleId");
        parameters += servlet.addNumericParameter(data.getAccountIdLastUpdate(), "AccountIdLastUpdate");

        if(data.getDescription() != null){
            parameters += servlet.addStringParameter(data.getDescription(), "Description");
        }

        if(data.getInactive() != null){
            parameters += servlet.addNumericParameter(data.getInactive() ? 1 : 0, "Inactive");
        }

        Document doc =  servlet.execute(parameters, data.getStoredProcedure(), data.getDatabase());
        String info = servlet.getErrorMessage(doc);

        if(servlet.errorIndicatesNonQuery(info)) {
            return true;
        }

        return false;
    }
}
