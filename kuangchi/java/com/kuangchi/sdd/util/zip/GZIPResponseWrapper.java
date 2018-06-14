package com.kuangchi.sdd.util.zip;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {

    private Logger logger;

    protected HttpServletResponse origResponse;

    protected ServletOutputStream stream;

    protected PrintWriter writer;

    protected int error;

    public GZIPResponseWrapper(HttpServletResponse response) {
        super(response);

        this.logger = Logger.getLogger(getClass());

        this.origResponse = null;
        this.stream = null;
        this.writer = null;
        this.error = 0;

        this.origResponse = response;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        return new GZIPResponseStream(this.origResponse);
    }

    public void finishResponse() {
        try {
            if (this.writer != null) {
                this.writer.close();
            } else if (this.stream != null)
                this.stream.close();
        } catch (IOException e) {
            this.logger.info("finishResponse", e);
        }
    }

    public void flushBuffer() throws IOException {
        this.stream.flush();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (this.writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }

        if (this.stream == null) {
            this.stream = createOutputStream();
        }

        return this.stream;
    }

    public PrintWriter getWriter() throws IOException {
        if (this.error == 403) {
            return super.getWriter();
        }

        if (this.writer != null) {
            return this.writer;
        }

        if (this.stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }

        this.stream = createOutputStream();
        this.writer = new PrintWriter(new OutputStreamWriter(this.stream, this.origResponse.getCharacterEncoding()));

        return this.writer;
    }

    public void setContentLength(int length) {
    }

    public void sendError(int err, String message) throws IOException {
        super.sendError(err, message);
        this.error = err;

        if (this.logger.isDebugEnabled())
            this.logger.debug("sending error: " + err + " [" + message + "]");
    }
}