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
import com.schedulerapp.FixClasses.lastlogtime;
import com.schedulerapp.FixClasses.logytodb;
import com.schedulerapp.common.JobInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class BatchJobB implements BatchJobInterface
{
    static final Logger logger = Logger.getLogger("BatchJobB");
   
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
     * @param jobInfo EXEfileRun job parameters
     */
    @Override
    public void jobrunn(JobInfo jobInfo){    
        
        
                final String exefiled = jobInfo.getExefilename();
                final String jobname = jobInfo.getJobName();
                final String jobid = jobInfo.getJobId();
                String needjobcheck = jobInfo.getJobcheck();
                String needjobid = jobInfo.getNeedjobid();
                
        
                logger.log(Level.INFO, "Start of BatchJobB "+jobid+" "+jobname+" at {0}...", new Date());


                if (needjobid=="0") { needjobid="19999999999991"; }
                String jobid_last_time;
                String needjobid_last_time;
                try {
                    lastlogtime tz =new lastlogtime();
                    jobid_last_time=tz.lastlogtime(jobid);
                    needjobid_last_time=tz.lastlogtime(needjobid);
                    if (needjobcheck.equals("true") && (jobid_last_time.equals("still has not run") || jobid_last_time.equals("never ran")) && needjobid_last_time.equals("completed today")){  // ha kell figyelni a függőséget és ő még ma nem futott
                    
                        logytodb l = new logytodb();
                        int vid = 0;
                        try {
                                vid = l.logentry(Integer.parseInt(jobid), jobname, "Start of BatchJobB at " + new Date() + "...");
                            } catch (SQLException ex) {
                                Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        
                        BufferedReader stdError = null;
                        String s = null;
                        String se = "-";
                        String ss = "-";
                             
                        try {
                            
                                    Runtime rt = Runtime.getRuntime();
                                    String[] commands = {exefiled};
                                    Process proc;
                                    proc = rt.exec(commands);
                                    BufferedReader stdInput = new BufferedReader(new 
                                    InputStreamReader(proc.getInputStream()));
                                    stdError = new BufferedReader(new 
                                    InputStreamReader(proc.getErrorStream()));

                                    // read the output from the command
                                    System.out.println("Here is the standard output of the command:\n");

                                    while ((se = stdInput.readLine()) != null) {
                                        System.out.println(se);
                                    }
                                    
                                    System.out.println("Here is the standard error of the command (if any):\n");

                                    while ((s = stdError.readLine()) != null) {
                                        System.out.println(s);
                                        ss = ss +" "+ s;
                                    }
                                    System.out.println(ss);
                                    
                                    if (!ss.equals("-")) {
                                        
                                            l.logclose(vid, "Az 'B'-ExeRun batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+ss);
                                            Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ss);
                                            
                                    } else {
                                            
                                            System.err.println("Az 'B'-ExeRun batch lefutott "+jobInfo.getJobName()+" - "+jobInfo.getDescription());
                                            logger.log(Level.INFO, "Running job: {0}", jobInfo);
                                            Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, "run success");
                                            
                                            try {
                                                    l.logclose(vid, "Az 'B'-ExeRun batch lefutott "+jobInfo.getJobName(), "");
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            
                                            Thread.sleep(60000);
                                    }
                                    
                             
                            } catch (IOException ex) {

                                try {
                                        Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                                        l.logclose(vid, "Az 'B'-ExeRun batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+ex.getMessage());
                                       
                                    } catch (SQLException ex1) {
                                        Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                            }  
     
                        
                } else if (needjobcheck.equals("false")) {
                        
                         logytodb l = new logytodb();
                        int vid = 0;
                        try {
                                vid = l.logentry(Integer.parseInt(jobid), jobname, "Start of BatchJobB "+jobid+" "+jobname+" at " + new Date() + "...");
                            } catch (SQLException ex) {
                                Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        
                        BufferedReader stdError = null;
                        String s;
                        String se = "-";
                        String ss = "-";
                             
                        try {
                            
                                    Runtime rt = Runtime.getRuntime();
                                    String[] commands = {exefiled};
                                    Process proc;
                                    proc = rt.exec(commands);
                                    BufferedReader stdInput = new BufferedReader(new 
                                    InputStreamReader(proc.getInputStream()));
                                    stdError = new BufferedReader(new 
                                    InputStreamReader(proc.getErrorStream()));

                                    // read the output from the command
                                    System.out.println("Here is the standard output of the command:\n");

                                    while ((se = stdInput.readLine()) != null) {
                                        System.out.println(se);
                                    }
                                    
                                    System.out.println("Here is the standard error of the command (if any):\n");
                                    
                                    while ((s = stdError.readLine()) != null) {
                                        System.out.println(s);
                                        ss = ss +" "+ s;
                                    }
                                    System.out.println(ss);
                                    
                                    if (!ss.equals("-")) {    
                                        
                                            l.logclose(vid, "Az 'B'-ExeRun batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+ss);
                                            Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ss);
                                            
                                        } else {
                                            
                                            System.err.println("Az 'B'-ExeRun batch lefutott "+jobInfo.getJobName()+" - "+jobInfo.getDescription());
                                            logger.log(Level.INFO, "Running job: {0}", jobInfo);
                                            Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, "run success");
                                            
                                            try {
                                                    l.logclose(vid, "Az 'B'-ExeRun batch lefutott "+jobInfo.getJobName(), "");
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            
                                            Thread.sleep(60000);
                                        }
                                    
                             
                            } catch (IOException ex) {

                                try {
                                        Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                                        l.logclose(vid, "Az 'B'-ExeRun batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error: "+ex.getMessage());
                                       
                                    } catch (SQLException ex1) {
                                        Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                            }
            
                    } else { 
                    
                        Thread.sleep(30000);
                        logger.log(Level.INFO, "End of BatchJobB "+jobid+" "+jobname+" at {0}", new Date());
                        logger.log(Level.INFO, "It does not have the time {0} {1}", new Object[]{jobid, jobname});  }
                    
                } catch (Exception ex) {Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);}
                
                logger.log(Level.INFO, "End of BatchJobB "+jobid+" "+jobname+" at {0}", new Date());
        }

}
