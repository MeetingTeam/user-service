package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.repositories.UserRepository;
import meetingteam.userservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${cognito.user-pool-id}")
    private String cognitoUserPoolId;

    @Transactional
    public void registerUser(CreateUserDto userDto){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

//        AdminGetUserRequest request = AdminGetUserRequest.builder()
//                .userPoolId(cognitoUserPoolId)
//                .username(userId)
//                .build();
//        AdminGetUserResponse response = cognitoClient.adminGetUser(request);
//        String email=null;
//        response.userAttributes().forEach(attr->{
//            System.out.println(attr.name() + ": " + attr.value());
//        });

//        User user = modelMapper.map(userDto, User.class);
//        user.setIsOnline(false);
//        user.setLastActive(LocalDateTime.now());
//        userRepository.save(user);
    }
}
