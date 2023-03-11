package com.joizhang.naiverpc.spring.beans.factory.annotation;

import com.joizhang.naiverpc.spring.annotation.NaiveRpcService;
import com.joizhang.naiverpc.utils.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ServiceAnnotationUtils {

    private ServiceAnnotationUtils() {
    }

    /**
     * Resolve the service interface name from @Service annotation attributes.
     *
     * @param serviceAnnotationAttributes annotation attributes of {@link NaiveRpcService}
     * @param beanClass                   the default class of interface
     * @return the interface name if found
     */
    public static String resolveInterfaceName(Map<String, Object> serviceAnnotationAttributes,
                                              Class<?> beanClass) {
        // 1. get from NaiveRpcService.interfaceName()
        String interfaceClassName = getAttribute(serviceAnnotationAttributes, "interfaceName");
        if (StringUtils.hasText(interfaceClassName)) {
            return interfaceClassName;
        }
        // 2. get from NaiveRpcService.interfaceClass()
        Class<?> interfaceClass = getAttribute(serviceAnnotationAttributes, "interfaceClass");
        if (interfaceClass == null || void.class.equals(interfaceClass)) { // default or set void.class for purpose.
            interfaceClass = null;
        }
        // 3. get from annotation element type, ignore GenericService
        if (interfaceClass == null && beanClass != null) {
            Class<?>[] allInterfaces = ClassUtils.getAllInterfacesForClass(beanClass);
            if (allInterfaces.length > 0) {
                interfaceClass = allInterfaces[0];
            }
        }
        Assert.notNull(interfaceClass, "@NaiveRpcService interface class must be present!");
        Assert.isTrue(interfaceClass.isInterface(), "The annotated type must be an interface!");
        return interfaceClass.getName();
    }

    /**
     * Get the attribute value
     *
     * @param attributes    the annotation attributes
     * @param attributeName the name of attribute
     * @return the attribute value if found
     */
    public static <T> T getAttribute(Map<String, Object> attributes, String attributeName) {
        return getAttribute(attributes, attributeName, false);
    }

    public static <T> T getAttribute(Map<String, Object> attributes, String attributeName, boolean required) {
        T value = getAttribute(attributes, attributeName, null);
        if (required && value == null) {
            throw new IllegalStateException("The attribute['" + attributeName + "] is required!");
        }
        return value;
    }

    /**
     * Get the attribute value with default value
     *
     * @param attributes    the annotation attributes
     * @param attributeName the name of attribute
     * @param defaultValue  the default value of attribute
     * @return the attribute value if found
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(Map<String, Object> attributes, String attributeName, T defaultValue) {
        T value = (T) attributes.get(attributeName);
        return value == null ? defaultValue : value;
    }


    /**
     * Filter default value of Annotation type
     *
     * @param annotationType       annotation type from {@link Annotation#annotationType()}
     * @param annotationAttributes annotation attributes
     * @return filtered annotation attributes
     */
    public static Map<String, Object> filterDefaultValues(Class<? extends Annotation> annotationType,
                                                          Map<String, Object> annotationAttributes) {
        Map<String, Object> filterDefaultValues = new LinkedHashMap<>(annotationAttributes.size());
        annotationAttributes.forEach((key, value) -> {
            if (!Objects.deepEquals(value, getDefaultValue(annotationType, key))) {
                filterDefaultValues.put(key, value);
            }
        });
        // remove void class, compatible with spring 3.x
        Object interfaceClassValue = filterDefaultValues.get("interfaceClass");
        if ("void".equals(interfaceClassValue)) {
            filterDefaultValues.remove("interfaceClass");
        }
        return filterDefaultValues;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getDefaultValue(Class<? extends Annotation> annotationType, String key) {
        Method method = null;
        try {
            method = annotationType.getDeclaredMethod(key);
        } catch (NoSuchMethodException ignored) {
        }
        return method == null ? null : (T) method.getDefaultValue();
    }
}
