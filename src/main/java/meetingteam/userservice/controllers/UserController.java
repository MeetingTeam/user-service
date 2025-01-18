package meetingteam.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity registerUser(@Valid @RequestBody CreateUserDto userDto){
        userService.registerUser(userDto);
        return ResponseEntity.ok().build();
    }
}
