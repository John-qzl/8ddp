package com.cssrc.ibms.core.flow.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import com.cssrc.ibms.core.bpm.ContextFactory;
import com.cssrc.ibms.core.bpm.entity.Definitions;
import com.cssrc.ibms.core.bpm.entity.ExtensionElements;
import com.cssrc.ibms.core.bpm.entity.Process;
import com.cssrc.ibms.core.bpm.entity.FlowElement;
import com.cssrc.ibms.core.bpm.entity.RootElement;
import com.cssrc.ibms.core.bpm.entity.SubProcess;
import com.cssrc.ibms.core.bpm.entity.omgdc.ObjectFactory;


public class BPMN20Util {
	
	/**
	 * 根据流程元素类型，从流程对象中取得流程元素。
	 * @param process 流程对象
	 * @param deepIntoSub 是否对流程中的子流程递归调用
	 * @param flowTypes 流程元素类型
	 * @return 指定类型的流程元素
	 */
	public static List<FlowElement>  getFlowElementByType(Process process,boolean deepIntoSub,Class<? extends FlowElement>... flowTypes){
		List<FlowElement> flowElements = new ArrayList<FlowElement>();
		List<JAXBElement<? extends FlowElement>> jaxbElementFlowElements =  process.getFlowElement();
		for(JAXBElement<? extends FlowElement> jAXBElement:jaxbElementFlowElements){
			FlowElement flowElement = jAXBElement.getValue();
			for(Class<? extends FlowElement> flowType:flowTypes){
				if(flowType.isInstance(flowElement)){
					flowElements.add(flowElement);
					break;
				}
			}
			
			if(deepIntoSub){
				if(flowElement instanceof SubProcess){
					flowElements.addAll(getFlowElementByTypeInSubProcess((SubProcess)flowElement,true,flowTypes));
				}
			}
		}
		return flowElements;
	}
	
	
	/**
	 * 根据流程元素类型，从子流程对象中取得流程元素。
	 * @param subProcess 流程对象
	 * @param deepIntoSub 是否对子流程中的子流程递归调用
	 * @param flowTypes 流程元素类型
	 * @return 指定类型的流程元素
	 */
	public static List<FlowElement> getFlowElementByTypeInSubProcess(SubProcess subProcess,boolean deepIntoSub,Class<? extends FlowElement>... flowTypes){
		List<FlowElement> flowElements = new ArrayList<FlowElement>();
		List<JAXBElement<? extends FlowElement>> jaxbElementFlowElements =  subProcess.getFlowElement();
		for(JAXBElement<? extends FlowElement> jAXBElement:jaxbElementFlowElements){
			FlowElement flowElement = jAXBElement.getValue();
			for(Class<? extends FlowElement> flowType:flowTypes){
				if(flowType.isInstance(flowElement)){
					flowElements.add(flowElement);
					break;
				}
			}
			if(deepIntoSub){
				if(flowElement instanceof SubProcess){
					flowElements.addAll(getFlowElementByTypeInSubProcess((SubProcess)flowElement,true,flowTypes));
				}
			}
		}
		return flowElements;
	}
	
	public static OutputStream marshall(Object jaxbElement,OutputStream os) throws JAXBException{
		JAXBContext jctx = JAXBContext.newInstance(ObjectFactory.class);
		Marshaller marshaller = jctx.createMarshaller();
		marshaller.marshal(jaxbElement, os);
		return os;
	}
	
	public static Object unmarshall(InputStream is,Class<? extends Object>... classes) throws JAXBException, IOException{
		JAXBContext jctx = ContextFactory.newInstance(classes);
		Unmarshaller unmarshaller = jctx.createUnmarshaller();
		Object obj = null;
		try{
			 obj = unmarshaller.unmarshal(is);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static Object unmarshall(String bpmnxml,Class<? extends Object> classes) throws JAXBException, IOException{
		InputStream is = new ByteArrayInputStream(bpmnxml.getBytes());
		
		return unmarshall(is,classes);
	}
	
	/**
	 * 根据输入流，创建流程对象
	 * @param is
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static Definitions createDefinitions(InputStream is) throws JAXBException, IOException{
		@SuppressWarnings("unchecked")
		JAXBElement<Definitions> jAXBElement = (JAXBElement<Definitions>) unmarshall(is,
				ObjectFactory.class,
				com.cssrc.ibms.core.bpm.entity.activiti.ObjectFactory.class,
				com.cssrc.ibms.core.bpm.entity.omgdc.ObjectFactory.class,
				com.cssrc.ibms.core.bpm.entity.omgdi.ObjectFactory.class,
				com.cssrc.ibms.core.bpm.entity.ht.ObjectFactory.class,
				com.cssrc.ibms.core.bpm.entity.bpmndi.ObjectFactory.class);
		Definitions definitions = jAXBElement.getValue();
		return definitions;
	}
	/**
	 * 根据bpmn20流程定义xml文件，创建流程对象
	 * @param bpmnxml
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static Definitions createDefinitions(String bpmnxml) throws JAXBException, IOException{
		InputStream is = new ByteArrayInputStream(bpmnxml.getBytes());
		return createDefinitions(is);
	}
	
	/**
	 * 根据输入流，取得定义的流程
	 * @param is
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static List<Process> getProcess(InputStream is) throws JAXBException, IOException{
		
		List<Process> processes = new ArrayList<Process>();
		Definitions definitions = createDefinitions(is);
		List<JAXBElement<? extends RootElement>> bPMNElements =definitions.getRootElement();
		for(JAXBElement<? extends RootElement> jAXBe:bPMNElements){
			RootElement element =  jAXBe.getValue();
			if(element instanceof Process){
				processes.add((Process)element);
			}
		}
		return processes;
	}
	
	/**
	 * 根据bpmn20流程定义xml文件，取得定义的流程
	 * @param bpmnxml 流程定义
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static List<Process> getProcess(String bpmnxml) throws JAXBException, IOException{
		InputStream is = new ByteArrayInputStream(bpmnxml.getBytes("UTF-8"));
		return getProcess(is);
	}
	

	/**
	 * @param flowElement
	 * @param qname
	 * @return
	 */
	public static List<Object> getFlowElementExtension(FlowElement flowElement,QName qname){
		List<Object> extensions = new ArrayList<Object>();
		ExtensionElements extensionElements = flowElement.getExtensionElements();
		if(extensionElements==null){
			return extensions;
		}
		List<Object> objects = extensionElements.getAny();
		for(Object obj: objects){
			if(obj instanceof JAXBElement){
				JAXBElement extension = (JAXBElement)obj;
				if(extension.getName().equals(qname)){
					extensions.add(extension.getValue());
				}
			}
		}
		return extensions;
	}
}
