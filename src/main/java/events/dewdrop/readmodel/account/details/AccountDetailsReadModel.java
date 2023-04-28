package events.dewdrop.readmodel.account.details;

import events.dewdrop.api.result.Result;
import events.dewdrop.entities.AccountDetails;
import events.dewdrop.message.event.account.AccountCreated;
import events.dewdrop.message.event.account.FundsDeposited;
import events.dewdrop.message.event.account.FundsWithdrawn;
import events.dewdrop.query.GetAccountByIdQuery;
import events.dewdrop.read.readmodel.annotation.EventHandler;
import events.dewdrop.read.readmodel.annotation.ReadModel;
import events.dewdrop.read.readmodel.annotation.Stream;
import events.dewdrop.read.readmodel.annotation.StreamStartPosition;
import events.dewdrop.read.readmodel.query.QueryHandler;
import events.dewdrop.read.readmodel.stream.StreamType;
import events.dewdrop.repository.AccountDetailsRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@ReadModel
@Stream(name = "AccountAggregate")
@Stream(name = "UserCreated", streamType = StreamType.EVENT, subscribed = false)
public class AccountDetailsReadModel {
    @Autowired
    AccountDetailsRepository accountDetailsRepository;

    @StreamStartPosition(name = "AccountAggregate")
    public Long startPosition() {
        Optional<Long> position = accountDetailsRepository.lastAccountVersion();
        return position.orElse(0L);
    }

    @StreamStartPosition(name = "UserCreated", streamType = StreamType.EVENT)
    public Long startPositionForUser() {
        Optional<Long> position = accountDetailsRepository.lastUserVersion();
        return position.orElse(0L);
    }

    @EventHandler
    @Transactional
    public void on(AccountCreated event) {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.on(event);
        accountDetailsRepository.save(accountDetails);
        log.info("saved account:{}", accountDetails);
    }

    @EventHandler
    @Transactional
    public void on(FundsDeposited event) {
        Optional<AccountDetails> accountDetails = accountDetailsRepository.findByAccountId(event.getAccountId());
        accountDetails.ifPresent(details -> details.on(event));
    }

    @EventHandler
    @Transactional
    public void on(FundsWithdrawn event) {
        Optional<AccountDetails> accountDetails = accountDetailsRepository.findByAccountId(event.getAccountId());
        accountDetails.ifPresent(details -> details.on(event));
    }

    @QueryHandler
    public Result<AccountDetails> findByAccountId(GetAccountByIdQuery query) {
        Optional<AccountDetails> byAccountId = accountDetailsRepository.findByAccountId(query.getAccountId());
        log.info("findByAccountId:{}", byAccountId);
        return byAccountId.stream()
            .findFirst()
            .map(Result::of)
            .orElse(Result.empty());
    }
}
