package com.nyha.taxi.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.nyha.taxi.model.VehicleDistanceInfo;
import com.nyha.taxi.model.VehicleSignal;

@Configuration
public class CustomConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.trackerConsumerGroupId}")
    private String consumerGroup;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, VehicleSignal> vehicleSignalKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, VehicleSignal> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(vehicleSignalKafkaListenerConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, VehicleSignal> vehicleSignalKafkaListenerConsumerFactory() {
        if (bootstrapServers == null) throw new RuntimeException("spring.kafka.bootstrap-servers property wasn't set");
        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.GROUP_ID_CONFIG, consumerGroup,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
                ), new LongDeserializer(), new JsonDeserializer<>(VehicleSignal.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, VehicleDistanceInfo> distanceInfoKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, VehicleDistanceInfo> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(distanceInfoKafkaListenerConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, VehicleDistanceInfo> distanceInfoKafkaListenerConsumerFactory() {
        if (bootstrapServers == null) throw new RuntimeException("spring.kafka.bootstrap-servers property wasn't set");
        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.GROUP_ID_CONFIG, consumerGroup,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
                ), new LongDeserializer(), new JsonDeserializer<>(VehicleDistanceInfo.class)
        );
    }
}
