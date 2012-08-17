package org.ssh.tool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.Fraction;
import org.junit.Test;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

public class MathTest {

    @Test
    //分子，分母
    public void testFraction(){
        String us = "23 31/37";
        Fraction fr = Fraction.getFraction(us);
        System.out.println(us + " :" + fr.doubleValue());
    }

    //http://code.google.com/p/aviator/wiki/User_Guide_zh
    @Test
    public void testAviator(){
        //直级计算
        Long result = (Long) AviatorEvaluator.execute("1+2+3");
        System.out.println(result);

        //编译计算
        String expression = "a-(b-c)>100";
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(expression);

        Map<String, Object> env = new HashMap<String, Object>();
        env.put("a", 100.3);
        env.put("b", 45);
        env.put("c", -199.100);

        // 执行表达式
        Boolean result2 = (Boolean) compiledExp.execute(env);
        System.out.println(result2);

        //另一个编译计算例子
        expression = "a-(b-c)";
        // 编译表达式
        compiledExp = AviatorEvaluator.compile(expression);

        env = new HashMap<String, Object>();
        env.put("a", 100.3);
        env.put("b", 45);
        env.put("c", -199.100);

        // 执行表达式
        Double result3 = (Double)compiledExp.execute(env);
        System.out.println(result3);
    }
}
