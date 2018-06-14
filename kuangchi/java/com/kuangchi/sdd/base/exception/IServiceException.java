package com.kuangchi.sdd.base.exception;

import org.apache.log4j.Logger;

public class IServiceException extends IRuntimeException
{
  private static final long serialVersionUID = 1794343867L;
  private Logger logger;

  public IServiceException()
  {
    this.logger = Logger.getLogger(getClass());
  }

  public IServiceException(String message)
  {
    super(message);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IServiceException : " + message);
  }

  public IServiceException(Throwable cause) {
    super(cause);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IServiceException : " + cause);
  }

  public IServiceException(String message, Throwable cause) {
    super(message, cause);

    this.logger = Logger.getLogger(getClass());

    this.logger.error("IServiceException : " + message, cause);
  }
}