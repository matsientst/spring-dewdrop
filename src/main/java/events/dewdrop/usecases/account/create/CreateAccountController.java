package events.dewdrop.usecases.account.create;


import java.net.URI;
import java.util.UUID;
import javax.validation.Valid;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.account.CreateAccountCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    consumes = {MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
public class CreateAccountController {
    @Autowired
    private CreateAccountUseCase createAccountUsecase;


    @PatchMapping("/accounts/create")
    public ResponseEntity create(@Valid @RequestBody CreateAccountCommand createAccountCommand) throws ValidationException {
        Result<Boolean> uuidResult = createAccountUsecase.create(createAccountCommand);
        if (uuidResult.isValuePresent() && uuidResult.get()) {
            UUID userId = createAccountCommand.getAccountId();
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/accounts/{id}")
                .buildAndExpand(userId)
                .toUri();
            return ResponseEntity.created(location)
                .header("id", userId.toString()).build();
        } else {
            return ResponseEntity.internalServerError()
                .header("message", uuidResult.getException()
                    .getMessage())
                .build();
        }
    }
}
