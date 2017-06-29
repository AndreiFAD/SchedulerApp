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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
/**
 *
 * @author Fekete András Demeter 
 */
public class ReadFormJson {
    
    /**
     *
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
     * 
     */
    public String readformjson(String mysqlurl,String mysqluser,String mysqlpass,String mysqlquery,
            String oracletabel,String oraclecol,String oradeletetabel,String oracledbname) throws SQLException{
        
        String resultrun = null;
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
                ResultSet r=null;
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
                if (oradeletetabel=="N") {

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
                        conn.rollback();
                        conn.close();
                        System.out.println(e.getMessage());
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
                                                        
                                                        String rowitem = jsonparser(rs.getString(1),rs.getString(2));
                                                        Rows+=rowitem;

                                                    if (b==100){
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
                                                        conn = null;
                                                        b=0;

                                                        Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;

                                                    }  else if (cikl==count){
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
                                                        conn = null;
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
                                                                conn = null;
                                                            } catch (SQLException eu){
                                                                
                                                                System.out.println(eu.getMessage());
                                                                conn.rollback();
                                                                conn.close();
                                                            }
                    }
                conn.rollback();
                conn.close();
                conn = null;
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

    /**
     *
     * @param id T id 
     * @param jsontest JSONObject
     * @return itemlist
     */
    public String jsonparser(String id,String jsontest) {
            String itemlist ="";
            JSONObject obj=(JSONObject)JSONValue.parse(jsontest);
            JSONArray msg = (JSONArray) obj.get("claim_forms");
            for (int i = 0; i < msg.size(); i++) {
                    JSONObject jsonin = (JSONObject)msg.get(i);
                    Iterator<String> iterator = jsonin.keySet().iterator();
                    while (iterator.hasNext()) {
                        String name = iterator.next();
                        if (jsonin.get(name) instanceof JSONArray) {
                            JSONArray msg3 = (JSONArray)jsonin.get(name);
                            for (int w = 0; w < msg3.size(); w++) {
                                JSONObject obj5=(JSONObject)msg3.get(w);
                                if (obj5.get("value") instanceof JSONArray) {
                                        JSONArray msg9 = (JSONArray)obj5.get("value");
                                        for (int q = 0; q < msg9.size(); q++) {
                                            //System.out.println("select 'claim_forms','"+name.replace("\'", "''")+"','"+((String)jsonin.get("title")).replace("\'", "''")+"','"+((String)jsonin.get("tag")).replace("\'", "''")+"','"+ ((String)obj5.get("head")).replace("\'", "''")+"','"+ ((String)obj5.get("tag")).replace("\'", "''")+"','"+ ((String)msg9.get(q)).replace("\'", "''")+"' from dual \r\n union all \r\n ");
                                            itemlist += "select '"+id+"','claim_forms','"+name.replace("\'", "''")+"','"+jsonin.get("title").toString().replace("\'", "''")+"','"+jsonin.get("tag").toString().replace("\'", "''")+"','"+ obj5.get("head").toString().replace("\'", "''")+"','"+ obj5.get("tag").toString().replace("\'", "''")+"','"+ msg9.get(q).toString().replace("\'", "''")+"' from dual \r\n union all \r\n ";
                                        }
                                } else {
                                //System.out.println("select 'claim_forms','"+name.replace("\'", "''")+"','"+((String)jsonin.get("title")).replace("\'", "''")+"','"+((String)jsonin.get("tag")).replace("\'", "''")+"','"+ ((String)obj5.get("head")).replace("\'", "''")+"','"+ ((String)obj5.get("tag")).replace("\'", "''")+"','"+ ((String)obj5.get("value")).replace("\'", "''")+"' from dual \r\n union all \r\n ");
                                itemlist += "select '"+id+"','claim_forms','"+name.replace("\'", "''")+"','"+jsonin.get("title").toString().replace("\'", "''")+"','"+jsonin.get("tag").toString().replace("\'", "''")+"','"+ obj5.get("head").toString().replace("\'", "''")+"','"+ obj5.get("tag").toString().replace("\'", "''")+"','"+ obj5.get("value").toString().replace("\'", "''")+"' from dual \r\n union all \r\n ";
                                }
                            }  
                        } else {
                    }
                }
            }
            return itemlist;
    }
}