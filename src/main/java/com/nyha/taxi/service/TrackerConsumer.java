package com.nyha.taxi.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.nyha.taxi.model.VehicleDistanceInfo;
import com.nyha.taxi.model.VehicleSignal;

@Service
public class TrackerConsumer {

    @Value("${spring.kafka.output-topic}")
    private String topic;

    private final KafkaTemplate<Long, VehicleDistanceInfo> distanceInfoSender;

    private final Map<Long, Double> distanceStorage = new ConcurrentHashMap<>();
    private final Map<Long, VehicleSignal> lastReceivedSignals = new ConcurrentHashMap<>();

    @Autowired
    public TrackerConsumer(KafkaTemplate<Long, VehicleDistanceInfo> distanceInfoSender) {
        this.distanceInfoSender = distanceInfoSender;
    }

    @KafkaListener(
            topics = "${spring.kafka.input-topic}",
            concurrency = "3",
            groupId = "${spring.kafka.trackerConsumerGroupId}",
            containerFactory = "vehicleSignalKafkaListenerFactory")
    public void consumeJson(VehicleSignal vehicleSignal) {
        VehicleDistanceInfo vehicleDistanceInfo = updateVehicleDistanceInfo(vehicleSignal);
        System.out.println(Thread.currentThread().getName() + " received message.");
        distanceInfoSender.send(topic, vehicleDistanceInfo.getId(), vehicleDistanceInfo);
    }

    private VehicleDistanceInfo updateVehicleDistanceInfo(VehicleSignal vehicleSignal) {
        Long id = vehicleSignal.getId();
        if (lastReceivedSignals.containsKey(id)) {
            return createVehicleDistanceInfo(vehicleSignal);
        }
        return createDefaultInfo(vehicleSignal);
    }

    private VehicleDistanceInfo createDefaultInfo(VehicleSignal vehicleSignal) {
        Long id = vehicleSignal.getId();
        lastReceivedSignals.put(id, vehicleSignal);
        distanceStorage.put(id, 0.0);
        return new VehicleDistanceInfo(id, 0);
    }

    private VehicleDistanceInfo createVehicleDistanceInfo(VehicleSignal currSignal) {
        Long id = currSignal.getId();
        VehicleSignal prevSignal = lastReceivedSignals.get(id);
        double prevDistance = distanceStorage.get(id);
        double currDistance = calculateDistance(prevSignal, currSignal);
        double newDistance = prevDistance + currDistance;
        lastReceivedSignals.put(id, currSignal);
        distanceStorage.put(id, newDistance);
        return new VehicleDistanceInfo(id,  newDistance);
    }

    private double calculateDistance(VehicleSignal prev, VehicleSignal curr) {
        double a = prev.getLongitude() - curr.getLongitude();
        double b = prev.getLatitude() - curr.getLatitude();
        return Math.sqrt(a * a + b * b);
    }

}
