package events.dewdrop.aggregate;

import java.util.UUID;
import events.dewdrop.aggregate.annotation.Aggregate;
import events.dewdrop.aggregate.annotation.AggregateId;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.command.CommandHandler;
import events.dewdrop.message.command.user.CreateUserCommand;
import events.dewdrop.message.event.user.UserCreated;
import events.dewdrop.read.readmodel.annotation.EventHandler;
import events.dewdrop.structure.api.validator.DewdropValidator;

@Aggregate
public class UserAggregate {
    @AggregateId
    private UUID userId;
    private String username;

    @CommandHandler
    public UserCreated register(CreateUserCommand command) throws ValidationException {
        DewdropValidator.validate(command);

        return new UserCreated(command.getUserId(), command.getUsername(), command.getEmail());
    }

    @EventHandler
    public void on(UserCreated event) {
        this.userId = event.getUserId();
        this.username = event.getUsername();
    }
}
