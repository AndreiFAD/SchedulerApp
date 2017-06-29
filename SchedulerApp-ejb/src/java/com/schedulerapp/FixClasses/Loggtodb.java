/*
 * Copyright (C) 2017 Fekete András Demeter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.schedulerapp.FixClasses;

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;

/**
 *
 * @author Fekete András Demeter 
 */
public class Loggtodb {
    
    /**
     *
     * @return Connection
     * @throws SQLException SQLException
     */
    public Connection connt() throws SQLException{
      OracleDataSource ods = new OracleDataSource();
      ods.setURL("jdbc:oracle:thin:@host.domain.com:1526:rep_1");
      ods.setUser("user");
      ods.setPassword("passwd");
      Connection conn = ods.getConnection();
      return conn;
    }
       
    /**
     *
     * @param id job id
     * @param jobname job name
     * @param jobinfo job info
     * @return int return record rowid
     * @throws SQLException  SQLException from oracle
     */
    public int logentry(int id,String jobname,String jobinfo) throws SQLException{

      String jobquery = "{call logme_package.new_process(?,?,?,?)}";
      
      Connection conn=connt();
      CallableStatement callStmt = conn.prepareCall(jobquery);
      callStmt.setInt(2, id);
      callStmt.setString(3, jobname);
      callStmt.setString(4, jobinfo);
      callStmt.registerOutParameter(1, OracleTypes.NUMBER);
      callStmt.execute();
      int rset = (int)callStmt.getInt(1);
      callStmt.close();
      conn.close();
      return rset;
    } 

    /**
     *
     * @param vid record rowid
     * @param jobinfo job info
     * @param joberror job error
     * @throws SQLException  SQLException from oracle
     */
    public void logclose(int vid,String jobinfo,String joberror) throws SQLException{

      String jobquery = "{call logme_package.close_process(?,?,?)}";
      Connection conn=connt();
      CallableStatement callStmt = conn.prepareCall(jobquery);
      callStmt.setInt(1, vid);
      callStmt.setString(2, jobinfo);
      callStmt.setString(3, joberror);
      callStmt.execute();
      callStmt.close();
      conn.close();
    }
    
    /**
     *
     * @param jobid  job id
     * @return String when run it
     * @throws SQLException  SQLException from oracle
     */
    public String last_log_time(String jobid) throws SQLException{

      String jobquery = "{call logme_package.log_time_send(?,?)}";
      Connection conn=connt();
      CallableStatement callStmt = conn.prepareCall(jobquery);
      callStmt.setString(1, jobid);
      callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
      callStmt.execute();
      String rset = (String)callStmt.getString(2);
      callStmt.close();
      conn.close();
      return rset;
      
    }

}
