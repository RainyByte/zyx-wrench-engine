package cn.rainybyte.wrench.test.design.framework.biz.tree.node;

import cn.rainybyte.wrench.design.framework.tree.StrategyHandler;
import cn.rainybyte.wrench.test.design.framework.biz.tree.AbstractXxxSupport;
import cn.rainybyte.wrench.test.design.framework.biz.tree.factory.DefaultStrategyFactory;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberLevel1Node extends AbstractXxxSupport {

    @Override
    protected String doApply(String requestParameter, DefaultStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("[级别节点-1]规则决策树 userId:{}", requestParameter);
        return "level-1" + JSON.toJSONString(dynamicContext);
    }

    @Override
    public StrategyHandler<String, DefaultStrategyFactory.DynamicContext, String> get(String requestParameter, DefaultStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

}
