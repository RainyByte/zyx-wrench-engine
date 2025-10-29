package cn.rainybyte.wrench.test.design.framework;

import cn.rainybyte.wrench.design.framework.link.model1.ILogicLink;
import cn.rainybyte.wrench.test.design.framework.biz.rule01.factory.Rule01TradeRuleFactory;
import cn.rainybyte.wrench.test.design.framework.biz.rule02.factory.Rule02TradeRuleFactory;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Link01Test {

    @Resource
    public Rule01TradeRuleFactory rule01TradeRuleFactory;

    @Test
    public void test_model01_test() throws Exception{
        ILogicLink<String, Rule02TradeRuleFactory.DynamicContext, String> logicLink = rule01TradeRuleFactory.openLogicLink();
        String logic = logicLink.apply("123", new Rule02TradeRuleFactory.DynamicContext());
        log.info("测试结果: {}", JSON.toJSONString(logic));
    }
}
