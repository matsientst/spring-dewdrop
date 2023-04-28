package events.dewdrop.repository;

import java.util.Optional;
import java.util.UUID;
import events.dewdrop.entities.AccountDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsRepository extends PagingAndSortingRepository<AccountDetails, UUID> {

    Optional<AccountDetails> findByAccountId(UUID accountId);

    @Query("SELECT coalesce(max(ac.accountVersion), 0) FROM AccountDetails ac")
    Optional<Long> lastAccountVersion();

    @Query("SELECT coalesce(max(ac.userVersion), 0) FROM AccountDetails ac")
    Optional<Long> lastUserVersion();
}
