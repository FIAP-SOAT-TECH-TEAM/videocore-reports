package com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.listener;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Listener que inicia o processamento de mensagens do Service Bus após a
 * inicialização do bean.
 */
@RequiredArgsConstructor @Component
public class ProcessVideoStatusUpdateListener {

	private final ServiceBusProcessorClient processVideoStatusUpdate;

	@PostConstruct
	public void run() {
		processVideoStatusUpdate.start();
	}
}
