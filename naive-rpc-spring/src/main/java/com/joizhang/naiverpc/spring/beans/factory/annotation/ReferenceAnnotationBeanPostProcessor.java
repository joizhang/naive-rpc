package com.joizhang.naiverpc.spring.beans.factory.annotation;

import com.joizhang.naiverpc.spring.annotation.NaiveRpcReference;
import com.joizhang.naiverpc.utils.ArrayUtils;
import com.joizhang.naiverpc.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.Member;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class ReferenceAnnotationBeanPostProcessor extends AbstractAnnotationBeanPostProcessor
        implements ApplicationContextAware, BeanFactoryPostProcessor {

    public static final String BEAN_NAME = "referenceAnnotationBeanPostProcessor";

    private static final String ID_ATTRIBUTE = "id";
    private static final String INTERFACE_CLASS_ATTRIBUTE = "interfaceClass";
    private static final String INTERFACE_NAME_ATTRIBUTE = "interfaceName";
    private static final String REFERENCE_PROPS_ATTRIBUTE = "referenceProps";
    private static final String OBJECT_TYPE_ATTRIBUTE = "objectType";

    private final ConcurrentMap<InjectionMetadata.InjectedElement, String> injectedFieldReferenceBeanCache =
            new ConcurrentHashMap<>(CACHE_SIZE);

    private final ConcurrentMap<InjectionMetadata.InjectedElement, String> injectedMethodReferenceBeanCache =
            new ConcurrentHashMap<>(CACHE_SIZE);

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public ReferenceAnnotationBeanPostProcessor() {
        super(NaiveRpcReference.class);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanType = beanFactory.getType(beanName);
            if (beanType != null) {
                AnnotatedInjectionMetadata metadata = findInjectionMetadata(beanName, beanType, null);
                try {
                    prepareInjection(metadata);
                } catch (BeansException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IllegalStateException("Prepare reference injection element failed", e);
                }
            }
        }
    }

    protected void prepareInjection(AnnotatedInjectionMetadata metadata) throws BeansException {
        try {
            for (AnnotatedFieldElement fieldElement : metadata.getFieldElements()) {
                if (fieldElement.injectedObject != null) {
                    continue;
                }
                Class<?> injectedType = fieldElement.field.getType();
                AnnotationAttributes attributes = fieldElement.attributes;
                String referenceBeanName = registerReferenceBean(
                        fieldElement.getPropertyName(),
                        injectedType,
                        attributes,
                        fieldElement.field
                );

                fieldElement.injectedObject = referenceBeanName;
                injectedFieldReferenceBeanCache.put(fieldElement, referenceBeanName);
            }

            for (AnnotatedMethodElement methodElement : metadata.getMethodElements()) {
                if (methodElement.injectedObject != null) {
                    continue;
                }
                Class<?> injectedType = methodElement.getInjectedType();
                AnnotationAttributes attributes = methodElement.attributes;
                String referenceBeanName = registerReferenceBean(
                        methodElement.getPropertyName(),
                        injectedType,
                        attributes,
                        methodElement.method
                );

                methodElement.injectedObject = referenceBeanName;
                injectedMethodReferenceBeanCache.put(methodElement, referenceBeanName);
            }
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException("prepare reference annotation failed", e);
        }
    }

    public String registerReferenceBean(String propertyName,
                                        Class<?> injectedType,
                                        Map<String, Object> attributes,
                                        Member member) throws BeansException {
        boolean renameable = true;
        String referenceBeanName = getAttribute(attributes, ID_ATTRIBUTE);
        if (hasText(referenceBeanName)) {
            renameable = false;
        } else {
            referenceBeanName = propertyName;
        }

        String checkLocation = "Please check " + member.toString();

        String interfaceName = getInterfaceName(attributes, injectedType);
        if (StringUtils.isBlank(interfaceName)) {
            throw new BeanCreationException("Need to specify the 'interfaceName' or 'interfaceClass' attribute of '@NaiveRpcReference'. " + checkLocation);
        }

        boolean isContains;
        if ((isContains = beanDefinitionRegistry.containsBeanDefinition(referenceBeanName)) || beanDefinitionRegistry.isAlias(referenceBeanName)) {
            String preReferenceBeanName = referenceBeanName;
            if (!isContains) {
                String[] aliases = beanDefinitionRegistry.getAliases(referenceBeanName);
                if (ArrayUtils.isNotEmpty(aliases)) {
                    for (String alias : aliases) {
                        if (beanDefinitionRegistry.containsBeanDefinition(alias)) {
                            preReferenceBeanName = alias;
                            break;
                        }
                    }
                }
            }
            BeanDefinition prevBeanDefinition = beanDefinitionRegistry.getBeanDefinition(preReferenceBeanName);
            String prevBeanType = prevBeanDefinition.getBeanClassName();
            String prevBeanDesc = referenceBeanName + "[" + prevBeanType + "]";
            String newBeanDesc = referenceBeanName + "[" + interfaceName + "]";

            if (isReferenceBean(prevBeanDefinition)) {
                String prevInterfaceName = (String) prevBeanDefinition.getAttribute(INTERFACE_NAME_ATTRIBUTE);
                if (StringUtils.isEquals(prevInterfaceName, interfaceName)) {
                    return referenceBeanName;
                }
                prevBeanDesc = referenceBeanName + "[" + prevInterfaceName + "]";
            }

            if (!renameable) {
                throw new BeanCreationException("Already exists another bean definition with the same bean name [" + referenceBeanName + "], " +
                        "but cannot rename the reference bean name (specify the id attribute or java-config bean), " +
                        "please modify the name of one of the beans: " +
                        "prev: " + prevBeanDesc + ", new: " + newBeanDesc + ". " + checkLocation);
            }

            int index = 2;
            String newReferenceBeanName = null;
            while (newReferenceBeanName == null || beanDefinitionRegistry.containsBeanDefinition(newReferenceBeanName)
                    || beanDefinitionRegistry.isAlias(newReferenceBeanName)) {
                newReferenceBeanName = referenceBeanName + "#" + index;
                index++;
            }
            newBeanDesc = newReferenceBeanName + "[" + interfaceName + "]";

            log.warn("Already exists another bean definition with the same bean name [" + referenceBeanName + "], " +
                    "rename reference bean to [" + newReferenceBeanName + "]. " +
                    "It is recommended to modify the name of one of the beans to avoid injection problems. " +
                    "prev: " + prevBeanDesc + ", new: " + newBeanDesc + ". " + checkLocation);
            referenceBeanName = newReferenceBeanName;
        }

        Class<?> interfaceClass = getInterfaceClass(attributes, injectedType);

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName(injectedType.getName());
        beanDefinition.getPropertyValues().add(ID_ATTRIBUTE, referenceBeanName);
        beanDefinition.setAttribute(REFERENCE_PROPS_ATTRIBUTE, attributes);
        beanDefinition.setAttribute(INTERFACE_CLASS_ATTRIBUTE, interfaceClass);
        beanDefinition.setAttribute(INTERFACE_NAME_ATTRIBUTE, interfaceName);

        GenericBeanDefinition targetDefinition = new GenericBeanDefinition();
        targetDefinition.setBeanClass(interfaceClass);
        beanDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, referenceBeanName + "_decorated"));

        beanDefinition.setAttribute(OBJECT_TYPE_ATTRIBUTE, interfaceClass);

        beanDefinitionRegistry.registerBeanDefinition(referenceBeanName, beanDefinition);
        log.info("Register reference bean: " + referenceBeanName + " for interface " + interfaceName + " at " + member);
        return referenceBeanName;
    }

    private String getInterfaceName(Map<String, Object> attributes, Class<?> injectedType) {
        String interfaceName = (String) attributes.get(INTERFACE_NAME_ATTRIBUTE);
        if (StringUtils.isBlank(interfaceName)) {
            Class<?> interfaceClass = getInterfaceClass(attributes, injectedType);
            if (interfaceClass != null && interfaceClass != void.class) {
                interfaceName = interfaceClass.getName();
            } else if (injectedType != null) {
                interfaceName = injectedType.getName();
            }
        }
        return interfaceName;
    }

    private Class<?> getInterfaceClass(Map<String, Object> attributes, Class<?> injectedType) {
        Class<?> interfaceClass = (Class<?>) attributes.get(INTERFACE_CLASS_ATTRIBUTE);
        if (interfaceClass == null || interfaceClass == void.class) {
            return injectedType;
        }
        return interfaceClass;
    }

    private boolean isReferenceBean(BeanDefinition beanDefinition) {
        return beanDefinition.getAttribute(INTERFACE_CLASS_ATTRIBUTE) != null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(Map<String, Object> attributes, String key) {
        return (T) attributes.get(key);
    }

    @Override
    protected Object doGetInjectedBean(AnnotationAttributes attributes,
                                       Object bean,
                                       String beanName,
                                       Class<?> injectedType,
                                       AnnotatedInjectElement injectedElement) throws Exception {
        if (injectedElement.injectedObject == null) {
            throw new IllegalStateException("The AnnotatedInjectElement of @NaiveRpcReference should be inited before injection");
        }
        return getBeanFactory().getBean((String) injectedElement.injectedObject);
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition,
                                                Class<?> beanType,
                                                String beanName) {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
