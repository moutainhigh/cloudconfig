package com.kuangchi.sdd.base.exception;

public class IException extends Exception
{
  private static final long serialVersionUID = -176710809L;

  public IException(String message)
  {
    super(message);
  }
  
  public IException()
  {
  }  

  public IException(Throwable cause) {
    super(cause);
  }

  public IException(String message, Throwable cause) {
    super(message, cause);
  }
}