package com.engcpp.hystrix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * @author engcpp
 */

@Suite.SuiteClasses({RestClientTestOK.class, RestClientTestFail.class, RestClientTestOK.class,
		RestClientTestFail.class, RestClientTestOK.class,
		RestClientTestFail.class, RestClientTestFail.class,
		RestClientTestOK.class, RestClientTestOK.class})
@RunWith(Suite.class)
public class SequentialTests {
}
