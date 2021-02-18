package com.cssrc.ibms.core.mdmsync.test;

import com.cssrc.ibms.core.mdmsync.action.ExecuteCommand;
import com.cssrc.ibms.core.mdmsync.comand.OrgSyncCommand;

public class TestCommand
{
    public static void main(String... args)
    {
        OrgSyncCommand com = new OrgSyncCommand();
        ExecuteCommand.exe(com);
    }
}
