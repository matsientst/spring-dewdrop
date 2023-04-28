package events.dewdrop.message.command.user;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserCommand extends UserCommand {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;


    public CreateUserCommand(UUID userId, String username, String email) {
        super(userId);
        this.username = username;
        this.email = email;
    }
}
