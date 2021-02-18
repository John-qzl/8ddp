package rtx;

import java.io.PrintStream;

public class RTXSvrApi
{
  static int PRO_ADDUSER = 1;
  static int PRO_DELUSER = 2;
  static int PRO_GETUSERDETAILINFO = 4;
  static int PRO_SETUSERDETAILINFO = 5;
  static int PRO_GETUSERSMPLINFO = 6;
  static int PRO_SETUSERSMPLINFO = 3;
  static int PRO_SETUSERPRIVILEGE = 7;
  static int PRO_IFEXIST = 8;
  static int PRO_TRANUSER = 9;
  static int PRO_ADDDEPT = 257;
  static int PRO_DELDEPT = 258;
  static int PRO_SETDEPT = 259;
  static int PRO_GETCHILDDEPT = 260;
  static int PRO_GETDEPTALLUSER = 261;
  static int PRO_SETDEPTPRIVILEGE = 262;
  static int PRO_GETDEPTSMPLINFO = 263;
  static int PRO_SMS_LOGON = 4096;
  static int PRO_SMS_SEND = 4097;
  static int PRO_SMS_NICKLIST = 4098;
  static int PRO_SMS_FUNCLIST = 4099;
  static int PRO_SMS_CHECK = 4100;
  static int PRO_SYS_GETSESSIONKEY = 8192;
  static int PRO_SYS_GETUSERSTATUS = 8193;
  static int PRO_SYS_SENDIM = 8194;
  static int PRO_SYS_SESSIONKEYVERIFY = 8195;
  static int PRO_EXT_NOTIFY = 8448;
  static int PRO_IMPORTUSER = 1;
  static int PRO_EXMPORTUSER = 2;
  static String OBJNAME_RTXEXT = "EXTTOOLS";
  static String OBJNAME_RTXSYS = "SYSTOOLS";
  static String OBJNAME_DEPTMANAGER = "DEPTMANAGER";
  static String OBJNAME_USERMANAGER = "USERMANAGER";
  static String OBJNAME_SMSMANAGER = "SMSOBJECT";
  static String OBJNAME_USERSYNC = "USERSYNC";
  static String KEY_TYPE = "TYPE";
  static String KEY_USERID = "USERID";
  static String KEY_USERNAME = "USERNAME";
  static String KEY_UIN = "UIN";
  static String KEY_NICK = "NICK";
  static String KEY_MOBILE = "MOBILE";
  static String KEY_OUTERUIN = "OUTERUIN";
  static String KEY_LASTMODIFYTIME = "LASTMODIFYTIME";
  static String KEY_FACE = "FACE";
  static String KEY_PASSWORD = "PWD";
  static String KEY_AGE = "AGE";
  static String KEY_GENDER = "GENDER";
  static String KEY_BIRTHDAY = "BIRTHDAY";
  static String KEY_BLOODTYPE = "BLOODTYPE";
  static String KEY_CONSTELLATION = "CONSTELLATION";
  static String KEY_COLLAGE = "COLLAGE";
  static String KEY_HOMEPAGE = "HOMEPAGE";
  static String KEY_EMAIL = "EMAIL";
  static String KEY_PHONE = "PHONE";
  static String KEY_FAX = "FAX";
  static String KEY_ADDRESS = "ADDRESS";
  static String KEY_POSTCODE = "POSTCODE";
  static String KEY_COUNTRY = "COUNTRY";
  static String KEY_PROVINCE = "PROVINCE";
  static String KEY_CITY = "CITY";
  static String KEY_MEMO = "MEMO";
  static String KEY_STREET = "STREET";
  static String KEY_MOBILETYPE = "MOBILETYPE";
  static String KEY_AUTHTYPE = "AUTHTYPE";
  static String KEY_POSITION = "POSITION";
  static String KEY_OPENGSMINFO = "OPENGSMINFO";
  static String KEY_OPENCONTACTINFO = "OPENCONTACTINFO";
  static String KEY_PUBOUTUIN = "PUBOUTUIN";
  static String KEY_PUBOUTNICK = "PUBOUTNICK";
  static String KEY_PUBOUTNAME = "PUBOUTNAME";
  static String KEY_PUBOUTDEPT = "PUBOUTDEPT";
  static String KEY_PUBOUTPOSITION = "PUBOUTPOSITION";
  static String KEY_PUBOUTINFO = "PUBOUTINFO";
  static String KEY_OUTERPUBLISH = "OUTERPUBLISH";
  static String KEY_LDAPID = "LDAPID";
  static String KEY_DEPTID = "DEPTID";
  static String KEY_PDEPTID = "PDEPTID";
  static String KEY_SORTID = "SORTID";
  static String KEY_NAME = "NAME";
  static String KEY_INFO = "INFO";
  static String KEY_COMPLETEDELBS = "COMPLETEDELBS";
  static String KEY_DENY = "DENY";
  static String KEY_ALLOW = "ALLOW";
  static String KEY_SESSIONKEY = "SESSIONKEY";
  static String KEY_MODIFYMODE = "MODIFYMODE";
  static String KEY_DATA = "DATA";
  static String KEY_SENDER = "SENDER";
  static String KEY_FUNNO = "FUNCNO";
  static String KEY_RECEIVER = "RECEIVER";
  static String KEY_RECEIVERUIN = "RECEIVERUIN";
  static String KEY_SMS = "SMS";
  static String KEY_CUT = "CUT";
  static String KEY_NOTITLE = "NOTITLE";
  static String KEY_DELFLAG = "DELFLAG";
  static String KEY_RECVUSERS = "RECVUSERS";
  static String KEY_IMMSG = "IMMSG";
  static String KEY_MSGID = "MSGID";
  static String KEY_MSGINFO = "MSGINFO";
  static String KEY_ASSISTANTTYPE = "ASSTYPE";
  static String KEY_TITLE = "TITLE";
  static String KEY_DELAYTIME = "DELAYTIME";
  static String KEY_RESULT_INCODE = "INNERCODE";
  static String KEY_RESULT_ERR_INFO = "ERR_INFO";
  static String KEY_RESULT_CODE = "CODE";
  static String KEY_RESULT_TYPE = "TYPE";
  static String KEY_RESULT_NAME = "NAME";
  static String KEY_RESULT_VALUE = "VALUE";
  static String KEY_RESULT_VARIANT = "VARIANT";
  private int jdField_for;
  private int a;
  private int jdField_do;
  private int jdField_if;

