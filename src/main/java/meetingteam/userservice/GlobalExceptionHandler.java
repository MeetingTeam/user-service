package meetingteam.userservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import meetingteam.commonlibrary.dtos.ErrorDto;
import meetingteam.commonlibrary.exceptions.BadRequestException;
import meetingteam.commonlibrary.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final Logger LOGGER= LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.BAD_REQUEST,"Bad request", ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDeniedException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.FORBIDDEN,"Access denied", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ErrorDto> handleUnAuthorizedException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.UNAUTHORIZED,"Unauthorized", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> handleValidationException(BindException ex){
        LOGGER.error(ex.getMessage());
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error","", errors);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintException(ConstraintViolationException ex){
        LOGGER.error(ex.getMessage());
        Map<String, String> errors = new HashMap();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for(var violation : violations){
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error","", errors);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDto> handleHandlerMethodValidationException(HandlerMethodValidationException ex){
        LOGGER.error(ex.getMessage());
        var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",ex.getReason());
        return ResponseEntity.status(500).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnknownException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
