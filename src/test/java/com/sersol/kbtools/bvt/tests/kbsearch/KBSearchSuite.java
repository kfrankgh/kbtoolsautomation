package com.sersol.kbtools.bvt.tests.kbsearch;

import com.sersol.common.bvt.runners.RandomSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RandomSuite.class)
@SuiteClasses({
    HoldingSearchIT.class,
    AuthorityTitleSearchIT.class,
    MarcSearchIT.class,
    DatabaseSearchIT.class,
    SubjectSearchIT.class,
    RuleSearchIT.class
})

public class KBSearchSuite {}