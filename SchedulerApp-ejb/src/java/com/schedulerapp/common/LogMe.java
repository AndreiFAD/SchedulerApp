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


/**
 *
 * @author Fekete András Demeter 
 */

public class LogMe implements java.io.Serializable
{
   
    public String jobId;
    public String startDate;
    public String endDate;
    public String jobstatus;
    public String jobname;

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
     * @param jobId job Id
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     *
     * @return start Date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate start Date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return end Date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate end Date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return jobstatus
     */
    public String getJobstatus() {
        return jobstatus;
    }

    /**
     *
     * @param jobstatus job status
     */
    public void setJobstatus(String jobstatus) {
        this.jobstatus = jobstatus;
    }

    /**
     *
     * @return jobname
     */
    public String getJobname() {
        return jobname;
    }

    /**
     *
     * @param jobname job name
     */
    public void setJobname(String jobname) {
        this.jobname = jobname;
    }



    //JOB_STATUS
    //JOB_PROGRESS_PERCENT
    //JOB_INFO
    //JOB_LASTERROR
    //Details required by the SchedulerExpression
    
    
    /**
     *
     */
    public LogMe() {
        this("-","-","-","-","-");
    }
    
    /**
     *
     * @param jobId job Id
     * @param startDate start Date
     * @param endDate end Date
     * @param jobstatus job status
     * @param jobname job name
     */
    public LogMe(String jobId, String startDate, String endDate,String jobstatus,String jobname)
    {
        this.jobId = "-";
        this.startDate = "-";
        this.endDate = "-";
        this.jobstatus = "-";
        this.jobname = "-";

        
    }


  
}
