package common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringUtils {
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringUtils.context = context;
    }

    public static void autowireBean(Object bean, boolean throwExceptionIfSpringNotInitialized) {
        if (context == null && throwExceptionIfSpringNotInitialized) {
            throw new RuntimeException("Spring context has not been set, probably SpringInjector is not a spring bean");
        }

        if (context != null) {
            context.getAutowireCapableBeanFactory().autowireBean(bean);
        }
    }

    public static void autowireBean(Object bean) {
        autowireBean(bean, false);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
}
