package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.userservice.contraints.WebsocketTopics;
import meetingteam.userservice.dtos.FriendRequest.ResFriendRequestDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.models.FriendRelation;
import meetingteam.userservice.models.FriendRequest;
import meetingteam.userservice.models.User;
import meetingteam.userservice.models.enums.FriendStatus;
import meetingteam.userservice.repositories.FriendRelationRepository;
import meetingteam.userservice.repositories.FriendRequestRepository;
import meetingteam.userservice.repositories.UserRepository;
import meetingteam.userservice.services.FriendRequestService;
import meetingteam.userservice.services.RabbitmqService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {
    private final FriendRequestRepository friendRequestRepo;
    private final FriendRelationRepository friendRelationRepo;
    private final UserRepository userRepo;
    private final RabbitmqService rabbitmqService;
    private final ModelMapper modelMapper;

    @Override
    public void createFriendRequest(String email, String content) {
        User recipient=userRepo.findByEmail(email).orElseThrow(()->new BadRequestException("Sorry!!Double check that the email is correct"));

        var userId= AuthUtil.getUserId();
        if(recipient.getId().equals(userId))
            throw new BadRequestException("Hmn! It seems that the email you enter is your own");

        if(friendRelationRepo.havingFriend(userId,recipient.getId())>0)
            throw new BadRequestException("The owner of this email has already been your friend");
        if(friendRequestRepo.existsBySenderAndRecipient(userRepo.getById(userId), recipient))
            throw new BadRequestException("You have already sent a friend request");

        var request= FriendRequest.builder()
                .sender(userRepo.getById(userId))
                .recipient(recipient)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        friendRequestRepo.save(request);

        var requestDto= modelMapper.map(request, ResFriendRequestDto.class);
        rabbitmqService.sendToUser(recipient.getId(),WebsocketTopics.NewFriendRequest,requestDto);
    }

    @Transactional
    public void acceptFriend(String requestId,  boolean isAccepted) {
        String userId= AuthUtil.getUserId();

        FriendRequest request= friendRequestRepo.findById(requestId).orElseThrow(()->new BadRequestException("Request not found"));

        if(!userId.equals(request.getRecipient().getId()))
            throw new BadRequestException("If you want to make friend with someone, you need to send friend request to them");

        request.setIsAccepted(isAccepted);
        friendRequestRepo.save(request);

        if(isAccepted){
            FriendRelation fr= friendRelationRepo.findByUsers(userRepo.getById(userId), request.getSender());
            if(fr==null) fr=new FriendRelation(userRepo.getById(userId),request.getSender(), FriendStatus.FRIEND);
            else fr.setStatus(FriendStatus.FRIEND);
            friendRelationRepo.save(fr);

            friendRequestRepo.deleteById(requestId);

            var senderDto= modelMapper.map(request.getSender(), ResUserDto.class);
            var recipientDto= modelMapper.map(request.getSender(), ResUserDto.class);
            rabbitmqService.sendToUser(request.getSender().getId(), WebsocketTopics.UpdateFriend, recipientDto);
            rabbitmqService.sendToUser(request.getRecipient().getId(),WebsocketTopics.UpdateFriend, senderDto);
        }
    }

    @Override
    public void deleteFriendRequest(String requestId) {
        var request= friendRequestRepo.findById(requestId).orElseThrow(()->new BadRequestException("Request not found"));
        String userId= AuthUtil.getUserId();
        if(!request.getSender().getId().equals(userId))
            throw new AccessDeniedException("You don't have permissions to delete this request");

        friendRequestRepo.deleteById(requestId);
    }

    @Override
    public List<ResFriendRequestDto> getReceivedRequests() {
        var requests= friendRequestRepo.getReceivedRequests(AuthUtil.getUserId());
        return requests.stream()
                .map(request->modelMapper.map(request, ResFriendRequestDto.class))
                .toList();
    }

    @Override
    public List<ResFriendRequestDto> getSentRequests() {
        var requests= friendRequestRepo.getSentRequests(AuthUtil.getUserId());
        return requests.stream()
                .map(request->modelMapper.map(request, ResFriendRequestDto.class))
                .toList();
    }
}
