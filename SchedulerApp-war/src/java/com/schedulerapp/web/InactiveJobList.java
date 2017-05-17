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

import com.schedulerapp.common.JobInfo;
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
@ManagedBean(name = "InactiveJobList")
@RequestScoped
public class InactiveJobList implements java.io.Serializable
{
    @EJB
    private JobSessionBean jobSessionBean;
    private List<JobInfo> jobList = null;

    /** Creates a new instance of Inactive JobList */
    public InactiveJobList()
    {
    }
    
    /**
     * Creates a new instance of Inactive JobList
     */
    @PostConstruct
    public void initialize()
    {
        System.out.println("InactiveJobList - initialize() called!");
        try {
            jobList = null;
            jobList = jobSessionBean.getInactiveJobList();
        } catch (Exception ex) {
            Logger.getLogger(InactiveJobList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns a list of inactive Jobs from DB
     *
     * @return list of inactive Jobs
     */
    public List<JobInfo> getJobs()
    {
        
        System.out.println("InactiveJobList - getJobs() called!");  
        return jobList;
        
    }
}
