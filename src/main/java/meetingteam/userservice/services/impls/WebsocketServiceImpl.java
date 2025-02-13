package meetingteam.userservice.services.impls;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import meetingteam.userservice.contraints.WebsocketTopics;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.services.RabbitmqService;
import meetingteam.userservice.services.WebsocketService;

@Service
@RequiredArgsConstructor
public class WebsocketServiceImpl implements WebsocketService{
          private final RabbitmqService rabbitmqService;

          @Override
          public void addOrUpdateFriend(String destUserId, ResUserDto userDto) {
                    rabbitmqService.sendToUser(destUserId, WebsocketTopics.AddOrUpdateFriend, userDto);
          }

          @Override
          public void deleteFriend(String destUserId, String delFriendId) {
                    rabbitmqService.sendToUser(destUserId, WebsocketTopics.DeleteFriend, delFriendId);
          }   
}
