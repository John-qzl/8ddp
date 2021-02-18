package com.cssrc.ibms.core.webseal.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cssrc.ibms.core.webseal.pdf.intf.AbstractIbmsSeal;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/** 
* @ClassName: PdfPositionEngine 
* @Description: pdf关键字定位引擎
* @author zxg 
* @date 2016年12月5日 下午3:06:16 
*  
*/
public class PdfPositionEngine {
	private AbstractIbmsSeal pdfSeal;

	public PdfPositionEngine() {

	}

	public PdfPositionEngine(AbstractIbmsSeal pdfDoucument) {
		this.pdfSeal = pdfDoucument;
	}

	public List<KeyPosition> getPosition() throws IOException,
			DocumentException {
		// 获取pdf reader;
		PdfReader pdfReader = this.pdfSeal.getReader();
		// 获取需要定位的关键字;
		List<Execute> executes = pdfSeal.getKeyExecute().getKeyExecute();
		//将要返回的所有定位好的Coordinate;
		List<KeyPosition> positions = new ArrayList<KeyPosition>();
		//pdf解析器
		PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
				pdfReader);
		//解析pdf定位key的坐标返回list
		for (Execute execute: executes) {
			//监听pdf解析
			PdfKeyFinder textMarginFinder = new PdfKeyFinder(
					execute.getKey(),execute.getImgpath());
			//开始解析pdf
			pdfReaderContentParser.processContent(pdfSeal.getCreentPage(),
					textMarginFinder);
			//将定位到的坐标添加到positions里
			positions.addAll(textMarginFinder.getPositions());
		}
		return positions;
	}

}
