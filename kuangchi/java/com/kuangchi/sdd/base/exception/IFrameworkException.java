package com.kuangchi.sdd.base.exception;

import org.apache.log4j.Logger;

public class IFrameworkException extends IException
{
  private static final long serialVersionUID = 1586289080L;
  private Logger logger;

  public IFrameworkException()
  {
    this.logger = Logger.getLogger(getClass());
  }

  public IFrameworkException(String message)
  {
    super(message);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IFrameworkException : " + message);
  }

  public IFrameworkException(Throwable cause) {
    super(cause);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IFrameworkException : " + cause);
  }

  public IFrameworkException(String message, Throwable cause) {
    super(message, cause);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IFrameworkException : " + message, cause);
  }
}