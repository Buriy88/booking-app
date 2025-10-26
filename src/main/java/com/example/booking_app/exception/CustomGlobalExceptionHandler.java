package com.example.booking_app.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    private Map<String, Object> baseBody(HttpStatus status,
                                         String error, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(
        EntityNotFoundException ex, HttpServletRequest req) {

        Map<String, Object> body = baseBody(
            HttpStatus.NOT_FOUND, "Entity Not Found", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
        MethodArgumentNotValidException ex, HttpServletRequest req) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = baseBody(
            HttpStatus.BAD_REQUEST, "Validation Error",
            "Validation failed for one or more fields", req.getRequestURI());
        body.put("fields", fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
        Exception ex, HttpServletRequest req) {

        Map<String, Object> body = baseBody(
            HttpStatus.FORBIDDEN, "Access Denied",
            ex.getMessage() != null ? ex.getMessage() : "Forbidden",
            req.getRequestURI());
        body.put("reason", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
        AuthenticationException ex, HttpServletRequest req) {

        Map<String, Object> body = baseBody(
            HttpStatus.UNAUTHORIZED, "Unauthorized",
            ex.getMessage() != null ? ex.getMessage() : "Unauthorized",
            req.getRequestURI());
        body.put("reason", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationException(
        RegistrationException ex, HttpServletRequest req) {

        Map<String, Object> body = baseBody(
            HttpStatus.CONFLICT, "Registration Conflict", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
        DataIntegrityViolationException ex, HttpServletRequest req) {

        Map<String, Object> body = baseBody(
            HttpStatus.CONFLICT, "Data Integrity Violation",
            ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage(),
            req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        // TODO: замінити на логування через SLF4J
        ex.printStackTrace();

        Map<String, Object> body = baseBody(
            HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
            ex.getMessage() != null ? ex.getMessage() : "Unexpected error",
            req.getRequestURI());
        body.put("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
