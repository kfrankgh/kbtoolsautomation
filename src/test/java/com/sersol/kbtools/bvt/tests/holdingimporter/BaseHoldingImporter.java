package com.sersol.kbtools.bvt.tests.holdingimporter;

import com.sersol.common.bvt.pages.PageRegistry;
import com.sersol.kbtools.bvt.dataServlet.DataServletActions;
import com.sersol.kbtools.bvt.dataServlet.account.account.AccountResult;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportState;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportStateResult;
import com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding.RuleDatabaseHolding;
import com.sersol.kbtools.bvt.dataServlet.rule.ruleDatabaseHolding.RuleDatabaseHoldingResult;
import com.sersol.kbtools.bvt.pages.HoldingImporterForm;
import com.sersol.kbtools.bvt.pages.HomePage;
import com.sersol.kbtools.bvt.pages.ImportQueueForm;
import com.sersol.kbtools.bvt.pages.ReviewChangesForm;
import com.sersol.kbtools.bvt.tests.KBToolsTest;
import com.sersol.kbtools.bvt.utils.HelperMethods;

import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static com.sersol.kbtools.bvt.tests.ITestConstants.USERNAME;

class BaseHoldingImporter extends KBToolsTest {

	private HelperMethods utils = new HelperMethods();

	protected static final String ABTED_CLEAN_IMPORT_FILE = "ABTED-CleanImport.txt";
	protected static final String RULE_ID = "72923";
	protected static final String RULE_DESCRIPTION = "title split for testing CLASSIC-1372";
	protected static final String HOLDING_VALIDATION_ERRORS = "Holding validation errors";

	protected DataServletActions servletActions = new DataServletActions();

	protected void acceptChanges(String dataBase){
		 
		 PageRegistry.get(HomePage.class).selectHoldingImporterLink();
		 ImportQueueForm importQueueForm = PageRegistry.get(HoldingImporterForm.class).selectImportsInQueueLink();
		 ReviewChangesForm reviewChangesForm = importQueueForm.clickDBlink(dataBase);
		 reviewChangesForm.clickYesLink();
		 HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
		 holdingImporterForm.selectImportQueueLink();
		 importQueueForm.waitForDBprocessing(dataBase);
		 importQueueForm.waitForTextNotPresent(dataBase);
	 }

	protected ImportQueueForm loadFile(String fileName, String dataBase, boolean augmentData)throws URISyntaxException{
		//GO TO HOLDING IMPORTER PAGE AND SELECT FILE
		 HoldingImporterForm holdingImporterForm = PageRegistry.get(HomePage.class).selectHoldingImporterLink();
		 holdingImporterForm.setFileToImport(fileName);
		 if (augmentData)
			 holdingImporterForm.setAugmentLiveData();
		 holdingImporterForm.clickLoadButton();
		 
		 //WAIT FOR FILE IMPORTED.
		 PageRegistry.get(HomePage.class).selectHoldingImporterLink();
		 ImportQueueForm importQueueForm = PageRegistry.get(HoldingImporterForm.class).selectImportsInQueueLink();
		 importQueueForm.waitForLinkPresent(dataBase);
		 return importQueueForm;
	 }

	/**
	 * Deletes from the import queue, deactivate the rule, import the file, and activate the rule
	 * via DataServlet.
	 * @param databaseCode
	 * @param ruleId
	 * @param importFile The file that contains the default data required for your import tests
	 */
	protected void cleanupImportData(String databaseCode, int ruleId, String importFile){
		cancelImport(databaseCode);
		deactivateRule(databaseCode, ruleId);
		loadFile(importFile);
		acceptImport(databaseCode);
		activateRule(databaseCode, ruleId);
	}

	/**
	 * Loads the specified file for import which exists in the "test/resources" folder via DataServlet.
	 * @param fileName
	 */
	protected void loadFile(String fileName){
		String resourceFile = ClassLoader.getSystemResource(fileName).getFile();
		AccountResult account = servletActions.accountQueries.getAccount(USERNAME);

		boolean loaded = servletActions.holdingQueries.loadFile(resourceFile, account.getAccountId());
		assertThat(String.format("FILE LOADED FOR IMPORT: %s", fileName), loaded, is(true));
	}

	private void acceptImport(String databaseCode){
		final int MAX_WAIT_COUNTER = 60 * 5; // wait about 5 minutes
		int counter = 0;

		// wait until import is ready for acceptance
		HoldingImportStateResult importInQueue = getImportInQueue(databaseCode);
		while(importInQueue.getProcessed() == null && counter < MAX_WAIT_COUNTER){
			utils.pause(utils.MIN_WAIT_MILLISECONDS);
			importInQueue = getImportInQueue(databaseCode);
			counter++;
		}

		boolean accepted = servletActions.holdingQueries.acceptImport(importInQueue.getHoldingImportDatabaseId(), importInQueue.getUserId());
		assertThat(String.format("IMPORT ACCEPTED: %s", databaseCode), accepted, is(true));

		// wait until import is done
		importInQueue = getImportInQueue(databaseCode);
		counter = 0;
		while(importInQueue != null && counter < MAX_WAIT_COUNTER){
			utils.pause(utils.MIN_WAIT_MILLISECONDS);
			importInQueue = getImportInQueue(databaseCode);
			counter++;
		}
	}

	/**
	 * Cancels import via DataServlet if the specified import is in the queue.
	 * Otherwise it does nothing.
	 * @param databaseCode
	 */
	protected void cancelImport(String databaseCode){
		HoldingImportStateResult targetImport = getImportInQueue(databaseCode);
		if(targetImport != null){
			boolean cancelled = servletActions.holdingQueries.cancelImport(targetImport.getHoldingImportDatabaseId(), targetImport.getUserId());
			assertThat(String.format("IMPORT CANCELLED: %s", databaseCode), cancelled, is(true));
		}
	}

	/**
	 * Gets the import identified by the database code from the import queue via DataServlet.
	 * @param databaseCode
	 * @return It returns null if the import is not found.
	 */
	protected HoldingImportStateResult getImportInQueue(String databaseCode){
		List<HoldingImportStateResult> importQueue = servletActions.holdingQueries.getImportQueue(new HoldingImportState());
		return importQueue.stream()
				.filter(result -> result.getDatabaseCode().equalsIgnoreCase(databaseCode))
				.findFirst()
				.orElse(null);
	}

	private void activateRule(String databaseCode, int ruleId){
		List<RuleDatabaseHoldingResult> rules = getRules(databaseCode, ruleId);
		if(rules.size() > 0){
			boolean activated = servletActions.ruleQueries.activateRule(ruleId, rules.get(0).getAccountIdLastUpdate());
			assertThat(String.format("RULE ACTIVATED: %s, %s", databaseCode, ruleId), activated, is(true));
		}
	}

	private void deactivateRule(String databaseCode, int ruleId){
		List<RuleDatabaseHoldingResult> rules = getRules(databaseCode, ruleId);
		if(rules.size() > 0){
			boolean deactivated = servletActions.ruleQueries.deactivateRule(ruleId, rules.get(0).getAccountIdLastUpdate());
			assertThat(String.format("RULE ACTIVATED: %s, %s", databaseCode, ruleId), deactivated, is(true));
		}
	}

	private List<RuleDatabaseHoldingResult> getRules(String databaseCode, int ruleId){
		RuleDatabaseHolding data = new RuleDatabaseHolding();
		data.setDatabaseCode(databaseCode);
		data.setRuleId(ruleId);
		return servletActions.ruleQueries.searchRules(data);
	}
}
