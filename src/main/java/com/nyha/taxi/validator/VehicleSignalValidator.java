package com.nyha.taxi.validator;

import org.springframework.stereotype.Service;

import com.nyha.taxi.model.VehicleSignal;

@Service
public class VehicleSignalValidator {

    public boolean validate(VehicleSignal vehicleSignal) {
        return idValidate(vehicleSignal.getId())
                && coordinatesValidate(vehicleSignal.getLongitude(), vehicleSignal.getLatitude());
    }

    private boolean idValidate(Long id) {
        return id != null && id > 0;
    }

    private boolean coordinatesValidate(double longitude, double latitude) {
        return longitude > 0 && latitude > 0;
    }
}
