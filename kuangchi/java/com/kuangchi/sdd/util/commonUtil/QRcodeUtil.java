package com.kuangchi.sdd.util.commonUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class QRcodeUtil {

	private static final Map<EncodeHintType, Object> hints;
    private static final Map<DecodeHintType, String> hint;
    static {
        hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        hint = new HashMap<DecodeHintType, String>();
        hint.put(DecodeHintType.CHARACTER_SET, "UTF-8");
    }
    public static File makeQRcode(String content, String file) {
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 500, 500, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        File fileCode = new File(file);
        try {
            MatrixToImageWriter.writeToFile(matrix, "png", fileCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCode;
    }
    
    public static File makeQRcode(String content){
    	
    	File file = new File(System.getProperty("java.io.tmpdir"),UUID.randomUUID().toString()+".png");
        return  makeQRcode(content,file.getAbsolutePath());
    }

    public static String decodeQRcode(String filePath) {
        File file = new File(filePath);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = null;
        try {
            result = new MultiFormatReader().decode(bitmap, hint);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public static void main(String[] args) {
        /*String file = "code.png";
        String content = "nishi";
        makeQRcode(content,file);*/
        
      //  String d = decodeQRcode(file);
      //  System.out.println(d);
        
        
        
        String file = "code.png";
        String content = "1234567890";
        //makeQRcodeCODE_128(content,file);
        
        System.out.println(QRcodeUtil.makeQRcode(content));
    }
    
    
    
    public static File makeQRcodeCODE_128(String content, String file) {
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 500, 200, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        File fileCode = new File(file);
        try {
            MatrixToImageWriter.writeToFile(matrix, "png", fileCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCode;
    }
}
