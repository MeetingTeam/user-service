package meetingteam.userservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.dtos.PagedResponseDto;
import meetingteam.commonlibrary.dtos.Pagination;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.models.User;
import meetingteam.userservice.models.enums.FriendStatus;
import meetingteam.userservice.repositories.FriendRelationRepository;
import meetingteam.userservice.services.FriendService;
import meetingteam.userservice.services.WebsocketService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRelationRepository friendRelationRepo;
    private final WebsocketService websocketService;
    private final ModelMapper modelMapper;

    public PagedResponseDto<ResUserDto> getFriends(Integer pageNo, Integer pageSize){
        String userId= AuthUtil.getUserId();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<User> friendsPage= friendRelationRepo.getFriends(userId, pageRequest);
        var friendDtos= friendsPage.stream()
                .map(friend -> modelMapper.map(friend, ResUserDto.class))
                .toList();

        var pagination= new Pagination(pageNo, friendsPage.getTotalPages(), friendsPage.getTotalElements());
        return new PagedResponseDto<>(friendDtos, pagination);
    }

    @Override
    public List<ResUserDto> searchFriendsByName(String searchName) {
        String userId= AuthUtil.getUserId();
        var users= friendRelationRepo.searchFriendsByName(userId,
                searchName.toLowerCase(), PageRequest.of(0,10));
        return users.stream()
                .map(user->modelMapper.map(user, ResUserDto.class))
                .toList();
    }

    public void unfriend(String friendId) {
        String userId= AuthUtil.getUserId();
        friendRelationRepo.updateFriendStatus(FriendStatus.UNFRIEND,userId, friendId);

        websocketService.deleteFriend(userId, friendId);
        websocketService.deleteFriend(friendId, userId);
    }

    public boolean isFriend(String userId, String friendId){
        return friendRelationRepo.havingFriend(userId, friendId)>0;
    }
}
