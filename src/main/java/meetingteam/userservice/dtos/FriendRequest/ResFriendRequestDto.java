package meetingteam.userservice.dtos.FriendRequest;

import lombok.Data;
import meetingteam.userservice.dtos.User.ResUserDto;

import java.time.LocalDateTime;

@Data
public class ResFriendRequestDto {
    private String id;

    private ResUserDto sender;

    private ResUserDto recipient;

    private String content;

    private Boolean isAccepted;

    private LocalDateTime createdAt;
}
