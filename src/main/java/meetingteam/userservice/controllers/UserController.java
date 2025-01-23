package meetingteam.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.dtos.User.UpdateUserDto;
import meetingteam.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity addUser(
            @Valid @RequestBody CreateUserDto userDto){
        userService.addUser(userDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity updateUser(
            @Valid @RequestBody UpdateUserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @PostMapping("/private/user-status")
    public ResponseEntity changeUserStatus(
            @RequestParam("userId") String userId,
            @RequestParam("isOnline") boolean isOnline){
        userService.changeUserStatus(userId, isOnline);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity getUserInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }
}
