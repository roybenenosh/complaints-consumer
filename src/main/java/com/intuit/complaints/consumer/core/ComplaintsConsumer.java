package com.intuit.complaints.consumer.core;

import com.intuit.complaints.consumer.ComplaintRepository;
import com.intuit.complaints.dal.Complaint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ComplaintsConsumer {

    private final ComplaintRepository complaintRepository;

    @Lazy
    public ComplaintsConsumer(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @KafkaListener(topics = "complaints", groupId = "group_id")
    public void consume(Complaint complaint) {
        complaintRepository.save(complaint);
    }

}
