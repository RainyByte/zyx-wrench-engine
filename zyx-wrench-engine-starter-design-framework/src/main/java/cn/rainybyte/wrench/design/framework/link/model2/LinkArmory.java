package cn.rainybyte.wrench.design.framework.link.model2;

import cn.rainybyte.wrench.design.framework.link.model2.chain.BusinessLinkedList;
import cn.rainybyte.wrench.design.framework.link.model2.handler.ILogicHandler;

/**
 * 链路装配
 */
public class LinkArmory<T, D extends DynamicContext, R> {

    private final BusinessLinkedList<T, D, R> logicLink;

    @SafeVarargs
    public LinkArmory(String linkName, ILogicHandler<T, D, R>... logicHandlers){
        logicLink = new BusinessLinkedList<T, D, R>(linkName);
        for(ILogicHandler<T, D, R> logicHandler : logicHandlers){
            logicLink.add(logicHandler);
        }
    }

    public BusinessLinkedList<T, D, R>  getLogicLink(){
        return logicLink;
    }

}
