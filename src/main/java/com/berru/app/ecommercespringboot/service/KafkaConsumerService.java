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

    @KafkaListener(topics = "category-created", groupId = "my-group")
    public void consumeCategoryCreated(ConsumerRecord<String, String> record) {
        log.info("Consumed category created message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "category-updated", groupId = "my-group")
    public void consumeCategoryUpdated(ConsumerRecord<String, String> record) {
        log.info("Consumed category updated message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "category-deleted", groupId = "my-group")
    public void consumeCategoryDeleted(ConsumerRecord<String, String> record) {
        log.info("Consumed category deleted message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "customer-created", groupId = "my-group")
    public void consumeCustomerCreated(ConsumerRecord<String, String> record) {
        log.info("Consumed customer created message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "customer-updated", groupId = "my-group")
    public void consumeCustomerUpdated(ConsumerRecord<String, String> record) {
        log.info("Consumed customer updated message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "customer-deleted", groupId = "my-group")
    public void consumeCustomerDeleted(ConsumerRecord<String, String> record) {
        log.info("Consumed customer deleted message: Key = {}, Value = {}", record.key(), record.value());
    }


    @KafkaListener(topics = "product-updates", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Consumed message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "product-deletions", groupId = "my-group")
    public void consumeProductDeletion(ConsumerRecord<String, String> record) {
          log.info("Consumed product deletion message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "order-placed", groupId = "my-group")
    public void consumeOrderPlaced(ConsumerRecord<String, String> record) {
        log.info("Consumed order placed message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "order-cancelled", groupId = "my-group")
    public void consumeOrderCancelled(ConsumerRecord<String, String> record) {
        log.info("Consumed order cancelled message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "order-shipped", groupId = "my-group")
    public void consumeOrderShipped(ConsumerRecord<String, String> record) {
        log.info("Consumed order shipped message: Key = {}, Value = {}", record.key(), record.value());
    }

    @KafkaListener(topics = "order-delivered", groupId = "my-group")
    public void consumeOrderDelivered(ConsumerRecord<String, String> record) {
        log.info("Consumed order delivered message: Key = {}, Value = {}", record.key(), record.value());
    }

}
