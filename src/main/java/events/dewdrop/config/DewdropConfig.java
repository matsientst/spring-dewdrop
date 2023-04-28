package events.dewdrop.config;

import events.dewdrop.Dewdrop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DewdropConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public DewdropProperties dewdropProperties() {
        DewdropProperties properties = DewdropProperties.builder()
            .packageToScan("events.dewdrop")
            .connectionString("esdb://localhost:2113?tls=false")
            .streamPrefix("dewdrop")
            .create();
        return properties;
    }

    @Bean
    public Dewdrop dewdrop() {
        return DewdropSettings.builder()
            .properties(dewdropProperties())
            .dependencyInjectionAdapter(new DewdropDependencyInjection(applicationContext))
            .create()
            .start();
    }
}
