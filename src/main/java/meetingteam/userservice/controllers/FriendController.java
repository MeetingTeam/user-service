package meetingteam.userservice.controllers;

import lombok.RequiredArgsConstructor;
import meetingteam.userservice.services.FriendService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity getFriends(){
        return ResponseEntity.ok(friendService.getFriends());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<HttpStatus> unfriend(
            @PathVariable("friendId") String friendId){
        friendService.unfriend(friendId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/private/is-friend")
    public ResponseEntity<Boolean> isFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId){
        return ResponseEntity.ok(friendService.isFriend(userId, friendId));
    }
}
