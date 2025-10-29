package cn.rainybyte.wrench.test.design.framework.biz.tree;

import cn.rainybyte.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import cn.rainybyte.wrench.test.design.framework.biz.tree.factory.DefaultStrategyFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractXxxSupport extends AbstractMultiThreadStrategyRouter<String, DefaultStrategyFactory.DynamicContext, String> {

    @Override
    protected void multiThread(String requestParameter, DefaultStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        // todo
    }

}
