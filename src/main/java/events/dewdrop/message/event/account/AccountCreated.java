package events.dewdrop.message.event.account;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountCreated extends AccountEvent {
    private UUID userId;

    public AccountCreated(UUID accountId, UUID userId) {
        super(accountId);
        this.userId = userId;
    }
}
