package com.markatta.jee5unit.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.persistence.PersistenceContext;

/**
 * Utility class for injecting stuff in ejbs
 * @author johan
 */
public class Injector {

    /**
     * Inject toInject into a field that is annotated with annotationClass and
     * is of type fieldClass on bean. Will go through fields of all superclasses
     * and inject them as well.
     *
     * @param fieldClass use <code>null</code> if field class is not important
     */
    public void injectField(Object bean, Class annotationClass, Class fieldClass, Object toInject) {

        Class beanClass = bean.getClass();

        while (beanClass != Object.class) {
            // first: check all fields
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass) && (fieldClass == null || field.getType() == fieldClass)) {

                    try {
                        field.setAccessible(true);
                        field.set(bean, toInject);
                    } catch (Exception ex) {
                        throw new RuntimeException("Error injecting " +
                                toInject.getClass().getSimpleName() +
                                " into field " + field + " of bean " + bean, ex);
                    }
                }
            }

            // then we check all setters
            for (Method method : beanClass.getMethods()) {
                // only handles annotation on the setter
                if (method.isAnnotationPresent(annotationClass) && (fieldClass == null || method.getParameterTypes()[0] == fieldClass)) {
                    if (!method.getName().startsWith("set")) {
                        throw new UnsupportedOperationException("Injection of annotated field only supported when annotation is on setter.");
                    }
                    try {
                        method.invoke(bean, toInject);
                    } catch (Exception ex) {
                        throw new RuntimeException("Error injecting " +
                                toInject.getClass().getSimpleName() +
                                " with method " + method + " of bean " + bean, ex);
                    }
                }
            }

            // do the same with the superclass
            beanClass = beanClass.getSuperclass();


        }
    }

    /**
     *
     * @param beanClass The class to check
     * @param annotationClass The annotation to look for
     * @return <code>true</code> if the annotation is present on any field or method in <code>beanClass</code> or any
     *         of its superclasses
     */
    public boolean containsAnnotation(Class beanClass, Class annotationClass) {

        while (beanClass != Object.class) {
            // first: check all fields
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    return true;
                }
            }

            // then we check all setters
            for (Method method : beanClass.getMethods()) {
                // only handles annotation on the setter
                if (method.isAnnotationPresent(annotationClass)) {
                    return true;
                }
            }

            // do the same with the superclass
            beanClass = beanClass.getSuperclass();
        }
        return false;
    }
}
