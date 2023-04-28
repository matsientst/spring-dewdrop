package events.dewdrop.message.event.account;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import events.dewdrop.aggregate.annotation.AggregateId;
import events.dewdrop.structure.api.Event;

@Getter
@NoArgsConstructor
public class AccountEvent extends Event {
    @AggregateId
    protected UUID accountId;

    public AccountEvent(UUID accountId) {
        this.accountId = accountId;
    }
}
