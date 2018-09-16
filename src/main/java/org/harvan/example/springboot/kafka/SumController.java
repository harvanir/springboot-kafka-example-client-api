package org.harvan.example.springboot.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (
 *
 */
@RestController
public class SumController {
	@Autowired
	private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;
	private String requestTopic = KafkaConstant.TOPIC;
	private String requestReplyTopic = KafkaConstant.REQUESTREPLY_TOPIC;
	private RecordHeader header = new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes());
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private RestTemplate restTemplate;
	private String url;
	@Value("${server.consumer.port}")
	private String directServerConsumerPort;
	private String listen2Url;

	@ResponseBody
	@PostMapping(value = "/sum/{loop}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Model sum(@RequestBody String request, @PathVariable(name = "loop") String loopValue)
			throws InterruptedException, ExecutionException, IOException {
		ConsumerRecord<String, String> consumerRecord = null;
		int loop = Integer.parseInt(loopValue);
		List<RequestReplyFuture<String, String, String>> listReply = new ArrayList<>(loop);

		Long start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			// create producer record
			ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, request);
			// set reply topic in header
			record.headers().add(header);

			// post in kafka topic
			RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);
			listReply.add(sendAndReceive);
		}

		System.out.println("Size: " + listReply.size());
		for (int i = 0; i < listReply.size(); i++) {
			RequestReplyFuture<String, String, String> sendAndReceive = listReply.get(i);
			// get consumer record
			consumerRecord = sendAndReceive.get();
		}

		Long end = System.currentTimeMillis();
		System.out.println("Elapse in : " + (end - start) + " ms.");

		// return consumer value
		return mapper.readValue(consumerRecord.value(), Model.class);
	}

	private String getUrl() {
		if (url == null) {
			url = "http://localhost:" + directServerConsumerPort;
		}
		return url;
	}

	private String getListen2Url() {
		if (listen2Url == null) {
			listen2Url = getUrl() + "/listen2";
		}
		return listen2Url;
	}

	@PostMapping(value = "/sum2/{loop}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Model sum2(@RequestBody String request, @PathVariable(name = "loop") String loopValue)
			throws IOException, InterruptedException, ExecutionException {
		ResponseEntity<String> temp = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		int loop = Integer.parseInt(loopValue);
		List<Future<ResponseEntity<String>>> listResponse = new ArrayList<>(loop);
		int procCore = Runtime.getRuntime().availableProcessors() * 2;
		ExecutorService executorService = Executors.newFixedThreadPool(procCore);

		Long start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			Future<ResponseEntity<String>> future = executorService.submit(new Callable<ResponseEntity<String>>() {
				@Override
				public ResponseEntity<String> call() throws Exception {
					HttpEntity<String> entity = new HttpEntity<>(request, headers);
					return restTemplate.postForEntity(getListen2Url(), entity, String.class);
				}
			});

			listResponse.add(future);
		}

		System.out.println("Size: " + listResponse.size());
		for (int i = 0; i < listResponse.size(); i++) {
			Future<ResponseEntity<String>> future = listResponse.get(i);
			temp = future.get();
		}

		Long end = System.currentTimeMillis();
		System.out.println("Elapse in : " + (end - start) + " ms.");
		executorService.shutdown();

		return mapper.readValue(temp.getBody(), Model.class);
	}
}