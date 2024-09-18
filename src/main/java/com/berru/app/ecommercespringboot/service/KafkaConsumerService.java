package com.berru.app.ecommercespringboot.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "product-updates", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Consumed message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "product-deletions", groupId = "my-group")
    public void consumeProductDeletion(ConsumerRecord<String, String> record) {
          log.info("Consumed product deletion message: Key = {}, Value = {}", record.key(), record.value());
    }
}
