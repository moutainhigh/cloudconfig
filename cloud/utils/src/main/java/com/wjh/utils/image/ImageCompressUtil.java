package com.wjh.utils.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageCompressUtil {
	/**
	 * 压缩图片 
	 * @param srcImgPath
	 * @param distImgPath
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	 public static void resizeImage(String srcImgPath, String distImgPath,int width, int height) throws IOException {  

		        String subfix = "jpg";

		        subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1,srcImgPath.length());

		         

		        File srcFile = new File(srcImgPath);  

		        Image srcImg = ImageIO.read(srcFile);  

		         

		        BufferedImage buffImg = null; 

		        if(subfix.equals("png")){

		            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		        }else{

		            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		        }

		     

		        Graphics2D graphics = buffImg.createGraphics();

		        graphics.setBackground(Color.WHITE);

		        graphics.setColor(Color.WHITE);

		        graphics.fillRect(0, 0, width, height);

		        graphics.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);  

		 

		        ImageIO.write(buffImg, subfix, new File(distImgPath));  

		    }  
	 
	 public static void main(String[] args) {
		try {
			resizeImage("f:/abc.jpg", "f:/xyz.jpg", 100, 100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
