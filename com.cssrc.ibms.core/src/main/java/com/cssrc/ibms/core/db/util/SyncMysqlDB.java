package com.cssrc.ibms.core.db.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import com.cssrc.ibms.api.system.util.SysContextUtil;

public class SyncMysqlDB extends DBSynchronization
{
    @Override
    public void executeSql(List<String> sqlFiles, Connection conn)
        throws Exception
    {
        // 循环list，执行sql
        Statement smt = conn.createStatement();
        String tablespace = SysContextUtil.getTableSpace();
        smt.execute("select '" + tablespace + "' INTO @spacename;");
        
        BufferedReader bufferReader = null;
        try
        {
            for (int i = 0; i < sqlFiles.size(); i++)
            {
                
                String fileName = sqlFiles.get(i);
                bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
                String tempStr = null;
                StringBuffer executeSql = new StringBuffer();
                while ((tempStr = bufferReader.readLine()) != null)
                {
                    executeSql.append(tempStr).append("\n");
                }
                if (executeSql != null && executeSql.length() > 0)
                {
                    try
                    {
                        smt.execute(executeSql.toString());
                    }
                    catch (Exception e)
                    {
                        System.out.println("执行有问题的mysqlSQL：" + executeSql.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bufferReader != null)
            {
                bufferReader.close();
                bufferReader = null;
            }
            if (smt != null)
            {
                smt.close();
                smt = null;
            }
        }
        
    }

    @Override
    public void formatDb(Connection conn)
        throws Exception
    {
        //mysql 不需要转表空间
    }
    
    
    
    
}
