package cn.rainybyte.wrench.dynamic.config.center.config;

import cn.rainybyte.wrench.dynamic.config.center.domain.model.valobj.AttributeVO;
import cn.rainybyte.wrench.dynamic.config.center.domain.service.DynamicConfigCenterService;
import cn.rainybyte.wrench.dynamic.config.center.domain.service.IDynamicConfigCenterService;
import cn.rainybyte.wrench.dynamic.config.center.listener.DynamicConfigCenterAdjustListener;
import cn.rainybyte.wrench.dynamic.config.center.types.common.Constants;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        DynamicConfigCenterAutoProperties.class,
        DynamicConfigCenterRegisterAutoProperties.class
})
public class DynamicConfigCenterRegisterAutoConfig {
    private final Logger log = LoggerFactory.getLogger(DynamicConfigCenterRegisterAutoConfig.class);

    @Bean("zyxWrenchRedissonClient")
    public RedissonClient redissonClient(DynamicConfigCenterRegisterAutoProperties properties) {
        Config config = new Config();

        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMidIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive());

        RedissonClient redissonClient = Redisson.create(config);

        log.info("zyx-wrench，注册器（redis）链接初始化完成。{} {} {}",
                properties.getHost(), properties.getPoolSize(), !redissonClient.isShutdown());

        return redissonClient;
    }

    @Bean
    public IDynamicConfigCenterService dynamicConfigCenterService(DynamicConfigCenterAutoProperties dynamicConfigCenterAutoProperties, RedissonClient zyxWrenchRedissonClient){
        return new DynamicConfigCenterService(dynamicConfigCenterAutoProperties, zyxWrenchRedissonClient);
    }

    @Bean
    public DynamicConfigCenterAdjustListener dynamicConfigCenterAdjustListener(IDynamicConfigCenterService dynamicConfigCenterService){
        return new DynamicConfigCenterAdjustListener(dynamicConfigCenterService);
    }

    @Bean(name = "dynamicConfigCenterRedisTopic")
    public RTopic threadPoolConfigAdjustListener(DynamicConfigCenterAutoProperties autoProperties,
                                                 RedissonClient client,
                                                 DynamicConfigCenterAdjustListener listener){
        RTopic topic = client.getTopic(Constants.getTopic(autoProperties.getSystem()));
        topic.addListener(AttributeVO.class, listener);
        return topic;
    }

}
