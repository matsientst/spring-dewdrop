package events.dewdrop.usecases.user.create;

import java.net.URI;
import java.util.UUID;
import javax.validation.Valid;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.user.CreateUserCommand;
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
public class CreateUserController {
    @Autowired
    private CreateUserUseCase createUserUsecase;

    @PatchMapping("/users/create")
    public ResponseEntity create(@Valid @RequestBody CreateUserCommand createUserCommand) throws ValidationException {
        Result<Boolean> uuidResult = createUserUsecase.create(createUserCommand);
        if (uuidResult.isValuePresent() && uuidResult.get()) {
            UUID userId = createUserCommand.getUserId();
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{id}")
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
