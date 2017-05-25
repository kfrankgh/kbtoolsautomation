package com.sersol.kbtools.bvt.tests;


import com.sersol.kbtools.bvt.tests.holdingimporter.HoldingImporterSuite;
import com.sersol.kbtools.bvt.tests.normalizer.KBToolsTitleNormalizationSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HoldingImporterSuite.class,
        KBToolsTitleNormalizationSuite.class,
})
public class RegressionSuiteSlow {
}
