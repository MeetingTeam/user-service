package meetingteam.userservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import meetingteam.userservice.dtos.User.CreateUserDto;

@SpringBootConfiguration
class UserServiceApplicationTests {
    private final Validator validator;

    public UserServiceApplicationTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void contextLoads() {
        var createUserDto= new CreateUserDto();
        createUserDto.setNickName("HungTran");
        createUserDto.setEmail("tienhung170904@gmail.com");
        createUserDto.setGender(false);

        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(createUserDto);
        assertTrue(!violations.isEmpty(), "DTO should be invalid");
    }
}
