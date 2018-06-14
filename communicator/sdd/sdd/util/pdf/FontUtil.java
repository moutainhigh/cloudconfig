package com.kuangchi.sdd.util.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

/**
 * Created by ccjjxl on 2014/11/2.
 */
public class FontUtil {

    public static Font getFont() throws IOException, DocumentException {
        BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese =  new  Font(baseFontChinese , 12 , Font.NORMAL);
        return fontChinese;
    }
}
