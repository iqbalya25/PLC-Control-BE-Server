package org.example.cloudbackend.service.impl;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.example.cloudbackend.mqtt.MqttGateway;
import org.example.cloudbackend.service.PlcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PlcServiceImpl implements PlcService, MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(PlcServiceImpl.class);

    private final MqttGateway mqttGateway;
    private String lastStatus = "OFF";

    @Value("${mqtt.topic.status}")
    private String statusTopic;

    @Value("${mqtt.topic.command}")
    private String commandTopic;

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    private MqttClient mqttClient;

    @Autowired
    public PlcServiceImpl(MqttGateway mqttGateway) {
        this.mqttGateway = mqttGateway;
    }

    @PostConstruct
    public void init() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId + "-subscriber");
            mqttClient.setCallback(this);
            mqttClient.connect();
            mqttClient.subscribe(statusTopic);
            logger.info("Subscribed to topic: {}", statusTopic);
        } catch (MqttException e) {
            logger.error("Error initializing MQTT client", e);
        }
    }

    @Override
    public void sendCommand(String command) {
        mqttGateway.sendToMqtt(commandTopic, command);
        logger.info("Sent command to topic: {}, command: {}", commandTopic, command);
    }

    @Override
    public String getStatus() {
        return lastStatus;
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.error("Connection to MQTT broker lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (topic.equals(statusTopic)) {
            this.lastStatus = new String(message.getPayload());
            logger.info("Received status update on topic {}: {}", topic, this.lastStatus);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("Message delivery completed");
    }
}