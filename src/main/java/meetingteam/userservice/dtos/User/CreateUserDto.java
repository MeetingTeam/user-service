package meetingteam.userservice.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    @NotBlank
    private String nickName;

    private LocalDate birthday;

    private Boolean gender;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number")
    private String phoneNumber;
}
