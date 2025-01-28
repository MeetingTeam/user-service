package meetingteam.userservice.dtos.FriendRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotNull
    private String content;
}
