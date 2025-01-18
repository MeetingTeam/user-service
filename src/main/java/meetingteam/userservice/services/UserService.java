package meetingteam.userservice.services;

import meetingteam.userservice.dtos.User.CreateUserDto;

public interface UserService {
    void registerUser(CreateUserDto userDto);
}
