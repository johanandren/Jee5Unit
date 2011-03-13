package com.markatta.jee5unit.runners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that makes Jee5Unit provide a JNDIContext to the unittests
 * @author johan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface UsesJNDI {
    /**
     * Set to true to make the test entity manager become published into JNDI
     * as </code>entityManagerName</code>
     */
    boolean publishEntityManager() default false;
    /**
     * Set to a specific name to publish the entity manager into
     */
    String entityManagerName() default "EntityManager";
}
