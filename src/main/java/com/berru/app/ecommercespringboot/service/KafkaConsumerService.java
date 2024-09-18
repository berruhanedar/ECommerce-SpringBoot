package com.berru.app.ecommercespringboot.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "address-created", groupId = "my-group")
    public void consumeAddressCreated(ConsumerRecord<String, String> record) {
        log.info("Consumed address created message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "address-updated", groupId = "my-group")
    public void consumeAddressUpdated(ConsumerRecord<String, String> record) {
        log.info("Consumed address updated message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "address-deleted", groupId = "my-group")
    public void consumeAddressDeleted(ConsumerRecord<String, String> record) {
        log.info("Consumed address deleted message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "product-updates", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Consumed message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "product-deletions", groupId = "my-group")
    public void consumeProductDeletion(ConsumerRecord<String, String> record) {
          log.info("Consumed product deletion message: Key = {}, Value = {}", record.key(), record.value());
    }
}
