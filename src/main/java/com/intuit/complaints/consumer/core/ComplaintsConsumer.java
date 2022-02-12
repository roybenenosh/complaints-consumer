package com.intuit.complaints.consumer.core;

import com.intuit.complaints.consumer.ComplaintRepository;
import com.intuit.complaints.dal.Complaint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class ComplaintsConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private final ComplaintRepository complaintRepository;
    private final ConsumerFactory<String, Complaint> consumerFactory;

    private final String KAFKA_COMPLAINTS_TOPIC = "complaints";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Application started at: " + new Date(event.getTimestamp()) + ". Consuming complaints");
        consumeComplaints();
    }

    private void consumeComplaints() {
        KafkaConsumer<String, Complaint> consumer = (KafkaConsumer<String, Complaint>) consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(KAFKA_COMPLAINTS_TOPIC));

        Duration duration = Duration.ofSeconds(1L);

        try {
            while (true) {
                ConsumerRecords<String, Complaint> records = consumer.poll(duration);

                List<Complaint> complaintList = new ArrayList<>();

                for (ConsumerRecord<String, Complaint> record : records) {
                    complaintList.add(record.value());
                }

                if (complaintList.size() > 0) {
                    log.info("Saving " + complaintList.size() + (complaintList.size() == 1 ? " complaint" : "complaints"));
                    complaintRepository.saveAll(complaintList);
                }

                consumer.commitAsync();
            }
        } catch (Exception e) {
            log.error("Error: Cannot consume complaints", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
