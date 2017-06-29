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
import com.schedulerapp.FixClasses.MailSMTP;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fekete András Demeter 
 */
public class SshTunnelOraExport {
    
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
     * @return ssh_tunnel ssh_tunnel
     * @throws SQLException SQLException
     */
    public static SshTunnel somethingexp(String sshuser, String sshpassword, String sshhost, String dbuserName, String dbpassword, String rhost, String rsid, int rport) throws SQLException {
        return new SshTunnel(sshuser, sshpassword, sshhost, dbuserName, dbpassword, rhost, rsid, rport);
    }
        
    /**
     *
     * @param sshuser sshuser
     * @param sshpassword sshpassword
     * @param sshhost sshhost
     * @param dbuserName sshhost
     * @param dbpassword dbpassword
     * @param rhost rhost
     * @param rsid rsid
     * @param rport rport
     * @param filePath filePath
     * @param filename filename
     * @param filenamefix filenamefix
     * @param fileformat fileformat
     * @param dbname dbname
     * @param SqlQuery SqlQuery
     * @param subject subject
     * @param jobid jobid
     * @param To To
     * @param cc cc
     * @param bcc bcc
     * @param attachment attachment
     * @return results results
     * @throws SQLException SQLException
     */
    public String SshTunnelOraExport(String sshuser, String sshpassword, String sshhost, String dbuserName, String dbpassword, String rhost, String rsid, int rport
                                   , String filePath, String filename, String filenamefix, String fileformat, String dbname, String SqlQuery
                                   , String subject, String jobid, String To, String cc, String bcc, String attachment ) throws SQLException {
        
        Connection conn = null;
        Session sessionssh = null;
        String resultrun = "";
        
        try {
            
            SshTunnel result = somethingexp(sshuser, sshpassword, sshhost, dbuserName, dbpassword, rhost, rsid, rport);
            sessionssh = result.getSessionssh();
            conn = result.getConn();
            String result_from_sshtunnel = result.getResults();
            
            if (result_from_sshtunnel=="success") {
                
           
                if (filenamefix.equals("-")){
                    SimpleDateFormat sdfDate = new SimpleDateFormat(".yyyyMMdd.HHmmss");
                    Date now = new Date();
                    String strDate = sdfDate.format(now);
                    filenamefix = strDate;
                } else {
                    filenamefix="";
                }
        
                if (fileformat.equals(".csv")) {

                        try {                

                                String selectTableSQL = SqlQuery;
                                conn.setAutoCommit(false);
                                PreparedStatement stmt = conn.prepareStatement(selectTableSQL);
                                ResultSet resultSet = stmt.executeQuery();
                                ResultSetToCSV resultSetToCSV = new ResultSetToCSV(resultSet,filePath+"/"+filename+filenamefix+fileformat);

                                MailSMTP sendmessage = new MailSMTP();

                                if (To.contains("@") || cc.contains("@") || bcc.contains("@")) {

                                    if (attachment.equals("-")) {
                                        sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  "-", filePath+"/"+filename+filenamefix+fileformat);
                                    } else {
                                        sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  filePath+"/"+filename+filenamefix+fileformat, "-");
                                    }
                                }
                                
                                if(conn != null && !conn.isClosed()){
                                    System.out.println("Closing Database Connection");
                                    conn.close();
                                }
                                if(sessionssh !=null && sessionssh.isConnected()){
                                    System.out.println("Closing SSH Connection");
                                    sessionssh.disconnect();
                                }
                                
                                resultrun = "success";

                        } catch (Exception e){

                            resultrun = e.getMessage();

                                System.out.println(e.getMessage());

                                conn.rollback();
                                
                                if(conn != null && !conn.isClosed()){
                                    System.out.println("Closing Database Connection");
                                    conn.close();
                                }
                                if(sessionssh !=null && sessionssh.isConnected()){
                                    System.out.println("Closing SSH Connection");
                                    sessionssh.disconnect();
                                }
                                
                        }

                } else if (fileformat.equals(".xls")) {

                        try {                

                                String selectTableSQL = SqlQuery;
                                conn.setAutoCommit(false);
                                PreparedStatement stmt = conn.prepareStatement(selectTableSQL);
                                ResultSet resultSet = stmt.executeQuery();

                                ResultSetToXLS resultSetToExcel = new ResultSetToXLS(resultSet, new ResultSetToXLS.FormatType[] {}, "datalist");
                                resultSetToExcel.generate(new File(filePath+"/"+filename+filenamefix+fileformat));
                                MailSMTP sendmessage = new MailSMTP();

                                if (To.contains("@") || cc.contains("@") || bcc.contains("@")) {

                                    if (attachment.equals("-")) {
                                        sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  "-", filePath+"/"+filename+filenamefix+fileformat);
                                    } else {
                                        sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  filePath+"/"+filename+filenamefix+fileformat, "-");
                                    }

                                }

                                if(conn != null && !conn.isClosed()){
                                    System.out.println("Closing Database Connection");
                                    conn.close();
                                }
                                if(sessionssh !=null && sessionssh.isConnected()){
                                    System.out.println("Closing SSH Connection");
                                    sessionssh.disconnect();
                                }
                                
                                resultrun = "success";

                        } catch (Exception e){

                                resultrun = e.getMessage();

                                System.out.println(e.getMessage());

                                conn.rollback();
                                
                                if(conn != null && !conn.isClosed()){
                                    System.out.println("Closing Database Connection");
                                    conn.close();
                                }
                                if(sessionssh !=null && sessionssh.isConnected()){
                                    System.out.println("Closing SSH Connection");
                                    sessionssh.disconnect();
                                }
                               
                        }

                } else if (fileformat.equals(".xlsx")) {

                        try {                

                                String selectTableSQL = SqlQuery;
                                conn.setAutoCommit(false);
                                PreparedStatement stmt = conn.prepareStatement(selectTableSQL);
                                ResultSet resultSet = stmt.executeQuery();

                                ResultSetToXLSX resultSetToExcel = new ResultSetToXLSX(resultSet, new ResultSetToXLSX.FormatType[] {}, "datalist");
                                resultSetToExcel.generate(new File(filePath+"/"+filename+filenamefix+fileformat));

                                MailSMTP sendmessage = new MailSMTP();

                                if (To.contains("@") || cc.contains("@") || bcc.contains("@")) {
                                    if (attachment.equals("-")) {
                                        sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  "-", filePath+"/"+filename+filenamefix+fileformat);
                                    } else {
                                        sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  filePath+"/"+filename+filenamefix+fileformat, "-");
                                    }
                                }

                                if(conn != null && !conn.isClosed()){
                                    System.out.println("Closing Database Connection");
                                    conn.close();
                                }
                                if(sessionssh !=null && sessionssh.isConnected()){
                                    System.out.println("Closing SSH Connection");
                                    sessionssh.disconnect();
                                }
                                
                                resultrun = "success";

                        } catch (Exception e){

                                resultrun = e.getMessage();

                                System.out.println(e.getMessage());

                                conn.rollback();
                                
                                if(conn != null && !conn.isClosed()){
                                    System.out.println("Closing Database Connection");
                                    conn.close();
                                }
                                if(sessionssh !=null && sessionssh.isConnected()){
                                    System.out.println("Closing SSH Connection");
                                    sessionssh.disconnect();
                                }
                                
                        }

            }
                
         } else {
              resultrun=result_from_sshtunnel;  
         }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            
        }finally{
            if(conn != null && !conn.isClosed()){
                System.out.println("Closing Database Connection");
                conn.close();
            }
            if(sessionssh !=null && sessionssh.isConnected()){
                System.out.println("Closing SSH Connection");
                sessionssh.disconnect();
            }
        }
        
        return resultrun;
    }
}
