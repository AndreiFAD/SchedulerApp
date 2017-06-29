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
package com.schedulerapp.ProcessClasses;

import com.schedulerapp.FixClasses.SshTunnel;

import com.jcraft.jsch.Session;
import com.schedulerapp.FixClasses.JdbcHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Fekete András Demeter 
 */
public class SshTunnelOraImport {
    
    /**
     *
     * @param sshuser  sshuser
     * @param sshpassword sshpassword
     * @param sshhost sshhost
     * @param dbuserName dbuserName
     * @param dbpassword dbpassword
     * @param rhost rhost
     * @param rsid rsid
     * @param rport rport
     * @return ssh_tunnel ssh_tunnel
     * @throws SQLException SQLException
     */
    public static SshTunnel somethingimp(String sshuser, String sshpassword, String sshhost, String dbuserName, String dbpassword, String rhost, String rsid, int rport) throws SQLException {
        return new SshTunnel(sshuser, sshpassword, sshhost, dbuserName, dbpassword, rhost, rsid, rport);
    }
        
    /**
     *
     * @param sshuser sshuser
     * @param sshpassword sshpassword
     * @param sshhost sshhost
     * @param dbuserName dbuserName
     * @param dbpassword dbpassword
     * @param rhost rhost
     * @param rsid rsid
     * @param rport rport
     * @param exportquery exportquery
     * @param oracledbname oracledbname
     * @param oracletabel oracletabel
     * @param oradeletetabel oradeletetabel
     * @param oraclecol oraclecol
     * @return results results
     * @throws SQLException SQLException
     */
    public String SshTunnelOraImport(String sshuser, String sshpassword, String sshhost, String dbuserName, String dbpassword, String rhost, String rsid, int rport
                                   , String exportquery, String oracledbname, String oracletabel, String oradeletetabel, String oraclecol ) throws SQLException {
        
        Connection connimport = null;
        Session sessionssh = null;
        String resultrun= null;
        
        try {
            SshTunnel result = somethingimp(sshuser, sshpassword, sshhost, dbuserName, dbpassword, rhost, rsid, rport);
            sessionssh = result.getSessionssh();
            connimport = result.getConn();
            String result_from_sshtunnel = result.getResults();
            
            if (result_from_sshtunnel=="success") {
           
            System.err.println("import Database connection is active");
            Statement s=null;
            Statement stmt = null;
            ResultSet r = null;
            ResultSet rs = null;
            int count = 0;
            int numberOfColumns = 0;
            try{
               
            stmt = connimport.createStatement();
 
            rs = stmt.executeQuery(exportquery);
            
             
            ResultSetMetaData meta = rs.getMetaData() ;
            numberOfColumns = meta.getColumnCount() ;
            System.err.println(numberOfColumns);
           
            s = connimport.createStatement();
            r = s.executeQuery("SELECT COUNT(1) AS rowcount FROM ("+exportquery+") d");
            System.err.println("SELECT COUNT(1) AS rowcount FROM ("+exportquery+") d");
            r.next();
            count = r.getInt("rowcount");
            r.close();
            
            System.err.println(count);
           
            
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
            String Rows=null;
            Rows ="insert into " + oracletabel + " (" + oraclecol + " ) " ;
            if (oradeletetabel.equals("N")) {
               
            } else {
                System.err.println("trunc table");
                java.sql.Connection conn = null;
                try{
                    
                    String dbname = oracledbname;
                    JdbcHandler Hand =new JdbcHandler();
                    conn = Hand.getConnection(dbname);
                    conn.setAutoCommit(false);
                    System.err.println("truncate "+ conn.getClientInfo(dbname));
                    Statement stmtora = conn.createStatement();
                    ResultSet rsora;
                    System.err.println("truncate table " + oracletabel);
                    rsora = stmtora.executeQuery("truncate table " + oracletabel );
                    conn.commit();
                    conn.close();
                    System.err.println("sikeres : truncate table " + oracletabel);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                    conn.rollback();
                    conn.close();
                }
               
            }
           
            int b = 0;
            int cikl =0;
            java.sql.Connection conn = null;
            rs = stmt.executeQuery(exportquery);
            
            try{
                                     while (rs.next()) {
                                                   cikl+=1;
                                                    b+=1;
                                                    String rowitem = rs.getString(1);
                                                    rowitem = rowitem.replace("\'", "''");
                                                    
                                                   String row = " select '" + rowitem + "'"  ;
                                                   
                                                   String rowitem2 = null;
                                                   for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) {
                                                       rowitem2=rs.getString(i);
                                                       
                                                       try{
                                                            rowitem2 = rowitem2.replace("\'", "''");
                                                       } catch (Exception t) {
                                                           rowitem2=rs.getString(i);
                                                       }
                                                       
                                                       row += ",'" + rowitem2 + "'" ;
                                                    }
                                          
                                                   Rows+=row+" from dual \r\n union all \r\n ";
                                                   
                                                if (b==900){
                                                    conn = null;
                                                    Rows=Rows.substring(0, Rows.length() - 13);
                                                    String SqlQuery=Rows;
                                                    String dbname = oracledbname;
                                                    JdbcHandler Hand =new JdbcHandler();
                                                    conn = Hand.getConnection(dbname);
                                                    conn.setAutoCommit(false);
                                                    System.err.println("insert "+ conn.getClientInfo(dbname));
                                                    Statement stmtora = conn.createStatement();
                                                    ResultSet rsora;
                                                    rsora = stmtora.executeQuery(SqlQuery);
                                                    conn.commit();
                                                    conn.close();
                                                    b=0;
                                                    
                                                    Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;
                                                
                                                }  else if (cikl==count){
                                                    
                                                    Rows=Rows.substring(0, Rows.length() - 13);
                                                    String SqlQuery=Rows;
                                                    String dbname = oracledbname;
                                                    JdbcHandler Hand =new JdbcHandler();
                                                    conn = Hand.getConnection(dbname);
                                                    conn.setAutoCommit(false);
                                                    System.err.println("insert "+ conn.getClientInfo(dbname));
                                                    Statement stmtora = conn.createStatement();
                                                    ResultSet rsora;
                                                    rsora = stmtora.executeQuery(SqlQuery);
                                                    conn.commit();
                                                    conn.close();
                                                    b=0;
                                                    
                                                    Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;
                                                
                                                }  else {
                                                  
                                                }
                                                
                                                
                                               
                                            }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(Rows);
                
                if (oradeletetabel.equals("N")) {
               
                } else { 
                                                        
                                                        try{
                                                            conn = null;
                                                            String dbname = oracledbname;
                                                            JdbcHandler Hand =new JdbcHandler();
                                                            conn = Hand.getConnection(dbname);
                                                            conn.setAutoCommit(false);
                                                            Statement stmtora = conn.createStatement();
                                                            ResultSet rsora;
                                                            rsora = stmtora.executeQuery("truncate table " + oracletabel );
                                                            conn.commit();
                                                            conn.close();
                                                        } catch (SQLException eu){
                                                            
                                                            System.out.println(eu.getMessage());
                                                            conn.rollback();
                                                            conn.close();
                                                        }
                }
                conn.rollback();
                conn.close();
            }                              
                                                
            resultrun = "success";                   
         } else {
              resultrun=result_from_sshtunnel;  
         }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            resultrun = e.getMessage(); 
        }finally{
            if(connimport != null && !connimport.isClosed()){
                System.out.println("Closing Database Connection");
                connimport.close();
            }
            if(sessionssh !=null && sessionssh.isConnected()){
                System.out.println("Closing SSH Connection");
                sessionssh.disconnect();
            }
        }
    return resultrun;
    }
}
