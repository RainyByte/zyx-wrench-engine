package cn.rainybyte.wrench.dynamic.config.center.domain.service;

import cn.rainybyte.wrench.dynamic.config.center.domain.model.valobj.AttributeVO;

/**
 * 动态配置中心服务接口
 */
public interface IDynamicConfigCenterService {

    /**
     * 对Spring容器中的bean进行代理，扫描并注入@DCCValue注解的属性，实现属性动态赋值
     */
    Object proxyObject(Object bean);

    /**
     * 接收到配置变更事件时，动态调整目标bean的属性值
     */
    void adjustAttributeValue(AttributeVO attributeVO);

}
