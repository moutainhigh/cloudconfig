package com.kuangchi.sdd.util.commonUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.struts2.ServletActionContext;

public class FileUpLoadUtil {

    public static final String UPLOAD_FILE_PATH = "file/upload";

    public static final int MAX_FILE_SIZE = 5120;

    public static final String DOWNLOAD_ATTACH_TYPE = "attach";

    public static final String DOWNLOAD_INLINE_TYPE = "inline";

    private static final int bufferSize = 5120;

    public static int fileUpload(File[] file, String[] fileName) {
        int count = 0;
        File[] srcFiles = file;

        for (int i = 0; i < srcFiles.length; ++i) {
            String dstPath = ServletActionContext.getServletContext().getRealPath("file/upload") + "\\" + fileName[i];
            File dstFiles = new File(dstPath);
            count = count + copy(srcFiles[i], dstFiles);
        }
        return count;
    }

    private static int copy(File oldFile, File newFile) {
        int count;
        InputStream in = null;
        OutputStream out = null;
        count = 0;
        in = null;
        out = null;
        if (!oldFile.exists())
            return 0;
        try {
            in = new BufferedInputStream(new FileInputStream(oldFile), 5120);
            out = new BufferedOutputStream(new FileOutputStream(newFile), 5120);
            byte buffer[] = new byte[5120];
            for (int len = 0; (len = in.read(buffer)) > 0;)
                out.write(buffer, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != in)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (null != out)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        count = 1;
        return count;
    }
}