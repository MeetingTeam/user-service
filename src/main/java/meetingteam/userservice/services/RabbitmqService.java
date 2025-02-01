package meetingteam.userservice.services;

public interface RabbitmqService {
    void sendToUser(String userId, String topic, Object payload);
}
