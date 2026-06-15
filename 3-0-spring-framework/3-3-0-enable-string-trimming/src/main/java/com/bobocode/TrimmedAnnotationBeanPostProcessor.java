package com.bobocode;

import com.bobocode.annotation.Trimmed;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        boolean hasTrimmedParams = Arrays.stream(bean.getClass().getDeclaredMethods())
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .anyMatch(p -> p.isAnnotationPresent(Trimmed.class));

        if (!hasTrimmedParams) {
            return bean;
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(Trimmed.class) && args[i] instanceof String) {
                    args[i] = ((String) args[i]).trim();
                }
            }
            return proxy.invokeSuper(obj, args);
        });

        return enhancer.create();
    }
}
