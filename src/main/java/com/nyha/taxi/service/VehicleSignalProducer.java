package com.nyha.taxi.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.nyha.taxi.model.VehicleSignal;

@Service
@Scope("prototype")
public class VehicleSignalProducer {

    @Value("${spring.kafka.input-topic}")
    private String topic;

    private final KafkaTemplate<Long, VehicleSignal> vehicleSignalKafkaSender;

    @Autowired
    public VehicleSignalProducer(KafkaTemplate<Long, VehicleSignal> vehicleSignalKafkaSender) {
        this.vehicleSignalKafkaSender = vehicleSignalKafkaSender;
    }

    public void sendVehicleSignal(VehicleSignal signal) throws ExecutionException, InterruptedException, TimeoutException {
        vehicleSignalKafkaSender.send(topic, signal.getId(), signal).get(2, TimeUnit.MINUTES);
    }
}
