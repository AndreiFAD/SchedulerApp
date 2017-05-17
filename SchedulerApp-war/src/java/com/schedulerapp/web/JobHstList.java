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

import com.schedulerapp.common.JobHst;
import com.schedulerapp.ejb.JobSessionBean;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Fekete András Demeter
 */
@ManagedBean(name = "JobHstList")
@RequestScoped
public class JobHstList implements java.io.Serializable
{
    @EJB
    private JobSessionBean jobSessionBean;
    private List<JobHst> jobHstList = null;

    /** Creates a new instance List of Job modification log */
    public JobHstList()
    {
    }

    /**
     * Creates a new instance List of Job modification log
     */
    @PostConstruct
    public void initialize()
    {
        System.out.println("JobList - initialize() called!");
        try{
            jobHstList = jobSessionBean.getJobHstList(SessionUtils.getJobno());
        } catch (Exception ex) {
            
            Logger.getLogger(JobHstList.class.getName()).log(Level.SEVERE, null, ex);
            
        }
     
    }
    
    /**
     * Returns a list of Job modification log
     *
     * @return  List of Job modification log
     */
    public List<JobHst> getJobs()
    {

        System.out.println("JobList - getJobs() called!");
        return jobHstList;

    }
    
      
    
    

}
