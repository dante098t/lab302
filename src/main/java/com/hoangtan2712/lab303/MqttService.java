package com.hoangtan2712.lab303;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttService implements MqttCallback {

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    private MqttClient client;

    @PostConstruct
    public void init() throws MqttException {
        client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions opt = new MqttConnectOptions();
        opt.setCleanSession(true);
        opt.setAutomaticReconnect(true);

        client.setCallback(this);
        client.connect(opt);
        client.subscribe(topic);
        System.out.println("MQTT đã kết nối & subscribe topic: " + topic);
    }

    public void publish(String payload) throws MqttException {
        MqttMessage msg = new MqttMessage(payload.getBytes());
        msg.setQos(1);
        client.publish(topic, msg);
        System.out.println("Đã publish: " + payload);
    }

    @PreDestroy
    public void destroy() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
            System.out.println("MQTT đã ngắt kết nối");
        }
    }

    @Override public void connectionLost(Throwable cause) {
        System.out.println("Mất kết nối MQTT: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Nhận được từ [" + topic + "]: " + new String(message.getPayload()));
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {}
}