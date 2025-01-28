package meetingteam.userservice.repositories;

import jakarta.transaction.Transactional;
import meetingteam.userservice.models.FriendRelation;
import meetingteam.userservice.models.User;
import meetingteam.userservice.models.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRelationRepository extends JpaRepository<FriendRelation,String> {
    @Query("select fr from FriendRelation fr where (fr.friend1=?1 and fr.friend2=?2) or (fr.friend1=?2 and fr.friend2=?1)")
    public FriendRelation findByUsers(User user1,User user2);

    @Query("select fr1.friend2 from FriendRelation fr1 where fr1.friend1.id=?1 and fr1.status=meetingteam.userservice.models.enums.FriendStatus.FRIEND "
            +"union select fr2.friend1 from FriendRelation fr2 where fr2.friend2.id=?1 and fr2.status=meetingteam.userservice.models.enums.FriendStatus.FRIEND")
    List<User> getFriends(String userId);

    @Modifying
    @Transactional
    @Query("update FriendRelation set status=?1 where (friend1.id=?2 and friend2.id=?3) " +
            "or (friend1.id=?3 and friend2.id=?2)")
    int updateFriendStatus(FriendStatus status, String friendId1, String friendId2);

    @Query("select count(fr) from FriendRelation fr where ((fr.friend1.id=?1 and fr.friend2.id=?2) or (fr.friend1.id=?2 and fr.friend2.id=?1)) and " +
            "fr.status=meetingteam.userservice.models.enums.FriendStatus.FRIEND")
    int havingFriend(String userId, String friendId);
}
