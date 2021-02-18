package com.cssrc.ibms.dp.form.service;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;

/**
 * Created by FZH on 2018/8/16.
 */
public class TestWord03 {


    public static void main(String[] args) {
        System.out.println( "------Word2003解析Strart------");
        testWord2003();
        testWord2007();
        System.out.println("------Word2003解析End------");
        System.out.println("-------------------------------------------------");
        //System.out.println("2007 = [" + "------开始------" + "]");
        //testWord2003();
        //System.out.println("2007 = [" + "------结束------" + "]");
    }

    private static void testWord2003() {
        try {
            String[] s = new String[20];
            FileInputStream in = new FileInputStream("D:\\mayi.doc");
            POIFSFileSystem pfs = new POIFSFileSystem(in);
            HWPFDocument hwpf = new HWPFDocument(pfs);
            Range range = hwpf.getRange();
            TableIterator it = new TableIterator(range);
            int index = 0;
            while (it.hasNext()) {
                Table tb = (Table) it.next();
                for (int i = 0; i < tb.numRows(); i++) {
                    //System.out.println("Numrows :"+tb.numRows());
                    TableRow tr = tb.getRow(i);
                    for (int j = 0; j < tr.numCells(); j++) {
                        //System.out.println("numCells :"+tr.numCells());
//						System.out.println("j   :"+j);
                        TableCell td = tr.getCell(j);
                        for (int k = 0; k < td.numParagraphs(); k++) {
                            //System.out.println("numParagraphs :"+td.numParagraphs());
                            Paragraph para = td.getParagraph(k);
                            s[index] = para.text().trim();
                            index++;
                        }
                    }
                }
            }
//			System.out.println(s.toString());
            for (int i = 0; i < s.length; i++) {
                System.out.println(s[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void testWord2007() {
        try {
            InputStream is = new FileInputStream("D:\\sinye.docx");
//      XWPFDocument doc = new XWPFDocument(OPCPackage.open(is));
            XWPFDocument doc = new XWPFDocument(POIXMLDocument.openPackage("D:\\sinye.docx"));
//      XWPFDocument doc = new XWPFDocument(is);
            List<XWPFParagraph> paras = doc.getParagraphs();
            for (XWPFParagraph para : paras) {
                //当前段落的属性
//		       CTPPr pr = para.getCTP().getPPr();
                System.out.println(para.getText());
            }
            //获取文档中所有的表格
            List<XWPFTable> tables = doc.getTables();
            List<XWPFTableRow> rows;
            List<XWPFTableCell> cells;
            for (XWPFTable table : tables) {
                //表格属性
//		       CTTblPr pr = table.getCTTbl().getTblPr();
                //获取表格对应的行
                rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    //获取行对应的单元格
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        System.out.println(cell.getText());
                    }
                }
            }
            close(is);
        }//end method
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        /**
         * 关闭输入流
         * @param is
         */
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void testWord1() {
        try {
            //word 2003： 图片不会被读取
            InputStream is = new FileInputStream(new File("D:\\sinye.doc"));
            WordExtractor ex = new WordExtractor(is);
            String text2003 = ex.getText();
            System.out.println(text2003);
            //word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
            OPCPackage opcPackage = POIXMLDocument.openPackage("D:\\sinye.doc");
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
            String text2007 = extractor.getText();
            System.out.println(text2007);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



