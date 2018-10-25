package com.engcpp.hystrix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author engcpp
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({RestClientTestFail.class, RestClientTestFail.class, 
    RestClientTestOK.class, RestClientTestOK.class, RestClientTestFail.class, 
    RestClientTestOK.class, RestClientTestFail.class})
public class ParallelTests {}
