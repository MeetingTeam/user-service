package meetingteam.userservice.services;

import meetingteam.userservice.dtos.FriendRequest.ResFriendRequestDto;

import java.util.List;

public interface FriendRequestService {
    ResFriendRequestDto createFriendRequest(String email, String content);
    void acceptFriend(String requestId, boolean isAccepted);
    void deleteFriendRequest(String requestId);
    List<ResFriendRequestDto> getReceivedRequests();
    List<ResFriendRequestDto> getSentRequests();
}
