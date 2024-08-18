package org.example.cloudbackend.controller;

import org.example.cloudbackend.dto.CommandDTO;
import org.example.cloudbackend.dto.StatusDTO;
import org.example.cloudbackend.service.PlcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plc")
public class PlcController {

    private final PlcService plcService;

    @Autowired
    public PlcController(PlcService plcService) {
        this.plcService = plcService;
    }

    @PostMapping("/command")
    public ResponseEntity<String> sendCommand(@RequestBody CommandDTO command) {
        plcService.sendCommand(command.getCommand());
        return ResponseEntity.ok("Command sent: " + command.getCommand());
    }

    @GetMapping("/status")
    public ResponseEntity<StatusDTO> getStatus() {
        String status = plcService.getStatus();
        return ResponseEntity.ok(new StatusDTO(status));
    }
}