package com.hoangtan2712.lab303;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/mqtt")
public class MqttController {
    private final MqttService mqttService;

    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/publish")
    public String publish() {
        try {
            mqttService.publish("xin loi thay Tu vi da nop bai tre lab303 a");
            return "Message published successfully!";
        } catch (MqttException e) {
            return "Publish failed: " + e.getMessage();
        }
    }
}
