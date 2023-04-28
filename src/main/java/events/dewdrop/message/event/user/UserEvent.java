package events.dewdrop.message.event.user;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import events.dewdrop.aggregate.annotation.AggregateId;
import events.dewdrop.structure.api.Event;

@Getter
@NoArgsConstructor
public class UserEvent extends Event {
    @AggregateId
    protected UUID userId;

    public UserEvent(UUID userId) {
        this.userId = userId;
    }
}
