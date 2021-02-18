package com.cssrc.ibms.dp.form.model;
/**
 * 跨行元素元数据
 * 
 *
 */
public class CrossRangeCellMeta {
	private int firstRowIndex;
	private int firstColIndex;
	private int rowspan;//跨越行数
	private int colspan;//跨越列数
	 
	public CrossRangeCellMeta(int firstRowIndex,int  firstColIndex,int rowspan,int colspan){
		super();
		this.firstRowIndex=firstRowIndex;
		this.firstColIndex=firstColIndex;
		this.rowspan=rowspan;
		this.colspan=colspan;
	}
	
	public int getFirstRow(){
		return firstRowIndex;
	}
	public int getLsatRow(){
		return firstRowIndex+rowspan-1;
	}
	public int getFirstCol(){
		return firstColIndex;
	}
	public int getLastCol(){
		return firstColIndex+colspan-1;
	}
	public int getColSpan(){
		return colspan;
	}
	public int getRowSpan(){
		return rowspan;
	}
}
