package events.dewdrop;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.with;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.entities.AccountDetails;
import events.dewdrop.message.command.account.CreateAccountCommand;
import events.dewdrop.message.command.account.DepositFundsCommand;
import events.dewdrop.message.command.account.WithdrawFundsCommand;
import events.dewdrop.message.command.user.CreateUserCommand;
import events.dewdrop.query.GetAccountByIdQuery;
import events.dewdrop.usecases.account.create.CreateAccountUseCase;
import events.dewdrop.usecases.account.deposit.DepositFundsUseCase;
import events.dewdrop.usecases.account.withdraw.WithdrawFundsUseCase;
import events.dewdrop.usecases.user.create.CreateUserUseCase;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class FullIntegrationTest {
    @Autowired
    CreateUserUseCase createUserUseCase;
    @Autowired
    CreateAccountUseCase createAccountUseCase;
    @Autowired
    DepositFundsUseCase depositFundsUseCase;
    @Autowired
    WithdrawFundsUseCase withdrawFundsUseCase;
    @Autowired
    Dewdrop dewdrop;

    @Test
    void runThrough() throws ValidationException {
        UUID userId = createUser();
        UUID accountId = createAccount(userId);

        depositFunds(userId, accountId);
        withdrawFunds(userId, accountId);

        with().pollInterval(fibonacci(SECONDS))
            .await()
            .until(() -> getByAccountId(accountId));
    }

    private boolean getByAccountId(UUID accountId) {
        Result<AccountDetails> result = dewdrop.executeQuery(new GetAccountByIdQuery(accountId));
        if (result.isValuePresent()) {
            AccountDetails accountDetails = result.get();
            log.info("Found AccountDetails: {}", accountDetails);
            if (accountDetails.getBalance()
                .compareTo(new BigDecimal(125)) == 0) {
                return true;
            }

        }
        return false;
    }

    private void withdrawFunds(UUID userId, UUID accountId) throws ValidationException {
        WithdrawFundsCommand withdrawFundsCommand = new WithdrawFundsCommand(accountId, new BigDecimal(25), userId);
        Result<Boolean> withdrawFundsResult = withdrawFundsUseCase.withdraw(withdrawFundsCommand);
    }

    private void depositFunds(UUID userId, UUID accountId) throws ValidationException {
        DepositFundsCommand depositFundsCommand = new DepositFundsCommand(accountId, new BigDecimal(50), userId);
        Result<Boolean> depositFundsResult = depositFundsUseCase.deposit(depositFundsCommand);
    }

    private UUID createAccount(UUID userId) throws ValidationException {
        UUID accountId = UUID.randomUUID();
        CreateAccountCommand createAccountCommand = new CreateAccountCommand(accountId, userId, new BigDecimal(100));
        Result<Boolean> createAccountResult = createAccountUseCase.create(createAccountCommand);
        return accountId;
    }

    private UUID createUser() throws ValidationException {
        UUID userId = UUID.randomUUID();
        String username = RandomStringUtils.random(10, true, false);
        CreateUserCommand createUserCommand = new CreateUserCommand(userId, username, username + "@gmail.com");
        Result<Boolean> createUserResult = createUserUseCase.create(createUserCommand);
        return userId;
    }
}
