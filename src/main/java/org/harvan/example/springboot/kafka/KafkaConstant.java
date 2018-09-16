/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot.kafka;

/**
 * 
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (28 May 2018)
 *
 */
public class KafkaConstant {
	private KafkaConstant() {
	}

	private static final String GROUP_PREFIX = "-group";
	public static final String TOPIC = "request-topic";
	public static final String REQUESTREPLY_TOPIC = "requestreply-topic";
	public static final String REQUESTREPLY_GROUP_ID = REQUESTREPLY_TOPIC + GROUP_PREFIX;
}