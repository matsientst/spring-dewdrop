package events.dewdrop.message.command.account;


import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAccountCommand extends AccountCommand {
    @NotNull(message = "UserId is required")
    private UUID userId;
    @NotNull(message = "Deposit amount is required")
    @Positive(message = "Initial deposit must be larger than 0")
    private BigDecimal initialDeposit;

    public CreateAccountCommand(UUID accountId, UUID userId, BigDecimal initialDeposit) {
        super(accountId);
        this.userId = userId;
        this.initialDeposit = initialDeposit;
    }
}
