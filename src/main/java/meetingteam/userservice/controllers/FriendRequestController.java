package meetingteam.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meetingteam.userservice.dtos.FriendRequest.ResFriendRequestDto;
import meetingteam.userservice.dtos.FriendRequest.CreateRequestDto;
import meetingteam.userservice.services.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend-request")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<Void> createFriendRequest(
            @RequestBody @Valid CreateRequestDto requestDto){
        friendRequestService.createFriendRequest(requestDto.getEmail(), requestDto.getContent());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Void> acceptFriend(
            @PathVariable("requestId") String requestId,
            @RequestParam("isAccepted") boolean isAccepted){
        friendRequestService.acceptFriend(requestId, isAccepted);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteFriendRequest(
            @PathVariable("requestId") String id){
        friendRequestService.deleteFriendRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/received-requests")
    public ResponseEntity<List<ResFriendRequestDto>> getReceivedRequests(){
        return ResponseEntity.ok(friendRequestService.getReceivedRequests());
    }

    @GetMapping("/sent-requests")
    public ResponseEntity<List<ResFriendRequestDto>> getSentRequests(){
        return ResponseEntity.ok(friendRequestService.getSentRequests());
    }
}
