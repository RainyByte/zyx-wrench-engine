package cn.rainybyte.wrench.design.framework.tree;

/**
 *  策略处理器
 */
public interface StrategyHandler<T, D, R> {

    StrategyHandler DEFAULT = (T, D) -> null;

    /**
     * 受理执行的业务流程。每个业务流程执行时，如果有数据是从前面节点到后面节点要使用的，那么可以填充到dynamicContext上下文当中。
     *
     * @param requestParameter 入参
     * @param dynamicContext   上下文
     * @return 返参
     */
    R apply(T requestParameter, D dynamicContext) throws Exception;

}
