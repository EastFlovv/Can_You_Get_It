package com.allback.cygiconcert.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.*;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;


@Component
public class KafkaInterceptor implements HandlerInterceptor {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Kafka Consumer 생성 메서드
     *
     * @param consumerGroupId
     * @return Kafka Consumer
     */
    private KafkaConsumer<String, String> createConsumer(String consumerGroupId) {
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(GROUP_ID_CONFIG, consumerGroupId);
        properties.setProperty(MAX_POLL_RECORDS_CONFIG, "1");   // consumer는 한 번에 1개만 poll 할 수 있다.
        properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(properties);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 10초 대기 (일부러 성능 떨어뜨리기)
        Thread.sleep(5000);

        // TODO : request의 Header를 보고, Kafka 대기표 끊은 애들만!

        // kafka consumer 생성
        KafkaConsumer<String, String> consumer = createConsumer(groupId);

        // consume할 topic과 particion
        TopicPartition partition = new TopicPartition(topic, 2);
        consumer.assign(Collections.singletonList(partition));

        // 레코드 읽어오기
        // TODO : Offset 안 올라갈 때가 가끔 있음. 에러 해결해야됨
//        consumer.poll(Duration.ZERO);
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        System.out.println("----- " + consumer.position(partition));
        currentOffsets.put(partition, new OffsetAndMetadata(consumer.position(partition)));

        // 읽은 메시지 commit 하기 (Offset 증가)
        consumer.commitSync(currentOffsets);
    }

}
