package cn.rainybyte.wrench.test.design.framework.biz.tree.factory;

import cn.rainybyte.wrench.design.framework.tree.StrategyHandler;
import cn.rainybyte.wrench.test.design.framework.biz.tree.node.RootNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 工厂节点。提供内聚的上下文类对象，DynamicContext可以自主的填充流程节点中下游数据，被下游链路使用。
 */
@Service
public class DefaultStrategyFactory {

    private final RootNode rootNode;

    public DefaultStrategyFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    public StrategyHandler<String, DynamicContext, String> strategyHandler(){
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{

        private int level;

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value){
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key){
            return (T) dataObjects.get(key);
        }

    }

}
