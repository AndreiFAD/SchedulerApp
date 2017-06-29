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
package com.schedulerapp.web;

import com.schedulerapp.FixClasses.JdbcHandler;
import com.schedulerapp.common.JobHst;
import com.schedulerapp.common.JobInfo;
import com.schedulerapp.common.LogMe;
import com.schedulerapp.ejb.JobSessionBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Fekete András Demeter
 */
@ManagedBean(name = "JobMBean")
@SessionScoped
public class JobMBean implements java.io.Serializable
{
    @EJB
    private JobSessionBean jobSessionBean;
    JobInfo selectedJob;
    private JobInfo newJob;
    List<JobHst> jobHstList;   
    List<LogMe> jobLogMe;  
    /** Creates a new instance of JobMBean */
    public JobMBean()
    {
    }
    /**
     * Getter method for the newJob property
     *
     * @return String
     * @throws Exception
     */

    public String createJobFromDB() throws Exception
    {
        String sqlstring = "select \n" +
                            "nvl(JOBID,'-'), nvl(JOBNAME,'-'), nvl(JOBCLASSNAME,'-'), nvl(DESCRIPTION,'-'),STARTDATE, \n" +
                            "ENDDATE, nvl(SECOND,'-'), nvl(MINUTE,'-'), nvl(HOUR,'-'), nvl(DAYOFWEEK,'-'), \n" +
                            "nvl(DAYOFMONTH,'-'), nvl(MONTH,'-'), nvl(YEAR,'-'), nvl(FILEPATH,'-'), nvl(FILENAME,'-'), \n" +
                            "nvl(FILEFORMAT,'-'), nvl(DBNAME,'-'), nvl(SQLQUERY,'-'), nvl(METHODTYPE,'-'), nvl(FILENAMEFIX,'-'), \n" +
                            "nvl(EXCELPATH,'-'), nvl(EXCELNAME,'-'), nvl(MACRONAME,'-'), nvl(EXEFILENAME,'-'), nvl(MYSQLURL,'-'), \n" +
                            "nvl(MYSQLUSER,'-'), nvl(MYSQLPASS,'-'), nvl(MYSQLQUERY,'-'), nvl(ORACLEDBNAME,'-'), nvl(ORACLETABEL,'-'), \n" +
                            "nvl(ORADELETETABEL,'-'), nvl(ORACLECOL,'-'), \n" +
                
                            "  nvl(EMAILSUBJECT,'-'), \n" +
                            "  nvl(EMAILTO,'-'), \n" +
                            "  nvl(EMAILCC,'-'), \n" +
                            "  nvl(EMAILBCC,'-'), \n" +
                            "  nvl(EMAILATTACH,'-'), \n" +
                            "  nvl(EMAILMACROFINALFILE,'-'), \n" +
                            "  nvl(USERNAME,'-'), \n" +  
                            "  nvl(jobcheck,'-'), \n" + 
                            "  nvl(needjobid,'-'), \n" + 
                            
                            "nvl(FILEFOLDERPATH,'-'), \n" + 
                            "nvl(FILESTRINGNAME,'-'), \n" + 
                            "nvl(CSVSPLITBY,'-'), \n" + 
                            "nvl(MOVEPATCH,'-'), \n" + 
                            "nvl(FILEFORMATTYPETOORA,'-'), \n" + 
                
                            "nvl(sshuser,''), \n" +
                            "nvl(sshpassword,''), \n" +
                            "nvl(sshhost,''), \n" +
                            "nvl(dbuserName,''), \n" +
                            "nvl(dbpassword,''), \n" +
                            "nvl(rhost,''), \n" +
                            "nvl(rsid,''), \n" +
                            "nvl(rport,''), \n" +
                            
                            "nvl(exportquery,'') \n" +
                
                            "from SCHEDULER_APP_JOBS";
        FacesContext context = FacesContext.getCurrentInstance();
        java.sql.Connection conn = null;
        try{
                
                JdbcHandler Hand =new JdbcHandler();
                conn = Hand.getConnection("rep_1");
                conn.setAutoCommit(false);
                Statement stmtora = conn.createStatement();
                ResultSet rsora = stmtora.executeQuery(sqlstring);
                while (rsora.next()) {
                    System.err.println(rsora.toString());
                    JobInfo newitem = getNewJob();
                    try{
                        newitem.setJobId(rsora.getString(1));
                        newitem.setJobName(rsora.getString(2));
                        newitem.setJobClassName(rsora.getString(3));
                        newitem.setDescription(rsora.getString(4));
                        newitem.setStartDate(rsora.getDate(5));
                        newitem.setEndDate(rsora.getDate(6));
                        newitem.setSecond(rsora.getString(7));
                        newitem.setMinute(rsora.getString(8));
                        newitem.setHour(rsora.getString(9));
                        newitem.setDayOfWeek(rsora.getString(10));
                        newitem.setDayOfMonth(rsora.getString(11));
                        newitem.setMonth(rsora.getString(12));
                        newitem.setYear(rsora.getString(13));
                        newitem.setFilePath(rsora.getString(14));
                        newitem.setFilename(rsora.getString(15));
                        newitem.setFileformat(rsora.getString(16));
                        newitem.setDbname(rsora.getString(17));
                        newitem.setSqlQuery(rsora.getString(18));
                        newitem.setMethodtype(rsora.getString(19));
                        newitem.setFilenamefix(rsora.getString(20));
                        newitem.setExcelpath(rsora.getString(21));
                        newitem.setExcelname(rsora.getString(22));
                        newitem.setMacroname(rsora.getString(23));
                        newitem.setExefilename(rsora.getString(24));
                        newitem.setMysqlurl(rsora.getString(25));
                        newitem.setMysqluser(rsora.getString(26));
                        newitem.setMysqlpass(rsora.getString(27));
                        newitem.setMysqlquery(rsora.getString(28));
                        newitem.setOracledbname(rsora.getString(29));
                        newitem.setOracletabel(rsora.getString(30));
                        newitem.setOradeletetabel(rsora.getString(31));
                        newitem.setOraclecol(rsora.getString(32));
                        
                        newitem.setEmailsubject(rsora.getString(33));
                        newitem.setEmailto(rsora.getString(34));
                        newitem.setEmailcc(rsora.getString(35));
                        newitem.setEmailbcc(rsora.getString(36));
                        newitem.setEmailattach(rsora.getString(37));
                        newitem.setEmailmacrofinalfile(rsora.getString(38));
                        newitem.setUsername(rsora.getString(39).toUpperCase());
                        
                        newitem.setJobcheck(rsora.getString(40));
                        newitem.setNeedjobid(rsora.getString(41));
                        
                        newitem.setFilefolderpath(rsora.getString(42));
                        newitem.setFilestringname(rsora.getString(43));
                        newitem.setCvsSplitBy(rsora.getString(44));
                        newitem.setMovePatch(rsora.getString(45));
                        newitem.setFileformattypetoora(rsora.getString(46));
                        
                        newitem.setSshuser(rsora.getString(47));
                        newitem.setSshpassword(rsora.getString(48));
                        newitem.setSshhost(rsora.getString(49));
                        newitem.setDbuserName(rsora.getString(50));
                        newitem.setDbpassword(rsora.getString(51));
                        newitem.setRhost(rsora.getString(52));
                        newitem.setRsid(rsora.getString(53));
                        newitem.setRport(rsora.getString(54));
                        
                        newitem.setExportquery(rsora.getString(55));
                        
                        jobSessionBean.createJob(newitem,"n");
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Success", "Job import is successful! - id:"+rsora.getString(1)));
                      } catch (Exception e){
                          System.out.println(e);
                            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                             "Failed", "Job with the ID already exist or inactive! - id:"+rsora.getString(1)));
                      }
                        
                    }
                conn.commit();
                conn.close();
                
            } catch (SQLException e){
                conn.close();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                             "Failed", e.getMessage().toString()));
                System.out.println(e.getMessage());
            }
        return "JobList";
    }
    
    /**
     *
     * @return JobInfo
     * @throws java.sql.SQLException
     */
    public JobInfo getNewJob() throws java.sql.SQLException
    {   
        
        
        
        String sqlstring =  "select to_char(max(JOBID)+1) from (\n" +
                            "select distinct JOBID from (\n" +
                            "select distinct to_number(JOBID) JOBID from SCHEDULER_APP_JOBS\n" +
                            "union\n" +
                            "select distinct to_number(JOBID) JOBID from SCHEDULER_APP_JOBS_HST))";
        
        newJob.setJobId("987000");
        java.sql.Connection conn = null;
        try{ 
                
                JdbcHandler Hand =new JdbcHandler();
                conn = Hand.getConnection("rep_1");
                conn.setAutoCommit(false);
                Statement stmtora = conn.createStatement();
                ResultSet rsora = stmtora.executeQuery(sqlstring);
                while (rsora.next()) {
                try{
                        newJob.setJobId(rsora.getString(1));

                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                
                    System.out.println(sqlstring);
                    conn.commit();
                    conn.close();
                } catch (SQLException e){
                    conn.rollback();
                    conn.close();
                    System.out.println(e);
                }
        
        
        
        newJob.setUsername(SessionUtils.getUserName());
        return newJob;
    }
    

    
    /**
     * Setter method for the newJob property
     *
     * @param newJob
     */
    public void setNewJob(JobInfo newJob)
    {
        this.newJob = newJob;
    }
    
    
   
    /**
     * Getter method for the selectedJob property
     * @return selectedJobInfo
     */
    public JobInfo getSelectedJob()
    {
        return selectedJob;
    }
    /**
     * Setter method for the selectedJob property
     * @param selectedJob
     * @return String
     */
    public String setSelectedJob(JobInfo selectedJob)
    {
        this.selectedJob = jobSessionBean.getJobInfo(selectedJob);
        HttpSession session = SessionUtils.getSession();
	session.setAttribute("jobno", this.selectedJob.getJobId());
        return "JobDetails";
    }
    
    /**
     * run test job
     * @return String
     */
    public String TestJob()
    {
        try
        {   
            new Thread(new Runnable() {
                    public void run() {
                        selectedJob = jobSessionBean.testjob(selectedJob);
                    }
                }).start();
            
        }
        catch (Exception ex)
        {
            Logger.getLogger(JobMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "JobDetails";
    }
    
    /**
     *
     * @param selectedJob
     * @return String
     */
    public String setSelectedJob2(JobInfo selectedJob) throws Exception
    {
        this.selectedJob = jobSessionBean.getJobInfo2(selectedJob);
        HttpSession session = SessionUtils.getSession();
	session.setAttribute("jobno", this.selectedJob.getJobId());
        return "InactiveJobDetails";
    }
    /**
     * Action handler for back to Listing Page
     * @return  String
     */
    public String gotoListing()
    {
        return "JobList";
    }
    /**
     * Action handler for back to Listing Page
     * @return  String
     */
    public String gotoLogin()
    {
            return "login";
    }
    /**
     * Action handler for New Job button
     * @return String
     */
    public String gotoNew()
    {
        System.out.println("gotoNew() called!!!");
        newJob = new JobInfo();
        return "JobNew";
    }
    /**
     * Action handler for Duplicate button in the Details page
     * @return String
     */
    public String duplicateJob()
    {
        newJob = selectedJob;
        newJob.setJobId("<Job ID>");
        return "JobNew";
    }

    /**
     * Action handler for Update button in the inactiv job Details page
     * @return String
     */
    public String updateInactiveJob()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        try
        {   
            selectedJob = jobSessionBean.updateInactiveJob(selectedJob);
            saveJobitemtoDB(selectedJob,"update_inactive");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success", "Job successfully updated!"));
        }
        catch (Exception ex)
        {
            Logger.getLogger(JobMBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", ex.getCause().getMessage()));
        }
        return null;
    }
        
    /**
     * Action handler for Update button in the Details page
     * @return  String
    */
    public String selectedjobidd(){
        
        return selectedJob.getJobId();
    }
    
    /**
     *
     * @return String
     */
    public String updateJob()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        try
        {   
            
            selectedJob = jobSessionBean.updateJob(selectedJob);
            saveJobitemtoDB(selectedJob,  "update");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success", "Job successfully updated!"));
        }
        catch (Exception ex)
        {
            Logger.getLogger(JobMBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", ex.toString()));
        }
        return null;
    }
    /**
     * Action handler for inactivate button in the Details page
     * @return String
     * @throws java.sql.SQLException
     */
    public String inactivateJob() throws SQLException
    {
        
        FacesContext context = FacesContext.getCurrentInstance();
        try
        {
        jobSessionBean.inactivateJob(selectedJob);
        saveJobitemtoDB(selectedJob,"inactivate");
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success", "Job successfully deactivate! jobid: " + selectedJob.getJobId()));
        }catch (Exception ex)
        {
            Logger.getLogger(JobMBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", ex.getCause().getMessage()));
        }
        
        return "JobList";
    }
    /**
     * Action handler for Delete button in the Details page
     *
     *
     * @return String
     * @throws SQLException
     */

    public String deleteJob() throws SQLException, Exception
    {
        jobSessionBean.deleteJob(selectedJob);
        saveJobitemtoDB(selectedJob,"delete");
        return "InactiveJobList";
    }
    
    /**
     *
     * @return String
     * @throws SQLException
     */
    public String reactivateJob() throws SQLException, Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        jobSessionBean.removeFromInactiveList(selectedJob);
        saveJobitemtoDB(selectedJob,"reactivate");
        
            try
                {
                jobSessionBean.createJob(selectedJob,"n");
                
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Sucess", "Job successfully reactivate! jobid: " + selectedJob.getJobId()));
                }
                catch (Exception ex)
                {
                    Logger.getLogger(JobMBean.class.getName()).log(Level.SEVERE, null, ex);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed", ex.getCause().getMessage()));
                }
        
        return "InactiveJobList";
    }
    
    /**
     * Action handler for Create button in the New page
     *
     * @return String
     */

    public String createJob()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        try
        {
            
            selectedJob = jobSessionBean.createJob(newJob, "ff");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucess", "Job successfully created!"));

            if (selectedJob.getMethodtype().equals("OracleExport")) {
                selectedJob.setJobClassName("java:module/BatchJobC");
                
                return "OracleExport";
            } else if (selectedJob.getMethodtype().equals("MySQLimporttoOracle")){
                selectedJob.setJobClassName("java:module/BatchJobD");
                
                return "MySQLtoOracle";
            } else if (selectedJob.getMethodtype().equals("exerun")){
                selectedJob.setJobClassName("java:module/BatchJobB");
                
                return "ConsoleExeRun";
            } else if (selectedJob.getMethodtype().equals("RunExcelMAcro")){
                selectedJob.setJobClassName("java:module/BatchJobA");
                
                return "ExcelMacroRun";   
            } else if (selectedJob.getMethodtype().equals("-")){
                selectedJob.setJobClassName("java:module/-");
                
                return "JobDetails";
            } else if (selectedJob.getMethodtype().equals("READFORM")){  
                selectedJob.setJobClassName("java:module/BatchJobE");
                
                return "MySQLtoOracle";
            } else if (selectedJob.getMethodtype().equals("CSVtoOracle")){  
                selectedJob.setJobClassName("java:module/BatchJobF");
                
                return "CSVtoOracle";
            } else if (selectedJob.getMethodtype().equals("SSHtunnelexportfromOracle")){  
                selectedJob.setJobClassName("java:module/BatchJobG");
                
                return "OracleExportWithSSH"; //
            } else if (selectedJob.getMethodtype().equals("SSHtunnelOracleimporttoOracle")){  
                selectedJob.setJobClassName("java:module/BatchJobH");
                
                return "OracleImportToOracleWithSSH"; //
            } else {
                
            return "JobDetails";
            }
            
        }
        catch (Exception ex)
        {
            Logger.getLogger(JobMBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", ex.getCause().getMessage()));
        }
        return null;
    }
    
    /**
     *
     * @param jobInfo
     * @param MOD
     * @throws Exception
     */
    public void saveJobitemtoDB(JobInfo jobInfo, String MOD) throws Exception
    {
        
        String hello=null;
        if (jobInfo.getEndDateStr().equals("Never")){
            hello="null";
            } else {
            hello="TO_DATE('"+jobInfo.getEndDateStr()+"', 'mm/dd/yyyy')";   
        }

        String sqlstring="INSERT \n" +
                        "INTO SCHEDULER_APP_JOBS_HST \n" +
                        "  ( \n" +
                        " JOBID,JOBNAME,JOBCLASSNAME,DESCRIPTION,STARTDATE,\n" +
                        "  ENDDATE,SECOND,MINUTE,HOUR,DAYOFWEEK,DAYOFMONTH,\n" +
                        "  MONTH,YEAR,FILEPATH,FILENAME,\n" +
                        "  FILEFORMAT,DBNAME,SQLQUERY,METHODTYPE,\n" +
                        "  FILENAMEFIX,EXCELPATH,EXCELNAME,MACRONAME,\n" +
                        "  EXEFILENAME,MYSQLURL,MYSQLUSER,MYSQLPASS,\n" +
                        "  MYSQLQUERY,ORACLEDBNAME,ORACLETABEL,\n" +
                        "  ORADELETETABEL,ORACLECOL,\n" +
                
                        "  FILEFOLDERPATH,\n" +
                        "  FILESTRINGNAME,\n" +
                        "  CSVSPLITBY,\n" +
                        "  MOVEPATCH,\n" +
                
                        "  EMAILSUBJECT,\n" +
                        "  EMAILTO,\n" +
                        "  EMAILCC,\n" +
                        "  EMAILBCC,\n" +
                        "  EMAILATTACH,\n" +
                        "  EMAILMACROFINALFILE,\n" +
                
                        "  FILEFORMATTYPETOORA, \n" +
                        "  USERNAME, \n" +
                        "  jobcheck, \n" + 
                        "  needjobid, \n" + 
                        "  MOD_USER, \n" +
                        "  DATE_STR, \n" +
                        "  MOD, \n" +
                        
                        "  sshuser, \n" +
                        "  sshpassword, \n" +
                        "  sshhost, \n" +
                        "  dbuserName, \n" +
                        "  dbpassword, \n" +
                        "  rhost, \n" +
                        "  rsid, \n" +
                        "  rport, \n" +
                        "  exportquery \n" +
                
                        "  ) \n" +
                        "  VALUES \n" +
                        "  ('"+jobInfo.getJobId()+"',"+
                        "'"+jobInfo.getJobName().replace("'", "''")+"',"+
                        "'"+jobInfo.getJobClassName()+"',"+
                        "'"+jobInfo.getDescription().replace("'", "''")+"',"+
                        "TO_DATE('"+jobInfo.getStartDateStr()+"', 'mm/dd/yyyy'),"+
                        hello+","+
                        "'"+jobInfo.getSecond().replace("'", "''")+"',"+
                        "'"+jobInfo.getMinute().replace("'", "''")+"',"+
                        "'"+jobInfo.getHour().replace("'", "''")+"',"+
                        "'"+jobInfo.getDayOfWeek().replace("'", "''")+"',"+
                        "'"+jobInfo.getDayOfMonth().replace("'", "''")+"',"+
                        "'"+jobInfo.getMonth().replace("'", "''")+"',"+
                        "'"+jobInfo.getYear().replace("'", "''")+"',"+
                        "'"+jobInfo.getFilePath().replace("'", "''")+"',"+
                        "'"+jobInfo.getFilename().replace("'", "''")+"',"+
                        "'"+jobInfo.getFileformat()+"',"+
                        "'"+jobInfo.getDbname().replace("'", "''")+"',"+
                        "'"+jobInfo.getSqlQuery().replace("'", "''")+"',"+
                        "'"+jobInfo.getMethodtype().replace("'", "''")+"',"+
                        "'"+jobInfo.getFilenamefix().replace("'", "''")+"',"+
                        "'"+jobInfo.getExcelpath().replace("'", "''")+"',"+
                        "'"+jobInfo.getExcelname().replace("'", "''")+"',"+
                        "'"+jobInfo.getMacroname().replace("'", "''")+"',"+
                        "'"+jobInfo.getExefilename().replace("'", "''")+"',"+
                        "'"+jobInfo.getMysqlurl().replace("'", "''")+"',"+
                        "'"+jobInfo.getMysqluser().replace("'", "''")+"',"+
                        "'"+jobInfo.getMysqlpass().replace("'", "''")+"',"+
                        "'"+jobInfo.getMysqlquery().replace("'", "''")+"',"+
                        "'"+jobInfo.getOracledbname().replace("'", "''")+"',"+
                        "'"+jobInfo.getOracletabel().replace("'", "''")+"',"+
                        "'"+jobInfo.getOradeletetabel().replace("'", "''")+"',"+
                        "'"+jobInfo.getOraclecol().replace("'", "''")+"',"+
                        "'"+jobInfo.getFilefolderpath().replace("'", "''")+"',"+
                        "'"+jobInfo.getFilestringname().replace("'", "''")+"',"+
                        "'"+jobInfo.getCvsSplitBy().replace("'", "''")+"',"+
                        "'"+jobInfo.getMovePatch().replace("'", "''")+"',"+
                        "'"+jobInfo.getEmailsubject().replace("'", "''")+"',"+
                        "'"+jobInfo.getEmailto().replace("'", "''")+"',"+
                        "'"+jobInfo.getEmailcc().replace("'", "''")+"',"+
                        "'"+jobInfo.getEmailbcc().replace("'", "''")+"',"+
                        "'"+jobInfo.getEmailattach()+"',"+
                        "'"+jobInfo.getEmailmacrofinalfile().replace("'", "''")+"',"+
                        "'"+jobInfo.getFileformattypetoora().replace("'", "''")+"',"+
                        "'"+jobInfo.getUsername().toUpperCase().replace("'", "''")+"',"+
                        "'"+jobInfo.getJobcheck().replace("'", "''")+"',"+
                        "'"+jobInfo.getNeedjobid().replace("'", "''")+"',"+
                        "'"+SessionUtils.getUserName().toString()+"',"+
                        "to_char(sysdate,'YYYYMMDDHH24MISS'),"+
                        "'"+MOD+"',"+
                
                        "'"+jobInfo.getSshuser().replace("'", "''")+"',"+
                        "'"+jobInfo.getSshpassword().replace("'", "''")+"',"+
                        "'"+jobInfo.getSshhost().replace("'", "''")+"',"+
                        "'"+jobInfo.getDbuserName().replace("'", "''")+"',"+
                        "'"+jobInfo.getDbpassword().replace("'", "''")+"',"+
                        "'"+jobInfo.getRhost().replace("'", "''")+"',"+
                        "'"+jobInfo.getRsid().replace("'", "''")+"',"+
                        "'"+jobInfo.getRport().replace("'", "''")+"',"+
                        "'"+jobInfo.getExportquery().replace("'", "''")+
                
                        ")";
                java.sql.Connection conn = null;
                try{
                    System.out.println(sqlstring);
                    JdbcHandler Hand =new JdbcHandler();
                    conn = Hand.getConnection("rep_1");
                    conn.setAutoCommit(false);
                    Statement stmtora = conn.createStatement();
                    ResultSet rsora = stmtora.executeQuery(sqlstring);
                    System.out.println(sqlstring);
                    conn.commit();
                    conn.close();
                } catch (SQLException e){
                    conn.rollback();
                    conn.close();
                    System.out.println(e.getMessage());
                }
        }
 
    }