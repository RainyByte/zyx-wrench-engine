package cn.rainybyte.wrench.rate.limter.aop;

import cn.rainybyte.wrench.dynamic.config.center.types.annotations.DCCValue;
import cn.rainybyte.wrench.rate.limter.types.annotations.RateLimiterAccessInterceptor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 */
@Aspect
public class RateLimiterAOP {

    private final Logger log = LoggerFactory.getLogger(RateLimiterAOP.class);

    @DCCValue("rateLimiterSwitch:open")
    private String rateLimiterSwitch;

    // 个人限制频率记录1分钟
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    // 个人限制黑名单24小时
    private final Cache<String, Long> blacklist = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Pointcut("@annotation(cn.rainybyte.wrench.rate.limter.types.annotations.RateLimiterAccessInterceptor)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(rateLimiterAccessInterceptor)")
    public Object doRouter(ProceedingJoinPoint jp, RateLimiterAccessInterceptor rateLimiterAccessInterceptor) throws Throwable {
        // 0.限流开关[open 开启、close 关闭] 关闭后，不再会走限流策略
        if (StringUtils.isBlank(rateLimiterSwitch) || "close".equals(rateLimiterSwitch)) {
            return jp.proceed();
        }

        String key = rateLimiterAccessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("annotation RateLimiter UID is null!");
        }

        // 获取拦截字段
        String keyAttr = getAttrValue(key, jp.getArgs());
        log.info("aop attr {}", keyAttr);

        // 黑名单拦截
        if (!"all".equals(keyAttr) && rateLimiterAccessInterceptor.blacklistCount() != 0 && null != blacklist.getIfPresent(keyAttr) && blacklist.getIfPresent(keyAttr) > rateLimiterAccessInterceptor.blacklistCount()) {
            log.info("限流-黑名单拦截");
            return fallbackMethodResult(jp, rateLimiterAccessInterceptor.fallbackMethod());
        }

        // 获取限流 -> Guava 缓存1分钟
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(rateLimiterAccessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }

        // 限流拦截
        if (!rateLimiter.tryAcquire()) {
            if (rateLimiterAccessInterceptor.blacklistCount() != 0) {
                if (null == blacklist.getIfPresent(keyAttr)) {
                    blacklist.put(keyAttr, 1L);
                } else {
                    blacklist.put(keyAttr, blacklist.getIfPresent(keyAttr) + 1L);
                }
            }
            log.info("限流-超频次拦截: {}", keyAttr);
            return fallbackMethodResult(jp, rateLimiterAccessInterceptor.fallbackMethod());
        }

        // 返回结果
        return jp.proceed();
    }

    /**
     * 调用用户配置的回调方法，当拦截后，返回回调结果
     */
    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        Method method = jp.getSignature().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }

    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String fieldValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(fieldValue)) {
                    break;
                }
                // fieldValue = BeanUtils.getProperty(arg, attr);
                // fix: 使用lombok时，uId这种字段的get方法与idea生成的get方法不同，会导致获取不到属性值，改成反射获取解决
                fieldValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.info("获取路由属性值失败 attr: {}", attr, e);
            }
        }
        return fieldValue;
    }

    /**
     * 获取对象的特定属性值
     */
    private Object getValueByName(Object item, String name) {
        try {
            Field field = getFieldByName(item, name);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(item);
            field.setAccessible(false);
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 根据名称获取方法，改方法同时兼顾继承类获取父类的属性
     */
    private Field getFieldByName(Object item, String name) {
        try {
            Field field;
            try {
                field = item.getClass().getField(name);
            } catch (NoSuchFieldException e) {
                field = item.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }


}
