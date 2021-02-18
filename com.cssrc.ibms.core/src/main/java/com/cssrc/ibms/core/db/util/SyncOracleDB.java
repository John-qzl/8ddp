package com.cssrc.ibms.core.db.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.cssrc.ibms.api.system.util.SysContextUtil;

public class SyncOracleDB extends DBSynchronization
{
    
    @Override
    public void executeSql(List<String> sqlFiles, Connection conn)
        throws Exception
    {
        // 循环list，执行sql
        Statement smt = conn.createStatement();
        BufferedReader bufferReader = null;
        
        // 2.1赋权限
        try
        {
            smt.execute("grant create table to " + this.getSysDataSource().getUserName());
            smt.execute("grant create procedure to " + this.getSysDataSource().getUserName());
            smt.execute("grant create sequence to " + this.getSysDataSource().getUserName());
            smt.execute("grant ALTER USER to " + this.getSysDataSource().getUserName());// 具有修改用户密码，表空等安全权限
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (smt != null)
            {
                smt.close();
                smt = null;
            }
            return;
        }
        
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
                        System.out.println("执行有问题的oracleSQL：" + executeSql.toString());
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
        for (int i = 0; i < sqlFiles.size(); i++)
        {
            String filepath = sqlFiles.get(i);
            String fileName = filepath.substring(filepath.lastIndexOf(File.separator), filepath.lastIndexOf("."));
            String procedureName = fileName.substring(fileName.indexOf("_") + 1, fileName.length());
            // 创建存储过程的对象
            CallableStatement csmt = null;
            csmt = conn.prepareCall("{ call " + procedureName + "('" + SysContextUtil.getTableSpace() + "') }");
            try
            {
                // 执行存储过程
                csmt.execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (csmt != null)
                {
                    csmt.close();
                }
            }
            
        }
        
    }
    
    @Override
    public void formatDb(Connection conn)
        throws Exception
    {
        this.formtTable(conn);
        this.formIndex(conn);
        this.formLobIndex(conn);
        this.rebuildIndex(conn);
    }

    private void formtTable(Connection conn)
        throws SQLException
    {
        StringBuffer sql=new StringBuffer("SELECT ");
        sql.append("('alter table '||a.TABLE_NAME||'  move tablespace  "+SysContextUtil.getTableSpace()+"')");
        sql.append("AS \"exe_sql\"");
        sql.append("FROM user_tables a ");
        sql.append("WHERE  tablespace_name!=UPPER('"+SysContextUtil.getTableSpace()+"')");
        Statement smt = conn.createStatement();
        try
        {
            ResultSet rset=smt.executeQuery(sql.toString());
            while(rset.next()) {
                smt.addBatch(rset.getString("exe_sql"));
            }
            smt.executeBatch();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(smt!=null) {
                smt.close();
            }
            
        }
    }
    
    
    
    public void formIndex(Connection conn) throws Exception{
        StringBuffer sql=new StringBuffer("SELECT ");
        sql.append("INDEX_NAME as \"INDEX_NAME\"");
        sql.append(",INDEX_TYPE as \"INDEX_TYPE\"");
        sql.append(" FROM dba_indexes t");
        sql.append(" WHERE t.tablespace_name!=UPPER('"+SysContextUtil.getTableSpace()+"')");
        sql.append(" AND t.owner=UPPER('"+SysContextUtil.getTableSpace()+"')");
        sql.append(" AND INDEX_TYPE='NORMAL'");
        
        Statement smt = conn.createStatement();
        
        try
        {
            ResultSet rset=smt.executeQuery(sql.toString());
            while(rset.next()) {
                smt.addBatch("ALTER INDEX  "+rset.getString("INDEX_NAME")+" REBUILD TABLESPACE  "+SysContextUtil.getTableSpace());
            }
            smt.executeBatch();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(smt!=null) {
                smt.close();
            }
            
        }
        
        
    }
    
    
    
    public void formLobIndex(Connection conn) throws Exception{
        StringBuffer sql=new StringBuffer("SELECT 'alter table '||t.owner||'.'||t.table_name||' ");
        sql.append("move tablespace "+SysContextUtil.getTableSpace()+" lob('||t.column_name ||') ");
        sql.append("store as(tablespace "+SysContextUtil.getTableSpace()+" )' \"exe_sql\"");
        sql.append("FROM dba_lobs t");
        sql.append(" WHERE t.tablespace_name!=UPPER('"+SysContextUtil.getTableSpace()+"')");
        sql.append(" AND t.owner=UPPER('"+SysContextUtil.getTableSpace()+"')");
        Statement smt = conn.createStatement();
        try
        {
            ResultSet rset=smt.executeQuery(sql.toString());
            while(rset.next()) {
                smt.addBatch(rset.getString("exe_sql"));
            }
            smt.executeBatch();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(smt!=null) {
                smt.close();
            }
            
        }
        
        
    }
    
    public void rebuildIndex(Connection conn) throws Exception{
        StringBuffer sql=new StringBuffer("SELECT 'ALTER INDEX  '||t.INDEX_NAME||' REBUILD' \"sql\"");
        sql.append(" FROM user_indexes t WHERE status='UNUSABLE' ");
        Statement smt = conn.createStatement();
        try
        {
            ResultSet rset=smt.executeQuery(sql.toString());
            while(rset.next()) {
                smt.addBatch(rset.getString("sql"));
            }
            smt.executeBatch();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(smt!=null) {
                smt.close();
            }
            
        }
        
        
    }
    

}
