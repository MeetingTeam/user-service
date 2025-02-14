package meetingteam.userservice.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
        message="Password must be at least 1 number, 1 special character, 1 uppercase letter, 1 lowercase letter")
    private String password;

    @NotBlank
    private String nickName;

    private LocalDate birthday;

    private Boolean gender=true;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number")
    private String phoneNumber;
}
