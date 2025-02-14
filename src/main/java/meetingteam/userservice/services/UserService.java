package meetingteam.userservice.services;

import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.dtos.User.UpdateUserDto;

import java.util.List;

public interface UserService {
    void addUser(CreateUserDto userDto);
    ResUserDto updateUser(UpdateUserDto userDto);
    void changeUserStatus(boolean isOnline);
    ResUserDto getUserInfo();
    List<ResUserDto> getUsersByIds(List<String> userIds);
    String getEmail();
}
