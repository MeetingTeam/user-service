package meetingteam.userservice.controllers;

import lombok.RequiredArgsConstructor;
import meetingteam.userservice.services.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @GetMapping
    public ResponseEntity getFriends(){
        return ResponseEntity.ok(friendService.getFriends());
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<HttpStatus> unfriend(
            @PathVariable("friendId") String friendId){
        friendService.unfriend(friendId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
