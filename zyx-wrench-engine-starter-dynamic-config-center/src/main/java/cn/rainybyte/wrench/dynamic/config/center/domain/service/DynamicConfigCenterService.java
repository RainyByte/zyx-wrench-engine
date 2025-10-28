package cn.rainybyte.wrench.dynamic.config.center.domain.service;

import cn.rainybyte.wrench.dynamic.config.center.config.DynamicConfigCenterAutoConfig;
import cn.rainybyte.wrench.dynamic.config.center.config.DynamicConfigCenterAutoProperties;
import cn.rainybyte.wrench.dynamic.config.center.domain.model.valobj.AttributeVO;
import cn.rainybyte.wrench.dynamic.config.center.types.annotations.DCCValue;
import cn.rainybyte.wrench.dynamic.config.center.types.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicConfigCenterService implements IDynamicConfigCenterService{

    private final Logger log = LoggerFactory.getLogger(DynamicConfigCenterAutoConfig.class);

    private final DynamicConfigCenterAutoProperties properties;

    private final RedissonClient redissonClient;

    // 记录已处理的bean，便于后续属性变更时快速定位
    private final Map<String, Object> dccBeanGroup = new ConcurrentHashMap<>();

    // 通过构造方法注入配置属性和Redis客户端
    public DynamicConfigCenterService(DynamicConfigCenterAutoProperties properties, RedissonClient redissonClient){
        this.properties = properties;
        this.redissonClient = redissonClient;
    }

    @Override
    public Object proxyObject(Object bean) {
        // 处理 AOP 代理，获取真实类和对象
        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;
        if(AopUtils.isAopProxy(bean)){
            targetBeanClass = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }

        Field[] fields = targetBeanClass.getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(DCCValue.class)){
                continue;
            }
            DCCValue dccValue = field.getAnnotation(DCCValue.class);
            String value = dccValue.value();
            if(StringUtils.isBlank(value)){
                throw new RuntimeException(field.getName() + " @DCCValue is not config value config case [isSwitch/isSwitch:1]");
            }
            String[] splits = value.split(Constants.SYMBOL_COLON);
            String key = properties.getKey(splits[0].trim());
            String defaultValue = splits.length == 2 ? splits[1] : null;
            String setValue = defaultValue;
            try{
                if(StringUtils.isBlank(defaultValue)){
                    throw new RuntimeException("dcc config error " + key + "is not null - 请配置默认值！");
                }
                // Redis操作，判断配置的key是否存在，不存在则创建键，存在则获取新值
                RBucket<String> bucket = redissonClient.getBucket(key);
                boolean exists = bucket.isExists();
                if(!exists){
                    bucket.set(defaultValue);
                }else{
                    setValue = bucket.get();
                }
                field.setAccessible(true);
                field.set(targetBeanObject, setValue);
                field.setAccessible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dccBeanGroup.put(key, targetBeanObject);
        }
        return bean;
    }

    @Override
    public void adjustAttributeValue(AttributeVO attributeVO) {
        String key = properties.getKey(attributeVO.getAttribute());
        String value = attributeVO.getValue();;
        RBucket<String> bucket = redissonClient.getBucket(key);
        boolean exists = bucket.isExists();
        if(!exists) return;
        bucket.set(attributeVO.getValue());
        Object objectBean = dccBeanGroup.get(key);
        if(null == objectBean)  return;
        Class<?> objBeanClass = objectBean.getClass();
        if(AopUtils.isAopProxy(objectBean)){
            objBeanClass = AopUtils.getTargetClass(objectBean);
        }
        try{
            Field field = objBeanClass.getDeclaredField(attributeVO.getAttribute());
            field.setAccessible(true);
            field.set(objectBean, value);
            field.setAccessible(false);
            log.info("DCC 节点监听，动态配置值 {} {}", key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
