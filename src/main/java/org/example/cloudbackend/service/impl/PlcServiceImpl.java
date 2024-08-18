package org.example.cloudbackend.service.impl;

import org.example.cloudbackend.mqtt.MqttGateway;
import org.example.cloudbackend.service.PlcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlcServiceImpl implements PlcService {

    private final MqttGateway mqttGateway;
    private String lastStatus = "OFF";

    @Autowired
    public PlcServiceImpl(MqttGateway mqttGateway) {
        this.mqttGateway = mqttGateway;
    }

    @Override
    public void sendCommand(String command) {
        mqttGateway.sendToMqtt(command);
    }

    @Override
    public String getStatus() {
        return lastStatus;
    }

    // This method can be called by an MQTT message listener to update the status
    public void updateStatus(String status) {
        this.lastStatus = status;
    }
}