  public native boolean Init();

  public native void UnInit();

  public native String GetError(int paramInt);

  public native String GetVersion();

  public native int GetNewObject(String paramString);

  public native String GetObjectName(int paramInt);

  public native int SetObjectName(int paramInt, String paramString);

  public native int GetNewPropertys();

  public native int IsHandle(int paramInt);

  public native int AddRefHandle(int paramInt);

  public native int ReleaseHandle(int paramInt);

  public native int AddProperty(int paramInt, String paramString1, String paramString2);

  public native int ClearProperty(int paramInt1, int paramInt2);

  public native int RemoveProperty(int paramInt, String paramString);

  public native String GetProperty(int paramInt, String paramString);

  public native int SetServerIP(int paramInt, String paramString);

  public native String GetServerIP(int paramInt);

  public native int GetServerPort(int paramInt);

  public native int SetServerPort(int paramInt1, int paramInt2);

  public native int GetPropertysCount(int paramInt);

  public native int GetPropertysItem(int paramInt1, int paramInt2);

  public native int Call(int paramInt1, int paramInt2, int paramInt3);

  public native int GetResultPropertys(int paramInt);

  public native int GetResultInt(int paramInt);

  public native String GetResultString(int paramInt);

  public String GetPropertyItemName(int paramInt)
  {
    return GetProperty(paramInt, KEY_RESULT_NAME);
  }

  public String GetPropertyItemValue(int paramInt)
  {
    return GetProperty(paramInt, KEY_RESULT_VALUE);
  }

