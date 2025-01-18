package meetingteam.userservice.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.exceptions.InternalServerException;
import meetingteam.commonlibrary.exceptions.UnauthorizedException;
import meetingteam.commonlibrary.utils.AuthUtil;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.models.User;
import meetingteam.userservice.repositories.UserRepository;
import meetingteam.userservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ServerErrorException;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${cognito.user-pool-id}")
    private String cognitoUserPoolId;

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
}
