package events.dewdrop.config;

import org.springframework.context.ApplicationContext;

public class DewdropDependencyInjection implements DependencyInjectionAdapter {
    private final ApplicationContext applicationContext;

    public DewdropDependencyInjection(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getBean(Class<?> clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}

