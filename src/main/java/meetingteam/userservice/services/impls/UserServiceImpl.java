package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.dtos.User.UpdateUserDto;
import meetingteam.userservice.models.User;
import meetingteam.userservice.repositories.UserRepository;
import meetingteam.userservice.services.FileService;
import meetingteam.userservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${cognito.user-pool-id}")
    private String cognitoUserPoolId;
    @Value("${s3.bucket-name}")
    private String bucketUrl;

    @Transactional
    public void addUser(CreateUserDto userDto){
        String userId= AuthUtil.getUserId();

        var getUserRequest= AdminGetUserRequest.builder()
                .userPoolId(cognitoUserPoolId)
                .username(userId)
                .build();
        var getUserResponse= cognitoClient.adminGetUser(getUserRequest);

        String email= null;
        for(var attr: getUserResponse.userAttributes()){
            if(attr.name().equals("email")) email= attr.value();
        }
        if(email == null) throw new BadRequestException("Email is required");

        User user = modelMapper.map(userDto, User.class);
        user.setId(userId);
        user.setEmail(email);
        user.setIsOnline(false);
        user.setLastActive(LocalDateTime.now());
        userRepository.save(user);
    }

    public ResUserDto updateUser(UpdateUserDto userDto){
        String userId= AuthUtil.getUserId();
        var user= userRepository.findById(userId).orElseThrow(()->new BadRequestException("User not found"));

        String preSignedUrl=null;
        if(userDto.getIconFilename()!=null){
            preSignedUrl= fileService.generatePreSignedUrl("users",userDto.getIconFilename(), user.getUrlIcon());
            user.setUrlIcon(preSignedUrl.split("\\?")[0]);
        }
        user.setNickName(userDto.getNickName());
        user.setGender(userDto.getGender());
        user.setBirthday(userDto.getBirthday());
        user.setPhoneNumber(userDto.getPhoneNumber());

        var savedUser= userRepository.save(user);

        var resUserDto=modelMapper.map(savedUser, ResUserDto.class);
        if(preSignedUrl!=null) resUserDto.setUrlIcon(preSignedUrl);
        return resUserDto;
    }

    public void changeUserStatus(String userId, boolean isOnline){
        User user = userRepository.findById(userId).orElseThrow(()->new BadRequestException("User not found"));
        user.setIsOnline(isOnline);
        userRepository.save(user);
    }

    public ResUserDto getUserInfo(){
        String userId= AuthUtil.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()->new BadRequestException("User not found"));
        return modelMapper.map(user, ResUserDto.class);
    }
}
