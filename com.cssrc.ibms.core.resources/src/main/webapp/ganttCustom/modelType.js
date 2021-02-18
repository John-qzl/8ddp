//审批模板对应的流程数据

/**质量策划共5个流程模板
		 * a 顶层项目
		 * 	1 合同节点改变（<所级质量策划流程>）
		 * 		①	科研项目-研究类1，科研项目-标准类3，软件项目5，生产项目6	（课题研究类项目质量策划审批流程）
		 * 		②	产品项目4	（实物产品研制类项目质量策划审批流程）
		 * 		③	科研项目-试验类2	（试验检测类项目质量策划审批流程）
		 * 	2 非合同节点改变（项目组内部质量策划审批流程2：提交质量策划审批->本级项目负责人批准）
		 * b 非顶层项目
		 * 	1 首次质量策划（项目组内部质量策划审批流程1：提交质量策划审批->顶层项目负责人审核->部门领导批准）
		 * 	2 非首次质量策划（项目组内部质量策划审批流程2：提交质量策划审批->本级项目负责人批准）
		 */
		//流程模板分为5类：modalType1:a1、modalType2:a2、modalType3:a3、modalType4:a4、modalType5:a5

//modelType1 流程中有驻军代表同意（驻军代表结束流程）
var modelType = new Object();
var modelType1=new Object();
modelType1.displayId=2210000000020058;
modelType1.defId=10000018120671;
modelType.modelType1=modelType1;

//modelType2 流程中没有驻军代表同意（分管所领导结束流程）
var modelType2=new Object();
modelType2.displayId=2210000000020058;
modelType2.defId=10000018690054;
modelType.modelType2=modelType2;
