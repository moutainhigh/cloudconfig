package com.kuangchi.sdd.base.exception;

public class IRuntimeException extends RuntimeException
{
  private static final long serialVersionUID = -899136072L;

  public IRuntimeException(String message)
  {
    super(message);
  }
  
  public IRuntimeException()
  {
  }

  public IRuntimeException(Throwable cause) {
    super(cause);
  }

  public IRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}