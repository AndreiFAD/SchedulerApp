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
import com.schedulerapp.ejb.JobSessionBean;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Fekete András Demeter
 */

@WebService(serviceName = "JobInfoWebService")
//@XmlRootElement
public class JobInfoWebService {
    
    @EJB
    public JobSessionBean jobSessionBean;
    public List<JobInfo> jobList = null;
    public List<JobInfo> jobList2 = null;
    
    /**
     * This is a sample web service operation
     * @return 
     * @throws org.primefaces.json.JSONException
     */
    @WebMethod(operationName = "Inactive")
    public String Inactive() throws JSONException {
        
        jobList2 = null;
        JSONArray tt = null;
        
        System.out.println("InactiveJobList - initialize() called!");
        try {
            
            jobList2 = jobSessionBean.getInactiveJobList();
            CreateJsonArrayFromList tz = new CreateJsonArrayFromList();
            tt = tz.CreateJsonArrayFromList(jobList2,"I");
        
        } catch (Exception ex) {
            Logger.getLogger(JobInfoWebService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (tt != null) {
            return tt.toString();
        } else {
            return "No jobs found with given criteria";
        }
    }

    /**
     * This is a sample web service operation
     * @return 
     * @throws org.primefaces.json.JSONException
     */
    @WebMethod(operationName = "Active")
    public String Active() throws JSONException {
        
        jobList = null;
        JSONArray tt = null;
        
        System.out.println("ActiveJobList - initialize() called!");
        try {
            
            jobList = jobSessionBean.getJobList();
            CreateJsonArrayFromList tz = new CreateJsonArrayFromList();
            tt = tz.CreateJsonArrayFromList(jobList,"A");
        
        } catch (Exception ex) {
            Logger.getLogger(JobInfoWebService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (tt != null) {
            return tt.toString();
        } else {
            return "No jobs found with given criteria";
        }
        
    }

    /**
     *
     * @param Id
     * @param Type
     * @return
     * @throws org.primefaces.json.JSONException
     */
    @WebMethod(operationName = "Details")
    public String Details(@WebParam(name = "Id") String Id, @WebParam(name = "Type") String Type) throws JSONException {
        
        JSONObject jobItem = null;
        
        
        if (Type.equals("A")) {
            
                jobItem = null;
                jobList = null;
                System.out.println("ActiveJobDetails - initialize() called!");
                try {

                     jobList = jobSessionBean.getJobList();

                } catch (Exception ex) {
                    Logger.getLogger(JobInfoWebService.class.getName()).log(Level.SEVERE, null, ex);
                }

                CreateJsonArrayFromList tz = new CreateJsonArrayFromList();
                JSONArray tt = tz.CreateJsonArrayFromList(jobList,"A");
                
                for (int i = 0; i < tt.length(); i++) {
                    JSONObject job = tt.getJSONObject(i);
                    String id = job.getString("jobId");
                    if (id.equals(Id)){
                        jobItem = job;
                    }
                
                }
 
        } else {
                
                jobItem = null;
                jobList2 = null;
                System.out.println("InactiveJobDetails - initialize() called!");
                try {

                     jobList2 = jobSessionBean.getInactiveJobList();

                } catch (Exception ex) {
                    Logger.getLogger(JobInfoWebService.class.getName()).log(Level.SEVERE, null, ex);
                }

                CreateJsonArrayFromList tzi = new CreateJsonArrayFromList();
                JSONArray tti = tzi.CreateJsonArrayFromList(jobList2,"I");
                
                for (int i = 0; i < tti.length(); i++) {
                       JSONObject job = tti.getJSONObject(i);
                       String id = job.getString("jobId");
                       if (id.equals(Id)){
                           jobItem = job;
                       }

                   }
        }
        
        if (jobItem != null) {
            return jobItem.toString();
        } else {
            return "No job found with given criteria";
        }

    }
    
    
    @WebMethod(operationName = "UpdateActiveJob")
    public String UpdateActiveJob(@WebParam(name = "Id") String Id, @WebParam(name = "Item") String Item) throws JSONException {
        
                
                String jobItemStr = "";
                
                
                        System.out.println("ActiveJobDetailsUpdate - initialize() called!");
                        try {

                             jobItemStr = jobSessionBean.updateJobWs(Id,Item);

                        } catch (Exception ex) {
                            Logger.getLogger(JobInfoWebService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                return jobItemStr;
        
    }
    
    @WebMethod(operationName = "Test_Run")
    public String Test_Run(@WebParam(name = "Id") String Id) throws JSONException {
        
        String message = "";
        
        System.out.println("ActiveJob Test run - initialize() called!");
        try {
            
            message = jobSessionBean.testjobws(Id);
           
        
        } catch (Exception ex) {
            Logger.getLogger(JobInfoWebService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (message != null) {
            return message;
        } else {
            return "No jobs found with given criteria";
        }
    }
    
    
    
}
