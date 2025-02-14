package meetingteam.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FriendRequest {
	@Id @UuidGenerator
	private String id;

	@ManyToOne
	@JoinColumn(name="senderId")
	private User sender; 

	@ManyToOne
	@JoinColumn(name="recipientId")
	private User recipient; // friend request

	@Column(columnDefinition = "TEXT")
	private String content;

	private Boolean isAccepted;

	@Column(nullable = false)
	private LocalDateTime createdAt;
}