  public int GetResultInnerCode(int paramInt)
  {
    String str = GetProperty(paramInt, KEY_RESULT_INCODE);
    return Integer.parseInt(str);
  }

  public String GetResultErrString(int paramInt)
  {
    String str = GetProperty(paramInt, KEY_RESULT_ERR_INFO);
    return str;
  }

  public int GetResultCode(int paramInt)
  {
    String str = GetProperty(paramInt, KEY_RESULT_CODE);
    return Integer.parseInt(str);
  }

  public int GetResultType(int paramInt)
  {
    String str = GetProperty(paramInt, KEY_RESULT_TYPE);
    return Integer.parseInt(str);
  }

  private void a(String paramString)
  {
    this.jdField_for = GetNewObject(paramString);
    this.a = GetNewPropertys();
  }

  private void a()
  {
    ReleaseHandle(this.jdField_for);
    ReleaseHandle(this.a);
    ReleaseHandle(this.jdField_if);
  }

  public void setServerIP(String paramString)
  {
    a(OBJNAME_RTXSYS);
    int i = SetServerIP(this.jdField_for, paramString);
    a();
  }

  public void setServerPort(int paramInt)
  {
    a(OBJNAME_RTXSYS);
    int i = SetServerPort(this.jdField_for, paramInt);
    a();
  }

  public String getServerIP()
  {
    a(OBJNAME_RTXSYS);
    String str = GetServerIP(this.jdField_for);
    a();
    return str;
  }

  public int getServerPort()
  {
    a(OBJNAME_RTXSYS);
    int i = GetServerPort(this.jdField_for);
    a();
    return i;
  }

  public int QueryUserState(String paramString)
  {
    a(OBJNAME_RTXSYS);
    AddProperty(this.a, KEY_USERNAME, paramString);
    int i = Call(this.jdField_for, this.a, PRO_SYS_GETUSERSTATUS);
    int j = GetResultInnerCode(i);
    int k = GetResultInt(i);
    if (j != 0)
    {
      System.out.println("QueryUserState 错误代码:" + j + "\t" + "错误信息：" + GetResultErrString(i));
      a();
      return j;
    }
    a();
    return k;
  }

  public int deleteUser(String paramString)
  {
    if ((paramString == null) || ("".equals(paramString)))
      return -1;
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_DELUSER);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("deleteUser 错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public int addUser(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_NICK, paramString1);
    AddProperty(this.a, KEY_DEPTID, paramString2);
    AddProperty(this.a, KEY_NAME, paramString3);
    AddProperty(this.a, KEY_PASSWORD, paramString4);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_ADDUSER);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("addUser 错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public String[][] GetUserSimpleInfo(String paramString)
  {
    String[][] arrayOfString = (String[][])null;
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString);
    int i = Call(this.jdField_for, this.a, PRO_GETUSERSMPLINFO);
    int j = GetResultInnerCode(i);
    if (j != 0)
    {
      System.out.println("GetUserSimpleInfo 错误代码:" + j + "\t" + "错误信息：" + GetResultErrString(i));
    }
    else
    {
      int k = GetResultPropertys(i);
      int m = GetPropertysCount(k);
      arrayOfString = new String[m][2];
      for (int n = 0; n < m; n++)
      {
        int i1 = GetPropertysItem(k, n);
        arrayOfString[n][0] = GetPropertyItemName(i1);
        arrayOfString[n][1] = GetPropertyItemValue(i1);
        ReleaseHandle(i1);
      }
    }
    a();
    return arrayOfString;
  }

