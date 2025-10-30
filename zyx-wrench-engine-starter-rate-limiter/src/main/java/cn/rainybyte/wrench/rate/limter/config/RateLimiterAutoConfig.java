package cn.rainybyte.wrench.rate.limter.config;

import cn.rainybyte.wrench.rate.limter.aop.RateLimiterAOP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限流配置
 */
@Configuration
public class RateLimiterAutoConfig {

    @Bean
    public RateLimiterAOP rateLimiterAOP(){
        return new RateLimiterAOP();
    }

}
