package org.harvan.example.springboot.kafka;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {
	@Bean
	public RestTemplate restTemplate() {
		PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager();
		HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(pccm).build();
		HttpComponentsClientHttpRequestFactory hcchrf = new HttpComponentsClientHttpRequestFactory();
		hcchrf.setHttpClient(httpClient);

		return new RestTemplate(hcchrf);
	}
}