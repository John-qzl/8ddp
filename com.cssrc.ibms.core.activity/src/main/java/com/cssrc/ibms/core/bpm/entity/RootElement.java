//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.13 at 11:13:53 ���� CST 
//


package com.cssrc.ibms.core.bpm.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tRootElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tRootElement">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tBaseElement">
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRootElement")
@XmlSeeAlso({
    ItemDefinition.class,
    Category.class,
    EndPoint.class,
    PartnerRole.class,
    PartnerEntity.class,
    Collaboration.class,
    Signal.class,
    EventDefinition.class,
    DataStore.class,
    Error.class,
    Resource.class,
    Interface.class,
    CorrelationProperty.class,
    Message.class,
    CallableElement.class,
    Escalation.class
})
public abstract class RootElement
    extends BaseElement
{


}
