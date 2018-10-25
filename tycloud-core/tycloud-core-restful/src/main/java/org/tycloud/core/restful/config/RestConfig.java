package org.tycloud.core.restful.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yaohelang on 2017/10/12.
 */
@Configuration
public class RestConfig {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
