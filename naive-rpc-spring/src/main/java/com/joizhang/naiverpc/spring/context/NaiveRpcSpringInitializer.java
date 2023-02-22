package com.joizhang.naiverpc.spring.context;

import com.joizhang.naiverpc.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import com.joizhang.naiverpc.spring.beans.factory.annotation.ServicePackagesHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NaiveRpcSpringInitializer {

    private static final Map<BeanDefinitionRegistry, NaiveRpcSpringInitContext> contextMap = new ConcurrentHashMap<>();

    private NaiveRpcSpringInitializer() {
    }

    public static void initialize(BeanDefinitionRegistry registry) {
        if (contextMap.putIfAbsent(registry, new NaiveRpcSpringInitContext()) != null) {
            return;
        }
        NaiveRpcSpringInitContext context = contextMap.get(registry);
        // find beanFactory
        ConfigurableListableBeanFactory beanFactory = findBeanFactory(registry);
        // init context
        initContext(context, registry, beanFactory);
    }

    private static ConfigurableListableBeanFactory findBeanFactory(BeanDefinitionRegistry registry) {
        ConfigurableListableBeanFactory beanFactory;
        if (registry instanceof ConfigurableListableBeanFactory) {
            beanFactory = (ConfigurableListableBeanFactory) registry;
        } else if (registry instanceof GenericApplicationContext) {
            GenericApplicationContext genericApplicationContext = (GenericApplicationContext) registry;
            beanFactory = genericApplicationContext.getBeanFactory();
        } else {
            throw new IllegalStateException("Can not find Spring BeanFactory from registry: " + registry.getClass().getName());
        }
        return beanFactory;
    }

    private static void initContext(NaiveRpcSpringInitContext context,
                                    BeanDefinitionRegistry registry,
                                    ConfigurableListableBeanFactory beanFactory) {
        context.setRegistry(registry);
        context.setBeanFactory(beanFactory);
        // bind initialization context to spring context
        beanFactory.registerSingleton(context.getClass().getName(), context);
        // mark context as bound
        context.markAsBound();
        // register common beans
        registerCommonBeans(registry);
    }

    private static void registerCommonBeans(BeanDefinitionRegistry registry) {
        registerInfrastructureBean(registry, ServicePackagesHolder.BEAN_NAME, ServicePackagesHolder.class);

        // Register @NaiveRpcReference Annotation Bean Processor as an infrastructure Bean
        registerInfrastructureBean(registry,
                ReferenceAnnotationBeanPostProcessor.BEAN_NAME, ReferenceAnnotationBeanPostProcessor.class);
    }

    /**
     * Register Infrastructure Bean
     *
     * @param beanDefinitionRegistry {@link BeanDefinitionRegistry}
     * @param beanType               the type of bean
     * @param beanName               the name of bean
     * @return if it's a first time to register, return <code>true</code>, or <code>false</code>
     */
    private static boolean registerInfrastructureBean(BeanDefinitionRegistry beanDefinitionRegistry,
                                                      String beanName,
                                                      Class<?> beanType) {

        boolean registered = false;

        if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
            registered = true;

            if (log.isDebugEnabled()) {
                log.debug("The Infrastructure bean definition [" + beanDefinition
                        + "with name [" + beanName + "] has been registered.");
            }
        }

        return registered;
    }
}
