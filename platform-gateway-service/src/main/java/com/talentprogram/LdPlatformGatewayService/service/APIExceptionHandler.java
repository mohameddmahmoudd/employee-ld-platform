package com.talentprogram.LdPlatformGatewayService.service;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler
{
  @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
  public ResponseEntity<ProblemDetail> handleBadRequest(RuntimeException ex, ServerWebExchange exchange) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Bad Request");
    pd.setDetail(ex.getMessage());
    pd.setProperty("path", exchange.getRequest().getURI().getPath());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ProblemDetail> handleValidationErrors(WebExchangeBindException ex, ServerWebExchange exchange) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    pd.setTitle("Validation failed");
    pd.setDetail("One or more fields are invalid");
    Map<String, String> fieldErrors = ex.getFieldErrors().stream()
        .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (a,b) -> a));
    pd.setProperty("errors", fieldErrors);
    pd.setProperty("path", exchange.getRequest().getURI().getPath());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(pd);
  }

   @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ProblemDetail> handleForbidden(AccessDeniedException ex, ServerWebExchange exchange) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    pd.setTitle("Forbidden");
    pd.setDetail(ex.getMessage());
    pd.setProperty("path", exchange.getRequest().getURI().getPath());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, ServerWebExchange exchange) {
    log.error("Unhandled exception at {}: {}", exchange.getRequest().getURI().getPath(), ex.toString(), ex);
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    pd.setTitle("Internal error");
    pd.setDetail("Unexpected error occurred");
    pd.setProperty("path", exchange.getRequest().getURI().getPath());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
  }
}
