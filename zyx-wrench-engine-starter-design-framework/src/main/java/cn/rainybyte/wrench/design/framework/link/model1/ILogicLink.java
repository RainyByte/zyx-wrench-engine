package cn.rainybyte.wrench.design.framework.link.model1;

/**
 * 规则责任链接口，受理业务逻辑
 */
public interface ILogicLink<T, D, R> extends ILogicChainArmory<T, D, R> {

    R apply(T requestParameter, D dynamicContext) throws Exception;

}
