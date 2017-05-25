package com.sersol.kbtools.bvt.tests;


import com.sersol.kbtools.bvt.tests.admin.KBToolsAdminSuite;
import com.sersol.kbtools.bvt.tests.advancedmarcsearch.KBToolsAdvancedMarcSearchSuite;
import com.sersol.kbtools.bvt.tests.kbsearch.KBSearchSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        KBSearchSuite.class,
        KBToolsAdminSuite.class,
        KBToolsAdvancedMarcSearchSuite.class
})
public class RegressionSuite {
}
