package com.rmit.destination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class DestinationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DestinationApplication.class, args);
	}

}
