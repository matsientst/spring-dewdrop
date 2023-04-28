package events.dewdrop.usecases.account.withdraw;

import javax.validation.Valid;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.account.WithdrawFundsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    consumes = {MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
public class WithdrawFundsController {
    @Autowired
    WithdrawFundsUseCase withdrawFundsUseCase;

    @PatchMapping("/accounts/withdraw")
    public ResponseEntity deposit(@Valid @RequestBody WithdrawFundsCommand withdrawFundsCommand) throws ValidationException {
        Result<Boolean> uuidResult = withdrawFundsUseCase.withdraw(withdrawFundsCommand);
        if (uuidResult.isValuePresent() && uuidResult.get()) {
            return ResponseEntity.ok()
                .build();
        } else {
            return ResponseEntity.internalServerError()
                .header("message", uuidResult.getException()
                    .getMessage())
                .build();
        }
    }
}
