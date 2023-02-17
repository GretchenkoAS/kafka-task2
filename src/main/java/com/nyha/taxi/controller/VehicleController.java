package com.nyha.taxi.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nyha.taxi.model.VehicleSignal;
import com.nyha.taxi.service.VehicleSignalProducer;
import com.nyha.taxi.validator.VehicleSignalValidator;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    private final VehicleSignalValidator signalValidator;

    private final VehicleSignalProducer signalProducer;

    @Autowired
    public VehicleController(VehicleSignalValidator signalValidator, VehicleSignalProducer signalProducer) {
        this.signalValidator = signalValidator;
        this.signalProducer = signalProducer;
    }

    @PostMapping("/signalize")
    public ResponseEntity<String> receiveSignal(@RequestBody VehicleSignal vehicleSignal) {
        if (!signalValidator.validate(vehicleSignal)) {
            return ResponseEntity.unprocessableEntity().body("Incorrect signal");
        }
        try {
            signalProducer.sendVehicleSignal(vehicleSignal);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Unavailable signal storage");
        }
        return ResponseEntity.accepted().body("Signal received");
    }


}
