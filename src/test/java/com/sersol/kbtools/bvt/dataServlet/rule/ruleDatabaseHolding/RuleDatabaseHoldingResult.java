package com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding;

import org.joda.time.DateTime;

public class RuleDatabaseHoldingResult {
    private Integer ruleId;
    private String description;
    private DateTime inactive;
    private DateTime updated;
    private String emailLastUpdate;
    private String databaseCode;
    private String title;
    private String ruleHoldingType;
    private Integer accountIdLastUpdate;

    public Integer getAccountIdLastUpdate() {
        return accountIdLastUpdate;
    }

    public void setAccountIdLastUpdate(Integer accountIdLastUpdate) {
        this.accountIdLastUpdate = accountIdLastUpdate;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * "null" indicates the rule is active. Otherwise the field is set to the date string
     * when it was deactivated.
     * @return
     */
    public DateTime getInactive() {
        return inactive;
    }

    public void setInactive(DateTime inactive) {
        this.inactive = inactive;
    }

    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    public String getEmailLastUpdate() {
        return emailLastUpdate;
    }

    public void setEmailLastUpdate(String emailLastUpdate) {
        this.emailLastUpdate = emailLastUpdate;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuleHoldingType() {
        return ruleHoldingType;
    }

    public void setRuleHoldingType(String ruleHoldingType) {
        this.ruleHoldingType = ruleHoldingType;
    }
}
