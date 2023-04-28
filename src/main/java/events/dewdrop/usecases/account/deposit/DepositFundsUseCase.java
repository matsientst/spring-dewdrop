package events.dewdrop.usecases.account.deposit;

import events.dewdrop.Dewdrop;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.account.DepositFundsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositFundsUseCase {
    @Autowired
    private Dewdrop dewdrop;

    public Result<Boolean> deposit(DepositFundsCommand depositFundsCommand) throws ValidationException {
        return dewdrop.executeCommand(depositFundsCommand);
    }
}
