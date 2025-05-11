package meetingteam.userservice.controllers;

import lombok.RequiredArgsConstructor;
import meetingteam.commonlibrary.dtos.PagedResponseDto;
import meetingteam.userservice.dtos.User.ResUserDto;
import meetingteam.userservice.services.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @GetMapping
    public ResponseEntity<PagedResponseDto<ResUserDto>> getFriends(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(friendService.getFriends(pageNo, pageSize));
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<Void> unfriend(
            @PathVariable("friendId") String friendId){
        friendService.unfriend(friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/private/is-friend")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Boolean> isFriend(
            @RequestParam("userId") String userId,
            @RequestParam("friendId") String friendId){
        return ResponseEntity.ok(friendService.isFriend(userId, friendId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResUserDto>> searchFriendsByName(
            @RequestParam("searchName") String searchName){
        return ResponseEntity.ok(friendService.searchFriendsByName(searchName));
    }
}
