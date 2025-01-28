package meetingteam.userservice.repositories;

import meetingteam.userservice.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    @Query("select fr from FriendRequest fr where fr.recipient.id=?1 and fr.isAccepted is null")
    List<FriendRequest> getReceivedRequests(String userId);

    @Query("select fr from FriendRequest fr where fr.sender.id=?1")
    List<FriendRequest> getSentRequests(String userId);
}
