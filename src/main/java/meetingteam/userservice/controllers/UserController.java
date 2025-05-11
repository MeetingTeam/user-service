package meetingteam.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.userservice.dtos.User.CreateUserDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.dtos.User.UpdateUserDto;
import meetingteam.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResUserDto> addUser(
            @Valid @RequestBody CreateUserDto userDto){
        userService.addUser(userDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<ResUserDto> updateUser(
            @Valid @RequestBody UpdateUserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @PostMapping("/private/user-status")
    public ResponseEntity<ResUserDto> changeUserStatus(
            @RequestParam("isOnline") boolean isOnline){
        userService.changeUserStatus(isOnline);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResUserDto> getUserInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PostMapping("/private/by-ids")
    public ResponseEntity<List<ResUserDto>> getUsersByIds(
            @RequestBody List<String> userIds){
        return ResponseEntity.ok(userService.getUsersByIds(userIds));
    }

    @GetMapping("/private/email")
    public ResponseEntity<String> getEmail(){
        return ResponseEntity.ok(userService.getEmail());
    }

    @GetMapping("/version")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<String> getVersion(){
        return ResponseEntity.ok("version 1.0");
    }
}
