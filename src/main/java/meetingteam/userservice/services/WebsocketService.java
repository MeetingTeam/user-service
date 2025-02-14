package meetingteam.userservice.services;

import meetingteam.userservice.dtos.User.ResUserDto;

public interface WebsocketService {
          void addOrUpdateFriend(String destUserId, ResUserDto userDto);
          void deleteFriend(String destUserId, String delFriendId);
}
