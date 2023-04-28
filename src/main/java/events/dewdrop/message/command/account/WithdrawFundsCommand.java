package events.dewdrop.message.command.account;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class WithdrawFundsCommand extends AccountCommand {
    private final BigDecimal amount;
    private final UUID userId;

    public WithdrawFundsCommand(UUID accountId, BigDecimal amount, UUID userId) {
        super(accountId);
        this.amount = amount;
        this.userId = userId;
    }
}
