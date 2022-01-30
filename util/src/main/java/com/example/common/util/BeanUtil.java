package com.example.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.stereotype.Component;

/**
 * @Author xiapeng
 * @Date: 2022/01/30/11:35 上午
 * @Description: bean工具类
 */
@Component("beanUtil")
public class BeanUtil implements BeanFactoryPostProcessor, ApplicationContextAware, ApplicationEventPublisherAware {
    private static ApplicationEventPublisher applicationEventPublisher;
    private static ConfigurableListableBeanFactory beanFactory;
    private static BeanFactoryResolver beanFactoryResolver;
    private static ApplicationContext applicationContext;

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static BeanFactoryResolver getBeanFactoryResolver() {
        return beanFactoryResolver;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private static void init(BeanFactory beanFactory) {
        BeanUtil.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        beanFactoryResolver = new BeanFactoryResolver(beanFactory);
    }

    private static void setEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        BeanUtil.applicationEventPublisher = applicationEventPublisher;
    }

    private static void setContext(ApplicationContext applicationContext) {
        BeanUtil.applicationContext = applicationContext;
    }

    /**
     * 从ApplicationContext获取指定Class的bean
     *
     * @param cls bean类型
     * @param <T> 形参类型
     * @return bean
     */
    public static <T> T getBean(Class<T> cls) {
        try {
            return applicationContext.getBean(cls);
        } catch (NoUniqueBeanDefinitionException e) {
            return null;
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 从ApplicationContext获取指定Class的bean
     *
     * @param id  bean的id
     * @param cls bean的类型
     * @param <T> 形参类型
     * @return 对应的bean
     */
    public static <T> T getBean(String id, Class<T> cls) {
        try {
            return applicationContext.getBean(cls);
        } catch (NoUniqueBeanDefinitionException e) {
            return null;
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 发送spring event
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        init(beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setContext(applicationContext);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        setEventPublisher(applicationEventPublisher);
    }
}
