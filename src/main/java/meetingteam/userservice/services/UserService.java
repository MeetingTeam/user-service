package meetingteam.userservice.services;

import lombok.RequiredArgsConstructor;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.models.User;
import meetingteam.userservice.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void registerUser(CreateUserDto userDto){
        User user = modelMapper.map(userDto, User.class);
        user.setIsOnline(false);
        user.setLastActive(LocalDateTime.now());

    }
}
