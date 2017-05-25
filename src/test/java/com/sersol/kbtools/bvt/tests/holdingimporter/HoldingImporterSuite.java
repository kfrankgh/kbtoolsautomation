package com.sersol.kbtools.bvt.tests.holdingimporter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.sersol.common.bvt.runners.RandomSuite;

@RunWith(RandomSuite.class)
@SuiteClasses({
        FastHoldingEditIT.class,
        ImportingFileIT.class,
        InputNormalizationIT.class,
        LoadingLiveDataIT.class,
        VerifyUiElementsIT.class
})


public class HoldingImporterSuite {

}





