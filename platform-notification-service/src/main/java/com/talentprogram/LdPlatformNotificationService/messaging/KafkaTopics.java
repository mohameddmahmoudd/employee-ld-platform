package com.talentprogram.LdPlatformNotificationService.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics 
{
    @Bean
    public NewTopic learningTopic(@Value("${topics.learning}") String learningTopic) {
        return TopicBuilder.name(learningTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic careerTopic(@Value("${topics.career}") String careerTopic) {
        return TopicBuilder.name(careerTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userTopic(@Value("${topics.user}") String userTopic) {
        return TopicBuilder.name(userTopic) 
                .partitions(3)
                .replicas(1)
                .build();     
    }

}
