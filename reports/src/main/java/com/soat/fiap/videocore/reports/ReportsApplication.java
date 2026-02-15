package com.soat.fiap.videocore.reports;

import com.soat.fiap.videocore.reports.common.hints.azure.BlobHints;
import com.soat.fiap.videocore.reports.common.hints.azure.CosmosHints;
import com.soat.fiap.videocore.reports.common.hints.jackson.JacksonHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints({CosmosHints.class, BlobHints.class, JacksonHints.class})
public class ReportsApplication {

    static void main(String[] args) {
        SpringApplication.run(ReportsApplication.class, args);
	}

}
