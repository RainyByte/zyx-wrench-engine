package cn.rainybyte.wrench.design.framework.link.model1;

/**
 * 装配链，提供添加节点和获取节点方法
 */
public interface ILogicChainArmory<T, D, R> {

    ILogicLink<T, D, R> next();

    ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next);

}
