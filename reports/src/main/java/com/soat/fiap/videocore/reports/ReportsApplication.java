package com.soat.fiap.videocore.reports;

import com.soat.fiap.videocore.reports.infrastructure.common.hints.azure.cosmos.CosmosHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints({CosmosHints.class})
public class ReportsApplication {

    static void main(String[] args) {
        SpringApplication.run(ReportsApplication.class, args);
	}

}
