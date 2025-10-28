package cn.rainybyte.wrench.dynamic.config.center.listener;

import cn.rainybyte.wrench.dynamic.config.center.domain.model.valobj.AttributeVO;
import cn.rainybyte.wrench.dynamic.config.center.domain.service.IDynamicConfigCenterService;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听来自Redis发布订阅通道的配置变更消息，驱动服务实现属性的动态变更
 */
public class DynamicConfigCenterAdjustListener implements MessageListener<AttributeVO> {

    private final Logger log = LoggerFactory.getLogger(DynamicConfigCenterAdjustListener.class);
    private final IDynamicConfigCenterService dynamicConfigCenterService;

    public DynamicConfigCenterAdjustListener(IDynamicConfigCenterService dynamicConfigCenterService) {
        this.dynamicConfigCenterService = dynamicConfigCenterService;
    }

    @Override
    public void onMessage(CharSequence charSequence, AttributeVO attributeVO) {
        // 当Redis发布订阅通道收到新的AttributeVO消息时，onMessage会被自动调用
        try {
            // 记录收到的属性名和新值，便于追踪和排查
            log.info("zyx-wrench-engine dcc config attribute:{} value:{}", attributeVO.getAttribute(), attributeVO.getValue());
            // 将变更请求委托给服务层处理
            dynamicConfigCenterService.adjustAttributeValue(attributeVO);
        } catch (Exception e) {
            log.error("zyx-wrench-engine dcc config attribute:{} value:{}", attributeVO.getAttribute(), attributeVO.getValue());
        }
    }
}
