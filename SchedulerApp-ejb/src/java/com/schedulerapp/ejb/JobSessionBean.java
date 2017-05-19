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
package com.schedulerapp.ejb;

import com.schedulerapp.common.JobInfo;
import com.schedulerapp.common.JobHst;
import com.schedulerapp.FixClasses.BatchJobInterface;
import com.schedulerapp.FixClasses.JdbcHandler;
import com.schedulerapp.common.LogMe;
import java.io.StringReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import javax.ejb.DuplicateKeyException;

import javax.ejb.LocalBean;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Fekete András Demeter 
 */
//@Startup
//@Singleton
@Stateless
@LocalBean
public class JobSessionBean
{

    @Resource
    TimerService timerService;  //Resource Injection
    static final Logger logger = Logger.getLogger("JobSessionBean");
    List<JobInfo> jobList2 = new ArrayList<JobInfo>();
    List<JobHst> jobHstList = new ArrayList<JobHst>();
    JobHst newJobHst;
    List<LogMe> jobLogMe = new ArrayList<LogMe>();
    LogMe newLogMe;
    
    
    /**
     *
     * @param id job id
     * @return modification list 
     * @throws Exception Exception
     */
    public List<JobHst> getJobHstList(String id) throws Exception
    {
        
        jobHstList.clear();
        System.out.println(id);
        String sqlstring = "select JOBID, MOD_USER, DATE_STR, MOD from (\n" +
                            "select JOBID, MOD_USER, to_char(to_date(DATE_STR,'YYYYMMDDHH24MISS'),'YYYY.MM.DD HH24:MI:SS') as DATE_STR, MOD from SCHEDULER_APP_JOBS_HST where jobid ='"+id+"' order by DATE_STR desc)\n" +
                            "where rownum < 11\n";
                
        java.sql.Connection conn = null;
        try{ 
                
                JdbcHandler Hand =new JdbcHandler();
                conn = Hand.getConnection("rep_1");
                conn.setAutoCommit(false);
                Statement stmtora = conn.createStatement();
                ResultSet rsora = stmtora.executeQuery(sqlstring);
                while (rsora.next()) {
                    JobHst newitem2 = new JobHst();

                try{
                        
                        newitem2.setJobId(rsora.getString(1));
                        newitem2.setModdate(rsora.getString(3));
                        newitem2.setUser(rsora.getString(2));
                        newitem2.setModtype(rsora.getString(4));
                        
                        jobHstList.add(newitem2);
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
 
        return jobHstList;
    }
    
    /**
     *
     * @return modification job item
     */
    public JobHst getNewJobHst()
    {
        return newJobHst;
    }
    
    /**
     *
     * @param id2 job id
     * @return logme list 
     * @throws Exception Exception
     */
    public List<LogMe> getJobLogMe(String id2) throws Exception
    {
        
        jobLogMe.clear();
        System.out.println(id2);
        String sqlstring = "select   nvl(JOB_START,'-'), nvl(JOB_END,'-'), nvl(to_char(JOB_NO),'-'), nvl(JOB_NAME,'-'), nvl(JOB_STATUS,'-') from (  \n" +
                            "select \n" +
                            "to_char(JOB_START,'YYYY.MM.DD HH24:MI:SS') JOB_START,\n" +
                            "to_char(JOB_END,'YYYY.MM.DD HH24:MI:SS') JOB_END,\n" +
                            "JOB_NO,\n" +
                            "JOB_NAME,\n" +
                            "JOB_STATUS \n" +
                            "from logme \n" +
                            "where JOB_NO = '"+id2+"' \n" +
                            "order by JOB_START desc) \n" +
                            "where rownum < 11\n";
                
        java.sql.Connection conn = null;
        try{ 
                
                JdbcHandler Hand =new JdbcHandler();
                conn = Hand.getConnection("rep_1");
                conn.setAutoCommit(false);
                Statement stmtora = conn.createStatement();
                ResultSet rsora = stmtora.executeQuery(sqlstring);
                while (rsora.next()) {
                    LogMe newitem3 = new LogMe();

                try{
                        
                        newitem3.setJobId(rsora.getString(3));
                        newitem3.setEndDate(rsora.getString(2));
                        newitem3.setStartDate(rsora.getString(1));
                        newitem3.setJobname(rsora.getString(4));
                        newitem3.setJobstatus(rsora.getString(5));
                        
                        
                        jobLogMe.add(newitem3);
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
 
        return jobLogMe;
    }
    
    /**
     *
     * @return new log item
     */
    public LogMe getNewLogMe()
    {
        return newLogMe;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    /***
     * @param timer
    @PostConstruct
    public void initialise()
    {
        System.out.println("###PostConstruct - calling TimerSessionBean...");
    }

    @PreDestroy
    public void cleanup()
    {
        System.out.println("###Predestroy - cleaning up...");
    }
    ***/

    /**
     * Callback method for the timers.
     * @param timer timer list
     */
    @Timeout
    public void timeout(Timer timer)
    {
        System.out.println("###Timer <" + timer.getInfo() + "> timeout at " + new Date());
        try
        {
            JobInfo jobInfo = (JobInfo) timer.getInfo();
            BatchJobInterface batchJob = (BatchJobInterface) InitialContext.doLookup(
                    jobInfo.getJobClassName());
            batchJob.executeJob(timer); //Asynchronous method
        }
        catch (NamingException ex)
        {
            logger.log(Level.SEVERE, null, ex);
        }
        catch (Exception ex1)
        {
            logger.severe("Exception caught: " + ex1);
        }
    }
    
  
    /**
     * run test
     * @param jobInfo job info
     * @return jobInfo
     */
    public JobInfo testjob(JobInfo jobInfo){
        
        BatchJobInterface batchJob;
        try {
            
                batchJob = (BatchJobInterface) InitialContext.doLookup(jobInfo.getJobClassName());
                batchJob.jobrunn(jobInfo); //Asynchronous method

            } catch (NamingException ex) {

                Logger.getLogger(JobSessionBean.class.getName()).log(Level.SEVERE, null, ex);

            }
            
        return jobInfo;
        
    }
    
    public String testjobws(String jobid){
        
        BatchJobInterface batchJob;
        try {
            
                Collection<Timer> timers = timerService.getTimers();
                for (Timer t : timers)
                {
                    JobInfo u = (JobInfo) t.getInfo();
                    if (jobid.equals(u.getJobId()))
                    {

            
                    batchJob = (BatchJobInterface) InitialContext.doLookup(u.getJobClassName());
                    batchJob.jobrunn(u); //Asynchronous method

                
                    }
                }
                
            } catch (NamingException ex) {

                Logger.getLogger(JobSessionBean.class.getName()).log(Level.SEVERE, null, ex);

            }
            

           return "success";
        
    }
    
    
    
    private Timer getTimer(JobInfo jobInfo)
    {
        Collection<Timer> timers = timerService.getTimers();
        for (Timer t : timers)
        {
            if (jobInfo.equals((JobInfo) t.getInfo()))
            {
                
                return t;
            }
        }
        return null;
    }
    
    
    
     public Timer getTimerwsA(String jobid)
    {
        Collection<Timer> timers = timerService.getTimers();
        for (Timer t : timers)
        {
            JobInfo u = (JobInfo) t.getInfo();
            if (jobid.equals(u.getJobId()))
            {
                
                return t;
            }
        }
        return null;
    }
    
   
    
    /**
     *
     * @param jobid
     * @param type
     * @return
     */
    public JobInfo getTimerwsD(String jobid) throws Exception
    {

            Collection<JobInfo> timers = getJobList();
            for (JobInfo t : timers)
            {
                if (jobid.equals(t.getJobId()))
                {
                    return t;
                }
            }
            return null;

    }
    
    /**
     * Returns the Timer object based on the given JobInfo
     */
    private JobInfo getTimer2(JobInfo jobInfo) throws Exception
    {
        Collection<JobInfo> timers = getInactiveJobList();
        for (JobInfo t : timers)
        {
            if (jobInfo.getJobId().equals((t.getJobId())))
            {
                return t;
            }
        }
        return null;
    }
    
    /**
     * Creates a timer based on the information in the JobInfo
     *
     * @param jobInfo jobInfo
     * @param f it's new or import from db 
     * @return jobInfo
     * @throws Exception Exception
     */

    public JobInfo createJob(JobInfo jobInfo, String f)
            throws Exception
    {
        //Check for duplicates
        if (getTimer(jobInfo) != null)
        {
            throw new DuplicateKeyException("Job with the ID already exist!");
        } 
        jobInfo.setUsername(jobInfo.getUsername().toUpperCase());
            if (jobInfo.getMethodtype().equals("OracleExport")) {
                jobInfo.setJobClassName("java:module/BatchJobC");
            } else if (jobInfo.getMethodtype().equals("MySQLimporttoOracle")){
                jobInfo.setJobClassName("java:module/BatchJobD");
            } else if (jobInfo.getMethodtype().equals("exerun")){
                jobInfo.setJobClassName("java:module/BatchJobB");
            } else if (jobInfo.getMethodtype().equals("RunExcelMAcro")){
                jobInfo.setJobClassName("java:module/BatchJobA");
            } else if (jobInfo.getMethodtype().equals("-")){
                jobInfo.setJobClassName("java:module/-");
            } else if (jobInfo.getMethodtype().equals("READFORM")){  
                jobInfo.setJobClassName("java:module/BatchJobE");
            } else if (jobInfo.getMethodtype().equals("CSVtoOracle")){  
                jobInfo.setJobClassName("java:module/BatchJobF");
            } else {}
        // insert db-be

        TimerConfig timerAConf = new TimerConfig(jobInfo, true);

        ScheduleExpression schedExp = new ScheduleExpression();
        schedExp.start(jobInfo.getStartDate());
        schedExp.end(jobInfo.getEndDate());
        schedExp.second(jobInfo.getSecond());
        schedExp.minute(jobInfo.getMinute());
        schedExp.hour(jobInfo.getHour());
        schedExp.dayOfMonth(jobInfo.getDayOfMonth());
        schedExp.month(jobInfo.getMonth());
        schedExp.year(jobInfo.getYear());
        schedExp.dayOfWeek(jobInfo.getDayOfWeek());
        logger.info("### Scheduler expr: " + schedExp.toString());
        Timer newTimer = timerService.createCalendarTimer(schedExp, timerAConf);
        JobInfo ji = (JobInfo)newTimer.getInfo();
        logger.info("New timer created: " + ji);
        ji.setNextTimeout(newTimer.getNextTimeout());
        
        if (f.equals("ff")){
        String hello=null;
        if (jobInfo.getEndDateStr().equals("Never")){
            hello="null";
            } else {
            hello="TO_DATE('"+jobInfo.getEndDateStr()+"', 'mm/dd/yyyy')";   
        }

        String sqlstring="INSERT \n" +
                        "INTO SCHEDULER_APP_JOBS \n" +
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
                        "  needjobid \n" + 
                
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
                        "'"+jobInfo.getFileformat().replace("'", "''")+"',"+
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
                        "'"+jobInfo.getNeedjobid().replace("'", "''")+
                
                        "')";
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
        f = null;
        return ji;
    }

    /**
     * Returns a list of JobInfo for the active timers
     *
     * @return list of active JobInfo
     * @throws Exception Exception
     */

    public List<JobInfo> getJobList() throws Exception
    {
        logger.info("getJobList() called!!!");
            List<JobInfo> jobList = new ArrayList<JobInfo>();
            
            Collection<Timer> timers = timerService.getTimers();
            
            for (Timer t : timers)
            {
            
                JobInfo jobInfo = (JobInfo) t.getInfo();
                jobInfo.setNextTimeout(t.getNextTimeout());
                
                jobList.add(jobInfo);
            }

        return jobList;
    }
    //getInactiveJobList
    
    /**
     * Returns a list of JobInfo for the inactive timers
     *
     * @return list of inactive JobInfo
     * @throws Exception Exception
     */

    public List<JobInfo> getInactiveJobList() throws Exception
    {
        logger.info("getInactiveJobList() called!!!");
            
               String sqlstring = "select \n" +
                            "nvl(JOBID,'-'), nvl(JOBNAME,'-'), nvl(JOBCLASSNAME,'-'), nvl(DESCRIPTION,'-'),STARTDATE, \n" +
                            "ENDDATE, nvl(SECOND,'-'), nvl(MINUTE,'-'), nvl(HOUR,'-'), nvl(DAYOFWEEK,'-'), \n" +
                            "nvl(DAYOFMONTH,'-'), nvl(MONTH,'-'), nvl(YEAR,'-'), nvl(FILEPATH,'-'), nvl(FILENAME,'-'), \n" +
                            "nvl(FILEFORMAT,'-'), nvl(DBNAME,'-'), nvl(SQLQUERY,'-'), nvl(METHODTYPE,''), nvl(FILENAMEFIX,'-'), \n" +
                            "nvl(EXCELPATH,'-'), nvl(EXCELNAME,'-'), nvl(MACRONAME,'-'), nvl(EXEFILENAME,'-'), nvl(MYSQLURL,'-'), \n" +
                            "nvl(MYSQLUSER,'-'), nvl(MYSQLPASS,'-'), nvl(MYSQLQUERY,'-'), nvl(ORACLEDBNAME,''), nvl(ORACLETABEL,'-'), \n" +
                            "nvl(ORADELETETABEL,''), nvl(ORACLECOL,'-'), \n" +
           
                            "  nvl(EMAILSUBJECT,'-'), \n" +
                            "  nvl(EMAILTO,''), \n" +
                            "  nvl(EMAILCC,''), \n" +
                            "  nvl(EMAILBCC,''), \n" +
                            "  nvl(EMAILATTACH,'-'), \n" +
                            "  nvl(EMAILMACROFINALFILE,'-'), \n" +
                            "  nvl(USERNAME,'-'), \n" +  
                            "  nvl(jobcheck,'-'), \n" + 
                            "  nvl(needjobid,'-'), \n" + 
                
                            "nvl(FILEFOLDERPATH,''), \n" + 
                            "nvl(FILESTRINGNAME,''), \n" + 
                            "nvl(CSVSPLITBY,''), \n" + 
                            "nvl(MOVEPATCH,''), \n" + 
                       
                            "nvl(FILEFORMATTYPETOORA,'') \n" + 
                       
                            " from SCHEDULER_APP_JOBS where ENDDATE < sysdate";
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
                    JobInfo newitem = new JobInfo();
                    
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
                        
                        if (!jobList2.contains(newitem)) {
                            jobList2.add(newitem);
                        }
                        
                       
                      } catch (Exception e){
                          System.out.println(e);
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

        return jobList2;
    }
    
    /**
     * Returns the updated JobInfo from inactive job list
     *
     * @param jobInfo jobInfo
     * @return JobInfo
     */

    public JobInfo getJobInfo2(JobInfo jobInfo) throws Exception
    {
        
        JobInfo j = getTimer2(jobInfo);
        return j;

    }
    
    /**
     * Returns the updated JobInfo from the timer
     *
     * @param jobInfo JobInfo
     * @return JobInfo
     */
    public JobInfo getJobInfo(JobInfo jobInfo)
    {
        Timer t = getTimer(jobInfo);
        if (t != null)
        {
            JobInfo j = (JobInfo) t.getInfo();
            j.setNextTimeout(t.getNextTimeout());
            return j;
        }
        return null;
    }



    /**
     * Updates a timer with the given JobInfo
     *
     * @param jobInfo JobInfo
     * @return JobInfo
     * @throws Exception Exception
     */

    public JobInfo updateJob(JobInfo jobInfo)
            throws Exception
    {
        Timer t = getTimer(jobInfo);
        if (t != null)
        {
            logger.info("Removing timer: " + t.getInfo());
            t.cancel();
            logger.info("Updating job with expr: " + jobInfo.getExpression());

        // update  db-be            
        String hello=null;
        if (jobInfo.getEndDateStr().equals("Never")){
            hello="null";
            } else {
            hello="TO_DATE('"+jobInfo.getEndDateStr()+"', 'mm/dd/yyyy')";   
        }
        
        String sqlstring="UPDATE SCHEDULER_APP_JOBS SET \n" +
                         "DESCRIPTION    = '"+jobInfo.getDescription().replace("'", "''")+"',\n" +
                         "JOBNAME        = '"+jobInfo.getJobName().replace("'", "''")+"',\n" +
                         "STARTDATE      = TO_DATE('"+jobInfo.getStartDateStr()+"', 'mm/dd/yyyy'), \n" +
                         "ENDDATE        = "+hello+",\n" +
                         "SECOND         = '"+jobInfo.getSecond().replace("'", "''")+"',\n" +
                         "MINUTE         = '"+jobInfo.getMinute().replace("'", "''")+"',\n" +
                         "HOUR           = '"+jobInfo.getHour().replace("'", "''")+"',\n" +
                         "DAYOFWEEK      = '"+jobInfo.getDayOfWeek().replace("'", "''")+"',\n" +
                         "DAYOFMONTH     = '"+jobInfo.getDayOfMonth().replace("'", "''")+"',\n" +
                         "MONTH          = '"+jobInfo.getMonth().replace("'", "''")+"',\n" +
                         "YEAR           = '"+jobInfo.getYear().replace("'", "''")+"',\n" +
                         "FILEPATH       = '"+jobInfo.getFilePath().replace("'", "''")+"',\n" +
                         "FILENAME       = '"+jobInfo.getFilename().replace("'", "''")+"',\n" +
                         "FILEFORMAT     = '"+jobInfo.getFileformat()+"',\n" +
                         "DBNAME         = '"+jobInfo.getDbname()+"',\n" +
                         "SQLQUERY       = '"+jobInfo.getSqlQuery().replace("'", "''")+"',\n" +
                         "FILENAMEFIX    = '"+jobInfo.getFilenamefix().replace("'", "''")+"',\n" +
                         "EXCELPATH      = '"+jobInfo.getExcelpath().replace("'", "''")+"',\n" +
                         "EXCELNAME      = '"+jobInfo.getExcelname().replace("'", "''")+"',\n" +
                         "MACRONAME      = '"+jobInfo.getMacroname().replace("'", "''")+"',\n" +
                         "EXEFILENAME    = '"+jobInfo.getExefilename().replace("'", "''")+"',\n" +
                         "MYSQLURL       = '"+jobInfo.getMysqlurl().replace("'", "''")+"',\n" +
                         "MYSQLUSER      = '"+jobInfo.getMysqluser().replace("'", "''")+"',\n" +
                         "MYSQLPASS      = '"+jobInfo.getMysqlpass().replace("'", "''")+"',\n" +
                         "MYSQLQUERY     = '"+jobInfo.getMysqlquery().replace("'", "''")+"',\n" +
                         "ORACLEDBNAME   = '"+jobInfo.getOracledbname()+"',\n" +
                         "ORACLETABEL    = '"+jobInfo.getOracletabel().replace("'", "''")+"',\n" +
                         "ORADELETETABEL = '"+jobInfo.getOradeletetabel()+"',\n" +
                         "ORACLECOL      = '"+jobInfo.getOraclecol().replace("'", "''")+"',\n" +
                         
                         "FILEFOLDERPATH = '"+jobInfo.getFilefolderpath().replace("'", "''")+"',\n" +
                         "FILESTRINGNAME = '"+jobInfo.getFilestringname().replace("'", "''")+"',\n" +
                         "CSVSPLITBY     = '"+jobInfo.getCvsSplitBy().replace("'", "''")+"',\n" +
                         "MOVEPATCH      = '"+jobInfo.getMovePatch().replace("'", "''")+"',\n" +
                         
                         "EMAILSUBJECT          = '"+jobInfo.getEmailsubject().replace("'", "''")+"',\n" +
                         "EMAILTO               = '"+jobInfo.getEmailto().replace("'", "''")+"',\n" +
                         "EMAILCC               = '"+jobInfo.getEmailcc().replace("'", "''")+"',\n" +
                         "EMAILBCC              = '"+jobInfo.getEmailbcc().replace("'", "''")+"',\n" +
                         "EMAILATTACH           = '"+jobInfo.getEmailattach()+"',\n" +
                         "EMAILMACROFINALFILE   = '"+jobInfo.getEmailmacrofinalfile().replace("'", "''")+"',\n" +
                
                         "FILEFORMATTYPETOORA= '"+jobInfo.getFileformattypetoora()+"',\n" +
                         "USERNAME= '"+jobInfo.getUsername().toUpperCase()+"',\n" +
                         "jobcheck= '"+jobInfo.getJobcheck()+"',\n" +
                         "needjobid= '"+jobInfo.getNeedjobid()+"'\n" +
                         "WHERE JOBID     = '"+jobInfo.getJobId()+"'";
                
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

            return createJob(jobInfo,"n");
        }
        return null;
    }

    /**
     * Updates a timer with the given JobInfo
     *
     * @param jobInfo jobInfo
     * @return jobInfo
     * @throws Exception Exception
     */

    public JobInfo updateInactiveJob(JobInfo jobInfo)
            throws Exception
    {

        // update  db-be            
        String hello=null;
        if (jobInfo.getEndDateStr().equals("Never")){
            hello="null";
            } else {
            hello="TO_DATE('"+jobInfo.getEndDateStr()+"', 'mm/dd/yyyy')";   

        }
        String sqlstring="UPDATE SCHEDULER_APP_JOBS SET \n" +
                         "DESCRIPTION    = '"+jobInfo.getDescription().replace("'", "''")+"',\n" +
                         "JOBNAME        = '"+jobInfo.getJobName().replace("'", "''")+"',\n" +
                         "STARTDATE      = TO_DATE('"+jobInfo.getStartDateStr()+"', 'mm/dd/yyyy'), \n" +
                         "ENDDATE        = "+hello+",\n" +
                         "SECOND         = '"+jobInfo.getSecond().replace("'", "''")+"',\n" +
                         "MINUTE         = '"+jobInfo.getMinute().replace("'", "''")+"',\n" +
                         "HOUR           = '"+jobInfo.getHour().replace("'", "''")+"',\n" +
                         "DAYOFWEEK      = '"+jobInfo.getDayOfWeek().replace("'", "''")+"',\n" +
                         "DAYOFMONTH     = '"+jobInfo.getDayOfMonth().replace("'", "''")+"',\n" +
                         "MONTH          = '"+jobInfo.getMonth().replace("'", "''")+"',\n" +
                         "YEAR           = '"+jobInfo.getYear().replace("'", "''")+"',\n" +
                         "FILEPATH       = '"+jobInfo.getFilePath().replace("'", "''")+"',\n" +
                         "FILENAME       = '"+jobInfo.getFilename().replace("'", "''")+"',\n" +
                         "FILEFORMAT     = '"+jobInfo.getFileformat()+"',\n" +
                         "DBNAME         = '"+jobInfo.getDbname()+"',\n" +
                         "SQLQUERY       = '"+jobInfo.getSqlQuery().replace("'", "''")+"',\n" +
                         "FILENAMEFIX    = '"+jobInfo.getFilenamefix().replace("'", "''")+"',\n" +
                         "EXCELPATH      = '"+jobInfo.getExcelpath().replace("'", "''")+"',\n" +
                         "EXCELNAME      = '"+jobInfo.getExcelname().replace("'", "''")+"',\n" +
                         "MACRONAME      = '"+jobInfo.getMacroname().replace("'", "''")+"',\n" +
                         "EXEFILENAME    = '"+jobInfo.getExefilename().replace("'", "''")+"',\n" +
                         "MYSQLURL       = '"+jobInfo.getMysqlurl().replace("'", "''")+"',\n" +
                         "MYSQLUSER      = '"+jobInfo.getMysqluser().replace("'", "''")+"',\n" +
                         "MYSQLPASS      = '"+jobInfo.getMysqlpass().replace("'", "''")+"',\n" +
                         "MYSQLQUERY     = '"+jobInfo.getMysqlquery().replace("'", "''")+"',\n" +
                         "ORACLEDBNAME   = '"+jobInfo.getOracledbname()+"',\n" +
                         "ORACLETABEL    = '"+jobInfo.getOracletabel().replace("'", "''")+"',\n" +
                         "ORADELETETABEL = '"+jobInfo.getOradeletetabel()+"',\n" +
                         "ORACLECOL      = '"+jobInfo.getOraclecol().replace("'", "''")+"',\n" +
                         
                         "FILEFOLDERPATH = '"+jobInfo.getFilefolderpath().replace("'", "''")+"',\n" +
                         "FILESTRINGNAME = '"+jobInfo.getFilestringname().replace("'", "''")+"',\n" +
                         "CSVSPLITBY     = '"+jobInfo.getCvsSplitBy().replace("'", "''")+"',\n" +
                         "MOVEPATCH      = '"+jobInfo.getMovePatch().replace("'", "''")+"',\n" +
                         
                         "EMAILSUBJECT          = '"+jobInfo.getEmailsubject().replace("'", "''")+"',\n" +
                         "EMAILTO               = '"+jobInfo.getEmailto().replace("'", "''")+"',\n" +
                         "EMAILCC               = '"+jobInfo.getEmailcc().replace("'", "''")+"',\n" +
                         "EMAILBCC              = '"+jobInfo.getEmailbcc().replace("'", "''")+"',\n" +
                         "EMAILATTACH           = '"+jobInfo.getEmailattach()+"',\n" +
                         "EMAILMACROFINALFILE   = '"+jobInfo.getEmailmacrofinalfile().replace("'", "''")+"',\n" +
                
                         "FILEFORMATTYPETOORA= '"+jobInfo.getFileformattypetoora()+"',\n" +
                         "USERNAME= '"+jobInfo.getUsername().toUpperCase()+"',\n" +
                         "jobcheck= '"+jobInfo.getJobcheck()+"',\n" +
                         "needjobid= '"+jobInfo.getNeedjobid()+"'\n" +
                         "WHERE JOBID     = '"+jobInfo.getJobId()+"'";
                
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
        
        return jobInfo;
    }
    
    /**
     * Updates a timer with the given JobInfo
     *
     * @param jobitems
     * @return jobInfo
     * @throws Exception Exception
     */

    public String updateInactiveJobFromWS(String jobitems)
            throws Exception
    {
        String result=null;
        JsonObject jobbody = Json.createReader(new StringReader(jobitems)).readObject();

                String sqlstring="UPDATE SCHEDULER_APP_JOBS SET \n" +
                                 "DESCRIPTION    = '"+jobbody.getString("description").replace("'", "''")+"',\n" +
                                 "JOBNAME        = '"+jobbody.getString("jobName").replace("'", "''")+"',\n" +
                                 "STARTDATE      = TO_DATE('"+jobbody.getString("StartDate")+"', 'mm/dd/yyyy'), \n" +
                                 "ENDDATE        = TO_DATE('"+jobbody.getString("EndDate")+"', 'mm/dd/yyyy'),\n" +
                                 "SECOND         = '"+jobbody.getString("second").replace("'", "''")+"',\n" +
                                 "MINUTE         = '"+jobbody.getString("minute").replace("'", "''")+"',\n" +
                                 "HOUR           = '"+jobbody.getString("hour").replace("'", "''")+"',\n" +
                                 "DAYOFWEEK      = '"+jobbody.getString("dayOfWeek").replace("'", "''")+"',\n" +
                                 "DAYOFMONTH     = '"+jobbody.getString("dayOfMonth").replace("'", "''")+"',\n" +
                                 "MONTH          = '"+jobbody.getString("month").replace("'", "''")+"',\n" +
                                 "YEAR           = '"+jobbody.getString("year").replace("'", "''")+"',\n" +
                                 "FILEPATH       = '"+jobbody.getString("filePath").replace("'", "''")+"',\n" +
                                 "FILENAME       = '"+jobbody.getString("filename").replace("'", "''")+"',\n" +
                                 "FILEFORMAT     = '"+jobbody.getString("fileformat")+"',\n" +
                                 "DBNAME         = '"+jobbody.getString("dbname")+"',\n" +
                                 "SQLQUERY       = '"+jobbody.getString("SqlQuery").replace("'", "''")+"',\n" +
                                 "FILENAMEFIX    = '"+jobbody.getString("filenamefix").replace("'", "''")+"',\n" +
                                 "EXCELPATH      = '"+jobbody.getString("Excelpath").replace("'", "''")+"',\n" +
                                 "EXCELNAME      = '"+jobbody.getString("excelname").replace("'", "''")+"',\n" +
                                 "MACRONAME      = '"+jobbody.getString("macroname").replace("'", "''")+"',\n" +
                                 "EXEFILENAME    = '"+jobbody.getString("exefilename").replace("'", "''")+"',\n" +
                                 "MYSQLURL       = '"+jobbody.getString("mysqlurl").replace("'", "''")+"',\n" +
                                 "MYSQLUSER      = '"+jobbody.getString("mysqluser").replace("'", "''")+"',\n" +
                                 "MYSQLPASS      = '"+jobbody.getString("mysqlpass").replace("'", "''")+"',\n" +
                                 "MYSQLQUERY     = '"+jobbody.getString("mysqlquery").replace("'", "''")+"',\n" +
                                 "ORACLEDBNAME   = '"+jobbody.getString("oracledbname")+"',\n" +
                                 "ORACLETABEL    = '"+jobbody.getString("oracletabel").replace("'", "''")+"',\n" +
                                 "ORADELETETABEL = '"+jobbody.getString("oradeletetabel")+"',\n" +
                                 "ORACLECOL      = '"+jobbody.getString("oraclecol").replace("'", "''")+"',\n" +

                                 "FILEFOLDERPATH = '"+jobbody.getString("filefolderpath").replace("'", "''")+"',\n" +
                                 "FILESTRINGNAME = '"+jobbody.getString("filestringname").replace("'", "''")+"',\n" +
                                 "CSVSPLITBY     = '"+jobbody.getString("csvSplitBy").replace("'", "''")+"',\n" +
                                 "MOVEPATCH      = '"+jobbody.getString("MovePatch").replace("'", "''")+"',\n" +

                                 "EMAILSUBJECT          = '"+jobbody.getString("emailsubject").replace("'", "''")+"',\n" +
                                 "EMAILTO               = '"+jobbody.getString("emailto").replace("'", "''")+"',\n" +
                                 "EMAILCC               = '"+jobbody.getString("emailcc").replace("'", "''")+"',\n" +
                                 "EMAILBCC              = '"+jobbody.getString("emailbcc").replace("'", "''")+"',\n" +
                                 "EMAILATTACH           = '"+jobbody.getString("emailattach")+"',\n" +
                                 "EMAILMACROFINALFILE   = '"+jobbody.getString("emailmacrofinalfile").replace("'", "''")+"',\n" +

                                 "FILEFORMATTYPETOORA= '"+jobbody.getString("fileformattypetoora")+"',\n" +
                                 "USERNAME= '"+jobbody.getString("username").toUpperCase()+"',\n" +
                                 "jobcheck= '"+jobbody.getString("jobcheck")+"',\n" +
                                 "needjobid= '"+jobbody.getString("needjobid")+"'\n" +
                                 "WHERE JOBID     = '"+jobbody.getString("jobId")+"'";

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
                            result = "sucess update";
                        } catch (SQLException e){
                            conn.rollback();
                            conn.close();
                            System.out.println(e.getMessage());
                        }
      if (result == "sucess update") {
          return result;
      }  else {
          return "update failed";
      }   
        
    }
    
    
   public String updateJobWs(String jobid, String jobidelement)
            throws Exception
    {
       JsonObject jobidelementjson = Json.createReader(new StringReader(jobidelement)).readObject();
       Collection<Timer> timers = timerService.getTimers();
        for (Timer x : timers)
        {
            JobInfo u = (JobInfo) x.getInfo();
            if (jobid.equals(u.getJobId()))
            {
                
                    Timer t = getTimer(u);
                    if (u != null)
                    {
                        logger.info("Removing timer: " + t.getInfo());
                        t.cancel();
                        logger.info("Updating job with expr: " + u.getExpression());

                       // update  db-be   
                        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        
                        Date datestart = format.parse(jobidelementjson.getString("StartDate"));
                        
                        try {
                            Date dateend = format.parse(jobidelementjson.getString("EndDate"));
                            u.setEndDate(dateend);
                        } catch (Exception e) {
                            
                        }
                        
                        
                        u.setJobName(jobidelementjson.getString("jobName"));
                        u.setJobClassName(jobidelementjson.getString("jobClassName"));
                        u.setDescription(jobidelementjson.getString("description"));
                        u.setStartDate(datestart);
                        
                        u.setSecond(jobidelementjson.getString("second"));
                        u.setMinute(jobidelementjson.getString("minute"));
                        u.setHour(jobidelementjson.getString("hour"));
                        u.setDayOfWeek(jobidelementjson.getString("dayOfWeek"));
                        u.setDayOfMonth(jobidelementjson.getString("dayOfMonth"));
                        u.setMonth(jobidelementjson.getString("month"));
                        u.setYear(jobidelementjson.getString("year"));
                        u.setFilePath(jobidelementjson.getString("filePath"));
                        u.setFilename(jobidelementjson.getString("filename"));
                        u.setFileformat(jobidelementjson.getString("fileformat"));
                        u.setDbname(jobidelementjson.getString("dbname"));
                        u.setSqlQuery(jobidelementjson.getString("SqlQuery"));
                        u.setMethodtype(jobidelementjson.getString("methodtype"));
                        u.setFilenamefix(jobidelementjson.getString("filenamefix"));
                        u.setExcelpath(jobidelementjson.getString("Excelpath"));
                        u.setExcelname(jobidelementjson.getString("excelname"));
                        u.setMacroname(jobidelementjson.getString("macroname"));
                        u.setExefilename(jobidelementjson.getString("exefilename"));
                        u.setMysqlurl(jobidelementjson.getString("mysqlurl"));
                        u.setMysqluser(jobidelementjson.getString("mysqluser"));
                        u.setMysqlpass(jobidelementjson.getString("mysqlpass"));
                        u.setMysqlquery(jobidelementjson.getString("mysqlquery"));
                        u.setOracledbname(jobidelementjson.getString("oracledbname"));
                        u.setOracletabel(jobidelementjson.getString("oracletabel"));
                        u.setOradeletetabel(jobidelementjson.getString("oradeletetabel"));
                        u.setOraclecol(jobidelementjson.getString("oraclecol"));
                        
                        u.setEmailsubject(jobidelementjson.getString("emailsubject"));
                        u.setEmailto(jobidelementjson.getString("emailto"));
                        u.setEmailcc(jobidelementjson.getString("emailcc"));
                        u.setEmailbcc(jobidelementjson.getString("emailbcc"));
                        u.setEmailattach(jobidelementjson.getString("emailattach"));
                        u.setEmailmacrofinalfile(jobidelementjson.getString("emailmacrofinalfile"));
                        u.setUsername(jobidelementjson.getString("username").toUpperCase());
                        
                        u.setJobcheck(jobidelementjson.getString("jobcheck"));
                        u.setNeedjobid(jobidelementjson.getString("needjobid"));
                        
                        u.setFilefolderpath(jobidelementjson.getString("filefolderpath"));
                        u.setFilestringname(jobidelementjson.getString("filestringname"));
                        u.setCvsSplitBy(jobidelementjson.getString("csvSplitBy"));
                        u.setMovePatch(jobidelementjson.getString("MovePatch"));
                        u.setFileformattypetoora(jobidelementjson.getString("fileformattypetoora"));
                    
                    String hello=null;
                    if (u.getEndDateStr().equals("Never")){
                        hello="null";
                        } else {
                        hello="TO_DATE('"+u.getEndDateStr()+"', 'mm/dd/yyyy')";   
                    }
                     
                    String sqlstring="UPDATE SCHEDULER_APP_JOBS SET \n" +
                                     "DESCRIPTION    = '"+u.getDescription().replace("'", "''")+"',\n" +
                                     "JOBNAME        = '"+u.getJobName().replace("'", "''")+"',\n" +
                                     "STARTDATE      = TO_DATE('"+u.getStartDateStr()+"', 'mm/dd/yyyy'), \n" +
                                     "ENDDATE        = "+hello+",\n" +
                                     "SECOND         = '"+u.getSecond().replace("'", "''")+"',\n" +
                                     "MINUTE         = '"+u.getMinute().replace("'", "''")+"',\n" +
                                     "HOUR           = '"+u.getHour().replace("'", "''")+"',\n" +
                                     "DAYOFWEEK      = '"+u.getDayOfWeek().replace("'", "''")+"',\n" +
                                     "DAYOFMONTH     = '"+u.getDayOfMonth().replace("'", "''")+"',\n" +
                                     "MONTH          = '"+u.getMonth().replace("'", "''")+"',\n" +
                                     "YEAR           = '"+u.getYear().replace("'", "''")+"',\n" +
                                     "FILEPATH       = '"+u.getFilePath().replace("'", "''")+"',\n" +
                                     "FILENAME       = '"+u.getFilename().replace("'", "''")+"',\n" +
                                     "FILEFORMAT     = '"+u.getFileformat()+"',\n" +
                                     "DBNAME         = '"+u.getDbname()+"',\n" +
                                     "SQLQUERY       = '"+u.getSqlQuery().replace("'", "''")+"',\n" +
                                     "FILENAMEFIX    = '"+u.getFilenamefix().replace("'", "''")+"',\n" +
                                     "EXCELPATH      = '"+u.getExcelpath().replace("'", "''")+"',\n" +
                                     "EXCELNAME      = '"+u.getExcelname().replace("'", "''")+"',\n" +
                                     "MACRONAME      = '"+u.getMacroname().replace("'", "''")+"',\n" +
                                     "EXEFILENAME    = '"+u.getExefilename().replace("'", "''")+"',\n" +
                                     "MYSQLURL       = '"+u.getMysqlurl().replace("'", "''")+"',\n" +
                                     "MYSQLUSER      = '"+u.getMysqluser().replace("'", "''")+"',\n" +
                                     "MYSQLPASS      = '"+u.getMysqlpass().replace("'", "''")+"',\n" +
                                     "MYSQLQUERY     = '"+u.getMysqlquery().replace("'", "''")+"',\n" +
                                     "ORACLEDBNAME   = '"+u.getOracledbname()+"',\n" +
                                     "ORACLETABEL    = '"+u.getOracletabel().replace("'", "''")+"',\n" +
                                     "ORADELETETABEL = '"+u.getOradeletetabel()+"',\n" +
                                     "ORACLECOL      = '"+u.getOraclecol().replace("'", "''")+"',\n" +

                                     "FILEFOLDERPATH = '"+u.getFilefolderpath().replace("'", "''")+"',\n" +
                                     "FILESTRINGNAME = '"+u.getFilestringname().replace("'", "''")+"',\n" +
                                     "CSVSPLITBY     = '"+u.getCvsSplitBy().replace("'", "''")+"',\n" +
                                     "MOVEPATCH      = '"+u.getMovePatch().replace("'", "''")+"',\n" +

                                     "EMAILSUBJECT          = '"+u.getEmailsubject().replace("'", "''")+"',\n" +
                                     "EMAILTO               = '"+u.getEmailto().replace("'", "''")+"',\n" +
                                     "EMAILCC               = '"+u.getEmailcc().replace("'", "''")+"',\n" +
                                     "EMAILBCC              = '"+u.getEmailbcc().replace("'", "''")+"',\n" +
                                     "EMAILATTACH           = '"+u.getEmailattach()+"',\n" +
                                     "EMAILMACROFINALFILE   = '"+u.getEmailmacrofinalfile().replace("'", "''")+"',\n" +

                                     "FILEFORMATTYPETOORA= '"+u.getFileformattypetoora()+"',\n" +
                                     "USERNAME= '"+u.getUsername().toUpperCase()+"',\n" +
                                     "jobcheck= '"+u.getJobcheck()+"',\n" +
                                     "needjobid= '"+u.getNeedjobid()+"'\n" +
                                     "WHERE JOBID     = '"+u.getJobId()+"'";

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
                            
                            JobInfo jobInfo = u;
                            hello = null;
                            if (jobInfo.getEndDateStr().equals("Never")){
                                hello="null";
                                } else {
                                hello="TO_DATE('"+jobInfo.getEndDateStr()+"', 'mm/dd/yyyy')";   
                            }

                            String sqlstring2="INSERT \n" +
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
                                            "  MOD \n" +
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
                                            "'"+jobidelementjson.getString("username")+"',"+
                                            "to_char(sysdate,'YYYYMMDDHH24MISS'),"+
                                            "'updateJobWs'"+
                                            ")";
                                    conn = null;
                                    try{
                                        System.out.println(sqlstring);
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection("rep_1");
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora = stmtora.executeQuery(sqlstring2);
                                        System.out.println(sqlstring);
                                        conn.commit();
                                        conn.close();
                                    } catch (SQLException e){
                                        conn.rollback();
                                        conn.close();
                                        System.out.println(e.getMessage());
                                    }
                            }
                            
                            
            
                            
                        createJob(u,"n");
                    }

            }
        return "success";
        }

    
    /**
     * Remove a timer with the given JobInfo
     *
     * @param jobInfo jobInfo
     * @throws SQLException SQLException
     */

    public void inactivateJob(JobInfo jobInfo) throws SQLException
    {
        Timer t = getTimer(jobInfo);
        if (t != null)
        {

            
// inactivate  db-be 
           String sqlstring="UPDATE SCHEDULER_APP_JOBS SET \n" +
                         "DESCRIPTION    = '"+jobInfo.getDescription().replace("'", "''")+"',\n" +
                         "JOBNAME        = '"+jobInfo.getJobName().replace("'", "''")+"',\n" +
                         "STARTDATE      = TO_DATE('"+jobInfo.getStartDateStr()+"', 'mm/dd/yyyy'), \n" +
                         "ENDDATE        = sysdate,\n" +
                         "SECOND         = '"+jobInfo.getSecond().replace("'", "''")+"',\n" +
                         "MINUTE         = '"+jobInfo.getMinute().replace("'", "''")+"',\n" +
                         "HOUR           = '"+jobInfo.getHour().replace("'", "''")+"',\n" +
                         "DAYOFWEEK      = '"+jobInfo.getDayOfWeek().replace("'", "''")+"',\n" +
                         "DAYOFMONTH     = '"+jobInfo.getDayOfMonth().replace("'", "''")+"',\n" +
                         "MONTH          = '"+jobInfo.getMonth().replace("'", "''")+"',\n" +
                         "YEAR           = '"+jobInfo.getYear().replace("'", "''")+"',\n" +
                         "FILEPATH       = '"+jobInfo.getFilePath().replace("'", "''")+"',\n" +
                         "FILENAME       = '"+jobInfo.getFilename().replace("'", "''")+"',\n" +
                         "FILEFORMAT     = '"+jobInfo.getFileformat()+"',\n" +
                         "DBNAME         = '"+jobInfo.getDbname()+"',\n" +
                         "SQLQUERY       = '"+jobInfo.getSqlQuery().replace("'", "''")+"',\n" +
                         "FILENAMEFIX    = '"+jobInfo.getFilenamefix().replace("'", "''")+"',\n" +
                         "EXCELPATH      = '"+jobInfo.getExcelpath().replace("'", "''")+"',\n" +
                         "EXCELNAME      = '"+jobInfo.getExcelname().replace("'", "''")+"',\n" +
                         "MACRONAME      = '"+jobInfo.getMacroname().replace("'", "''")+"',\n" +
                         "EXEFILENAME    = '"+jobInfo.getExefilename().replace("'", "''")+"',\n" +
                         "MYSQLURL       = '"+jobInfo.getMysqlurl().replace("'", "''")+"',\n" +
                         "MYSQLUSER      = '"+jobInfo.getMysqluser().replace("'", "''")+"',\n" +
                         "MYSQLPASS      = '"+jobInfo.getMysqlpass().replace("'", "''")+"',\n" +
                         "MYSQLQUERY     = '"+jobInfo.getMysqlquery().replace("'", "''")+"',\n" +
                         "ORACLEDBNAME   = '"+jobInfo.getOracledbname()+"',\n" +
                         "ORACLETABEL    = '"+jobInfo.getOracletabel().replace("'", "''")+"',\n" +
                         "ORADELETETABEL = '"+jobInfo.getOradeletetabel()+"',\n" +
                         "ORACLECOL      = '"+jobInfo.getOraclecol().replace("'", "''")+"',\n" +
                         
                         "FILEFOLDERPATH = '"+jobInfo.getFilefolderpath().replace("'", "''")+"',\n" +
                         "FILESTRINGNAME = '"+jobInfo.getFilestringname().replace("'", "''")+"',\n" +
                         "CSVSPLITBY     = '"+jobInfo.getCvsSplitBy().replace("'", "''")+"',\n" +
                         "MOVEPATCH      = '"+jobInfo.getMovePatch().replace("'", "''")+"',\n" +
                         
                         "EMAILSUBJECT          = '"+jobInfo.getEmailsubject().replace("'", "''")+"',\n" +
                         "EMAILTO               = '"+jobInfo.getEmailto().replace("'", "''")+"',\n" +
                         "EMAILCC               = '"+jobInfo.getEmailcc().replace("'", "''")+"',\n" +
                         "EMAILBCC              = '"+jobInfo.getEmailbcc().replace("'", "''")+"',\n" +
                         "EMAILATTACH           = '"+jobInfo.getEmailattach()+"',\n" +
                         "EMAILMACROFINALFILE   = '"+jobInfo.getEmailmacrofinalfile().replace("'", "''")+"',\n" +
                
                         "FILEFORMATTYPETOORA= '"+jobInfo.getFileformattypetoora()+"',\n" +
                         "USERNAME= '"+jobInfo.getUsername().toUpperCase()+"',\n" +
                         "jobcheck= '"+jobInfo.getJobcheck()+"',\n" +
                         "needjobid= '"+jobInfo.getNeedjobid()+"'\n" +
                         "WHERE JOBID     = '"+jobInfo.getJobId()+"'";
                
        
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
            
            t.cancel();
        }
    }
    
    
    /**
     * Remove from db with the given JobInfo
     *
     * @param jobInfo jobInfo
     * @throws SQLException SQLException
     */

    public void deleteJob(JobInfo jobInfo) throws SQLException
    {

       jobList2.remove(jobInfo);

        // deleteJob from  db
           String sqlstring="delete SCHEDULER_APP_JOBS  \n" +
                         "WHERE JOBID     = '"+jobInfo.getJobId()+"'";
                
        
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
                    System.out.println(e);
                } 
        
    }
    
    /**
     * Remove from inactive joblist with the given JobInfo
     *
     * @param jobInfo jobInfo
     * @throws SQLException SQLException
     */

     public void removeFromInactiveList(JobInfo jobInfo) throws SQLException
    {
        jobInfo.setEndDate(null);
        String sqlstring="UPDATE SCHEDULER_APP_JOBS SET \n" +
                         "DESCRIPTION    = '"+jobInfo.getDescription().replace("'", "''")+"',\n" +
                         "JOBNAME        = '"+jobInfo.getJobName().replace("'", "''")+"',\n" +
                         "STARTDATE      = TO_DATE('"+jobInfo.getStartDateStr()+"', 'mm/dd/yyyy'), \n" +
                         "ENDDATE        = null,\n" +
                         "SECOND         = '"+jobInfo.getSecond().replace("'", "''")+"',\n" +
                         "MINUTE         = '"+jobInfo.getMinute().replace("'", "''")+"',\n" +
                         "HOUR           = '"+jobInfo.getHour().replace("'", "''")+"',\n" +
                         "DAYOFWEEK      = '"+jobInfo.getDayOfWeek().replace("'", "''")+"',\n" +
                         "DAYOFMONTH     = '"+jobInfo.getDayOfMonth().replace("'", "''")+"',\n" +
                         "MONTH          = '"+jobInfo.getMonth().replace("'", "''")+"',\n" +
                         "YEAR           = '"+jobInfo.getYear().replace("'", "''")+"',\n" +
                         "FILEPATH       = '"+jobInfo.getFilePath().replace("'", "''")+"',\n" +
                         "FILENAME       = '"+jobInfo.getFilename().replace("'", "''")+"',\n" +
                         "FILEFORMAT     = '"+jobInfo.getFileformat()+"',\n" +
                         "DBNAME         = '"+jobInfo.getDbname()+"',\n" +
                         "SQLQUERY       = '"+jobInfo.getSqlQuery().replace("'", "''")+"',\n" +
                         "FILENAMEFIX    = '"+jobInfo.getFilenamefix().replace("'", "''")+"',\n" +
                         "EXCELPATH      = '"+jobInfo.getExcelpath().replace("'", "''")+"',\n" +
                         "EXCELNAME      = '"+jobInfo.getExcelname().replace("'", "''")+"',\n" +
                         "MACRONAME      = '"+jobInfo.getMacroname().replace("'", "''")+"',\n" +
                         "EXEFILENAME    = '"+jobInfo.getExefilename().replace("'", "''")+"',\n" +
                         "MYSQLURL       = '"+jobInfo.getMysqlurl().replace("'", "''")+"',\n" +
                         "MYSQLUSER      = '"+jobInfo.getMysqluser().replace("'", "''")+"',\n" +
                         "MYSQLPASS      = '"+jobInfo.getMysqlpass().replace("'", "''")+"',\n" +
                         "MYSQLQUERY     = '"+jobInfo.getMysqlquery().replace("'", "''")+"',\n" +
                         "ORACLEDBNAME   = '"+jobInfo.getOracledbname()+"',\n" +
                         "ORACLETABEL    = '"+jobInfo.getOracletabel().replace("'", "''")+"',\n" +
                         "ORADELETETABEL = '"+jobInfo.getOradeletetabel()+"',\n" +
                         "ORACLECOL      = '"+jobInfo.getOraclecol().replace("'", "''")+"',\n" +
                         
                         "FILEFOLDERPATH = '"+jobInfo.getFilefolderpath().replace("'", "''")+"',\n" +
                         "FILESTRINGNAME = '"+jobInfo.getFilestringname().replace("'", "''")+"',\n" +
                         "CSVSPLITBY     = '"+jobInfo.getCvsSplitBy().replace("'", "''")+"',\n" +
                         "MOVEPATCH      = '"+jobInfo.getMovePatch().replace("'", "''")+"',\n" +
                         
                         "EMAILSUBJECT          = '"+jobInfo.getEmailsubject().replace("'", "''")+"',\n" +
                         "EMAILTO               = '"+jobInfo.getEmailto().replace("'", "''")+"',\n" +
                         "EMAILCC               = '"+jobInfo.getEmailcc().replace("'", "''")+"',\n" +
                         "EMAILBCC              = '"+jobInfo.getEmailbcc().replace("'", "''")+"',\n" +
                         "EMAILATTACH           = '"+jobInfo.getEmailattach()+"',\n" +
                         "EMAILMACROFINALFILE   = '"+jobInfo.getEmailmacrofinalfile().replace("'", "''")+"',\n" +
                
                         "FILEFORMATTYPETOORA= '"+jobInfo.getFileformattypetoora()+"',\n" +
                         "USERNAME= '"+jobInfo.getUsername().toUpperCase()+"',\n" +
                         "jobcheck= '"+jobInfo.getJobcheck()+"',\n" +
                         "needjobid= '"+jobInfo.getNeedjobid()+"'\n" +
                         "WHERE JOBID     = '"+jobInfo.getJobId()+"'";
                
        
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
       jobList2.remove(jobInfo);

    }

}
