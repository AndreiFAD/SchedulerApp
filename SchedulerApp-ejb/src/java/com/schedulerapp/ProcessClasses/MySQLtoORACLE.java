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

import com.schedulerapp.FixClasses.JdbcHandler;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fekete András Demeter 
 */
public class MySQLtoORACLE {
       
    /**
     *
     * @param mysqlurl mysql connectionstring
     * @param mysqluser mysql user
     * @param mysqlpass mysql password
     * @param mysqlquery mysql query
     * @param oracledbname oracle database name : rep_1 or rep_2 or apex_1
     * @param oracletabel oracle table name
     * @param oradeletetabel if Y trunc table before insert 
     * @param oraclecol oracle table cols name
     * @return String result
     * @throws SQLException SQL Exception
     */
    public String MySQLtoORACLE(String mysqlurl,String mysqluser,String mysqlpass,String mysqlquery, String oracledbname, String oracletabel, String oradeletetabel,String oraclecol) throws SQLException{
        
        
    String resultrun= null;
    String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    try {
        Class.forName(DATABASE_DRIVER);
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(MySQLtoORACLE.class.getName()).log(Level.SEVERE, null, ex);
    }
      Connection conn_mysql = null;
    try {
        conn_mysql = (Connection) DriverManager.getConnection(mysqlurl,mysqluser,mysqlpass);
    } catch (SQLException ex) {
        Logger.getLogger(MySQLtoORACLE.class.getName()).log(Level.SEVERE, null, ex);
    }
   
       try {
           
            System.err.println("MysqlDatabase connection is active");
            Statement s=null;
            Statement stmt = null;
            ResultSet r = null;
            ResultSet rs = null;
            int count = 0;
            int numberOfColumns = 0;
            try{
               
             stmt = conn_mysql.createStatement();
 
            rs = stmt.executeQuery(mysqlquery);
            
             
            ResultSetMetaData meta = rs.getMetaData() ;
            numberOfColumns = meta.getColumnCount() ;
            System.err.println(numberOfColumns);
           
             s = conn_mysql.createStatement();
             r = s.executeQuery("SELECT COUNT(1) AS rowcount FROM ("+mysqlquery+") d");
            System.err.println("SELECT COUNT(1) AS rowcount FROM ("+mysqlquery+") d");
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
                    Statement stmtora = conn.createStatement();
                    ResultSet rsora;
                    rsora = stmtora.executeQuery("truncate table " + oracletabel );
                    conn.commit();
                    conn.close();
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                    conn.rollback();
                    conn.close();
                }
               
            }
           
            int b = 0;
            int cikl =0;
            java.sql.Connection conn = null;
            rs = stmt.executeQuery(mysqlquery);
            
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
 
        } catch (Exception e) {
            
            
                    resultrun = e.getMessage();
                    System.err.print("Got an exception! ");
                    System.err.print(e.getMessage());
        
        
        } finally  {
           
            conn_mysql.close();
            System.out.println("MysqlDatabase connection close now");
            
      }
       return resultrun;
    }
    
}