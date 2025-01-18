package meetingteam.userservice.dtos.User;

import lombok.Data;

@Data
public class UpdateUserDto extends CreateUserDto {
    private String iconFilename;
}
