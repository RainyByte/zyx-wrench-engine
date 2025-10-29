package cn.rainybyte.wrench.design.framework.tree;

/**
 *  策略映射器
 */
public interface StrategyMapper<T, D, R> {

    /**
     * 获取每一个要执行的节点，相当于一个流程走完进入到下一个流程的过程
     *
     * @param requestParameter 入参
     * @param dynamicContext   上下文
     * @return 返参
     * @throws Exception 异常
     */
    StrategyHandler<T, D, R> get(T requestParameter, D dynamicContext) throws Exception;

}
