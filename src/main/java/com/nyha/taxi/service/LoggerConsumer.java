package com.nyha.taxi.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.nyha.taxi.model.VehicleDistanceInfo;

@Service
public class LoggerConsumer {
    @KafkaListener(
            topics = "${spring.kafka.output-topic}",
            concurrency = "1",
            groupId = "${spring.kafka.loggingConsumerGroupId}",
            containerFactory = "vehicleSignalKafkaListenerFactory")
    public void consumeJson(VehicleDistanceInfo distanceInfo) {
        System.out.println("Taxi id: " + distanceInfo.getId() + "\n" +
                "Distance : " + distanceInfo.getDistance());
    }
}
