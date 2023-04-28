package events.dewdrop.message.command.account;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import events.dewdrop.aggregate.annotation.AggregateId;
import events.dewdrop.structure.api.Command;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AccountCommand extends Command {
    @NotNull(message = "AccountId is required")
    @AggregateId
    protected UUID accountId;

    public AccountCommand(UUID accountId) {
        this.accountId = accountId;
    }

}
