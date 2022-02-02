package com.example.common.util;

import com.sun.org.apache.xpath.internal.operations.Variable;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @Author xiapeng
 * @Date: 2022/02/03/12:01 上午
 * @Description:
 */
public class MethodUtil {
    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final ThreadLocal<StandardEvaluationContext> SPEL_CONTEXT = ThreadLocal.withInitial(StandardEvaluationContext::new);
    private static final ExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 获取spel表达式的值
     *
     * @param args       方法参数列表
     * @param method     方法
     * @param expression 表达式
     * @param clazz      目标class
     * @param <T>        目标类型
     * @return 计算后的值
     */
    public static <T> T getSPEL(Object[] args, Method method, String expression, Class<T> clazz) {
        // spel上下文
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        StandardEvaluationContext context = SPEL_CONTEXT.get();
        if (parameterNames != null && parameterNames.length > 0) {
            //将方法参数放入spel上下文中
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        //context.setVariables(null);
        context.setBeanResolver(BeanUtil.getBeanFactoryResolver());
        return SPEL_EXPRESSION_PARSER.parseExpression(expression).getValue(context, clazz);
    }

    public static void setSpelContextVariable(String name, Object value) {
        SPEL_CONTEXT.get().setVariable(name, value);
    }
}
