package cn.rainybyte.wrench.rate.limter.types.annotations;

import java.lang.annotation.*;

/**
 * 自定义注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RateLimiterAccessInterceptor {

    /**
     *  用哪个字段作为拦截表示，为配置则默认走全部
     */
    String key() default "all";

    /**
     * 限制频次（每秒请求次数）
     */
    double permitsPerSecond();

    /**
     * 黑名单拦截（多少次限制后加入黑名单） 0 - 不限制
     */
    double blacklistCount();

    /**
     * 拦截后的执行方法
     */
    String fallbackMethod();

}
