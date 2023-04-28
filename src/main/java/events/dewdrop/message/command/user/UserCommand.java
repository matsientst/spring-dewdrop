package events.dewdrop.message.command.user;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import events.dewdrop.aggregate.annotation.AggregateId;
import events.dewdrop.structure.api.Command;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserCommand extends Command {
    @NotNull(message = "UserId is required")
    @AggregateId
    protected UUID userId;

    public UserCommand(UUID userId) {
        this.userId = userId;
    }

}
