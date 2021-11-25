package com.topicos.backend.exceptions;


import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String code;

  private final String description;

  private final Integer statusCode;

  public UnauthorizedException(String code, String description) {
    super(description);
    this.code = code;
    this.description = description;
    this.statusCode = HttpStatus.UNAUTHORIZED.value();
  }

  public UnauthorizedException(String code, String description, Integer statusCode, Throwable cause) {
    super(description, cause);
    this.code = code;
    this.description = description;
    this.statusCode = statusCode;
  }

  public String getCode() {
    return this.code;
  }

  public String getDescription() {
    return this.description;
  }

  public Integer getStatusCode() {
    return this.statusCode;
  }

}
