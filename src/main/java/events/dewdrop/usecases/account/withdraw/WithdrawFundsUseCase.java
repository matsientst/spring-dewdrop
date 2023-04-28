package events.dewdrop.usecases.account.withdraw;

import events.dewdrop.Dewdrop;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.account.WithdrawFundsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WithdrawFundsUseCase {
    @Autowired
    private Dewdrop dewdrop;

    public Result<Boolean> withdraw(WithdrawFundsCommand withdrawFundsCommand) throws ValidationException {
        return dewdrop.executeCommand(withdrawFundsCommand);
    }
}
