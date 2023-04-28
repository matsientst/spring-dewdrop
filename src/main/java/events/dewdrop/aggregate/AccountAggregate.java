package events.dewdrop.aggregate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import events.dewdrop.aggregate.annotation.Aggregate;
import events.dewdrop.aggregate.annotation.AggregateId;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.command.CommandHandler;
import events.dewdrop.message.command.account.CreateAccountCommand;
import events.dewdrop.message.command.account.DepositFundsCommand;
import events.dewdrop.message.command.account.WithdrawFundsCommand;
import events.dewdrop.message.event.account.AccountCreated;
import events.dewdrop.message.event.account.FundsDeposited;
import events.dewdrop.message.event.account.FundsWithdrawn;
import events.dewdrop.read.readmodel.annotation.EventHandler;
import events.dewdrop.structure.api.Event;
import events.dewdrop.structure.api.validator.DewdropValidator;

@Aggregate
public class AccountAggregate {
    @AggregateId
    private UUID accountId;
    private UUID userId;
    private BigDecimal balance = new BigDecimal(0);

    @CommandHandler
    public List<Event> create(CreateAccountCommand command) throws ValidationException {
        DewdropValidator.validate(command);

        AccountCreated accountCreated = new AccountCreated(command.getAccountId(), command.getUserId());
        FundsDeposited fundsDeposited = new FundsDeposited(command.getAccountId(), command.getInitialDeposit(), command.getUserId());
        return List.of(accountCreated, fundsDeposited);
    }

    @CommandHandler
    public Event deposit(DepositFundsCommand command) throws ValidationException {
        DewdropValidator.validate(command);
        FundsDeposited fundsDeposited = new FundsDeposited(command.getAccountId(), command.getAmount(), command.getUserId());
        return fundsDeposited;
    }

    @CommandHandler
    public Event withdraw(WithdrawFundsCommand command) throws ValidationException {
        DewdropValidator.validate(command);
        FundsWithdrawn fundsDeposited = new FundsWithdrawn(command.getAccountId(), command.getAmount(), command.getUserId());
        return fundsDeposited;
    }

    @EventHandler
    public void on(AccountCreated accountCreated) {
        this.accountId = accountCreated.getAccountId();
        this.userId = accountCreated.getUserId();
    }

    @EventHandler
    public void on(FundsDeposited fundsDeposited) {
        this.balance = this.balance.add(fundsDeposited.getDeposit());
    }

    @EventHandler
    public void on(FundsWithdrawn fundsWithdrawn) {
        this.balance = this.balance.subtract(fundsWithdrawn.getDeposit());
    }
}
