package com.dvo.notification_service.configuration;

import com.dvo.notification_service.event.ProfileEvent;
import com.dvo.notification_service.event.ProgressEvent;
import com.dvo.notification_service.event.TrackEvent;
import com.dvo.notification_service.event.UserEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupID;

    @Bean
    public ConsumerFactory<String, UserEvent> userEventConsumerFactory() {
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(UserEvent.class, false)
        );
    }

    @Bean(name = "userEventKafkaListener")
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> userEventConcurrentKafkaListenerContainerFactory(){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserEvent>();
        factory.setConsumerFactory(userEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProfileEvent> profileEventConsumerFactory(){
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(ProfileEvent.class, false)
        );
    }

    @Bean(name = "profileEventKafkaListener")
    public ConcurrentKafkaListenerContainerFactory<String, ProfileEvent> profileEventConcurrentKafkaListenerContainerFactory(){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProfileEvent>();
        factory.setConsumerFactory(profileEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProgressEvent> progressEventConsumerFactory(){
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(ProgressEvent.class, false)
        );
    }

    @Bean(name = "progressEventKafkaListener")
    public ConcurrentKafkaListenerContainerFactory<String, ProgressEvent> progressEventConcurrentKafkaListenerContainerFactory(){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProgressEvent>();
        factory.setConsumerFactory(progressEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TrackEvent> trackEventConsumerFactory(){
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(TrackEvent.class, false)
        );
    }

    @Bean(name = "trackEventKafkaListener")
    public ConcurrentKafkaListenerContainerFactory<String, TrackEvent> trackEventConcurrentKafkaListenerContainerFactory(){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, TrackEvent>();
        factory.setConsumerFactory(trackEventConsumerFactory());
        return factory;
    }

    private Map<String, Object> baseConsumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return config;
    }

}
