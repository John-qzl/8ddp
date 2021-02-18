package rtx;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.apache.log4j.Logger;

import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.date.DateUtil;

public class RTXUtil {
	public static Logger logger = Logger.getLogger(RTXUtil.class.getName());

	public static boolean sendNotify(String receiver, String title,
			String content, String delayTime) {
		String str = "0";
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			i = localRTXSvrApi.sendNotify(receiver, title, content, str,
					delayTime);
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static boolean addDept(ISysOrg paramDeptDb) {
		// 设置维度为行政维度
		paramDeptDb.setDemId(1L);
		String str = "0";
		if (paramDeptDb.getOrgSupId() != 0L) {
			str = "" + paramDeptDb.getOrgSupId();
		}
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init()) {
			System.out.println("rtx.RTXUtil dd.id=" + paramDeptDb.getOrgId()
					+ " ParentDeptId=" + str);
			i = localRTXSvrApi.addDept("" + paramDeptDb.getOrgId(),
					paramDeptDb.getOrgName(), paramDeptDb.getOrgName(), str);
		}
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static boolean setDept(ISysOrg paramDeptDb) {
		String str1 = "" + paramDeptDb.getOrgId();
		String str2 = paramDeptDb.getOrgName();
		String str3 = paramDeptDb.getOrgName();
		String str4 = "0";
		if (paramDeptDb.getOrgSupId() != 0L) {
			str4 = "" + paramDeptDb.getOrgSupId();
		}
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			i = localRTXSvrApi.setDept(str1, str2, str3, str4);
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static boolean deleteDept(String paramString) {
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			i = localRTXSvrApi.deleteDept(paramString, "1");
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static void sendSms() {
		String str1 = "13952867098";
		String str2 = "111";
		String str3 = "SDK手机短信测试";
		int i = 1;
		int j = 0;
		int k = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init()) {
			k = localRTXSvrApi.sendSms(str2, str1, str3, i, j);
			if (k == 0)
				System.out.println("发送成功");
			else
				System.out.println("发送失败");
		}
		localRTXSvrApi.UnInit();
	}

	public static boolean auth(ISysUser paramUserDb, String paramString) {
		String str = RTXUserProxy.getRTXName(paramUserDb);
		if (str.equals(""))
			return false;
		ReleaseManager localReleaseManager = new ReleaseManager();
		try {
			IDispatch localIDispatch1 = new IDispatch(localReleaseManager,
					"RTXSAPIRootObj.RTXSAPIRootObj");
			IDispatch localIDispatch2 = (IDispatch) localIDispatch1
					.get("UserAuthObj");
			Boolean localBoolean = (Boolean) localIDispatch2.method(
					"SignatureAuth", new Object[] { str, paramString });
			boolean bool = localBoolean.booleanValue();
			return bool;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			localReleaseManager.release();
		}
		return false;
	}

	public static boolean delUserFromDept(ISysUser paramUserDb,
			String paramString) {
		String str = RTXUserProxy.getRTXName(paramUserDb);
		if (str.equals(""))
			return false;
		ReleaseManager localReleaseManager = new ReleaseManager();
		boolean i = false;
		try {
			IDispatch localIDispatch1 = new IDispatch(localReleaseManager,
					"RTXSAPIRootObj.RTXSAPIRootObj");
			IDispatch localIDispatch2 = (IDispatch) localIDispatch1
					.get("DeptManager");
			localIDispatch2.method("DelUserFromDept", new Object[] { str,
					paramString });
			i = true;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			localReleaseManager.release();
		}
		return i;
	}

	public static boolean delUser(ISysUser paramUserDb) {
		String str = RTXUserProxy.getRTXName(paramUserDb);
		if (str.equals(""))
			return false;
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			i = localRTXSvrApi.deleteUser(str);
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static void initDepts(String paramString) {
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		ISysOrgService dService = (ISysOrgService) AppUtil
				.getBean(ISysOrgService.class);
		if (localRTXSvrApi.Init()) {
			ISysOrg localDeptDb = dService.getByCode(paramString);
			initDeptTree(localDeptDb, localRTXSvrApi);
		}
		localRTXSvrApi.UnInit();
	}

	public static void initDept(ISysOrg paramDeptDb, RTXSvrApi paramRTXSvrApi) {
		String str1 = "" + paramDeptDb.getOrgId();
		String str2 = paramDeptDb.getOrgName();
		String str3 = paramDeptDb.getOrgName();
		ISysOrgService dService = (ISysOrgService) AppUtil
				.getBean(ISysOrgService.class);
		ISysOrg localDeptDb = dService.getByCode(paramDeptDb.getCode());
		String str4 = "" + localDeptDb.getOrgId();
		if (paramDeptDb.getOrgSupId() == 0L)
			str4 = "0";
		int i = -1;
		logger.info("leaf=" + paramDeptDb.getOrgName() + " ParentDeptId="
				+ str4);
		i = paramRTXSvrApi.addDept(str1, str2, str3, str4);
		if (i == 0)
			logger.info("添加部门" + paramDeptDb.getOrgName() + "成功");
		else
			logger.error("添加部门" + paramDeptDb.getOrgName() + "失败");
	}

	public static void initDeptTree(ISysOrg paramDeptDb, RTXSvrApi paramRTXSvrApi) {
		if (!paramDeptDb.getCode().equals("root"))
			initDept(paramDeptDb, paramRTXSvrApi);
		ISysOrgService dService = (ISysOrgService) AppUtil
				.getBean(ISysOrgService.class);
		List<?extends ISysOrg> localVector = dService.getByOrgPath(paramDeptDb.getPath());// TODO
																				// --yangbo
																				// 这里怀疑有错。
		int i = localVector.size();
		if (i == 0)
			return;
		Iterator<?extends ISysOrg> localIterator = localVector.iterator();
		while (localIterator.hasNext()) {
			ISysOrg localDeptDb = localIterator.next();
			initDeptTree(localDeptDb, paramRTXSvrApi);
		}
	}

	public static boolean addUser(ISysUser paramUserDb, String paramString1,
			String paramString2, String paramString3) {
		String str = RTXUserProxy.getRTXName(paramUserDb);
		if (str.equals(""))
			return false;
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			i = localRTXSvrApi.addUser(str, paramString1, paramString2,
					paramString3);
		if (i == 0) {
			// paramUserDb.setUin(getUin(RTXUserProxy.getRTXName(paramUserDb)));
			// paramUserDb.save();
		}
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static boolean addUserToDept(ISysUser paramUserDb,
			String paramString1, String paramString2, boolean paramBoolean) {
		String str = RTXUserProxy.getRTXName(paramUserDb);
		if (str.equals(""))
			return false;
		ReleaseManager localReleaseManager = new ReleaseManager();
		boolean i = false;
		try {
			IDispatch localIDispatch1 = new IDispatch(localReleaseManager,
					"RTXSAPIRootObj.RTXSAPIRootObj");
			IDispatch localIDispatch2 = (IDispatch) localIDispatch1
					.get("DeptManager");
			localIDispatch2.method("AddUserToDept", new Object[] { str,
					paramString1, paramString2, new Boolean(paramBoolean) });
			i = true;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			localReleaseManager.release();
		}
		return i;
	}

	public static String getUin(String paramString) {
		String str = "";
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init()) {
			String[][] arrayOfString = localRTXSvrApi
					.GetUserSimpleInfo(paramString);
			str = arrayOfString[(arrayOfString.length - 1)][1];
		}
		localRTXSvrApi.UnInit();
		return str;
	}

	public static void initUsers() {
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		ISysUserService userService = (ISysUserService) AppUtil
				.getBean(ISysUserService.class);
		/*
		 * ISysUserOrgService sysUserOrgService = (ISysUserOrgService)
		 * AppUtil.getBean(ISysUserOrgService.class);
		 */
		ISysOrgService sysOrgService = (ISysOrgService) AppUtil
				.getBean(ISysOrgService.class);
		if (localRTXSvrApi.Init()) {
			List<?extends ISysUser> listUser = userService.getAll();
			Iterator<?extends ISysUser> localIterator1 = listUser.iterator();
			while (localIterator1.hasNext()) {
				ISysUser user = localIterator1.next();
				/*
				 * Iterator<ISysUserOrg> localIterator2 =
				 * sysUserOrgService.getOrgByUserId(user.getUserId(),
				 * 1L).iterator();
				 */
				Iterator<?extends ISysOrg> localIterator2 = sysOrgService
						.getByUserIdAndDemId(user.getUserId(), 1L).iterator();
				while (localIterator2.hasNext()) {
					ISysOrg sysOrg = localIterator2.next();
					String str1 = sysOrg.getOrgName();
					String str2 = "" + sysOrg.getOrgId();
					String str3 = user.getFullname();
					String str4 = user.getPassword();
					int i = -1;
					i = localRTXSvrApi.addUser(str1, str2, str3, str4);
					if (i == 0)
						System.out.println("添加" + user.getFullname() + "成功");
					else
						System.out.println("添加" + user.getFullname() + "失败");
				}
			}
		}
		localRTXSvrApi.UnInit();
	}

	public static boolean setUserSimpleInfo(ISysUser paramUserDb) {
		String str1 = RTXUserProxy.getRTXName(paramUserDb);
		if (str1.equals(""))
			return false;
		String str2 = paramUserDb.getUsername();
		String str3 = paramUserDb.getFullname();
		String str4 = paramUserDb.getEmail();
		String str5 = "" + paramUserDb.getSex();
		String str6 = paramUserDb.getMobile();
		String str7 = paramUserDb.getPhone();
		String str8 = paramUserDb.getPassword();
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			i = localRTXSvrApi.SetUserSimpleInfo(str1, str3, str4, str5, str6,
					str7, str8);
		localRTXSvrApi.UnInit();
		return i == 0;
	}

	public static boolean setUserDetailInfo(ISysUser paramUserDb) {
		String str1 = RTXUserProxy.getRTXName(paramUserDb);
		if (str1.equals(""))
			return false;
		setUserSimpleInfo(paramUserDb);
		String str2 = paramUserDb.getUsername();
		String str3 = paramUserDb.getAddress();
		String str4 = "0";
		if (paramUserDb.getBirthDay() != null) {
			int i = DateUtil.getYear(new Date())
					- DateUtil.getYear(paramUserDb.getBirthDay());
			str4 = "" + i;
		}
		String str5 = DateFormatUtil
				.format(paramUserDb.getBirthDay(), "yyMMdd");
		String str6 = "1";
		String str7 = "";
		String str8 = "";
		String str9 = "3";
		String str10 = "中国";
		String str11 = "8012";
		String str12 = "";
		String str13 = "";
		String str14 = "";
		String str15 = "";
		String str16 = paramUserDb.getAddress();
		String str17 = paramUserDb.getPhone();
		String str18 = paramUserDb.getMobile();
		String str19 = "";
		int j = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init())
			j = localRTXSvrApi.setUserDetailInfo(str1, str3, str4, str5, str6,
					str7, str8, str9, str10, str11, str12, str13, str19, str14,
					str15, str16, str17, str18);
		localRTXSvrApi.UnInit();
		return j == 0;
	}

	public static void main(String[] paramArrayOfString) {
		int i = -1;
		RTXSvrApi localRTXSvrApi = new RTXSvrApi();
		if (localRTXSvrApi.Init()) {
			String[][] arrayOfString = localRTXSvrApi
					.GetUserSimpleInfo("qq0001");
			for (int j = 0; j < arrayOfString.length; j++) {
				System.out.println(arrayOfString[j][0]);
				System.out.println(arrayOfString[j][1]);
			}
		}
		localRTXSvrApi.UnInit();
	}

	public static void delAllUser() {
		ISysUserService userService = (ISysUserService) AppUtil
				.getBean(ISysUserService.class);
		List<? extends ISysUser> listUser = userService.getAll();
		Iterator<? extends ISysUser> localIterator = listUser.iterator();
		while (localIterator.hasNext()) {
			ISysUser localUserDb = localIterator.next();
			delUser(localUserDb);
		}
	}
}
