package com.cssrc.ibms.report.webSign;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.webseal.pdf.KeyExecute;
import com.cssrc.ibms.core.webseal.pdf.PdfPositionEngine;
import com.cssrc.ibms.core.webseal.pdf.sign.PdfSeal;
import com.cssrc.ibms.core.webseal.pdf.sign.PdfSealOverFlow;
import com.cssrc.ibms.core.webseal.pdf.sign.PdfSignature;

public class SignPdfTest {

	/**
	 * 普通插入图片
	 * @throws Exception
	 */
	@Test
	public void sealPdf() throws Exception {
		try {
			String infile ="D:\\ibms\\reportlets\\test1.pdf";// 魔板路径
			String outfile ="D:\\ibms\\reportlets\\test2.pdf"; // 生成新的pdf的路径
			String image="D:\\ibms\\reportlets\\sign.png";
			KeyExecute keyExecute=new KeyExecute();
			keyExecute.addExecute("sh_陈京普",image);
			PdfSeal pdfSeal = new PdfSeal(infile, outfile,keyExecute);
			pdfSeal.setKeyExecute(keyExecute);
			pdfSeal.seal(null);
		} catch (Exception e) {
			fail("Not yet implemented");
		}
	}
	
	/**
	 * 悬浮插入图片
	 * @throws Exception
	 */
	@Test
	public void sealOverFlow() throws Exception {
		try {
			String infile ="D:\\ibms\\reportlets\\sign1.pdf";// 魔板路径
			String outfile ="D:\\ibms\\reportlets\\sign2.pdf"; // 生成新的pdf的路径
			String image="D:\\ibms\\reportlets\\sign.png";
			KeyExecute keyExecute=new KeyExecute();
			keyExecute.addExecute("sh_陈京普",image);
			PdfSealOverFlow pdfSeal = new PdfSealOverFlow(FileUtil.readByte(infile), outfile,keyExecute);
			pdfSeal.setKeyExecute(keyExecute);
			pdfSeal.seal(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 证书加密
	 * @throws Exception
	 */
	@Test
	public void signPdf() throws Exception {
		try {
			String infile ="D:\\ibms\\reportlets\\sign1.pdf";// 魔板路径
			String outfile ="D:\\ibms\\reportlets\\sign2.pdf"; // 生成新的pdf的路径
			String image="D:\\ibms\\reportlets\\sign.png";
			String certPath="D:\\ibms\\reportlets\\Lee.pfx";
			String pwd="888888";
			KeyExecute keyExecute=new KeyExecute();
			keyExecute.addExecute("sh_陈京普",image);
			PdfSignature pdfSignature=new PdfSignature(infile, outfile,certPath,pwd,keyExecute);
			pdfSignature.setKeyExecute(keyExecute);
			pdfSignature.seal(null);
		} catch (Exception e) {
			fail("Not yet implemented");
		}
	}
	
	/**
	 * 关键字定位
	 * @throws Exception
	 */
	@Test
	public void findText() throws Exception {
		try {
			//构建pdf model
			String infile ="D:\\ibms\\reportlets\\test1.pdf";// 魔板路径
			String outfile ="D:\\ibms\\reportlets\\test2.pdf"; // 生成新的pdf的路径
			String image="D:\\ibms\\reportlets\\sign.bmp";
			//构造定位model
			KeyExecute keyExecute=new KeyExecute();
			keyExecute.addExecute("sh_陈京普",image);
			//构造签名引擎
			PdfSeal pdfSeal = new PdfSeal(infile, outfile,keyExecute);
			//构建定位引擎
			PdfPositionEngine pdfPositionEngine=new PdfPositionEngine(pdfSeal);
			pdfPositionEngine.getPosition();
		} catch (Exception e) {
			fail("Not yet implemented");
		}finally{
			
		}
	}

}
