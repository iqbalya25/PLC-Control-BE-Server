package org.example.cloudbackend.service;

public interface PlcService {
    void sendCommand(String command);
    String getStatus();
}
