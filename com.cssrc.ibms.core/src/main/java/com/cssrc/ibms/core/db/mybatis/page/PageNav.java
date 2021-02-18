/*     */ package com.cssrc.ibms.core.db.mybatis.page;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ public class PageNav
/*     */ {
/*     */   private String strUrl;
/*     */   private PageBean1 _pv;
/*  13 */   private String _strPara = "";
/*  14 */   private static long i = 0L;
/*     */   private HttpServletRequest _request;
/*  17 */   private int count = 8;
/*     */ 
/*     */   public PageNav(HttpServletRequest request, PageBean1 pageBean)
/*     */   {
/*  29 */     this._request = request;
/*  30 */     this._pv = pageBean;
/*  31 */     this.strUrl = request.getRequestURL().toString();
/*  32 */     this._strPara = getPara(request);
/*     */   }
/*     */ 
/*     */   private String getPara(HttpServletRequest request)
/*     */   {
/*  46 */     String params = "";
/*  47 */     Enumeration e = request.getParameterNames();
/*  48 */     String key = "";
/*  49 */     String value = "";
/*  50 */     while (e.hasMoreElements())
/*     */     {
/*  52 */       key = (String)e.nextElement();
/*  53 */       if (key.equals(this._pv.getPageName()))
/*     */         continue;
/*  55 */       value = request.getParameter(key);
/*  56 */       if ((value == null) || (value.equals("")))
/*     */         continue;
/*  58 */       params = params + "&" + key + "=" + getValueByParameter(key);
/*     */     }
/*     */ 
/*  62 */     return params;
/*     */   }
/*     */ 
/*     */   private String getValueByParameter(String parameter)
/*     */   {
/*  72 */     String[] values = this._request.getParameterValues(parameter);
/*  73 */     String value = "";
/*  74 */     if (values.length == 1)
/*     */     {
/*  76 */       value = this._request.getParameter(parameter);
/*  77 */       if (value == null)
/*  78 */         return "";
/*  79 */       value = value.trim();
/*     */     }
/*     */     else
/*     */     {
/*  83 */       int k = values.length;
/*  84 */       for (int i = 0; i < values.length; i++)
/*     */       {
/*  86 */         if (i == k - 1)
/*  87 */           value = value + values[i].trim();
/*     */         else
/*  89 */           value = value + values[i].trim() + ",";
/*     */       }
/*     */     }
/*  92 */     return value;
/*     */   }
/*     */ 
/*     */   public String getNumber()
/*     */   {
/* 107 */     int currentPage = this._pv.getCurrentPage();
/* 108 */     List<Integer> list = PageUtils.getPageNumbers(this._pv.getCurrentPage(), this._pv.getTotalPage(), this.count);
/* 109 */     StringBuffer sb = new StringBuffer();
/* 110 */     sb.append("<div class='pageNav'>");
/* 111 */     String url = this.strUrl + "?page=%1$s" + this._strPara;
/* 112 */     String curTmplate = "<span class='spanCurPage'><a href='" + url + "'>%1$s</a></span>";
/* 113 */     String tmplate = "<span class='spanPage'><a href='" + url + "'>%1$s</a></span>";
/* 114 */     for (Integer it : list)
/*     */     {
/* 116 */       if (currentPage == it.intValue())
/* 117 */         sb.append(String.format(curTmplate, new Object[] { Integer.valueOf(it.intValue()) }));
/*     */       else
/* 119 */         sb.append(String.format(tmplate, new Object[] { Integer.valueOf(it.intValue()) }));
/*     */     }
/* 121 */     sb.append("</div>");
/* 122 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String getPage(boolean allRecord, boolean pagesInfo, boolean pageNav, boolean jumpPage)
/*     */     throws Exception
/*     */   {
/* 135 */     StringBuffer sb = new StringBuffer();
/* 136 */     sb.append("<div class='pageNav'>");
/* 137 */     if (allRecord)
/* 138 */       sb.append(showTotalRecord());
/* 139 */     if (pagesInfo)
/* 140 */       sb.append(showPageInfo());
/* 141 */     if (pageNav)
/* 142 */       sb.append(showPageNav());
/* 143 */     if (jumpPage)
/* 144 */       sb.append(showJumpPage());
/* 145 */     sb.append("</div>");
/* 146 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String getPage()
/*     */     throws Exception
/*     */   {
/* 156 */     StringBuffer sb = new StringBuffer();
/* 157 */     sb.append("<div class='pageNav'>");
/* 158 */     sb.append(showTotalRecord());
/* 159 */     sb.append(showPageInfo());
/* 160 */     sb.append(showPageNav());
/* 161 */     sb.append(showJumpPage());
/* 162 */     sb.append("</div>");
/* 163 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String getShortPage() throws Exception
/*     */   {
/* 168 */     StringBuffer sb = new StringBuffer();
/* 169 */     sb.append("<div class='pageNav'>");
/*     */ 
/* 171 */     sb.append(showPageNav());
/*     */ 
/* 173 */     sb.append("</div>");
/* 174 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String showJumpPage() throws Exception
/*     */   {
/* 179 */     StringBuffer sb = new StringBuffer();
/* 180 */     i += 1L;
/* 181 */     if (i > 100L)
/* 182 */       i = 0L;
/* 183 */     sb.append("<span class='jumpPage'>");
/* 184 */     sb.append(" 转到第<input type=\"text\" name=\"page" + i + "\" size=\"1\" class=\"input\" />页 <input type=\"button\" class=\"btnjump\" value=\"跳转\" onclick=\"goToPage" + i + "()\" /> ");
/* 185 */     sb.append("<script language=\"javascript\">");
/* 186 */     sb.append("function goToPage" + i + "(){");
/* 187 */     sb.append("value=document.all.page" + i + ".value;");
/* 188 */     sb.append("if(value.indexOf(\".\")==-1 && value.indexOf(\"-\")==-1 && value!==\"\" && !isNaN(value) && value<" + (this._pv.getTotalPage() + 1) + "){");
/* 189 */     sb.append("location.assign('" + this.strUrl + "?page='" + "+value+'" + this._strPara + "')");
/* 190 */     sb.append("}");
/* 191 */     sb.append("}");
/* 192 */     sb.append("</script>");
/* 193 */     sb.append("</span>");
/* 194 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String showPageInfo() throws Exception
/*     */   {
/* 199 */     return "<span class='pageInfo'> 第" + this._pv.getCurrentPage() + "/" + this._pv.getTotalPage() + "页 </span>";
/*     */   }
/*     */ 
/*     */   private String showPageNav() throws Exception
/*     */   {
/* 204 */     StringBuffer sb = new StringBuffer();
/* 205 */     sb.append("<span class='spanNav'>");
/* 206 */     if (this._pv.getTotalPage() > 1)
/* 207 */       sb.append("<a href=\"" + this.strUrl + "?page=1" + this._strPara + "\">第一页</a>");
/*     */     else
/* 209 */       sb.append("<a disabled='true' >第一页</a>");
/* 210 */     if (this._pv.isHasPreviousPage())
/* 211 */       sb.append("<a href=\"" + this.strUrl + "?page=" + this._pv.getPreviousPage() + this._strPara + "\">上一页</a>");
/*     */     else
/* 213 */       sb.append("<a disabled='true' >上一页</a>");
/* 214 */     if (this._pv.isHasNextPage())
/*     */     {
/* 216 */       sb.append("<a href=\"" + this.strUrl + "?page=" + this._pv.getNextPage() + this._strPara + "\">下一页</a>");
/*     */     }
/*     */     else
/*     */     {
/* 220 */       sb.append("<a disabled='true' >下一页</a>");
/*     */     }
/* 222 */     if (this._pv.getTotalPage() > 1)
/* 223 */       sb.append("<a href=\"" + this.strUrl + "?page=" + this._pv.getTotalPage() + this._strPara + "\">最末页</a>");
/*     */     else
/* 225 */       sb.append("<a disabled='true' >最末页</a>");
/* 226 */     sb.append("</span>");
/* 227 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String showTotalRecord() throws Exception
/*     */   {
/* 232 */     return "<span class='totalRecord'>共" + this._pv.getTotalCount() + "条记录 </span>";
/*     */   }
/*     */ }