  public int SetUserSimpleInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString1);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_GETUSERSMPLINFO);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
    {
      System.out.println("SetUserSimpleInfo 错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
      a();
      return this.jdField_do;
    }
    int i = GetResultPropertys(this.jdField_if);
    int j = GetPropertysCount(i);
    for (int k = 0; k < j; k++)
      if (k == 9)
      {
        if ((paramString7 == null) || ("".equals(paramString7)) || ("null".equals(paramString7)))
          continue;
        AddProperty(this.a, KEY_PASSWORD, paramString7);
      }
      else
      {
        int m = GetPropertysItem(i, k);
        AddProperty(this.a, GetPropertyItemName(m), GetPropertyItemValue(m));
        ReleaseHandle(m);
      }
    if ((paramString2 != null) && (!"".equals(paramString2)) && (!"null".equals(paramString2)))
      AddProperty(this.a, KEY_NAME, paramString2);
    if ((paramString4 != null) && (!"".equals(paramString4)) && (!"null".equals(paramString4)))
      AddProperty(this.a, KEY_GENDER, paramString4);
    if ((paramString5 != null) && (!"".equals(paramString5)) && (!"null".equals(paramString5)))
      AddProperty(this.a, KEY_MOBILE, paramString5);
    if ((paramString6 != null) && (!"".equals(paramString6)) && (!"null".equals(paramString6)))
      AddProperty(this.a, KEY_PHONE, paramString6);
    if ((paramString3 != null) && (!"".equals(paramString3)) && (!"null".equals(paramString3)))
      AddProperty(this.a, KEY_EMAIL, paramString3);
    i = Call(this.jdField_for, this.a, PRO_SETUSERSMPLINFO);
    j = GetResultInnerCode(i);
    if (j != 0)
      System.out.println("SetUserSimpleInfo2 错误代码:" + j + "\t" + "错误信息：" + GetResultErrString(i));
    a();
    return j;
  }

  public String[][] GetUserDetailInfo(String paramString)
  {
    String[][] arrayOfString = (String[][])null;
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString);
    int i = Call(this.jdField_for, this.a, PRO_GETUSERDETAILINFO);
    int j = GetResultInnerCode(i);
    if (j != 0)
    {
      System.out.println("GetUserDetailInfo 错误代码:" + j + "\t" + "错误信息：" + GetResultErrString(i));
    }
    else
    {
      int k = GetResultPropertys(i);
      int m = GetPropertysCount(k);
      arrayOfString = new String[m][2];
      for (int n = 0; n < m; n++)
      {
        int i1 = GetPropertysItem(k, n);
        arrayOfString[n][0] = GetPropertyItemName(i1);
        arrayOfString[n][1] = GetPropertyItemValue(i1);
        ReleaseHandle(i1);
      }
    }
    a();
    return arrayOfString;
  }

  public int setUserDetailInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16, String paramString17, String paramString18)
  {
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString1);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_GETUSERSMPLINFO);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
    {
      System.out.println("setUserDetailInfo 错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
      a();
      return this.jdField_do;
    }
    int i = GetResultPropertys(this.jdField_if);
    int j = GetPropertysItem(i, 0);
    AddProperty(this.a, KEY_DEPTID, GetPropertyItemValue(j));
    ReleaseHandle(j);
    if ((paramString2 != null) && (!"".equals(paramString2)) && (!"null".equals(paramString2)))
      AddProperty(this.a, KEY_ADDRESS, paramString2);
    if ((paramString5 != null) && (!"".equals(paramString5)) && (!"null".equals(paramString5)))
      AddProperty(this.a, KEY_BLOODTYPE, paramString5);
    if ((paramString9 != null) && (!"".equals(paramString9)) && (!"null".equals(paramString9)))
      AddProperty(this.a, KEY_COUNTRY, paramString9);
    if ((paramString15 != null) && (!"".equals(paramString15)) && (!"null".equals(paramString15)))
      AddProperty(this.a, KEY_PROVINCE, paramString15);
    if ((paramString6 != null) && (!"".equals(paramString6)) && (!"null".equals(paramString6)))
      AddProperty(this.a, KEY_CITY, paramString6);
    if ((paramString14 != null) && (!"".equals(paramString14)) && (!"null".equals(paramString14)))
      AddProperty(this.a, KEY_POSTCODE, paramString14);
    if ((paramString11 != null) && (!"".equals(paramString11)) && (!"null".equals(paramString11)))
      AddProperty(this.a, KEY_HOMEPAGE, paramString11);
    if ((paramString17 != null) && (!"".equals(paramString17)) && (!"null".equals(paramString17)))
      AddProperty(this.a, KEY_PHONE, paramString17);
    if ((paramString18 != null) && (!"".equals(paramString18)) && (!"null".equals(paramString18)))
      AddProperty(this.a, KEY_MOBILE, paramString18);
    if ((paramString12 != null) && (!"".equals(paramString12)) && (!"null".equals(paramString12)))
      AddProperty(this.a, KEY_MEMO, paramString12);
    if ((paramString13 != null) && (!"".equals(paramString13)) && (!"null".equals(paramString13)))
      AddProperty(this.a, KEY_POSITION, paramString13);
    if ((paramString10 != null) && (!"".equals(paramString10)) && (!"null".equals(paramString10)))
      AddProperty(this.a, KEY_FAX, paramString10);
    if ((paramString3 != null) && (!"".equals(paramString3)) && (!"null".equals(paramString3)))
      AddProperty(this.a, KEY_AGE, paramString3);
    if ((paramString4 != null) && (!"".equals(paramString4)) && (!"null".equals(paramString4)))
      AddProperty(this.a, KEY_BIRTHDAY, paramString4);
    if ((paramString7 != null) && (!"".equals(paramString7)) && (!"null".equals(paramString7)))
      AddProperty(this.a, KEY_COLLAGE, paramString7);
    if ((paramString16 != null) && (!"".equals(paramString16)) && (!"null".equals(paramString16)))
      AddProperty(this.a, KEY_STREET, paramString16);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_SETUSERDETAILINFO);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("setUserDetailInfo2 错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public String UinToUserName(String paramString)
  {
    String str = null;
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_GETUSERSMPLINFO);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
    {
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
      a();
      return null;
    }
    int i = GetResultPropertys(this.jdField_if);
    int j = GetPropertysItem(i, 7);
    str = GetPropertyItemValue(j);
    ReleaseHandle(j);
    a();
    return str;
  }

  public int addDept(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    a(OBJNAME_DEPTMANAGER);
    AddProperty(this.a, KEY_DEPTID, paramString1);
    AddProperty(this.a, KEY_NAME, paramString3);
    AddProperty(this.a, KEY_INFO, paramString2);
    AddProperty(this.a, KEY_PDEPTID, paramString4);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_ADDDEPT);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("addDept 错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public int setDept(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    a(OBJNAME_DEPTMANAGER);
    AddProperty(this.a, KEY_DEPTID, paramString1);
    AddProperty(this.a, KEY_NAME, paramString3);
    AddProperty(this.a, KEY_INFO, paramString2);
    AddProperty(this.a, KEY_PDEPTID, paramString4);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_SETDEPT);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public int deleteDept(String paramString1, String paramString2)
  {
    a(OBJNAME_DEPTMANAGER);
    AddProperty(this.a, KEY_DEPTID, paramString1);
    AddProperty(this.a, KEY_COMPLETEDELBS, paramString2);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_DELDEPT);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public int deptIsExist(String paramString)
  {
    if ((paramString == null) || ("".equals(paramString)))
      return -100;
    String str1 = "0";
    String str2 = "tempdept";
    a(OBJNAME_DEPTMANAGER);
    AddProperty(this.a, KEY_PDEPTID, str1);
    AddProperty(this.a, KEY_DEPTID, paramString);
    AddProperty(this.a, KEY_NAME, str2);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_ADDDEPT);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do == -5)
    {
      a();
      return this.jdField_do;
    }
    if (this.jdField_do == 0)
    {
      Call(this.jdField_for, this.a, PRO_DELDEPT);
      a();
      return this.jdField_do;
    }
    System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public String[] getDeptUsers(String paramString)
  {
    String[] arrayOfString = null;
    a(OBJNAME_DEPTMANAGER);
    AddProperty(this.a, KEY_DEPTID, paramString);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_GETDEPTALLUSER);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    int i = GetResultPropertys(this.jdField_if);
    int j = GetPropertysCount(i);
    if (j > 0)
    {
      arrayOfString = new String[j];
      for (int k = 0; k < j; k++)
      {
        int m = GetPropertysItem(i, k);
        arrayOfString[k] = UinToUserName(GetPropertyItemValue(m));
        ReleaseHandle(m);
      }
    }
    a();
    return arrayOfString;
  }

  public String[] getChildDepts(String paramString)
  {
    String[] arrayOfString = null;
    a(OBJNAME_DEPTMANAGER);
    AddProperty(this.a, KEY_PDEPTID, paramString);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_GETCHILDDEPT);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
    {
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
      a();
      return null;
    }
    int i = GetResultPropertys(this.jdField_if);
    int j = GetPropertysCount(i);
    if (j > 0)
    {
      arrayOfString = new String[j];
      for (int k = 0; k < j; k++)
      {
        int m = GetPropertysItem(i, k);
        arrayOfString[k] = GetPropertyItemValue(m);
        ReleaseHandle(m);
      }
    }
    a();
    return arrayOfString;
  }

  public int userIsExist(String paramString)
  {
    a(OBJNAME_USERMANAGER);
    AddProperty(this.a, KEY_USERNAME, paramString);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_IFEXIST);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    int i = GetResultInt(this.jdField_if);
    a();
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    return i;
  }

  public String getSessionKey(String paramString)
  {
    a(OBJNAME_RTXSYS);
    AddProperty(this.a, KEY_USERNAME, paramString);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_SYS_GETSESSIONKEY);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    String str = new String("");
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    else
      str = GetResultString(this.jdField_if);
    a();
    return str;
  }

  public int sendNotify(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    a(OBJNAME_RTXEXT);
    AddProperty(this.a, KEY_USERNAME, paramString1);
    AddProperty(this.a, KEY_TITLE, paramString2);
    AddProperty(this.a, KEY_MSGINFO, paramString3);
    AddProperty(this.a, KEY_TYPE, paramString4);
    AddProperty(this.a, KEY_MSGID, "0");
    AddProperty(this.a, KEY_ASSISTANTTYPE, "0");
    if (!"0".equals(paramString5))
      AddProperty(this.a, KEY_DELAYTIME, paramString5);
    this.jdField_if = Call(this.jdField_for, this.a, PRO_EXT_NOTIFY);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    a();
    return this.jdField_do;
  }

  public int sendSms(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
  {
    a(OBJNAME_SMSMANAGER);
    AddProperty(this.a, KEY_SENDER, paramString1);
    AddProperty(this.a, KEY_RECEIVER, paramString2);
    AddProperty(this.a, KEY_SMS, paramString3);
    AddProperty(this.a, KEY_CUT, String.valueOf(paramInt1));
    AddProperty(this.a, KEY_NOTITLE, String.valueOf(paramInt2));
    this.jdField_if = Call(this.jdField_for, this.a, PRO_SMS_SEND);
    this.jdField_do = GetResultInnerCode(this.jdField_if);
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(this.jdField_if));
    a();
    return this.jdField_do;
  }

  public String exportXmldata()
  {
    String str = null;
    a(OBJNAME_USERSYNC);
    AddProperty(this.a, "MODIFYMODE", "1");
    AddProperty(this.a, "XMLENCODE", "<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
    int i = Call(this.jdField_for, this.a, PRO_EXMPORTUSER);
    int j = GetResultPropertys(i);
    this.jdField_do = GetResultInnerCode(i);
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(i));
    str = GetResultString(i);
    a();
    return str;
  }

  public int importXmldata(String paramString)
  {
    a(OBJNAME_USERSYNC);
    AddProperty(this.a, KEY_MODIFYMODE, "1");
    AddProperty(this.a, KEY_DATA, paramString);
    int i = Call(this.jdField_for, this.a, PRO_IMPORTUSER);
    this.jdField_do = GetResultInnerCode(i);
    if (this.jdField_do != 0)
      System.out.println("错误代码:" + this.jdField_do + "\t" + "错误信息：" + GetResultErrString(i));
    a();
    return this.jdField_do;
  }

  static
  {
    System.loadLibrary("SDKAPIJava");
  }
}




