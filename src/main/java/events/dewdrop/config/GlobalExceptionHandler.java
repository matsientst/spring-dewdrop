package events.dewdrop.config;


import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import events.dewdrop.api.validators.ValidationError;
import events.dewdrop.api.validators.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    String errorPrefix = "{\"errors\": [\"";
    String errorSuffix = "\"]}";

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toLocalDate());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    @ExceptionHandler(value
        = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> illegalStateException(
        RuntimeException ex, WebRequest request) {
        String bodyOfResponse = errorPrefix + ex.getMessage() + errorSuffix;
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<Object> noSuchElementException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = errorPrefix + ex.getMessage() + errorSuffix;
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> validationException(
        ValidationException ex, WebRequest request) {
        StringBuilder bodyOfResponse = formatErrors(ex);

        return handleExceptionInternal(ex, bodyOfResponse.toString(),
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @NotNull
    private StringBuilder formatErrors(Throwable exception) {
        List<String> messages;
        if (exception instanceof ValidationException) {
            messages = ((ValidationException) exception)
                .getValidationResult()
                .get()
                .stream()
                .map(ValidationError::getMessage)
                .collect(toList());
        } else {
            messages = Arrays.asList(exception.getMessage());
        }

        return formatErrors(messages);
    }

    @NotNull
    StringBuilder formatErrors(List<String> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            messages = Arrays.asList("There was an unknown exception");
        }

        StringBuilder bodyOfResponse = new StringBuilder("{\"errors\": [");

        String result = messages.stream()
            .map(s -> "\"" + s + "\"")
            .collect(Collectors.joining(", "));

        bodyOfResponse.append(result);
        bodyOfResponse.append("]}");
        return bodyOfResponse;
    }

}
