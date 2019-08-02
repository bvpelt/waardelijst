package bsoft.nl.waardelijst.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@ControllerAdvice

public class CustomizedResponseEntityExceptionHandler {

    @Value("${maxCacheTime}")
    private int maxAge;

    @ExceptionHandler(WaardelijstEntryNotFound.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException01(WaardelijstEntryNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.notFound()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .build();
//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WaardelijstNotFound.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException02(WaardelijstNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.notFound()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .build();
// return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WaardelijstenNotFound.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException03(WaardelijstenNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.notFound()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .build();
// return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException99(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.notFound()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .build();
// return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
