package com.intuit.complaints.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.intuit.complaints.dal")
public class ComplaintsConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintsConsumerApplication.class, args);
	}

}
