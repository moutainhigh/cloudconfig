package com.xkd.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by gaodd on 17-9-21.
 */
public class ZipUtils {

    Logger log= LoggerFactory.getLogger(ZipUtils.class);


    private static final int BUFFER_SIZE = 8192; // 1024 * 8

    private ZipUtils() {

    }

    /**
     * 将一个目录打包成zip文件
     *
     * @param srcDir  要打包的目录
     * @param zipFile zip文件
     * @return
     */
    public static boolean compress(String srcDir, File zipFile) {
        return compress(srcDir, zipFile, false);
    }


    /**
     * 将一个目录打包成zip文件，并忽略第一层目录名
     *
     * @param srcDir
     * @param zipFile
     * @return
     */
    public static boolean compressWithoutDirectoryName(String srcDir, File zipFile) {
        return compress(srcDir, zipFile, true);
    }

    /**
     * 打包目录到zip文件
     *
     * @param srcDir               要打包的目录
     * @param zipFile              目标zip文件
     * @param withoutDirectoryName 是否忽略第一层目录名
     * @return
     */
    private static boolean compress(String srcDir, File zipFile, boolean withoutDirectoryName) {
        // 如果zip文件已经存在，则退出打包
        if (zipFile.exists()) {
            return false;
        }

        ZipOutputStream zos = null;
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            String os = System.getProperty("os.name");
            zos = new ZipOutputStream(fos);
            String saveFilename = zipFile.getPath();
			if (!os.toLowerCase().startsWith("win")) {
				try {
					Runtime.getRuntime().exec("chmod 777 " + saveFilename);
				} catch (IOException e) {
					 throw new RuntimeException("zip设置777权限失败：" + e.getMessage());
				}
			}
            // 默认的第一层zip目录为空
            String baseDir = "";
            compress(new File(srcDir), zos, baseDir, withoutDirectoryName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("找不到文件：" + e.getMessage());
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭zos流报错：" + e.getMessage());
                }
            }
        }
        return true;
    }


    private static boolean compress(File file, ZipOutputStream zos, String baseDir, boolean withoutDirectoryName) {
        // 如果这个file是目录
        if (file.isDirectory()) {
            // 解析目录中的文件
            if (!withoutDirectoryName) {
                baseDir = baseDir + file.getName() + "/";
            }
            return compressDirectory(file, zos, baseDir);
        }
        // 如果file是文件，那么就直接打包
        return compressFile(file, zos, baseDir);
    }

    private static boolean compressDirectory(File dir, ZipOutputStream zos, String baseDir) {
        // 如果目录不存在了，那么就没有要打包的，直接退出
        if (!dir.exists()) {
            return false;
        }

        // 列出目录中的所有文件
        File[] files = dir.listFiles();
        if (files.length == 0) {
            // 保证空目录也能打包
            return compressEmptyDirectory(dir, zos, baseDir);
        }
        // 分解文件
        for (File file : files) {
            // 然后递归compress方法再次判断是文件还是目录，因为目录中可以有文件，也可以有目录
            // baseDir是用来拼接路径的，如/a/b/c/test.txt 由于用了递归，所以会一层层拼接起来
            // 过程是这样
            // /
            // /a
            // /a/b
            // /a/b/c
            // /a/b/c/test/txt
            compress(file, zos, baseDir, false);
        }
        return true;
    }

    private static boolean compressEmptyDirectory(File file, ZipOutputStream zos, String baseDir) {
        // 如果file不存在了，直接退出
        if (!file.exists()) {
            return false;
        }

        try {
            // 这里ZipEntry的name是目录+fileName拼接起来的
            ZipEntry entry = new ZipEntry(baseDir);
            zos.putNextEntry(entry);
        } catch (IOException e) {
            throw new RuntimeException("添加zip entry错误：" + e.getMessage());
        }
        return true;
    }

    private static boolean compressFile(File file, ZipOutputStream zos, String baseDir) {
        // 如果file不存在了，直接退出
        if (!file.exists()) {
            return false;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            // 这里ZipEntry的name是目录+fileName拼接起来的
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zos.putNextEntry(entry);
            byte buffer[] = new byte[BUFFER_SIZE];
            int count = -1;
            while ((count = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                zos.write(buffer, 0, count);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("找不到文件：" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("添加zip entry错误：" + e.getMessage());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }








    public static void main(String[] args) {

        String srcDir = "F:\\wendanguanli\\download\\20171026140622\\一个空的文件夹";
        File zipFIle = new File("F:\\wendanguanli\\download\\20171026140622\\一个空的文件夹11.zip");

        if (!ZipUtils.compressWithoutDirectoryName(srcDir, zipFIle)) {
            System.out.println("文件打包失败!");
            return;
        }
        System.out.println("文件打包成功!");
    }


}
