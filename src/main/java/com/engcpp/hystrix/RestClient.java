package com.engcpp.hystrix;

import com.engcpp.hystrix.api.Profile;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wiremock.org.apache.http.client.HttpResponseException;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;

/**
 * @author engcpp
 */
@Service
@Scope(value = "prototype")
public class RestClient {
    private static final String URL = "http://localhost:%d/apis";
    public static String COMMAND_KEY = "RestClientGroup";
    private int port;
    private int timeout = 10000;

    protected RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Profile findProfile(boolean fail) {
            return new RestClientCommand(fail).execute();
    }

    public class RestClientCommand extends HystrixCommand<Profile> {
        private boolean fail;

        public RestClientCommand(boolean fail) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(COMMAND_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(COMMAND_KEY))
                .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(true)
                        .withCircuitBreakerRequestVolumeThreshold(1)
                        .withCircuitBreakerErrorThresholdPercentage(1)
                        .withCircuitBreakerSleepWindowInMilliseconds(1)
                        .withExecutionTimeoutEnabled(false)
                //    .withCircuitBreakerForceOpen(fail)
                //    .withCircuitBreakerForceClosed(!fail)
                ));

            this.fail = fail;
        }

        @Override
        protected Profile run() throws Exception {
            if (fail) {
                    throw new HttpResponseException(500, "Internal Server Error");
            } else {
                    final String api = String.format(URL + "/find/profile", port);
                    return getRestTemplate().getForObject(api, Profile.class);
            }
        }

        @Override
        protected Profile getFallback() {
            final String UNKOWN = "Unkown";

            return new Profile()
                            .withId("0")
                            .withEmail(UNKOWN)
                            .withGender(UNKOWN)
                            .withName(UNKOWN);
        }

    }
}