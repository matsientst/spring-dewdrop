package events.dewdrop.entities;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import events.dewdrop.message.event.account.AccountCreated;
import events.dewdrop.message.event.account.FundsDeposited;
import events.dewdrop.message.event.account.FundsWithdrawn;

@Entity
@Table(name = "account_details")
@Data
@NoArgsConstructor
@ToString
@Log4j2
public class AccountDetails {
    @Id
    private UUID accountId;
    @Column(name = "username")
    private String username;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "email")
    private String email;
    @Column(name = "balance")
    private BigDecimal balance = new BigDecimal(0);
    @Column(name = "accountVersion")
    private Long accountVersion;
    @Column(name = "userVersion")
    private Long userVersion;


    public void on(AccountCreated event) {
        setAccountId(event.getAccountId());
        setUserId(event.getUserId());
        setAccountVersion(event.getVersion());
        log.info("Creation() - AccountDetails:{}", this);
    }

    public void on(FundsDeposited event) {
        setBalance(getBalance().add(event.getDeposit()));
        setAccountVersion(event.getVersion());
        log.info("FundsDeposited() - AccountDetails:{}", this);
    }

    public void on(FundsWithdrawn event) {
        setBalance(getBalance()
            .subtract(event.getDeposit()));
        setAccountVersion(event.getVersion());
        log.info("FundsWithdrawn() - AccountDetails:{}", this);
    }
}
