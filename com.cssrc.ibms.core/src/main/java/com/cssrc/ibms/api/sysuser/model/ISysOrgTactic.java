package com.cssrc.ibms.api.sysuser.model;

public interface ISysOrgTactic {
	public static short ORG_TACTIC_WITHOUT = 0;
	public static short ORG_TACTIC_LEVEL = 1;
	public static short ORG_TACTIC_SELECT = 2;
	public static short ORG_TACTIC_COMBINATION = 3;
	Short getOrgTactic();
}