package com.berru.app.ecommercespringboot.service;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;



@Service
public class KafkaTopicService {

    private final KafkaAdmin kafkaAdmin;

    @Autowired
    public KafkaTopicService(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @PostConstruct
    public void createTopics() {

        NewTopic productTopic = new NewTopic("product-topic", 1, (short) 1);
        kafkaAdmin.createOrModifyTopics(productTopic);
    }
}
