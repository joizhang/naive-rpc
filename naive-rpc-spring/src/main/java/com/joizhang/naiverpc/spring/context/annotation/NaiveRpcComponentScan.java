package com.joizhang.naiverpc.spring.context.annotation;

import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NaiveRpcComponentScanRegistrar.class)
public @interface NaiveRpcComponentScan {

    /**
     * Alias for the basePackages() attribute.
     *
     * @return the base packages to scan
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated @Service classes.
     *
     * @return the base packages to scan
     */
    String[] basePackages() default {};

    /**
     * @return classes from the base packages to scan
     */
    Class<?>[] basePackageClasses() default {};
}
