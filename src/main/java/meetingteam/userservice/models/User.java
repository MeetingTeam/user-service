package meetingteam.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @UuidGenerator
    private String id;

    @Column(nullable=false, unique=true)
    private String email;

    private String nickName;

    private String password;

    private String urlIcon;

    private LocalDate birthday;

    private String phoneNumber;

    private String OTPcode; // for change password

    private LocalDateTime OTPtime;

    private Boolean isActivated;

    private LocalDateTime lastActive;

    private String status; //ONLINE, OFFLINE

    private String provider;

    public User(String id) {
        this.id=id;
    }
}
