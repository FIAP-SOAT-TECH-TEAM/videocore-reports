package com.soat.fiap.videocore.reports.infrastructure.common.config.azure.svcbus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

/**
 * Configuração do Azure Service Bus.
 *
 * <p>
 * Define beans para envio de mensagens e mantém nomes de filas/tópicos usados
 * pelo microsserviço.
 */
@Configuration
public class ServiceBusConfig {

	@Value("${azure.service-bus.connection-string}")
	private String connectionString;

	/**
	 * Tópico de status do processamento.
	 *
	 * <p>
	 * Publica atualizações de estado dos processos.
	 */
	public static final String PROCESS_STATUS_TOPIC = "process.status.topic";

	/**
	 * Fila principal para processamentos de erro no processamento de um video.
	 *
	 * <p>
	 * Responsável pelo fluxo de erros no processamento do vídeo.
	 */
	public static final String PROCESS_ERROR_QUEUE = "process.error.queue";

	/**
	 * Assinatura do Tópico de status do processamento.
	 *
	 * <p>
	 * Microsserviço de reports.
	 */
	public static final String REPORTS_PROCESS_STATUS_TOPIC_SUBSCRIPTION = "reports.process.status.topic.subscription";

	/** Cria e configura beans do Service Bus para envio de mensagens. */
	@Bean
	public ServiceBusClientBuilder serviceBusClientBuilder() {
		return new ServiceBusClientBuilder().connectionString(connectionString);
	}

	/**
	 * Cria um ServiceBusSenderClient para enviar mensagens de erro para a fila de
	 * erros.
	 *
	 * @param builder
	 *            builder do Service Bus
	 * @return client configurado para envio de erros
	 */
	@Bean
	public ServiceBusSenderClient processErrorSender(ServiceBusClientBuilder builder) {
		return builder.sender().queueName(PROCESS_ERROR_QUEUE).buildClient();
	}
}
