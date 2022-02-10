package com.intuit.complaints.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = ComplaintsConsumerApplication.class)
class ComplaintsConsumerApplicationTests {

	@Test
	void contextLoads() {
	}

}
