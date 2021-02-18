package com.cssrc.ibms.dp.form.util;

import com.cssrc.ibms.dp.form.service.FormService;
import org.apache.poi.hwpf.usermodel.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author FZH
 * @date 2018/8/21
 */
public class ReadWord2003Util {
    @Resource
    private FormService formService;

    /**
     * 关闭输入流
     *
     * @param is
     */
    public static void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输出书签信息
     *
     * @param bookmarks
     */
    public static void printInfo(Bookmarks bookmarks) {
        int count = bookmarks.getBookmarksCount();
        System.out.println("书签数量：" + count);
        Bookmark bookmark;
        for (int i = 0; i < count; i++) {
            bookmark = bookmarks.getBookmark(i);
            System.out.println("书签" + (i + 1) + "的名称是：" + bookmark.getName());
            System.out.println("开始位置：" + bookmark.getStart());
            System.out.println("结束位置：" + bookmark.getEnd());
        }
    }

    /**
     * 读表格
     * 每一个回车符代表一个段落，所以对于表格而言，每一个单元格至少包含一个段落，每行结束都是一个段落。
     *
     * @param range
     */
    public static void readTable(Range range) {

        String msg = "导入成功！";

        //遍历range范围内的table。
        TableIterator tableIter = new TableIterator(range);
        Table table;
        TableRow row;
        TableCell cell;
        int flag = 0;
        while (tableIter.hasNext()) {
            String oldHtml = "";
            Element root = DocumentHelper.createElement("html");
            Document document = DocumentHelper.createDocument(root);
            Element tablelElement = root.addElement("table").addAttribute("width", "100%").addAttribute("class", "layui-table");
            ;
            Element tbodyElement = tablelElement.addElement("tbody");
            table = tableIter.next();
            int rowNum = table.numRows();
            for (int j = 0; j < rowNum; j++) {
                if (j == 0) {
                    //首行设置
                    Element trElement = tbodyElement.addElement("tr").addAttribute("class", "firstRow");
                    row = table.getRow(j);
                    int cellNum = row.numCells();
                    for (int k = 0; k < cellNum; k++) {
                        cell = row.getCell(k);
                        String cellText = cell.text().trim();
                        if (cellText.equalsIgnoreCase("")) {
                            //输出单元格的文本
                            msg = "表格的首行每一列首行均不能为空！";
                            break;
                        } else {
                            trElement.addElement("td").addAttribute("valign", "top").setText(cellText);
                        }
                    }
                } else {
                    Element trElement = tbodyElement.addElement("tr");
                    row = table.getRow(j);
                    int cellNum = row.numCells();
                    //检查项描述（本行）
                    String checkItemDesc = ",";
                    for (int k = 0; k < cellNum; k++) {
                        cell = row.getCell(k);
                        String cellText = cell.text().trim();
                        if (cellText.equalsIgnoreCase("")) {
                            String nowCheckItemDESC = "";
                            if (checkItemDesc.length() > 1) {
                                nowCheckItemDESC = checkItemDesc.substring(1, checkItemDesc.length());
                            }
                            Element tdElement = trElement.addElement("td").addAttribute("valign", "top").addAttribute("class", "selectTdClass").addAttribute("input", "1").addAttribute("checkbox", "1").addAttribute("photo", "1");
                            tdElement.setText(cellText);
                            //填写
                            tdElement.addElement("input").addAttribute("type", "text").addAttribute("class", "dpInputText").addAttribute("photo", "是").addAttribute("title", nowCheckItemDESC);
                            //对勾
                            tdElement.addElement("input").addAttribute("type", "checkbox").addAttribute("disabled", "true").addAttribute("class", "dpCheckbox").addAttribute("photo", "是").addAttribute("title", nowCheckItemDESC);
                            //附件，拍照
                            tdElement.addElement("input").addAttribute("type", "button").addAttribute("disabled", "true").addAttribute("value", "附件").addAttribute("onclick", "addAndShowPhoto(this)");


                        } else {
                            checkItemDesc += cellText;
                            trElement.addElement("td").addAttribute("valign", "top").setText(cellText);
                        }
                    }
                }
            }
            oldHtml = document.asXML();
            int index1 = oldHtml.indexOf("<table");
            int index2 = oldHtml.lastIndexOf("</table>");
            oldHtml = oldHtml.substring(index1, index2 + 8);
            System.out.println(oldHtml);
            // Map<String,
            // Object>msg=formService.check(html,id,name,sign,status,attention,pid,fcid,MID);
            //Map<String, Object> msg2 = formService.save(oldHtml, "", "");
        }
    }

    /**
     * 读列表
     *
     * @param range
     */
    public static void readList(Range range) {
        //int num = range.numParagraphs();
        //Paragraph para;
        //for (int i=0; i<num; i++) {
        //    para = range.getParagraph(i);
        //    para.isInTable()
        //    if (para.isInList()) {
        //        System.out.println("list: " + para.text());
        //    }
        //}
    }

    /**
     * 输出Range
     *
     * @param range
     */
    public void printInfo(Range range) {
        //获取段落数
        int paraNum = range.numParagraphs();
        System.out.println(paraNum);
        for (int i = 0; i < paraNum; i++) {
            //this.insertInfo(range.getParagraph(i));
            System.out.println("段落" + (i + 1) + "：" + range.getParagraph(i).text());
            if (i == (paraNum - 1)) {
                this.insertInfo(range.getParagraph(i));
            }
        }
        int secNum = range.numSections();
        System.out.println(secNum);
        Section section;
        for (int i = 0; i < secNum; i++) {
            section = range.getSection(i);
            System.out.println(section.getMarginLeft());
            System.out.println(section.getMarginRight());
            System.out.println(section.getMarginTop());
            System.out.println(section.getMarginBottom());
            System.out.println(section.getPageHeight());
            System.out.println(section.text());
        }
    }

    /**
     * 插入内容到Range，这里只会写到内存中
     *
     * @param range
     */
    private void insertInfo(Range range) {
        range.insertAfter("Hello");
    }


}
