package com.kuangchi.sdd.util.commonUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

public class FileUtil {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public static String readFile(String pathname) {
        return readFile(new File(pathname));
    }

    public static String readFile(File file) {
        StringBuilder sr;
        FileReader fr = null;
        BufferedReader br = null;
        sr = new StringBuilder();
        try {
        fr = new FileReader(file);
        for (br = new BufferedReader(fr); br.ready(); sr.append(br.readLine()))
            ;
        
            if (br != null) {
                br.close();
                br = null;
            }
            if (fr != null) {
                fr.close();
                fr = null;
            }
        }
        catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (fr != null) {
                    fr.close();
                    fr = null;
                }
            }
            catch (Exception re) {
                re.printStackTrace();
            }
        }
        return sr.toString();

    }

    public static String writeFile(String oldpath, String newpath) {
        return writeFile(new File(oldpath), new File(newpath));
    }

    public static String writeFile(File oldFile, File newFile) {
        StringBuilder sr = new StringBuilder();
        FileReader fr = null;
        FileWriter fw = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            fr = new FileReader(oldFile);
            fw = new FileWriter(newFile);
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            while (br.ready()) {
                sr.append(br.readLine());
                bw.write(sr.toString());
                bw.newLine();
            }
            bw.flush();
        } catch (FileNotFoundException re) {
            re.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (fw != null) {
                    fw.close();
                    fw = null;
                }
                if (fr != null) {
                    fr.close();
                    fr = null;
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return sr.toString();
    }

    public static boolean stringToFile(String str, String pathname) {
        char[] buf;
        boolean flag = true;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            File file = new File(pathname);
            if (!(file.getParentFile().exists()))
                file.getParentFile().mkdirs();
            br = new BufferedReader(new StringReader(str));
            bw = new BufferedWriter(new FileWriter(file));
            buf = new char[1024];
            while (true) {
                int len;
                if ((len = br.read(buf)) == -1)
                    break;
                bw.write(buf, 0, len);
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            buf = null;
            return flag;
        } finally {
            try {
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return flag;
    }

    public static String fileToString(File file) {
        return fileToString(file, null);
    }

    public static String fileToString(File file, String encoding) {
        int n;
        InputStreamReader isr = null;
        StringWriter sw = new StringWriter();
        try {
            if ((encoding == null) || ("".equals(encoding.trim())))
                isr = new InputStreamReader(new FileInputStream(file));
            else {
                isr = new InputStreamReader(new FileInputStream(file), encoding);
            }

            char[] buffer = new char[1024];
            n = 0;
            while (true) {
                if (-1 == (n = isr.read(buffer)))
                    break;
                sw.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (isr != null) {
                    isr.close();
                    isr = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ((sw != null) ? sw.toString() : null);
    }

    public static String getFileLength(long length) {
        double f = Math.round(length / 1024.0D * 100.0D) / 100.0D;
        return f + "k";
    }

    public static String getExtension(String filename) {
        if ((filename == null) || ("".equals(filename)))
            return "";
        int i = filename.lastIndexOf(46);
        if (i >= 0)
            return filename.substring(i + 1).toLowerCase();

        return "";
    }

    public static boolean isDir(String pathname) {
        if ((pathname == null) || ("".equals(pathname)))
            return false;
        return new File(pathname).isDirectory();
    }

    public static boolean isExists(String pathname) {
        if ((pathname == null) || ("".equals(pathname)))
            return false;
        return new File(pathname).exists();
    }

    public static boolean isFile(String pathname) {
        if ((pathname == null) || ("".equals(pathname)))
            return false;
        return new File(pathname).isFile();
    }

    public static boolean isHidden(String pathname) {
        if ((pathname == null) || ("".equals(pathname)))
            return false;
        return new File(pathname).isHidden();
    }

    public static boolean canRead(String pathname) {
        if ((pathname == null) || ("".equals(pathname)))
            return false;
        return new File(pathname).canRead();
    }

    public static boolean canWrite(String pathname) {
        if ((pathname == null) || ("".equals(pathname)))
            return false;
        return new File(pathname).canWrite();
    }

    public static String getFileName(String pathname) {
        char separatorChar;
        try {
            separatorChar = '/';
            int index = pathname.lastIndexOf(separatorChar);
            if (index < 0) {
                separatorChar = '\\';
                index = pathname.lastIndexOf(separatorChar);
            }
            return pathname.substring(index + 1);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (int i = 0; i < files.length; ++i)
                    delete(files[i]);

            file.delete();
        } else
            file.delete();
    }

    public static void main(String[] args) throws Exception {
    }
}