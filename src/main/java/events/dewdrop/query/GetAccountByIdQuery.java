package events.dewdrop.query;

import java.util.UUID;
import lombok.Data;

@Data
public class GetAccountByIdQuery {
    private UUID accountId;

    public GetAccountByIdQuery(UUID accountId) {
        this.accountId = accountId;
    }
}

