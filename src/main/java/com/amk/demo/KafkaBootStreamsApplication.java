package com.amk.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(KafkaBindings.class)
public class KafkaBootStreamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaBootStreamsApplication.class, args);
	}
}
