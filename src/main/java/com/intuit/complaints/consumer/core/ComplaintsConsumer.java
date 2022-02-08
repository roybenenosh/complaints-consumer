package com.intuit.complaints.consumer.core;

import com.intuit.complaints.dal.Complaint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ComplaintsConsumer {

    @KafkaListener(topics = "complaints", groupId = "group_id")
    public void consume(Complaint complaint) throws IOException {
        log.info(complaint.toString());
    }

}
