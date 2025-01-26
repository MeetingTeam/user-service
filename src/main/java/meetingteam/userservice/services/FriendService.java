package meetingteam.userservice.services;

import meetingteam.userservice.dtos.User.ResUserDto;

import java.util.List;

public interface FriendService {
    List<ResUserDto> getFriends();
    void unfriend(String friendId);
    boolean isFriend(String userId, String friendId);
}
