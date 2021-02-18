package com.cssrc.ibms.system.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IDefAuthorizeService;
import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.IProCopytoService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.intf.ITaskExeService;
import com.cssrc.ibms.api.activity.intf.ITaskReminderService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskAmount;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.page.PageList;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.WarningSetting;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.index.model.IndexTab;
import com.cssrc.ibms.index.model.IndexTabList;
import com.cssrc.ibms.index.model.Infobox;
/**
 * 主页服务层
 * @author YangBo 2016-7-22
 *
 */
@Service
public class IndexService {
	@Resource
	private IBpmService bpmService;
	@Resource
	private IMessageSendService messageSendService;
	@Resource
	private IProcessRunService processRunService;
	@Resource
	private IDefinitionService definitionService;
	@Resource
	private IDefAuthorizeService defAuthorizeService;
	@Resource
	private ITaskExeService taskExeService;
	@Resource
	private ITaskReminderService reminderService;
	@Resource
	private IProCopytoService proCopytoService;


	public IndexTabList processCenter(Integer curPage, Integer pageSize,
			String curTab) {
		PageList list1 = pendingMatterPage(curPage, pageSize);

		IndexTab tab1 = new IndexTab();
		tab1.setTitle("我的待办");
		tab1.setKey("pendingMatter");
		tab1.setBadge(String.valueOf(list1.getTotalCount()));
		tab1.setActive(true);
		tab1.setList(list1);

		PageList list2 = completedMattersPage(curPage, pageSize);
		IndexTab tab2 = new IndexTab();
		tab2.setTitle("办结事宜");
		tab2.setKey("completedMatters");
		tab2.setBadge(String.valueOf(list2.getTotalCount()));
		tab2.setList(list2);

		List tabList = new ArrayList();
		tabList.add(tab1);
		tabList.add(tab2);

		IndexTabList indexTabList = new IndexTabList();

		indexTabList.setCurTab(curTab);
		setIndexTabList(indexTabList, curTab, tabList);

		indexTabList.setIndexTabList(tabList);
		return indexTabList;
	}

	private void setIndexTabList(IndexTabList indexTabList, String curTab,
			List<IndexTab> tabList) {
		for (IndexTab indexTab : tabList)
			if (curTab.equalsIgnoreCase(indexTab.getKey())) {
				if (BeanUtils.isNotEmpty(indexTab.getList()))
					indexTabList.setPageBean(indexTab.getList().getPageBean());
				indexTab.setActive(true);
			} else {
				indexTab.setActive(false);
			}
	}

