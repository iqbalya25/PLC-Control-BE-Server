package org.example.cloudbackend.controller;

import org.example.cloudbackend.dto.CommandDTO;
import org.example.cloudbackend.dto.StatusDTO;
import org.example.cloudbackend.service.PlcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/plc")
public class PlcController {

    private static final Logger logger = LoggerFactory.getLogger(PlcController.class);

    private final PlcService plcService;

    @Autowired
    public PlcController(PlcService plcService) {
        this.plcService = plcService;
    }

    @PostMapping("/command")
    public ResponseEntity<String> sendCommand(@RequestBody CommandDTO command) {
        logger.info("Received command: {}", command.getCommand());
        plcService.sendCommand(command.getCommand());
        return ResponseEntity.ok("Command sent: " + command.getCommand());
    }

    @GetMapping("/status")
    public ResponseEntity<StatusDTO> getStatus() {
        String status = plcService.getStatus();
        logger.info("Current status: {}", status);
        return ResponseEntity.ok(new StatusDTO(status));
    }
}