package events.dewdrop.usecases.account.create;

import events.dewdrop.Dewdrop;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.account.CreateAccountCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountUseCase {
    @Autowired
    private Dewdrop dewdrop;

    public Result<Boolean> create(CreateAccountCommand createAccountCommand) throws ValidationException {
        return dewdrop.executeCommand(createAccountCommand);
    }
}
