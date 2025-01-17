package meetingteam.userservice.dtos.User;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class CreateUserDto {
    @NotBlank
    private String nickName;

    private LocalDate birthday;

    private String phoneNumber;
}
