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
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class ComplaintsConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private final ComplaintRepository complaintRepository;

    private final String KAFKA_COMPLAINTS_TOPIC = "complaints";

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String maxInterval;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollRecords;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String complaintDeserializer;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Application started at: " + new Date(event.getTimestamp()) + ". Consuming complaints");
        consumeComplaints();
    }

    private void consumeComplaints() {
        KafkaConsumer<String, Complaint> consumer = generateConsumer();
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

    private KafkaConsumer<String, Complaint> generateConsumer() {
        // programmatically configure consumer props for multi message consume implementation
        // https://strimzi.io/blog/2021/01/07/consumer-tuning/
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, complaintDeserializer);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxInterval);
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);

        return new KafkaConsumer<>(props);
    }
}
