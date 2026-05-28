package com.example.auth.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
    }

    // Makes entityManagerFactory wait for Flyway to finish migrations
    @Bean
    public static BeanFactoryPostProcessor flywayDependencyPostProcessor() {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
                if (bf.containsBeanDefinition("entityManagerFactory")) {
                    BeanDefinition def = bf.getBeanDefinition("entityManagerFactory");
                    String[] deps = def.getDependsOn();
                    def.setDependsOn(deps == null
                            ? new String[]{"flyway"}
                            : Stream.concat(Arrays.stream(deps), Stream.of("flyway")).toArray(String[]::new));
                }
            }
        };
    }
}
