package com.engcpp.hystrix;

import static com.engcpp.hystrix.RestClient.COMMAND_KEY;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import wiremock.net.minidev.json.JSONObject;

/**
 *
 * @author engcpp
 */
@RunWith(SpringRunner.class)
public class RestClientTestOK {

    @Autowired
    public RestClient restClient;       
    
    @Rule
    public WireMockRule server = new WireMockRule(options()
        .dynamicPort()
        .containerThreads(4)
        .jettyAcceptors(1)
        .asynchronousResponseEnabled(false));

    private static final String PROFILE_NAME  = "Mocked User";
    private static final String PROFILE_EMAIL = "mocked.user@engcpp.com";
    private static final String PROFILE_GENDER = "male";
    private static final String PROFILE_ID  = "1";
    
    @TestConfiguration
    static class RestClientTestContextConfiguration {  
        @Bean
        public RestClient employeeService() {
            return new RestClient();
        }
    }
    
    @Before
    public void setUp() {
        JSONObject obj = new JSONObject();
        obj.appendField("id", PROFILE_ID);
        obj.appendField("name", PROFILE_NAME);
        obj.appendField("email", PROFILE_EMAIL);
        obj.appendField("gender", PROFILE_GENDER);
        
        stubFor(get(urlEqualTo("/apis/find/profile"))
               .willReturn(aResponse()
                   .withFixedDelay(3000)
                   .withStatus(200)
                   .withHeader("Content-Type", "application/json")
                   .withBody(obj.toJSONString())));                
    }
    
    @Test
    public void test1(){        
        restClient.setPort(server.port());
        long init = currentTimeMillis();
        restClient.findProfile(false);
        print(init, "OK1");
        assertFalse(isCircuitOpen());
    }
    
    @Test
    public void test2(){        
        restClient.setPort(server.port());
        long init = currentTimeMillis();
        restClient.findProfile(false);
        print(init, "OK2");
        assertFalse(isCircuitOpen());
    }    

    @Test
    public void test3(){        
        restClient.setPort(server.port());   
        long init = currentTimeMillis();
        restClient.findProfile(false);
        print(init, "OK3");
        assertFalse(isCircuitOpen());        
    }        
    
    private void print(long init, String method){
        out.println(method+" - Circuit is "+(isCircuitOpen()?"open":"closed")
            + " in " + (currentTimeMillis()-init) +" ms");
    }
        
    private static boolean isCircuitOpen() {
       HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory
               .getInstance(HystrixCommandKey.Factory.asKey(COMMAND_KEY));
       return circuitBreaker.isOpen();
    }

}
