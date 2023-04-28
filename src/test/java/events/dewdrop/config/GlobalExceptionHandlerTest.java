package events.dewdrop.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import events.dewdrop.api.validators.ValidationError;
import events.dewdrop.api.validators.ValidationException;
import events.dewdrop.api.validators.ValidationResult;
import events.dewdrop.message.command.user.CreateUserCommand;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;


class GlobalExceptionHandlerTest {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    ServletWebRequest servletWebRequest = new ServletWebRequest(servletRequest);

    @Test
    void validationException() {
        ValidationResult result = ValidationResult
            .of(new ValidationError("no good"))
            .and(ValidationResult
                .of(new ValidationError("super bad")));

        ValidationException exception = new ValidationException(result);

        ResponseEntity response = globalExceptionHandler.validationException(exception, servletWebRequest);
        String body = (String) response.getBody();
        assertEquals("{\"errors\": [\"no good\", \"super bad\"]}", body);
    }

    @Test
    void illegalStateException() {
        IllegalStateException exception = new IllegalStateException("no no no!!");

        ResponseEntity response = globalExceptionHandler.illegalStateException(exception, servletWebRequest);
        String body = (String) response.getBody();
        assertEquals("{\"errors\": [\"no no no!!\"]}", body);
    }


    @Test
    void handleMethodArgumentNotValid() throws NoSuchMethodException {
        CreateUserCommand command = new CreateUserCommand(null, "", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(command, "createUserCommand");
        errors.rejectValue("username", "invalid", "Username required");
        MethodParameter parameter = new MethodParameter(CreateUserCommand.class.getMethod("setUsername", String.class), 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, errors);

        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, servletWebRequest);
        Map<String, List> body = (Map<String, List>) response.getBody();
        assertEquals("Username required", body.get("errors").get(0));
    }

    @Test
    void formatErrors() {
        List<String> messages = List.of("test", "test2");
        StringBuilder builder = globalExceptionHandler.formatErrors(messages);
        assertEquals("{\"errors\": [\"test\", \"test2\"]}", builder.toString());
    }

    @Test
    void formatErrors_null() {
        StringBuilder builder = globalExceptionHandler.formatErrors(null);
        assertEquals("{\"errors\": [\"There was an unknown exception\"]}", builder.toString());
    }
}