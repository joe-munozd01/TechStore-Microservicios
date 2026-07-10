package com.example.mssoporte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsSoporteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSoporteApplication.class, args);
    }
}
