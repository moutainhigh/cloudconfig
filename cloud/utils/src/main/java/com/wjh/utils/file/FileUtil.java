package com.wjh.utils.file;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileUtil {

    /**
     * 获取文件绝对路径,参数filePathRelativeToClassPath如:/com/xxxx/xxxx/Test.txt
     *
     * @param filePathRelativeToClassPath
     * @return
     * @throws Exception
     */
    public static String getAbsoluteFilePath(String filePathRelativeToClassPath) throws Exception {
        if (!filePathRelativeToClassPath.startsWith("/")) {
            throw new Exception("parameter filePathRelativeToClassPath must start with '/' ");
        }
        URL url = FileUtil.class.getResource(filePathRelativeToClassPath);
        String path = url.getPath();
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")) {
            path = path.substring(1, path.length());
        }
        return path;
    }

    /**
     * 创建字符文件
     *
     * @param absoluteFilePath
     * @param content
     * @throws Exception
     */
    public static void createFile(String absoluteFilePath, String content) throws Exception {

        if (absoluteFilePath == null) {
            throw new Exception(" parameter pathname can not be null");
        }
        if (absoluteFilePath.endsWith("/") || absoluteFilePath.endsWith("\\")) {
            throw new Exception("\"" + absoluteFilePath + "\" is a directory format");
        }
        if (!absoluteFilePath.contains("/") && !absoluteFilePath.contains("\\")) {
            throw new Exception(absoluteFilePath + " is not a correct file path format ");
        }

        int index1 = absoluteFilePath.lastIndexOf("\\");
        int index2 = absoluteFilePath.lastIndexOf("/");
        int index = index1;
        if (index < index2) {
            index = index2;
        }
        String directory = absoluteFilePath.substring(0, index);
        System.out.println(directory);
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        File contentFile = new File(absoluteFilePath);
        if (contentFile.exists()) {
            contentFile.delete();
        }
        contentFile.createNewFile();
        FileWriter fw = null;
        fw = new FileWriter(contentFile);
        fw.write(content);
        fw.flush();
        fw.close();
    }


    /**
     * 复制文件
     *
     * @param fromFilePath
     * @param toFilePath
     * @throws Exception
     */
    public static void copyFile(String fromFilePath, String toFilePath) throws Exception {
        if (fromFilePath.endsWith("/") || fromFilePath.endsWith("\\")) {
            throw new Exception("\"" + fromFilePath + "\" is a directory format");
        }
        if (toFilePath.endsWith("/") || toFilePath.endsWith("\\")) {
            throw new Exception("\"" + toFilePath + "\" is a directory format");
        }

        File fromFile = new File(fromFilePath);
        if (!fromFile.exists()) {
            throw new Exception("\"" + fromFilePath + "\" does not exist");
        }


        File toFile = new File(toFilePath);
        if (toFile.exists()) {
            toFile.delete();
        }
        toFile.createNewFile();

        copyFile(fromFile, toFile);
    }


    /**
     * 复制文件
     *
     * @param fromFile
     * @param toFile
     * @throws Exception
     */
    public static void copyFile(File fromFile, File toFile) throws Exception {


        if (!fromFile.exists()) {
            throw new Exception("\"" + fromFile.getAbsolutePath() + "\" does not exist");
        }

        if (toFile.exists()) {
            toFile.delete();
        }
        toFile.createNewFile();

        FileInputStream fromFileS = new FileInputStream(fromFile);
        FileOutputStream toFileS = new FileOutputStream(toFile);
        FileChannel fromChannel = fromFileS.getChannel();
        FileChannel toChannel = toFileS.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (fromChannel.read(buffer) > 0) {
            buffer.flip();
            toChannel.write(buffer);
            buffer.clear();
        }

        fromChannel.close();
        fromFileS.close();
        toChannel.close();
        toFileS.close();


    }


    /**
     * 移动文件
     *
     * @param filePathname
     * @param toDirectory
     * @throws Exception
     */
    public static void moveFile(String filePathname, String toDirectory) throws Exception {
        if (!filePathname.contains("/") && !filePathname.contains("\\")) {
            throw new Exception("\"" + filePathname + "\" is not a valid pathname");
        }
        if (filePathname.endsWith("/") || filePathname.endsWith("\\")) {
            throw new Exception("\"" + filePathname + "\" is a directory format");
        }

        if (!toDirectory.contains("/") && !toDirectory.contains("\\") && !toDirectory.contains(":")) {
            throw new Exception("\"" + toDirectory + "\" is not a valid directory format");
        }

        File file = new File(filePathname);
        File directory = new File(toDirectory);
        String toFilename = toDirectory.endsWith("/") || toDirectory.endsWith("\\") ? (toDirectory + file.getName()) : (toDirectory + "/" + file.getName());
        File targetFile = new File(toFilename);

        if (!directory.exists()) {
            throw new Exception("\"" + toDirectory + "\" does not exist ");
        }
        copyFile(file, targetFile);
        file.delete();

    }

    /**
     * 复制目录
     *
     * @param fromDirectory
     * @param toDirectory
     * @throws Exception
     */
    public static void copyDirectory(String fromDirectory, String toDirectory) throws Exception {
        File fromDirectoryFile = new File(fromDirectory);
        File toDirectoryFile = new File(toDirectory);
        copyRecursiveFile(fromDirectoryFile.getAbsolutePath(), fromDirectoryFile, toDirectoryFile.getAbsolutePath());
    }


    private static void copyRecursiveFile(String fromBasePath, File fromFile, String toBasePath) throws Exception {
        if (fromFile.isDirectory()) {
            String fromFileP = (fromFile.getPath() + File.separator).replaceFirst(stringToRegString(fromBasePath), "");
            String absoluteToFileP = toBasePath + File.separator + fromFileP;
            File toFile = new File(absoluteToFileP);
            if (!toFile.exists()) {
                toFile.mkdirs();
            }
            File[] lists = fromFile.listFiles();
            for (File f : lists) {
                copyRecursiveFile(fromBasePath, f, toBasePath);
            }
        } else {
            String fromFileP = (fromFile.getAbsolutePath() + File.separator).replaceFirst(stringToRegString(fromBasePath), "");
            String absoluteToFileP = toBasePath + File.separator + fromFileP;
            File toFile = new File(absoluteToFileP);
            if (toFile.exists()) {
                toFile.delete();
            }
            System.out.println(toFile.getAbsolutePath());
            toFile.createNewFile();
            copyFile(fromFile, toFile);
        }

    }


    /**
     * 移动目录
     *
     * @param fromDirectory
     * @param toDirectory
     * @throws Exception
     */

    public static void moveDirectory(String fromDirectory, String toDirectory) throws Exception {
        copyDirectory(fromDirectory, toDirectory);
        deleteDirectory(fromDirectory);
    }


    /**
     * 删除文件
     *
     * @param filepath
     */
    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    deleteFile(f.getAbsolutePath());
                }
            }
        }
        file.delete();

    }


    /**
     * 删除目录
     *
     * @param directory
     */
    public static void deleteDirectory(String directory) {
        deleteFile(directory);

    }

    /**
     * 读取文件内容
     *
     * @param filepath
     * @param charSet
     * @return
     */
    public static String readTextContent(String filepath, String charSet) {
        File file = new File(filepath);
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        String str = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel channel = fis.getChannel();
            while (channel.read(buffer) > 0) {
                buffer.flip();
                int len = buffer.remaining();
                baos.write(buffer.array(), 0, len);
                buffer.clear();
            }
            str = baos.toString(charSet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return str;

    }


    private static String stringToRegString(String str) {
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (Character ch : chars) {
            if (ch.toString().equals("/")) {
                sb.append("\\" + ch);
            } else if (ch.toString().equals("\\")) {
                sb.append("\\" + ch);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {
//        createFile("f:/abd\\lkj.txt", "abc\n efg");
        System.out.println(readTextContent("f:/abd\\lkj.txt","utf-8"));
//			copyFile("f:/abd\\lkj.txt", "f:/abd\\abc.txt");
//		moveFile("f://lkj.txt", "f:/abd/");	


//			copyDirectory("F:\\abd\\", "F:\\jjj");

        //System.out.print(readTextContent("F:\\jjj\\lkj.txt", "GBK"));
//			moveDirectory("F:\\jjj\\", "f:/ff");
        //deleteFile("f:/abd/");
    }


}
