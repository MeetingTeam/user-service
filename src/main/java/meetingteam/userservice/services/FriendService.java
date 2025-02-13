package meetingteam.userservice.services;

import meetingteam.commonlibrary.dtos.PagedResponseDto;
import meetingteam.userservice.dtos.User.ResUserDto;

import java.util.List;

public interface FriendService {
    PagedResponseDto<ResUserDto> getFriends(Integer pageNo, Integer pageSize);
    List<ResUserDto> searchFriendsByName(String searchName);
    void unfriend(String friendId);
    boolean isFriend(String userId, String friendId);
}
