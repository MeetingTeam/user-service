package meetingteam.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String nickName;

    private String urlIcon;

    private LocalDate birthday;

    private String phoneNumber;

    private LocalDateTime lastActive;

    private Boolean isOnline;
}
