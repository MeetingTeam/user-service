package meetingteam.userservice.dtos.User;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResUserDto {
    private String id;

    private String email;

    private String nickName;

    private String urlIcon;

    private Boolean gender;

    private LocalDate birthday;

    private String phoneNumber;

    private LocalDateTime lastActive;

    private Boolean isOnline;
}
