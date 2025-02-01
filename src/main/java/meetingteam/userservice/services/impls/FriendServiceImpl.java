package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.userservice.contraints.WebsocketTopics;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.models.enums.FriendStatus;
import meetingteam.userservice.repositories.FriendRelationRepository;
import meetingteam.userservice.services.FriendService;
import meetingteam.userservice.services.RabbitmqService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRelationRepository friendRelationRepo;
    private final RabbitmqService rabbitmqService;
    private final ModelMapper modelMapper;

    public List<ResUserDto> getFriends(){
        String userId= AuthUtil.getUserId();
        var friendsList= friendRelationRepo.getFriends(userId);
        return friendsList.stream()
                .map(friend -> modelMapper.map(friend, ResUserDto.class))
                .toList();
    }
    @Transactional
    public void unfriend(String friendId) {
        String userId= AuthUtil.getUserId();
        friendRelationRepo.updateFriendStatus(FriendStatus.UNFRIEND,userId, friendId);

        rabbitmqService.sendToUser(userId, WebsocketTopics.DeleteFriend, friendId);
        rabbitmqService.sendToUser(friendId, WebsocketTopics.DeleteFriend, userId);
    }

    public boolean isFriend(String userId, String friendId){
        return friendRelationRepo.havingFriend(userId, friendId)>0;
    }
}
