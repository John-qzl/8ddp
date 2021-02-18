package com.cssrc.ibms.core.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import javax.xml.transform.sax.SAXResult;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

/**
 * @ClassName: XmlBeanUtil
 * @Description: xmL 与 java bean 互转
 * @author zxg
 * @date 2017年4月6日 上午9:27:47
 * 
 */
public class XmlBeanUtil
{
    
    public static Object unmarshall(String xml, Class<?> clsToUnbound)
        throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(new Class[] {clsToUnbound});
        return unmarshall(jc, xml);
    }
    
    public static String marshall(Object serObj, Class<?> clsToBound)
        throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(new Class[] {clsToBound});
        return marshall(jc, serObj);
    }
    
    public static String marshall(Object serObj, Class<?> clsToBound, String[] cdataEls)
        throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(new Class[] {clsToBound});
        return marshall(jc, serObj, cdataEls);
    }
    
    
    public static <T> T unmarshall(InputStream is, Class<T> clsToUnbound)
        throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(new Class[] {clsToUnbound});
        return (T)unmarshall(jc, is);
    }
    
    private static Object unmarshall(JAXBContext jc, InputStream is)
        throws JAXBException
    {
        Unmarshaller u = jc.createUnmarshaller();
        return u.unmarshal(is);
    }
    
    private static Object unmarshall(JAXBContext jc, String xml)
        throws JAXBException
    {
        Unmarshaller u = jc.createUnmarshaller();
        return u.unmarshal(new StringReader(xml));
    }
    
    private static String marshall(JAXBContext jc, Object serObj)
        throws JAXBException, PropertyException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller m = jc.createMarshaller();
        
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, System.getProperty("file.encoding"));
//        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.marshal(serObj, out);
        
        return out.toString();
    }
    
    private static String marshall(JAXBContext jc, Object serObj, String[] cdataEls)
        throws JAXBException, PropertyException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller m = jc.createMarshaller();
        
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, System.getProperty("file.encoding"));
//        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        XMLSerializer serializer = getXMLSerializer(cdataEls);
        serializer.setOutputByteStream(out);
        
        SAXResult result = null;
        try
        {
            result = new SAXResult(serializer.asContentHandler());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        m.marshal(serObj, result);
        
        return out.toString();
    }
    
    private static XMLSerializer getXMLSerializer(String[] aryProperty)
    {
        OutputFormat of = new OutputFormat();
        of.setCDataElements(aryProperty);
        
        of.setPreserveSpace(true);
        of.setIndenting(true);
        
        XMLSerializer serializer = new XMLSerializer(of);
        
        return serializer;
    }
    
}