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
package com.schedulerapp.batchjob;

import com.schedulerapp.FixClasses.BatchJobInterface;
import com.schedulerapp.FixClasses.Loggtodb;
import com.schedulerapp.FixClasses.Lastlogtime;
import com.schedulerapp.ProcessClasses.SQLRunToExport;
import com.schedulerapp.common.JobInfo;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.Timer;

/**
 *
 * @author Fekete András Demeter 
 */
@Stateless
public class BatchJobC implements BatchJobInterface
{
    static final Logger logger = Logger.getLogger("BatchJobC");
    
    /**
     *
     * @param timer jobInfo parameters
     */
    @Asynchronous
    @Override
    public void executeJob(Timer timer)
    {
     
        JobInfo jobInfo = (JobInfo) timer.getInfo();
        jobrunn(jobInfo);
     
    }
    
    /**
     *
     * @param jobInfo SQLRunToExport job parameters
     */
    @Override
    public void jobrunn(JobInfo jobInfo){ 
        

                final String filePath = jobInfo.getFilePath();
                final String filename = jobInfo.getFilename();
                final String filenamefix = jobInfo.getFilenamefix();
                final String fileformat = jobInfo.getFileformat();
                final String dbname = jobInfo.getDbname();
                final String SqlQuery = jobInfo.getSqlQuery();
                final String attachment = jobInfo.getEmailattach();
                String jobid = jobInfo.getJobId();
                String jobname = jobInfo.getJobName();
                String needjobcheck = jobInfo.getJobcheck();
                String needjobid = jobInfo.getNeedjobid();
                String subject = jobInfo.getEmailsubject();
                String To = jobInfo.getEmailto();
                String cc = jobInfo.getEmailcc();
                String bcc = jobInfo.getEmailbcc();
        
                
        
                logger.log(Level.INFO, "Start of BatchJobC "+jobid+" "+jobname+" at {0}...", new Date());

                if (needjobid=="0") { needjobid="19999999999991"; }
                String jobid_last_time;
                String needjobid_last_time;
                try {
                    
                    Lastlogtime tz =new Lastlogtime();
                    jobid_last_time=tz.lastlogtime(jobid);
                    needjobid_last_time=tz.lastlogtime(needjobid);
                    
                    if (needjobcheck.equals("true") && (jobid_last_time.equals("still has not run") || jobid_last_time.equals("never ran")) && needjobid_last_time.equals("completed today")){  // ha kell figyelni a függőséget és ő még ma nem futott
                    
 

                        
                        Loggtodb l = new Loggtodb();
                        int vid = 0;
                        
                        try {
                                vid = l.logentry(Integer.parseInt(jobid), jobname, "Start of BatchJobC at " + new Date() + "...");
                            } catch (SQLException ex) {
                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex);
                            }
     
                        
                        try {
                            
                                SQLRunToExport z= new SQLRunToExport();
                                String results = z.SQLRunToExport(filePath, filename, filenamefix, fileformat, dbname, SqlQuery, subject,  jobid,  To,  cc,  bcc, attachment);
                             

                                if (results.equals("success")){
                                    
                                      System.err.println("Az 'C'-Export batch lefutott "+jobInfo.getJobName()+" - "+jobInfo.getDescription());
                                        logger.log(Level.INFO, "Running job: {0}", jobInfo);

                                        Thread.sleep(60000);

                                        try {
                                                l.logclose(vid, "Az 'C'-Export batch lefutott "+jobInfo.getJobName(), "");
                                            } catch (SQLException ex1) {
                                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex1);
                                            }
                                } else {
                                     Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, results);
                                        try {
                                                l.logclose(vid, "Az 'C'-Export batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+results);
                                            } catch (SQLException ex1) {
                                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex1);
                                            }
                                }
                               
                              
                            } catch (Exception ex) {

                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex);
                                try {
                                        l.logclose(vid, "Az 'C'-Export batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+ex.getMessage());
                                    } catch (SQLException ex1) {
                                        Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                            }
                        logger.log(Level.INFO, "End of BatchJobC "+jobid+" "+jobname+" at {0}", new Date());
                
                        
                        
                } else if (needjobcheck.equals("false")) {   
                        
                        
                        Loggtodb l = new Loggtodb();
                        int vid = 0;
                        
                        try {
                                vid = l.logentry(Integer.parseInt(jobid), jobname, "Start of BatchJobC at " + new Date() + "...");
                            } catch (SQLException ex) {
                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex);
                            }
     
                        
                        try {
                            
                                SQLRunToExport z= new SQLRunToExport();
                                String results = z.SQLRunToExport(filePath, filename, filenamefix, fileformat, dbname, SqlQuery, subject,  jobid,  To,  cc,  bcc, attachment);
                                
                           
                                if (results.equals("success")){
                                    
                                      System.err.println("Az 'C'-Export batch lefutott "+jobInfo.getJobName()+" - "+jobInfo.getDescription());
                                        logger.log(Level.INFO, "Running job: {0}", jobInfo);

                                        Thread.sleep(60000);

                                        try {
                                                l.logclose(vid, "Az 'C'-Export batch lefutott "+jobInfo.getJobName(), "");
                                            } catch (SQLException ex1) {
                                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex1);
                                            }
                                } else {
                                     Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, results);
                                        try {
                                                l.logclose(vid, "Az 'C'-Export batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+results);
                                            } catch (SQLException ex1) {
                                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex1);
                                            }
                                }
                              
                            } catch (Exception ex) {

                                Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex);
                                try {
                                        l.logclose(vid, "Az 'C'-Export batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error "+ex.getMessage());
                                    } catch (SQLException ex1) {
                                        Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                            }
                        logger.log(Level.INFO, "End of BatchJobC "+jobid+" "+jobname+" at {0}", new Date());
                        
                              
                    } else { 
                                Thread.sleep(30000);
                                logger.log(Level.INFO, "End of BatchJobC "+jobid+" "+jobname+" at {0}", new Date());
                                logger.log(Level.INFO, "It does not have the time {0} {1}", new Object[]{jobid, jobname}); }
                } catch (Exception ex) {Logger.getLogger(BatchJobC.class.getName()).log(Level.SEVERE, null, ex);}
                  
    }

}
