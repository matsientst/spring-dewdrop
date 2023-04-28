package events.dewdrop.message.event.user;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreated extends UserEvent {
    private String username;
    private String email;

    public UserCreated(UUID userId, String username, String email) {
        super(userId);
        this.username = username;
        this.email = email;
    }
}
