package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.models.enums.FriendStatus;
import meetingteam.userservice.repositories.FriendRelationRepository;
import meetingteam.userservice.services.FriendService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRelationRepository frRepository;
    private final ModelMapper modelMapper;

    public List<ResUserDto> getFriends(){
        String userId= AuthUtil.getUserId();
        var friendsList= frRepository.getFriends(userId);
        return friendsList.stream()
                .map(friend -> modelMapper.map(friend, ResUserDto.class))
                .toList();
    }
    @Transactional
    public void unfriend(String friendId) {
        String userId= AuthUtil.getUserId();
        frRepository.updateFriendStatus(FriendStatus.UNFRIEND,userId, friendId);
//        socketTemplate.sendUser(friendId,"/deleteFriend",userId);
//        socketTemplate.sendUser(userId,"/deleteFriend", friendId);
    }
}
