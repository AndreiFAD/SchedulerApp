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
package com.schedulerapp.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fekete András Demeter 
 */
public class JobInfo implements java.io.Serializable
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public String jobId;
    private String jobName;
    private String jobClassName;
    private String description;
    //Details required by the SchedulerExpression
    private Date startDate;
    private Date endDate;
    private String second;
    private String minute;
    private String hour;
    private String dayOfWeek;
    private String dayOfMonth;
    private String month;
    private String year;
    private String filefolderpath;
    private String filestringname;
    private String cvsSplitBy;
    private String MovePatch;
    private String filePath;
    private String filename; 
    private String fileformat; 
    private String dbname; 
    private String SqlQuery;
    private String methodtype;
    private String filenamefix;
    private String Excelpath;
    private String excelname;
    private String macroname;
    private String exefilename;
    private String mysqlurl;
    private String mysqluser;
    private String mysqlpass;
    private String mysqlquery;
    private String oracledbname;
    private String oracletabel;
    private String oradeletetabel;
    private String oraclecol;
    private String fileformattypetoora;
    
    private String emailsubject;
    private String emailto;
    private String emailcc;
    private String emailbcc;
    private String emailattach;
    private String emailmacrofinalfile;
    
    private String username;
    private String jobcheck;
    private String needjobid;
    
    
    private Date nextTimeout;

    /**
     *
     */
    public JobInfo()
    {
        this("987000", "<Job Name>", "java:module/", "sql");
    }

    /**
     *
     * @param jobId job Id
     * @param jobName job Name
     * @param jobClassName job JavaClass Name
     * @param SqlQuery SqlQuery
     */
    public JobInfo(String jobId, String jobName, String jobClassName, String SqlQuery)
    {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobClassName = jobClassName;
        this.filePath = "-";
        this.filename = "-";
        this.fileformat = "-";
        this.dbname = "-";
        this.SqlQuery = "-";
        this.description = "-";
        this.filenamefix="-";
        this.Excelpath="-";
        this.excelname="-";
        this.macroname="-";
        this.methodtype = "-";
        this.exefilename ="-";
        this.mysqlurl ="-";
        this.mysqluser ="-";
        this.mysqlpass ="-";
        this.mysqlquery ="-";
        this.oracledbname ="-";
        this.oracletabel ="-";
        this.oradeletetabel ="N";
        this.oraclecol = "-";
        this.filefolderpath ="-";
        this.filestringname ="-";
        this.cvsSplitBy ="-";
        this.MovePatch ="-";
        this.fileformattypetoora="-";
        
        this.emailsubject="-";
        this.emailto="-";
        this.emailcc="-";
        this.emailbcc="-";
        this.emailattach="-";
        this.emailmacrofinalfile="-";
        this.username="-";
        this.jobcheck="false";
        this.needjobid="0";
        
        //Default values
        this.startDate = new Date();
        this.endDate = null;
        this.second = "0";
        this.minute = "0";
        this.hour = "0";
        this.dayOfMonth = "*";  //Every Day
        this.month = "*";       //Every Month
        this.year = "*";        //Every Year
        this.dayOfWeek = "*";   //Every Day of Week (Sun-Sat)
    }

    /**
     *
     * @return jobcheck id 
     */
    public String getJobcheck() {
        return jobcheck;
    }

    /**
     *
     * @param jobcheck set jobcheck
     */
    public void setJobcheck(String jobcheck) {
        this.jobcheck = jobcheck;
    }

    /**
     *
     * @return needjobid
     */
    public String getNeedjobid() {
        return needjobid;
    }

    /**
     *
     * @param needjobid set needjobid
     */
    public void setNeedjobid(String needjobid) {
        this.needjobid = needjobid;
    }
    
    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username set username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return emailsubject
     */
    public String getEmailsubject() {
        return emailsubject;
    }

    /**
     *
     * @param emailsubject set emailsubject
     */
    public void setEmailsubject(String emailsubject) {
        this.emailsubject = emailsubject;
    }

    /**
     *
     * @return emailto
     */
    public String getEmailto() {
        return emailto;
    }

    /**
     *
     * @param emailto set emailto
     */
    public void setEmailto(String emailto) {
        this.emailto = emailto;
    }

    /**
     *
     * @return emailcc
     */
    public String getEmailcc() {
        return emailcc;
    }

    /**
     *
     * @param emailcc set emailcc
     */
    public void setEmailcc(String emailcc) {
        this.emailcc = emailcc;
    }

    /**
     *
     * @return emailbcc
     */
    public String getEmailbcc() {
        return emailbcc;
    }

    /**
     *
     * @param emailbcc set emailbcc
     */
    public void setEmailbcc(String emailbcc) {
        this.emailbcc = emailbcc;
    }

    /**
     *
     * @return emailattach
     */
    public String getEmailattach() {
        return emailattach;
    }

    /**
     *
     * @param emailattach set emailattach
     */
    public void setEmailattach(String emailattach) {
        this.emailattach = emailattach;
    }

    /**
     *
     * @return emailmacrofinalfile
     */
    public String getEmailmacrofinalfile() {
        return emailmacrofinalfile;
    }

    /**
     *
     * @param emailmacrofinalfile set emailmacrofinalfile
     */
    public void setEmailmacrofinalfile(String emailmacrofinalfile) {
        this.emailmacrofinalfile = emailmacrofinalfile;
    }

    /**
     *
     * @return fileformattypetoora
     */
    public String getFileformattypetoora() {
        return fileformattypetoora;
    }

    /**
     *
     * @param fileformattypetoora set fileformattypetoora
     */
    public void setFileformattypetoora(String fileformattypetoora) {
        this.fileformattypetoora = fileformattypetoora;
    }

    /**
     *
     * @return MovePatch
     */
    public String getMovePatch() {
        return MovePatch;
    }

    /**
     *
     * @param MovePatch set MovePatch
     */
    public void setMovePatch(String MovePatch) {
        this.MovePatch = MovePatch;
    }
    
    /**
     *
     * @return filefolderpath
     */
    public String getFilefolderpath() {
        return filefolderpath;
    }

    /**
     *
     * @param filefolderpath set filefolderpath
     */
    public void setFilefolderpath(String filefolderpath) {
        this.filefolderpath = filefolderpath;
    }

    /**
     *
     * @return filestringname
     */
    public String getFilestringname() {
        return filestringname;
    }

    /**
     *
     * @param filestringname set filestringname
     */
    public void setFilestringname(String filestringname) {
        this.filestringname = filestringname;
    }

    /**
     *
     * @return csvSplitBy
     */
    public String getCvsSplitBy() {
        return cvsSplitBy;
    }

    /**
     *
     * @param cvsSplitBy set csvSplitBy
     */
    public void setCvsSplitBy(String cvsSplitBy) {
        this.cvsSplitBy = cvsSplitBy;
    }

    /**
     *
     * @return oraclecol
     */
    public String getOraclecol() {
        return oraclecol;
    }

    /**
     *
     * @param oraclecol set oraclecol
     */
    public void setOraclecol(String oraclecol) {
        this.oraclecol = oraclecol;
    }

    /**
     *
     * @return exefilename
     */
    public String getExefilename() {
        return exefilename;
    }

    /**
     *
     * @param exefilename set exefilename
     */
    public void setExefilename(String exefilename) {
        this.exefilename = exefilename;
    }

    /**
     *
     * @return Excelpath
     */
    public String getExcelpath() {
        return Excelpath;
    }

    /**
     *
     * @param Excelpath set Excelpath
     */
    public void setExcelpath(String Excelpath) {
        this.Excelpath = Excelpath;
    }

    /**
     *
     * @return excelname
     */
    public String getExcelname() {
        return excelname;
    }

    /**
     *
     * @param excelname set excelname
     */
    public void setExcelname(String excelname) {
        this.excelname = excelname;
    }

    /**
     *
     * @return macroname
     */
    public String getMacroname() {
        return macroname;
    }

    /**
     *
     * @param macroname set macroname
     */
    public void setMacroname(String macroname) {
        this.macroname = macroname;
    }
    
    /**
     *
     * @return methodtype
     */
    public String getMethodtype() {
        return methodtype;
    }

    /**
     *
     * @param methodtype set methodtype
     */
    public void setMethodtype(String methodtype) {
        this.methodtype = methodtype;
    }

    /**
     *
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     *
     * @param filename set filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     *
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     *
     * @return filenamefix
     */
    public String getFilenamefix() {
        return filenamefix;
    }

    /**
     *
     * @param filenamefix set filenamefix
     */
    public void setFilenamefix(String filenamefix) {
        this.filenamefix = filenamefix;
    }
    
    /**
     *
     * @return mysqlurl
     */
    public String getMysqlurl() {
        return mysqlurl;
    }

    /**
     *
     * @param mysqlurl set mysqlurl
     */
    public void setMysqlurl(String mysqlurl) {
        this.mysqlurl = mysqlurl;
    }

    /**
     *
     * @return mysqluser
     */
    public String getMysqluser() {
        return mysqluser;
    }

    /**
     *
     * @param mysqluser set mysqluser
     */
    public void setMysqluser(String mysqluser) {
        this.mysqluser = mysqluser;
    }

    /**
     *
     * @return mysqlpass
     */
    public String getMysqlpass() {
        return mysqlpass;
    }

    /**
     *
     * @param mysqlpass set mysqlpass
     */
    public void setMysqlpass(String mysqlpass) {
        this.mysqlpass = mysqlpass;
    }

    /**
     *
     * @return mysqlquery
     */
    public String getMysqlquery() {
        return mysqlquery;
    }

    /**
     *
     * @param mysqlquery set mysqlquery
     */
    public void setMysqlquery(String mysqlquery) {
        this.mysqlquery = mysqlquery;
    }

    /**
     *
     * @return oracledbname
     */
    public String getOracledbname() {
        return oracledbname;
    }

    /**
     *
     * @param oracledbname set oracledbname
     */
    public void setOracledbname(String oracledbname) {
        this.oracledbname = oracledbname;
    }

    /**
     *
     * @return oracletabel
     */
    public String getOracletabel() {
        return oracletabel;
    }

    /**
     *
     * @param oracletabel set oracletabel
     */
    public void setOracletabel(String oracletabel) {
        this.oracletabel = oracletabel;
    }

    /**
     *
     * @return oradeletetabel
     */
    public String getOradeletetabel() {
        return oradeletetabel;
    }

    /**
     *
     * @param oradeletetabel set oradeletetabel
     */
    public void setOradeletetabel(String oradeletetabel) {
        this.oradeletetabel = oradeletetabel;
    }
    
    /**
     *
     * @param filePath set filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     *
     * @return fileformat
     */
    public String getFileformat() {
        return fileformat;
    }

    /**
     *
     * @param fileformat set fileformat
     */
    public void setFileformat(String fileformat) {
        this.fileformat = fileformat;
    }

    /**
     *
     * @return dbname
     */
    public String getDbname() {
        return dbname;
    }

    /**
     *
     * @param dbname set dbname
     */
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    /**
     *
     * @return SqlQuery
     */
    public String getSqlQuery() {
        return SqlQuery;
    }

    /**
     *
     * @param SqlQuery set SqlQuery
     */
    public void setSqlQuery(String SqlQuery) {
        this.SqlQuery = SqlQuery;
    }

    /**
     *
     * @return jobId
     */
    public String getJobId()
    {
        return jobId;
    }

    /**
     *
     * @param jobId set jobId
     */
    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    /**
     *
     * @return jobClassName
     */
    public String getJobClassName()
    {
        return jobClassName;
    }

    /**
     *
     * @param jobClassName set jobClassName
     */
    public void setJobClassName(String jobClassName)
    {
        this.jobClassName = jobClassName;
    }

    /**
     *
     * @return jobName
     */
    public String getJobName()
    {
        return jobName;
    }

    /**
     *
     * @param jobName set jobName
     */
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    /**
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *
     * @param description set description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     *
     * @return endDate
     */
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     *
     * @return EndDateStr
     */
    public String getEndDateStr()
    {
        if (endDate != null)
        {
            return sdf.format(endDate);
        }
        return "Never";
    }

    /**
     *
     * @param endDate set EndDate
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    /**
     *
     * @return startDate
     */
    public Date getStartDate()
    {
        return startDate;
    }
    
    /**
     *
     * @return StartDateStr
     */
    public String getStartDateStr()
    {
        return sdf.format(startDate);
    }

    /**
     *
     * @param startDate set startDate
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    /**
     *
     * @return dayOfMonth
     */
    public String getDayOfMonth()
    {
        return dayOfMonth;
    }

    /**
     *
     * @param dayOfMonth set dayOfMonth
     */
    public void setDayOfMonth(String dayOfMonth)
    {
        this.dayOfMonth = dayOfMonth;
    }

    /**
     *
     * @return dayOfWeek
     */
    public String getDayOfWeek()
    {
        return dayOfWeek;
    }

    /**
     *
     * @param dayOfWeek set dayOfWeek
     */
    public void setDayOfWeek(String dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     *
     * @return hour
     */
    public String getHour()
    {
        return hour;
    }

    /**
     *
     * @param hour set hour
     */
    public void setHour(String hour)
    {
        this.hour = hour;
    }

    /**
     *
     * @return minute
     */
    public String getMinute()
    {
        return minute;
    }

    /**
     *
     * @param minute set minute
     */
    public void setMinute(String minute)
    {
        this.minute = minute;
    }

    /**
     *
     * @return month
     */
    public String getMonth()
    {
        return month;
    }

    /**
     *
     * @param month set month
     */
    public void setMonth(String month)
    {
        this.month = month;
    }

    /**
     *
     * @return second
     */
    public String getSecond()
    {
        return second;
    }

    /**
     *
     * @param second set second
     */
    public void setSecond(String second)
    {
        this.second = second;
    }

    /**
     *
     * @return year
     */
    public String getYear()
    {
        return year;
    }

    /**
     *
     * @param year set year
     */
    public void setYear(String year)
    {
        this.year = year;
    }

    /**
     *
     * @return nextTimeout
     */
    public Date getNextTimeout()
    {
        return nextTimeout;
    }

    /**
     *
     * @return nextTimeout str
     */
    public String getNextTimeoutStr()
    {
        return sdf2.format(nextTimeout);
    }

    /**
     *
     * @param nextTimeout set nextTimeout
     */
    public void setNextTimeout(Date nextTimeout)
    {
        this.nextTimeout = nextTimeout;
    }

    /*
     * Expression of the schedule set in the object
     *
     * @return StringExpression
     */

    public String getExpression()
    {
        return "sec=" + second + ";min=" + minute + ";hour=" + hour
                + ";dayOfMonth=" + dayOfMonth + ";month=" + month + ";year=" + year
                + ";dayOfWeek=" + dayOfWeek;
    }

    @Override
    public boolean equals(Object anotherObj)
    {
        if (anotherObj instanceof JobInfo)
        {
            return jobId.equals(((JobInfo) anotherObj).jobId);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return jobId + "-" + jobName + "-" + jobClassName;
    }
}
