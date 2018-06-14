package com.kuangchi.sdd.util.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {
	
	public static final Logger LOG = Logger.getLogger(PdfUtil.class);
	
	/**
	 * 图片转成单页pdf
	 * @param img 图片路径
	 * @return 转换的pdf file
	 * @throws Exception
	 */
	 public static File getFile(String img) throws Exception {
	        File fileImg = new File(img);
	        if (!fileImg.exists()){
	            throw  new FileNotFoundException("图片文件不存在");
	        }

	        Document document = new Document();
	        File res = new File(System.getProperty("java.io.tmpdir"),UUID.randomUUID().toString() +".pdf");
	        try {
	            PdfWriter.getInstance(document, new FileOutputStream(res));
	            document.open();
	            Image image = Image.getInstance(img);
	            document.add(image);
	        } catch (DocumentException e) {
	            e.printStackTrace();
	            throw new Exception(e);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            throw  new FileNotFoundException("生成的pdf文件不存在");
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new IOException(e);
	        }finally {
	            document.close();
	        }
	        return res;
	    }
	 
	public static File pdfMerge(String[] pdfFiles) throws DocumentException,
			IOException {
		File pdfMerge = new File(System.getProperty("java.io.tmpdir"),
				UUID.randomUUID().toString()+ ".pdf");

		FileOutputStream out = new FileOutputStream(pdfMerge);

		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open();

		PdfContentByte cb = writer.getDirectContent();

		int pageOfCurrentReaderPDF = 0;

		for (String pdf : pdfFiles) {
			try{
				PdfReader pdfReader = new PdfReader(pdf);
				int pdfTotalPages = pdfReader.getNumberOfPages();
				while (pageOfCurrentReaderPDF < pdfTotalPages) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					PdfImportedPage page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

				}

				pageOfCurrentReaderPDF = 0;
			}catch(IOException e){
				document.close();
				out.close();
				pdfMerge.delete();
				throw e;
			}
			

		}

		out.flush();
		document.close();
		out.close();

		return pdfMerge;
	}
	
	
	public static void main(String[] args) {
		String[] pdfs = new String[] {
				"C:\\Users\\WORK-A~1\\AppData\\Local\\Temp\\1414986137410-3.pdf",
				"C:\\Users\\WORK-A~1\\AppData\\Local\\Temp\\1414986137410-51.pdf",
				"C:\\Users\\WORK-A~1\\AppData\\Local\\Temp\\1414986137410-4.pdf",
				"C:\\Users\\WORK-A~1\\AppData\\Local\\Temp\\1414986137410-19.pdf" };
		
		
		try {
			LOG.info(PdfUtil.pdfMerge(pdfs));
		} catch (DocumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

}
