package com.talentprogram.LdPlatformNotificationService.messaging;
import java.util.Map;

public record EventModel
(
    Long eventId,
    String eventType, /*Optional may remove */
    String producer,
    String aggregate_type, /*The kind of thing this event is about */
    String aggregate_id,    /*The specific instance this event is about a specific user: mohamed*/
    Map<String, Object> data
)
{

}