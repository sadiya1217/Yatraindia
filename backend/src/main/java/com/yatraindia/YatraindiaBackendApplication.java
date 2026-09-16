package com.yatraindia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@EntityScan(basePackages = "com.yatraindia")
@SpringBootApplication
public class YatraindiaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                YatraindiaBackendApplication.class,
                args);
    }
}