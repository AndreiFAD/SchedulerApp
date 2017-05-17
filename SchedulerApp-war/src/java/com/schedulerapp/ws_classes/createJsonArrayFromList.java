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
package com.schedulerapp.ws_classes;

import com.schedulerapp.common.JobInfo;
import java.util.List;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Fekete András Demeter
 */

public class createJsonArrayFromList implements java.io.Serializable
{
    public JSONArray createJsonArrayFromList(List<JobInfo> list, String type) throws JSONException {
            
        JSONArray ja = new JSONArray();
        
        for (int i = 0; i < list.size(); i++) {

            JSONObject jo = new JSONObject();
            jo.put("jobId", list.get(i).getJobId());
            jo.put("jobName", list.get(i).getJobName());
            jo.put("StartDate", list.get(i).getStartDateStr());
            jo.put("EndDate", list.get(i).getEndDateStr());
            jo.put("jobClassName", list.get(i).getJobClassName());
            jo.put("description", list.get(i).getDescription());
            jo.put("second", list.get(i).getSecond());
            jo.put("minute", list.get(i).getMinute());
            jo.put("hour", list.get(i).getHour());
            jo.put("dayOfWeek", list.get(i).getDayOfWeek());
            jo.put("dayOfMonth", list.get(i).getDayOfMonth());
            jo.put("month", list.get(i).getMonth());
            jo.put("year", list.get(i).getYear());
            jo.put("filefolderpath", list.get(i).getFilefolderpath());
            jo.put("filestringname", list.get(i).getFilestringname());
            jo.put("csvSplitBy", list.get(i).getCvsSplitBy());
            jo.put("MovePatch", list.get(i).getMovePatch());
            jo.put("filePath", list.get(i).getFilePath());
            jo.put("filename", list.get(i).getFilename());
            jo.put("fileformat", list.get(i).getFileformat());
            jo.put("dbname", list.get(i).getDbname());
            jo.put("SqlQuery", list.get(i).getSqlQuery());
            jo.put("methodtype", list.get(i).getMethodtype());
            jo.put("filenamefix", list.get(i).getFilenamefix());
            jo.put("Excelpath", list.get(i).getExcelpath());
            jo.put("excelname", list.get(i).getExcelname());
            jo.put("macroname", list.get(i).getMacroname());
            jo.put("exefilename", list.get(i).getExefilename());
            jo.put("mysqlurl", list.get(i).getMysqlurl());
            jo.put("mysqluser", list.get(i).getMysqluser());
            jo.put("mysqlpass", list.get(i).getMysqlpass());
            jo.put("mysqlquery", list.get(i).getMysqlquery());
            jo.put("oracledbname", list.get(i).getOracledbname());
            jo.put("oracletabel", list.get(i).getOracletabel());
            jo.put("oradeletetabel", list.get(i).getOradeletetabel());
            jo.put("oraclecol", list.get(i).getOraclecol());
            jo.put("fileformattypetoora", list.get(i).getFileformattypetoora());
            jo.put("emailsubject", list.get(i).getEmailsubject());
            jo.put("emailto", list.get(i).getEmailto());
            jo.put("emailcc", list.get(i).getEmailcc());
            jo.put("emailbcc", list.get(i).getEmailbcc());
            jo.put("emailattach", list.get(i).getEmailattach());
            jo.put("emailmacrofinalfile", list.get(i).getEmailmacrofinalfile());
            jo.put("username", list.get(i).getUsername());
            jo.put("jobcheck", list.get(i).getJobcheck());
            jo.put("needjobid", list.get(i).getNeedjobid());
            if (type == "A"){
                jo.put("nextTimeout", list.get(i).getNextTimeoutStr());
            } else {
                jo.put("nextTimeout", "-");
            }
            
            
            ja.put(jo);
         
        }
            
        //JSONArray jsArray = new JSONArray(list);
        return ja;
    }
    
    public JSONArray createJsonArrayFromDetail(JobInfo jobInfo, String type) throws JSONException {
            
        JSONArray ja = new JSONArray();
        
            JSONObject jo = new JSONObject();
            jo.put("jobId", jobInfo.getJobId());
            jo.put("jobName", jobInfo.getJobName());
            jo.put("jobClassName", jobInfo.getJobClassName());
            jo.put("StartDate", jobInfo.getStartDateStr());
            jo.put("EndDate", jobInfo.getEndDateStr());
            jo.put("description", jobInfo.getDescription());
            jo.put("second", jobInfo.getSecond());
            jo.put("minute", jobInfo.getMinute());
            jo.put("hour", jobInfo.getHour());
            jo.put("dayOfWeek", jobInfo.getDayOfWeek());
            jo.put("dayOfMonth", jobInfo.getDayOfMonth());
            jo.put("month", jobInfo.getMonth());
            jo.put("year", jobInfo.getYear());
            jo.put("filefolderpath", jobInfo.getFilefolderpath());
            jo.put("filestringname", jobInfo.getFilestringname());
            jo.put("csvSplitBy", jobInfo.getCvsSplitBy());
            jo.put("MovePatch", jobInfo.getMovePatch());
            jo.put("filePath", jobInfo.getFilePath());
            jo.put("filename", jobInfo.getFilename());
            jo.put("fileformat", jobInfo.getFileformat());
            jo.put("dbname", jobInfo.getDbname());
            jo.put("SqlQuery", jobInfo.getSqlQuery());
            jo.put("methodtype", jobInfo.getMethodtype());
            jo.put("filenamefix", jobInfo.getFilenamefix());
            jo.put("Excelpath", jobInfo.getExcelpath());
            jo.put("excelname", jobInfo.getExcelname());
            jo.put("macroname", jobInfo.getMacroname());
            jo.put("exefilename", jobInfo.getExefilename());
            jo.put("mysqlurl", jobInfo.getMysqlurl());
            jo.put("mysqluser", jobInfo.getMysqluser());
            jo.put("mysqlpass", jobInfo.getMysqlpass());
            jo.put("mysqlquery", jobInfo.getMysqlquery());
            jo.put("oracledbname", jobInfo.getOracledbname());
            jo.put("oracletabel", jobInfo.getOracletabel());
            jo.put("oradeletetabel", jobInfo.getOradeletetabel());
            jo.put("oraclecol", jobInfo.getOraclecol());
            jo.put("fileformattypetoora", jobInfo.getFileformattypetoora());
            jo.put("emailsubject", jobInfo.getEmailsubject());
            jo.put("emailto", jobInfo.getEmailto());
            jo.put("emailcc", jobInfo.getEmailcc());
            jo.put("emailbcc", jobInfo.getEmailbcc());
            jo.put("emailattach", jobInfo.getEmailattach());
            jo.put("emailmacrofinalfile", jobInfo.getEmailmacrofinalfile());
            jo.put("username", jobInfo.getUsername());
            jo.put("jobcheck", jobInfo.getJobcheck());
            jo.put("needjobid", jobInfo.getNeedjobid());
            if (type.equals("A")) {
               jo.put("nextTimeout", jobInfo.getNextTimeoutStr());
            } else {
               jo.put("nextTimeout", "-");
            }
            
            
            ja.put(jo);
 
        return ja;
    }
    
    
    
}

