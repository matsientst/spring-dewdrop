package events.dewdrop.usecases.user.create;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import events.dewdrop.Dewdrop;
import events.dewdrop.api.result.Result;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.message.command.user.CreateUserCommand;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {
    @Mock
    private Dewdrop dewdrop;
    @InjectMocks
    CreateUserUseCase createUserUseCase;

    @Test
    @DisplayName("create() - Given the needed parameters, when create() is called, then dewdrop.executeCommand() is called")
    void create() throws ValidationException {
        doReturn(Result.of(true)).when(dewdrop)
            .executeCommand(any(CreateUserCommand.class));

        CreateUserCommand createUserCommand = new CreateUserCommand(UUID.randomUUID(), "username", "email");
        Result<Boolean> result = createUserUseCase.create(createUserCommand);
        assertThat(result.get(), is(true));
    }
}