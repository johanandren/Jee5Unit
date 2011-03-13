package com.markatta.jee5unit.core;

import javax.annotation.Resource;
import javax.ejb.EJB;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johan
 */
public class InjectorTest {

    public class ClassA {

        @Resource
        private String string;

        private String otherString;

        public String getString() {
            return string;
        }

        @Resource
        public void setOtherString(String otherString) {
            this.otherString = otherString;
        }

        public String getOtherString() {
            return otherString;
        }
    }

    public class ClassB extends ClassA {
    }

    @Test
    public void testInjectField() {
        Injector instance = new Injector();

        ClassA bean = new ClassA();
        String toInject = "tadaa";
        instance.injectField(bean, Resource.class, String.class, toInject);
        assertSame(toInject, bean.getString());
        assertSame(toInject, bean.getOtherString());
    }

    @Test
    public void testInjectFieldWithNullFieldClass() {
        Injector instance = new Injector();

        ClassA bean = new ClassA();
        String toInject = "tadaa";
        instance.injectField(bean, Resource.class, null, toInject);
        assertSame(toInject, bean.getString());
        assertSame(toInject, bean.getOtherString());
    }

    @Test
    public void testClearByInjecting() {
        Injector instance = new Injector();

        ClassA bean = new ClassA();
        String toInject = "tadaa";
        instance.injectField(bean, Resource.class, null, toInject);
        assertSame(toInject, bean.getString());
        assertSame(toInject, bean.getOtherString());

        instance.injectField(bean, Resource.class, null, null);
        assertNull(bean.getString());
        assertNull(bean.getOtherString());

    }

    @Test
    public void testInjectFieldInSuperclass() {
        Injector instance = new Injector();

        ClassB bean = new ClassB();
        String toInject = "tadaa";
        instance.injectField(bean, Resource.class, String.class, toInject);
        assertSame(toInject, bean.getString());
        assertSame(toInject, bean.getOtherString());
    }

    @Test
    public void testContainsAnnotation() {
        Injector instance = new Injector();
        assertTrue(instance.containsAnnotation(ClassA.class, Resource.class));
        assertTrue(instance.containsAnnotation(ClassB.class, Resource.class));
        assertFalse(instance.containsAnnotation(ClassA.class, EJB.class));
    }
}
