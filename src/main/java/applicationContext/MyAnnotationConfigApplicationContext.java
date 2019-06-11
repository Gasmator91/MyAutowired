package applicationContext;

import annotations.MyAutowired;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class MyAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {

    private Map<String, String> map = beanBounds();

    
    public MyAnnotationConfigApplicationContext() {
    }

    public MyAnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        super(annotatedClasses);
    }

    public MyAnnotationConfigApplicationContext(String... basePackages) {
        super(basePackages);
    }

    public MyAnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }


    private Map<String,String> beanBounds() {
        Map<String, String> map = new HashMap<>();
        String[] str = this.getBeanDefinitionNames();
        for (String arg : str) {
            if (arg != null) {
                Class cl = this.getType(arg);
                if (cl.isAnnotationPresent(Qualifier.class)) {
                    Qualifier anno = (Qualifier) cl.getAnnotation(Qualifier.class);
                    map.put(arg, anno.value());
                }
            }
        }
        return map;
    }

    private  String getBeanNameByQualifier(Map<String,String> map, String obj){
        for (Map.Entry<String,String> entry : map.entrySet()) {
            if (obj.equals(entry.getValue()))
                return entry.getKey();
        }
        return null;
    }

    private void fieldAutowired(Field[] fields, Object bean){
        for(Field field: fields) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                field.setAccessible(true);
                try {
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        String value = field.getAnnotation(Qualifier.class).value();
                        Object object = this.getBean(getBeanNameByQualifier(map,value));
                        field.set(bean, object);
                    }
                      else
                        field.set(bean, this.getBean(field.getType()));

                } catch ( NoSuchFieldError | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void methodAutowired(Method[] methods, Object bean){
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyAutowired.class)) {
                Object[] paramQualifiers = new Object[method.getParameterCount()];
                int i = 0;
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.isAnnotationPresent(Qualifier.class)) {
                        String value = parameter.getAnnotation(Qualifier.class).value();
                        paramQualifiers[i++] = this.getBean(getBeanNameByQualifier(map,value));
                    }
                    else
                        paramQualifiers[i++] = this.getBean(parameter.getType());
                }

                try {
                    method.setAccessible(true);
                    method.invoke(bean,paramQualifiers);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    private void setAutowiredItem(Object bean){
        Field[] fields = bean.getClass().getDeclaredFields();
        Method[] methods = bean.getClass().getDeclaredMethods();
        fieldAutowired(fields,bean);
        methodAutowired(methods, bean);
    }


    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = super.getBean(name);
        setAutowiredItem(bean);
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        Object bean = super.getBean(requiredType);
        setAutowiredItem(bean);
        return (T) bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        Object bean = super.getBean(name, requiredType);
        setAutowiredItem(bean);
        return (T) bean;
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        Object bean = super.getBean(name, args);
        setAutowiredItem(bean);
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        Object bean = super.getBean(requiredType, args);
        setAutowiredItem(bean);
        return (T) bean;
    }


}
