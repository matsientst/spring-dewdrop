package events.dewdrop.message.command.account;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DepositFundsCommand extends AccountCommand {
    private final BigDecimal amount;
    private final UUID userId;

    public DepositFundsCommand(UUID accountId, BigDecimal amount, UUID userId) {
        super(accountId);
        this.amount = amount;
        this.userId = userId;
    }
}
