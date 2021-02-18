    package com.cssrc.ibms.index.model;
     
     import java.util.ArrayList;
     import java.util.List;
     import javax.xml.bind.annotation.XmlAccessType;
     import javax.xml.bind.annotation.XmlAccessorType;
     import javax.xml.bind.annotation.XmlElement;
     import javax.xml.bind.annotation.XmlRootElement;
     import javax.xml.bind.annotation.XmlType;
     
     @XmlAccessorType(XmlAccessType.FIELD)
     @XmlType(name="", propOrder={"indexColumn"})
     @XmlRootElement(name="indexColumns")
     public class IndexColumns
     {

    	 @XmlElement(required=true)
    	 protected List<IndexColumn> indexColumn;

    	 public List<IndexColumn> getIndexColumn()
    	 {
    		 if (this.indexColumn == null) {
    			 this.indexColumn = new ArrayList();
    		 }
    		 return this.indexColumn;
    	 }
     }

