package meetingteam.userservice.dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserDto extends CreateUserDto {
    private String nickName;

    private LocalDate birthday;

    private Boolean gender=true;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number")
    private String phoneNumber;

    private String iconFilename;
}
