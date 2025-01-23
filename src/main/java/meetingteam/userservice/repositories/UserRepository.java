package meetingteam.userservice.repositories;

import meetingteam.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.id in ?1")
    List<User> findByIds(List<String> ids);
}
