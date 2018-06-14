package com.kuangchi.sdd.util.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class GZIPResponseStream extends ServletOutputStream {

    protected OutputStream bufferedOutput = null;

    protected boolean closed = false;

    protected HttpServletResponse response = null;

    protected ServletOutputStream output = null;

    private int bufferSize = 50000;

    public GZIPResponseStream(HttpServletResponse response) throws IOException {
        this.closed = false;
        this.response = response;
        this.output = response.getOutputStream();
        this.bufferedOutput = new ByteArrayOutputStream();
    }

    public void close() throws IOException {
        if (this.closed) {
            throw new IOException("This output stream has already been closed");
        }

        if (this.bufferedOutput instanceof ByteArrayOutputStream) {
            ByteArrayOutputStream baos = (ByteArrayOutputStream) this.bufferedOutput;

            ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
            GZIPOutputStream gzipstream = new GZIPOutputStream(compressedContent);
            byte[] bytes = baos.toByteArray();
            gzipstream.write(bytes);
            gzipstream.finish();

            byte[] compressedBytes = compressedContent.toByteArray();

            this.response.setContentLength(compressedBytes.length);
            this.response.addHeader("Content-Encoding", "gzip");
            this.output.write(compressedBytes);
            this.output.flush();
            this.output.close();
            this.closed = true;
        } else if (this.bufferedOutput instanceof GZIPOutputStream) {
            GZIPOutputStream gzipstream = (GZIPOutputStream) this.bufferedOutput;

            gzipstream.finish();

            this.output.flush();
            this.output.close();
            this.closed = true;
        }
    }

    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Cannot flush a closed output stream");
        }

        this.bufferedOutput.flush();
    }

    public void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        }

        checkBufferSize(1);

        this.bufferedOutput.write((byte) b);
    }

    private void checkBufferSize(int length) throws IOException {
        if (this.bufferedOutput instanceof ByteArrayOutputStream) {
            ByteArrayOutputStream baos = (ByteArrayOutputStream) this.bufferedOutput;

            if (baos.size() + length > this.bufferSize) {
                this.response.addHeader("Content-Encoding", "gzip");

                byte[] bytes = baos.toByteArray();

                GZIPOutputStream gzipstream = new GZIPOutputStream(this.output);
                gzipstream.write(bytes);

                this.bufferedOutput = gzipstream;
            }
        }
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        }

        checkBufferSize(len);

        this.bufferedOutput.write(b, off, len);
    }

    public boolean closed() {
        return this.closed;
    }

    public void reset() {
    }
}