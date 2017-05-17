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

public class JobHst implements java.io.Serializable
{
   
    public String jobId;
    public String user;
    public String moddate;
    public String modtype;
    //Details required by the SchedulerExpression

    /**
     *
     */
    public JobHst()
    {
        this("-","-","-","-");
    }
    
    /**
     *
     * @param jobId jobId
     * @param user username
     * @param moddate Modifications date
     * @param modtype Modifications type 
     */
    public JobHst(String jobId,String user,String moddate,String modtype)
    {
        this.jobId = "-";
        this.user = "-";
        this.moddate = "-";
        this.modtype = "-";
        
    }

    /**
     *
     * @return jobId jobId
     */
    public String getJobId() {
        return jobId;
    }

    /**
     *
     * @param jobId jobId
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     *
     * @return username username
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user username
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     *
     * @return Modifications date
     */
    public String getModdate() {
        return moddate;
    }

    /**
     *
     * @param moddate Modifications date
     */
    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    /**
     *
     * @return Modifications type
     */
    public String getModtype() {
        return modtype;
    }

    /**
     *
     * @param modtype Modifications type
     */
    public void setModtype(String modtype) {
        this.modtype = modtype;
    }

  
}