	public PageList<IProcessTask> pendingMatterPage(Integer curPage,
			Integer pageSize) {
		PageList<IProcessTask> list = new PageList();

		PagingBean pagingBean = new PagingBean();
		try {
			pagingBean.setCurrentPage(BeanUtils.isNotEmpty(curPage) ? curPage
					.intValue() : 1);
			pagingBean.setPageSize(BeanUtils.isNotEmpty(pageSize) ? pageSize
					.intValue() : 10);
			list = (PageList) this.bpmService.getTasks(
					UserContextUtil.getCurrentUserId(), null, null, null, null, "desc",
					pagingBean);

			Map waringSetMap = this.reminderService.getWaringSetMap();
			for (IProcessTask task : list) {
				int priority = Integer.parseInt(task.getPriority());
				if (waringSetMap.containsKey(Integer.valueOf(priority)))
					task.setDescription(((WarningSetting) waringSetMap
							.get(Integer.valueOf(priority))).getColor());
				else
					task.setDescription("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<?extends IProcessTask> pendingMatters(Integer curPage, Integer pageSize) {
		List<?extends IProcessTask> list = new ArrayList();

		PagingBean pb = new PagingBean();
		try {
			pb.setCurrentPage(BeanUtils.isNotEmpty(curPage) ? curPage
					.intValue() : 1);
			pb.setPageSize(BeanUtils.isNotEmpty(pageSize) ? pageSize.intValue()
					: 10);
			list = this.bpmService.getTasks(UserContextUtil.getCurrentUserId(), null,
					null, null, null, "desc", pb);

			Map waringSetMap = this.reminderService.getWaringSetMap();
			for (IProcessTask task : list) {
				int priority =Integer.parseInt(task.getPriority());
				if (waringSetMap.containsKey(Integer.valueOf(priority))){
				    task.setDescription(((WarningSetting) waringSetMap
                        .get(Integer.valueOf(priority))).getColor());
				}else{
	                task.setDescription("");
				}
					
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<?extends IProcessTask> pendingMatters() {
		return pendingMatters(Integer.valueOf(1), Integer.valueOf(10));
	}

	public ISysUser userInfo() {
		return (ISysUser) UserContextUtil.getCurrentUser();
	}
	/**
	 * 工作台显示未读信息
	 * @return
	 */
	public List<?> getMessage() {
		List list = new ArrayList();

		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPageSize(10);
		try {
			list = this.messageSendService.getNotReadMsgByUserId(UserContextUtil
					.getCurrentUserId().longValue(), pb);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	/**
	 * 工作台显示我参与审批 流程
	 * @return
	 */
	public List<IProcessRun> myAttend() {
		List list = new ArrayList();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPageSize(10);

		pb.setShowTotal(false);
		try {
			list = this.processRunService.getMyAttend(UserContextUtil
					.getCurrentUserId(), null, pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 工作台显示我发起的流程
	 * @return
	 */
	public List<IProcessRun> myStart() {
		List list = new ArrayList();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPageSize(10);
		try {
			list = this.processRunService.myStart(UserContextUtil.getCurrentUserId(),
					pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/*public List<OutMail> myNewMail() {
		List list = new ArrayList();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		try {
			list = this.outMailDao.getMailByUserId(UserContextUtil
					.getCurrentUserId().longValue(), pb);
		} catch (Exception e) {
			e.getStackTrace();
		}

		return list;
	}*/
	/**
	 * 我的流程清单
	 */
	public List<IDefinition> myProcess() {
		List list = new ArrayList();
		Long curUserId = UserContextUtil.getCurrentUserId();
		try {
			Map actRightMap = this.defAuthorizeService.getActRightByUserMap(
					curUserId, "start", false, false);

			String actRights = (String) actRightMap.get("authorizeIds");
			list = this.definitionService.getMyDefListForDesktop(actRights);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 我的办结
	 * @return
	 */
	public List<IProcessRun> myCompleted() {
		List list = new ArrayList();
		long curUserId = UserContextUtil.getCurrentUserId().longValue();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPageSize(10);

		pb.setShowTotal(false);
		try {
			list = this.processRunService.myCompleted(Long
					.valueOf(curUserId), pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<IProcessRun> alreadyMatters() {
		List list = new ArrayList();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPageSize(10);

		pb.setShowTotal(false);
		try {
			list = this.processRunService.myAlready(UserContextUtil.getCurrentUserId(),pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public PageList<IProcessRun> completedMattersPage(Integer curPage,
			Integer pageSize) {
		PageList list = new PageList();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(BeanUtils.isNotEmpty(curPage) ? curPage.intValue()
				: 1);
		pb.setPageSize(BeanUtils.isNotEmpty(pageSize) ? pageSize.intValue()
				: 10);
		try {
			list = (PageList) this.processRunService.completedMatters(UserContextUtil
					.getCurrentUserId(), pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<IProcessRun> completedMatters() {
		List list = new ArrayList();
		PagingBean pb = new PagingBean();
		pb.setCurrentPage(1);
		pb.setPageSize(10);

		pb.setShowTotal(false);
		try {
			list = this.processRunService.completedMatters(UserContextUtil
					.getCurrentUserId(), pb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

/*	public List<SysBulletin> getBulletin(String alias) {
		List list = this.sysBulletinService.getAllByAlias(alias);
		return list;
	}*/

	public Map<String, Object> getCurUser() {
		Map root = new HashMap();
		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		root.put("user", user);
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentOrg();
		root.put("org", org);
		IPosition pos = (IPosition) UserContextUtil.getCurrentPos();
		root.put("pos", pos);
		return root;
	}

	public List<Infobox> getInfobox() {
		List list = new ArrayList();
		try {
			Infobox myTaksBox = getMyTaksBox();
			//工作台内部消息
			/*Infobox myMessBox = getMyMessBox();*/
			Infobox myProCopytoBox = getMyProCopytoBox();
			Infobox myAlreadyBox = getMyAlreadyBox();
			Infobox myCompletedMattersBox = getMyCompletedMattersBox();
			Infobox myAccordingMattersBox = getMyAccordingMattersBox();
			//我的请求
			/*Infobox myRequestBox = getMyRequestBox();*/
			Infobox myCompletedBox = getMyCompletedBox();
			//我的草稿箱
			/*Infobox myDraftBox = getMyDraftBox();*/
			//我的日程
			/*Infobox myPlanBox = getMyPlanBox();*/

			list.add(myTaksBox);
			/*list.add(myMessBox);*/
			list.add(myProCopytoBox);
			list.add(myAlreadyBox);
			list.add(myCompletedMattersBox);
			list.add(myAccordingMattersBox);
			/*list.add(myRequestBox);*/

			/*list.add(myDraftBox);*/
			/*list.add(myPlanBox);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private Infobox getOnLineUsersBox() {
		Infobox infobox = new Infobox();
		infobox.setType(Integer.valueOf(2));
		infobox.setColor("light-brown");
		infobox.setDataText(String.valueOf(UserContextUtil.getOnLineUsers().size())); 
		infobox.setDataContent("在线人数");
		infobox.setData("196,128,202,177,154,94,100,170,224");
		return infobox;
	}

	/*private Infobox getMyPlanBox() {
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-check-square-o");
		infobox.setColor("brown");
		infobox.setDataContent("我的日程");
		infobox.setUrl("/oa/system/sysPlan/myList.do");

		QueryFilter filter = new QueryFilter(new JSONObject(), "", false, 1);
		filter.addFilterForIB("rate", Integer.valueOf(0));
		filter.addFilterForIB("userId", UserContextUtil.getCurrentUserId());
		List plans = this.planService.getMyList(filter);
		infobox.setDataText(plans.size()); // yangbo
		return infobox;
	}*/
	/**
	 * 工作台显示我的草稿
	 */
	private Infobox getMyDraftBox() {
		List<IProcessRun> list = (List<IProcessRun>) this.processRunService.getMyDraft(UserContextUtil.getCurrentUserId(),null);
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-pencil-square-o");
		infobox.setColor("wood");
		infobox.setDataText(String.valueOf(list.size()));
		infobox.setDataContent("我的草稿");
		infobox.setUrl("/oa/flow/processRun/myForm.do");
		return infobox;
	}
	
	/**
	 * 我的办结消息框
	 * @return
	 */
	private Infobox getMyCompletedBox() {
		List list = this.processRunService.myCompleted(UserContextUtil
				.getCurrentUserId(), null);
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-check-square-o");
		infobox.setColor("brown");
		infobox.setDataText(String.valueOf(list.size()));
		infobox.setDataContent("我的办结");
		infobox.setUrl("/oa/flow/processRun/myCompleted.do");
		return infobox;
	}
	/**
	 * 我的发起的流程(我的请求)
	 * @return
	 */
	private Infobox getMyRequestBox() {
		List list = this.processRunService.myStart(UserContextUtil
				.getCurrentUserId(), null);
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-hand-o-up");
		infobox.setColor("blue2");
		infobox.setDataText(String.valueOf(list.size()));
		infobox.setDataContent("我的请求");
		infobox.setUrl("/oa/flow/processRun/myRequest.do");
		return infobox;
	}
	/**
	 * 
	 * 工作台显示转办任务
	 * @return
	 */
	private Infobox getMyAccordingMattersBox() {
		List list = this.taskExeService.accordingMattersList(UserContextUtil
				.getCurrentUserId(), null);
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-paper-plane");
		infobox.setColor("pink");
		infobox.setDataText(String.valueOf(list.size()));
		infobox.setDataContent("转办代理事宜");
		infobox.setUrl("/oa/flow/taskExe/accordingMatters.do");
		return infobox;
	}

	private Infobox getMyCompletedMattersBox() {
		List list = this.processRunService.completedMatters(UserContextUtil
				.getCurrentUserId(), null);
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-history");
		infobox.setColor("green");
		infobox.setDataText(String.valueOf(list.size()));
		infobox.setDataContent("办结事宜");
		infobox.setUrl("/oa/flow/processRun/completedMatters.do");
		return infobox;
	}

	private Infobox getMyAlreadyBox() {
		List list2 = this.processRunService.myAlready(UserContextUtil
				.getCurrentUserId(), null);
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-gavel");
		infobox.setColor("red");
		infobox.setDataText(String.valueOf(list2.size()));
		infobox.setDataContent("已办事宜");
		infobox.setUrl("/oa/flow/processRun/alreadyMatters.do");
		return infobox;
	}

	private Infobox getMyProCopytoBox() {
		Integer proCount = this.proCopytoService.getCountByUser(UserContextUtil
				.getCurrentUserId());
		Integer noReadProCount = this.proCopytoService
				.getCountNotReadByUser(UserContextUtil.getCurrentUserId());
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-folder-o");
		infobox.setColor("blue2");
		infobox.setDataText("(" + noReadProCount + "/" + proCount + ")");
		infobox.setDataContent("抄送转发");
		infobox.setUrl("/oa/flow/proCopyto/myList.do");
		return infobox;
	}
	/**
	 * 工作台内部消息展示
	 * @return
	 */
	private Infobox getMyMessBox() {
		Integer messCount = this.messageSendService
				.getCountReceiverByUser(UserContextUtil.getCurrentUserId());
		Integer noReadMessCount = this.messageSendService
				.getCountNotReadMsg(UserContextUtil.getCurrentUserId());
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-comments");
		infobox.setColor("blue2");
		infobox.setDataText("(" + noReadMessCount + "/" + messCount + ")");
		infobox.setDataContent("内部消息");
		infobox.setUrl("/oa/system/messageReceiver/list.do");
		return infobox;
	}
	
	/**
	 * 工作台待办事宜展示
	 * @return
	 */
	private Infobox getMyTaksBox() {
		List<? extends ITaskAmount> countlist = this.bpmService
				.getMyTasksCount(UserContextUtil.getCurrentUserId());
		int count = 0;
		int notRead = 0;
		for (ITaskAmount taskAmount : countlist) {
			count += taskAmount.getTotal();
			notRead += taskAmount.getNotRead();
		}
		Infobox infobox = new Infobox();
		infobox.setIcon("fa-bars");
		infobox.setColor("blue");
		infobox.setDataText("(" + notRead + "/" + count + ")");
		infobox.setDataContent("待办事宜");
		infobox.setUrl("/oa/flow/task/pendingMatters.do");
		return infobox;
	}
	
	public String myCalendar() {
		JSONArray jsonAry = new JSONArray();

		for (int i = 0; i < 100; i++) {
			JSONObject json = new JSONObject();
			json.accumulate("title", "所有事件").accumulate("start",
					DateFormatUtil.format(new Date(), "yyyy-MM-dd"))
					.accumulate("backgroundColor", "#70AFC4").accumulate(
							"eventClick",
							"function(calEvent){alert(calEvent.title)}");
			jsonAry.add(json);
		}

		JSONObject json2 = new JSONObject();
		Calendar ca = Calendar.getInstance();

		json2.accumulate("title", "长事件").accumulate("start",
				DateFormatUtil.format(ca.getTime(), "yyyy-MM-dd"));
		ca.add(5, 3);
		json2.accumulate("end",
				DateFormatUtil.format(ca.getTime(), "yyyy-MM-dd")).accumulate(
				"backgroundColor", "#D9534F");
		jsonAry.add(json2);

		
		JSONObject json3 = new JSONObject();
		ca.add(5, 6);

		json3.accumulate("title", "连接到公司网站").accumulate("start",
				DateFormatUtil.format(ca.getTime(), "yyyy-MM-dd")).accumulate(
				"url", "http://www.cssrc.com.cn/").accumulate("backgroundColor",
				"#A8BC7B");
		jsonAry.add(json3);
		
		return jsonAry.toString();
	}

	public String barChart() {
		float[] data1 = new float[12];
		float[] data2 = new float[12];

		for (int i = 0; i <= 11; i++) {
			DecimalFormat dcmFmt = new DecimalFormat("0.0");
			float f1 = new Random().nextFloat() * 1000.0F;
			float f2 = new Random().nextFloat() * 2000.0F;
			data1[i] = Float.parseFloat(dcmFmt.format(f1));
			data2[i] = Float.parseFloat(dcmFmt.format(f2));
		}
		String d1 = JSONArray.fromObject(data1).toString();
		String d2 = JSONArray.fromObject(data2).toString();
		String data = "{title:{text:'',subtext:'纯属虚构'},tooltip:{trigger:'axis'},legend:{data:['蒸发量','降水量']},toolbox:{sho:true,feature:{mark:{sho:true},dataVie:{sho:true,readOnly:false},magicType:{sho:true,type:['line','bar']},restore:{sho:true},saveAsImage:{sho:true}}},calculable:true,xAxis:[{type:'category',data:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']}],yAxis:[{type:'value'}],series:[{name:'蒸发量',type:'bar',data:"
				+ d1
				+ ",markPoint:{data:[{type:'max',name:'最大值'},{type:'min',name:'最小值'}]},markLine:{data:[{type:'average',name:'平均值'}]}},{name:'降水量',type:'bar',data:"
				+ d2
				+ ",markPoint:{data:[{name:'年最高',value:182.2,xAxis:7,yAxis:183,symbolSize:18},{name:'年最低',value:2.3,xAxis:11,yAxis:3}]},markLine:{data:[{type:'average',name:'平均值'}]}}]}";

		return data;
	}

	public String lineChart() {
		int[] data1 = new int[7];
		int[] data2 = new int[7];

		for (int i = 0; i <= 6; i++) {
			data1[i] = ((int) (Math.random() * 10.0D) + 15);
			data2[i] = (int) (Math.random() * 10.0D);
		}
		String d1 = JSONArray.fromObject(data1).toString();
		String d2 = JSONArray.fromObject(data2).toString();
		String data = "{title:{text:\"\",subtext:\"纯属虚构\"},tooltip:{trigger:\"axis\"},legend:{data:[\"最高气温\",\"最低气温\"]},calculable:\"true\",xAxis:[{type:\"category\",boundaryGap:\"false\",data:[\"周一\",\"周二\",\"周三\",\"周四\",\"周五\",\"周六\",\"周日\"]}],yAxis:[{type:\"value\",axisLabel:{formatter:\"{value} °C\"}}],series:[{name:\"最高气温\",type:\"line\",data:"
				+ d1
				+ ","
				+ "markPoint:{"
				+ "data:["
				+ "{type:\"max\",name:\"最大值\"},"
				+ "{type:\"min\",name:\"最小值\"}"
				+ "]"
				+ "},markLine:{"
				+ "data:["
				+ "{type:\"average\",name:\"平均值\"}"
				+ "]"
				+ "}"
				+ "},"
				+ "{name:\"最低气温\","
				+ "type:\"line\","
				+ "data:"
				+ d2
				+ ","
				+ "markPoint:{"
				+ "data:["
				+ "{name:\"周最低\",value:\"-2\",xAxis:\"1\",yAxis:\"-1.5\"}"
				+ "]"
				+ "},"
				+ "markLine:{"
				+ "data:["
				+ "{type:\"average\",name:\"平均值\"}" + "]" + "}" + "}" + "]}";
		return data;
	}
}
