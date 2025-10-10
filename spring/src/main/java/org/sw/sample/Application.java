package org.sw.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot 메인 애플리케이션
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * ObjectMapper Bean 등록
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
