package ru.draen.hps.common.jpadao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.draen.hps.common.jpadao.transaction.StreamTransactionTemplate;

@Configuration
public class JpaDaoConfiguration {
    @Bean
    public StreamTransactionTemplate streamTransactionTemplate(PlatformTransactionManager transactionManager) {
        return new StreamTransactionTemplate(transactionManager);
    }
}
