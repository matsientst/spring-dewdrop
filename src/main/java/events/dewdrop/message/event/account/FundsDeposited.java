package events.dewdrop.message.event.account;

import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FundsDeposited extends AccountEvent {
    @NotNull(message = "Deposit amount is required")
    @Positive(message = "Deposit amount must be larger than 0")
    private BigDecimal deposit;
    @NotNull(message = "UserId is required")
    private UUID userId;

    public FundsDeposited(UUID accountId, BigDecimal deposit, UUID userId) {
        super(accountId);
        this.deposit = deposit;
        this.userId = userId;
    }
}
