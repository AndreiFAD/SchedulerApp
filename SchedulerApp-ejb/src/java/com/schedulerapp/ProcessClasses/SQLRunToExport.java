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

import com.schedulerapp.FixClasses.MailSMTP;
import com.schedulerapp.FixClasses.JdbcHandler;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fekete András Demeter 
 */
public class SQLRunToExport {

    /**
     *
     * @param filePath file full path
     * @param filename file name
     * @param filenamefix file name type 
     * @param fileformat file format
     * @param dbname oracle database name rep_1 or rep_2 or apex_1
     * @param SqlQuery sql query
     * @param subject mail subject
     * @param jobid job id
     * @param To TO
     * @param cc CC
     * @param bcc BCC
     * @param attachment attachment ( Yes or - )
     * @return String result
     * @throws Exception Exception
     */
    @SuppressWarnings("empty-statement")
    public String SQLRunToExport(String filePath, String filename, String filenamefix, String fileformat, String dbname, String SqlQuery, String subject, String jobid, String To, String cc, String bcc, String attachment) throws Exception {
        
        
        String resultrun = "";
        
        if (filenamefix.equals("-")){
            SimpleDateFormat sdfDate = new SimpleDateFormat(".yyyyMMdd.HHmmss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            filenamefix = strDate;
        } else {
            filenamefix="";
        }
        
        if (fileformat.equals(".csv")) {
            
                Connection conn = null;
                try {                
                        
			String selectTableSQL = SqlQuery;
			JdbcHandler Hand =new JdbcHandler();
			conn = Hand.getConnection(dbname);
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
                        
                        conn.close();
                        
                        resultrun = "success";
                        
        	} catch (Exception e){
                    
                    resultrun = e.getMessage();
                    
			System.out.println(e.getMessage());
                        
                        conn.rollback();
                        conn.close();
		}
                
                
        } else if (fileformat.equals(".xls")) {
            
                Connection conn = null;
                try {                
                        
			String selectTableSQL = SqlQuery;
			JdbcHandler Hand =new JdbcHandler();
			conn = Hand.getConnection(dbname);
                        conn.setAutoCommit(false);
                        PreparedStatement stmt = conn.prepareStatement(selectTableSQL);
                        ResultSet resultSet = stmt.executeQuery();
                        
                        ResultSetToXLS resultSetToExcel = new ResultSetToXLS(resultSet,
                            new ResultSetToXLS.FormatType[] {}, "datalist");
                        resultSetToExcel.generate(new File(filePath+"/"+filename+filenamefix+fileformat));
                        MailSMTP sendmessage = new MailSMTP();

                        if (To.contains("@") || cc.contains("@") || bcc.contains("@")) {
                            
                            if (attachment.equals("-")) {
                                sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  "-", filePath+"/"+filename+filenamefix+fileformat);
                            } else {
                                sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  filePath+"/"+filename+filenamefix+fileformat, "-");
                            }
                        
                        }
                        
                        conn.close();
                        
                        resultrun = "success";
                        
        	} catch (Exception e){
                    
                       resultrun = e.getMessage();
                       
			System.out.println(e.getMessage());
                        
                        conn.rollback();
                        conn.close();
		}
                
                
        } else if (fileformat.equals(".xlsx")) {
        
                Connection conn = null;
                try {                
                        
			String selectTableSQL = SqlQuery;
			JdbcHandler Hand =new JdbcHandler();
			conn = Hand.getConnection(dbname);
                        conn.setAutoCommit(false);
                        PreparedStatement stmt = conn.prepareStatement(selectTableSQL);
                        ResultSet resultSet = stmt.executeQuery();
          
                        ResultSetToXLSX resultSetToExcel = new ResultSetToXLSX(resultSet,
                                new ResultSetToXLSX.FormatType[] {}, "datalist");
                        resultSetToExcel.generate(new File(filePath+"/"+filename+filenamefix+fileformat));
                        
                        MailSMTP sendmessage = new MailSMTP();

                        if (To.contains("@") || cc.contains("@") || bcc.contains("@")) {
                            if (attachment.equals("-")) {
                                sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  "-", filePath+"/"+filename+filenamefix+fileformat);
                            } else {
                                sendmessage.mailSMTP(jobid, To, cc, bcc,  subject,  filePath+"/"+filename+filenamefix+fileformat, "-");
                            }
                        }
                        
                        conn.close();
                        
                        resultrun = "success";
                        
        	} catch (Exception e){
                    
                        resultrun = e.getMessage();
                        
			System.out.println(e.getMessage());
                        
                        conn.rollback();
                        conn.close();
		}
                
       
    }
    
       return resultrun;
    }
    
}
