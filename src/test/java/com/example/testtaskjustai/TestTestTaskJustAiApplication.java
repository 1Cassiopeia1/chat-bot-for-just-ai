package com.example.testtaskjustai;

import org.springframework.boot.SpringApplication;

public class TestTestTaskJustAiApplication {

    public static void main(String[] args) {
        SpringApplication.from(TestTaskJustAiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
