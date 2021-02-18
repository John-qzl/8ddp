package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.dp.product.acceptance.bean.ActualData;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;

@XmlRootElement(name = "productInfoBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductInfoBean {
	
	private ProductInfo productInfo;
	
	@XmlElement(name="actualData")
	@XmlElementWrapper(name="actualDataList")
	private List<ActualData> actualDataList;

	public ProductInfo getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(ProductInfo productInfo) {
		this.productInfo = productInfo;
	}

	public List<ActualData> getActualDataList() {
		return actualDataList;
	}

	public void setActualDataList(List<ActualData> actualDataList) {
		this.actualDataList = actualDataList;
	}
	
	
	
}
