package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.exceptions.InternalServerException;
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
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final FileService fileService;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Value("${cognito.client-id}")
    private String cognitoClientId;

    @Transactional
    public void addUser(CreateUserDto userDto){
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId(cognitoClientId)
                    .username(userDto.getEmail())
                    .password(userDto.getPassword())
                    .build();
            SignUpResponse signUpResponse = cognitoClient.signUp(signUpRequest);
            String userId= signUpResponse.userSub();

            User user = modelMapper.map(userDto, User.class);
            user.setId(userId);
            user.setIsOnline(false);
            user.setLastActive(LocalDateTime.now());
            userRepo.save(user);
        } catch (UsernameExistsException e) {
            throw new BadRequestException("Email already exists");
        } catch (InvalidParameterException e) {
            throw new BadRequestException("Invalid parameters");
        } catch (Exception e) {
            throw new InternalServerException("Sign up failed. Please try again");
        }
    }

    public ResUserDto updateUser(UpdateUserDto userDto){
        String userId= AuthUtil.getUserId();
        var user= userRepo.findById(userId).orElseThrow(()->new BadRequestException("User not found"));

        String preSignedUrl=null;
        if(userDto.getIconFilename()!=null){
            preSignedUrl= fileService.generatePreSignedUrl(userDto.getIconFilename(), user.getUrlIcon());
            user.setUrlIcon(preSignedUrl.split("\\?")[0]);
        }
        if(userDto.getNickName()!=null) user.setNickName(userDto.getNickName());
        if(userDto.getGender()!=null) user.setGender(userDto.getGender());
        if(userDto.getBirthday()!=null) user.setBirthday(userDto.getBirthday());
        if(userDto.getPhoneNumber()!=null) user.setPhoneNumber(userDto.getPhoneNumber());

        var savedUser= userRepo.save(user);

        var resUserDto=modelMapper.map(savedUser, ResUserDto.class);
        if(preSignedUrl!=null) resUserDto.setUrlIcon(preSignedUrl);
        return resUserDto;
    }

    public void changeUserStatus(String userId, boolean isOnline){
        User user = userRepo.findById(userId).orElseThrow(()->new BadRequestException("User not found"));
        user.setIsOnline(isOnline);
        userRepo.save(user);
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
}
