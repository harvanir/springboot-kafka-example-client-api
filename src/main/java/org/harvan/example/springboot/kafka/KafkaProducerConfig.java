package org.harvan.example.springboot.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

/**
 * 
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (29 May 2018)
 *
 */
@Configuration
public class KafkaProducerConfig {
	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	/**
	 * @param cf
	 *            is Autowired from
	 *            {@link KafkaAutoConfiguration#kafkaConsumerFactory()}
	 * @return
	 */
	@Bean
	public KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
		ContainerProperties containerProperties = new ContainerProperties(KafkaConstant.REQUESTREPLY_TOPIC);
		containerProperties.setGroupId(KafkaConstant.REQUESTREPLY_GROUP_ID);

		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}

	/**
	 * @param pf
	 *            is Autowired from
	 *            {@link KafkaAutoConfiguration#kafkaProducerFactory()}
	 * @param replyContainer
	 *            is Autowired from {@link #replyContainer(ConsumerFactory)}
	 * @return
	 */
	@Bean
	public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(ProducerFactory<String, String> pf,
			KafkaMessageListenerContainer<String, String> replyContainer) {
		return new ReplyingKafkaTemplate<>(pf, replyContainer);
	}
}