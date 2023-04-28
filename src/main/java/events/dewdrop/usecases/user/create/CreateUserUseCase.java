package events.dewdrop.usecases.user.create;

import events.dewdrop.Dewdrop;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.user.CreateUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCase {
    @Autowired
    private Dewdrop dewdrop;

    public Result<Boolean> create(CreateUserCommand createUserCommand) throws ValidationException {
        return dewdrop.executeCommand(createUserCommand);
    }
}
