package com.kuangchi.sdd.base.exception;


import org.apache.log4j.Logger;

public class IDataAccessException extends IRuntimeException
{
  private static final long serialVersionUID = 379501467L;
  private Logger logger;

  public IDataAccessException()
  {
    this.logger = Logger.getLogger(getClass());
  }

  public IDataAccessException(String message)
  {
    super(message);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IDataAccessException : " + message);
  }

  public IDataAccessException(Throwable cause) {
    super(cause);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IDataAccessException : " + cause);
  }

  public IDataAccessException(String message, Throwable cause) {
    super(message, cause);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IDataAccessException : " + message, cause);
  }
}