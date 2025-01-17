package meetingteam.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import meetingteam.userservice.models.enums.FriendStatus;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRelation {
    @Id @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name="friend1Id", nullable=false)
    private User friend1;

    @ManyToOne
    @JoinColumn(name="friend2Id", nullable=false)
    private User friend2;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private FriendStatus status;
}
