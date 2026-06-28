package com.tunduk.candidateservice;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestCandidateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(CandidateServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
