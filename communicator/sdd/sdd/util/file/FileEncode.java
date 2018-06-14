package com.kuangchi.sdd.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileEncode {

	public static void fileEncode(String filePath) throws IOException {

		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("文件不存在");
		}

		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

		byte[] headFile = new byte[10];

		int readTotal = randomAccessFile.read(headFile, 0, headFile.length);
		if (readTotal <= 0) {
			randomAccessFile.close();
			throw new IOException("文件读取失败");
		}
		changeBytePostion(headFile, readTotal);
		randomAccessFile.seek(0);
		randomAccessFile.write(headFile, 0, readTotal);
		randomAccessFile.close();

	}
	
	

	/**
	 * 前后置换
	 * @param bytes
	 * @param postion
	 */
	private static void changeBytePostion(byte[] bytes, int postion) {
		for (int i = 0; i < postion / 2; i++) {
			byte tmp = bytes[i];
			int p = postion - 1 - i;
			bytes[i] = bytes[p];
			bytes[p] = tmp;

		}
	}

	public static void main(String[] args) {
		String file = "F://test.jpg";

		try {
			fileEncode(file);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
