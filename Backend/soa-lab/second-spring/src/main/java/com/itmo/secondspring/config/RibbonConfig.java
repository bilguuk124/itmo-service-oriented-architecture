package com.itmo.secondspring.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name = "second-service", configuration = RibbonConfig.class)
public class RibbonConfig {

    @Bean
    public IRule ribbonRule(){
        return new RoundRobinRule();
    }
}
