//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.13 at 11:13:53 ���� CST 
//


package com.cssrc.ibms.core.bpm.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tTimerEventDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tTimerEventDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tEventDefinition">
 *       &lt;choice>
 *         &lt;element name="timeDate" type="{http://www.omg.org/spec/BPMN/20100524/MODEL}tExpression" minOccurs="0"/>
 *         &lt;element name="timeDuration" type="{http://www.omg.org/spec/BPMN/20100524/MODEL}tExpression" minOccurs="0"/>
 *         &lt;element name="timeCycle" type="{http://www.omg.org/spec/BPMN/20100524/MODEL}tExpression" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTimerEventDefinition", propOrder = {
    "timeDate",
    "timeDuration",
    "timeCycle"
})
public class TimerEventDefinition
    extends EventDefinition
{

    protected Expression timeDate;
    protected Expression timeDuration;
    protected Expression timeCycle;

    /**
     * Gets the value of the timeDate property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getTimeDate() {
        return timeDate;
    }

    /**
     * Sets the value of the timeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setTimeDate(Expression value) {
        this.timeDate = value;
    }

    /**
     * Gets the value of the timeDuration property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getTimeDuration() {
        return timeDuration;
    }

    /**
     * Sets the value of the timeDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setTimeDuration(Expression value) {
        this.timeDuration = value;
    }

    /**
     * Gets the value of the timeCycle property.
     * 
     * @return
     *     possible object is
     *     {@link Expression }
     *     
     */
    public Expression getTimeCycle() {
        return timeCycle;
    }

    /**
     * Sets the value of the timeCycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expression }
     *     
     */
    public void setTimeCycle(Expression value) {
        this.timeCycle = value;
    }

}
