package meetingteam.userservice.services;

import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.dtos.User.UpdateUserDto;

public interface UserService {
    void addUser(CreateUserDto userDto);
    ResUserDto updateUser(UpdateUserDto userDto);
    void changeUserStatus(String userId, boolean isOnline);
    ResUserDto getUserInfo();
}
