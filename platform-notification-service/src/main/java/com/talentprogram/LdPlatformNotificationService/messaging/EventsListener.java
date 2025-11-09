package com.talentprogram.LdPlatformNotificationService.messaging;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentprogram.LdPlatformNotificationService.dto.CreateNotificationDTO;
import com.talentprogram.LdPlatformNotificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class EventsListener
{
    private final NotificationService notificationService;
    private String learningTopic;
    private String careerTopic;
    private String userTopic;


    public EventsListener(NotificationService notificationService,
    @Value("${topics.learning}") String learningTopic,
    @Value("${topics.career}") String careerTopic,
    @Value("${topics.user}") String userTopic) {
        this.notificationService = notificationService;
        this.learningTopic = learningTopic;
        this.careerTopic = careerTopic;
        this.userTopic = userTopic;
    }

    @KafkaListener(topics = "${topics.learning}", groupId = "${spring.application.name}")
    @Transactional
    public void learningEvents(EventModel event)
    {
        switch (event.eventType()) {
            /* May add more events in the future */
            case "SUBMIT":
                onSubmitLearningEvent(event);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.eventType());
        }
    }

    private void onSubmitLearningEvent(EventModel event) {
        Long managerId  = ((Number) event.data().get("managerId")).longValue();
        Long employeeId = ((Number) event.data().get("employeeId")).longValue();
        String title    = (String) event.data().get("title");

        CreateNotificationDTO notification = new CreateNotificationDTO(
            managerId,
            com.talentprogram.LdPlatformNotificationService.model.Notification.Type.INFO,
            "New learning from user %d: %s".formatted(employeeId, title)
        );
        notificationService.create(notification);
    }

    @KafkaListener(topics = "${topics.career}", groupId = "${spring.application.name}")
    @Transactional
    public void careerEvents(EventModel event)
    {
        switch (event.eventType()) {
            /* May add more events in the future */
            case "SUBMIT":
                onSubmitCareerEvent(event);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.eventType());
        }
    }

    private void onSubmitCareerEvent(EventModel event) {
        Long managerId  = ((Number) event.data().get("managerId")).longValue();
        Long employeeId = ((Number) event.data().get("employeeId")).longValue();
        String title    = (String) event.data().get("title");

        CreateNotificationDTO notification = new CreateNotificationDTO(
            managerId,
            com.talentprogram.LdPlatformNotificationService.model.Notification.Type.INFO,
            "New career development from user %d: %s".formatted(employeeId, title)
        );
        notificationService.create(notification);
    }
}
