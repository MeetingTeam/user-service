package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.exceptions.InternalServerException;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.commonlibrary.utils.FileUtil;
import meetingteam.userservice.contraints.WebsocketTopics;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.dtos.User.UpdateUserDto;
import meetingteam.userservice.models.User;
import meetingteam.userservice.repositories.FriendRelationRepository;
import meetingteam.userservice.repositories.UserRepository;
import meetingteam.userservice.services.FileService;
import meetingteam.userservice.services.UserService;
import meetingteam.userservice.services.WebsocketService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final FileService fileService;
    private final WebsocketService websocketService;
    private final UserRepository userRepo;
    private final FriendRelationRepository friendRelationRepo;
    private final ModelMapper modelMapper;

    @Value("${cognito.client-id}")
    private String cognitoClientId;
    @Value("${cognito.user-pool-id}")
    private String cognitoPoolId;
    @Value("${s3.url}")
    private String s3BaseUrl;

    @Transactional
    public void addUser(CreateUserDto userDto){
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId(cognitoClientId)
                    .username(userDto.getEmail())
                    .password(userDto.getPassword())
                    .build();
            SignUpResponse signUpResponse = cognitoClient.signUp(signUpRequest);
            String userId = signUpResponse.userSub();

            User user = modelMapper.map(userDto, User.class);
            user.setId(userId);
            user.setIsOnline(false);
            user.setLastActive(LocalDateTime.now());
            userRepo.save(user);
        } catch (UsernameExistsException e) {
            log.error(e.getMessage());
            
            if(userRepo.existsByEmail(userDto.getEmail()))
                throw new BadRequestException("Email already exists");
            else {
                var deleteUserRequest = AdminDeleteUserRequest.builder()
                        .userPoolId(cognitoPoolId)
                        .username(userDto.getEmail())
                        .build();
                cognitoClient.adminDeleteUser(deleteUserRequest);
                throw new InternalServerException("Sign up failed. Please try again");
            }
        } catch (InvalidParameterException e) {
            log.error(e.getMessage());
            throw new BadRequestException("Invalid parameters");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException("Sign up failed. Please try again");
        }
    }

    public ResUserDto updateUser(UpdateUserDto userDto){
        String userId= AuthUtil.getUserId();
        var user= userRepo.findById(userId).orElseThrow(()->new BadRequestException("User not found"));

        String preSignedUrl=null;
        if(userDto.getUrlIcon()!=null) {
            if(!userDto.getUrlIcon().startsWith(s3BaseUrl))
                throw new BadRequestException("Invalid url icon");
            String iconFilename=userDto.getUrlIcon().substring(s3BaseUrl.length());
            if(!FileUtil.isImageUrl(iconFilename))
                throw new BadRequestException("Only support image url for avatar");
            fileService.addIsLinkedTag(iconFilename);
            if(user.getUrlIcon()!=null) fileService.deleteFile(user.getUrlIcon());
            user.setUrlIcon(userDto.getUrlIcon());
        }
        if(userDto.getNickName()!=null) user.setNickName(userDto.getNickName());
        if(userDto.getGender()!=null) user.setGender(userDto.getGender());
        if(userDto.getBirthday()!=null) user.setBirthday(userDto.getBirthday());
        if(userDto.getPhoneNumber()!=null) user.setPhoneNumber(userDto.getPhoneNumber());

        var savedUser= userRepo.save(user);

        var resUserDto=modelMapper.map(savedUser, ResUserDto.class);
        if(preSignedUrl!=null) resUserDto.setUrlIcon(preSignedUrl);

        var friendIds= friendRelationRepo.getFriendsIds(userId);
        for(var friendId: friendIds){
            websocketService.addOrUpdateFriend(friendId, resUserDto);
        }

        return resUserDto;
    }

    public void changeUserStatus(boolean isOnline){
        String userId= AuthUtil.getUserId();
        User user = userRepo.findById(userId).orElseThrow(()->new BadRequestException("User not found"));
        user.setIsOnline(isOnline);
        user.setLastActive(LocalDateTime.now());
        userRepo.save(user);
        var resUserDto=modelMapper.map(user, ResUserDto.class);

        var friendIds= friendRelationRepo.getFriendsIds(userId);
        for(var friendId: friendIds){
            websocketService.addOrUpdateFriend(friendId, resUserDto);
        }
    }

    public ResUserDto getUserInfo(){
        String userId= AuthUtil.getUserId();
        User user = userRepo.findById(userId)
                .orElseThrow(()->new BadRequestException("User not found"));
        return modelMapper.map(user, ResUserDto.class);
    }

    public List<ResUserDto> getUsersByIds(List<String> userIds){
        return userRepo.findByIds(userIds).stream()
                .map(user->modelMapper.map(user, ResUserDto.class))
                .toList();
    }

    public String getEmail(){
        String userId= AuthUtil.getUserId();
        return userRepo.findEmailById(userId);
    }
}
