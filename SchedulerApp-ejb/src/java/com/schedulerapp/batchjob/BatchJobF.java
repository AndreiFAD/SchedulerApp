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
import com.schedulerapp.ProcessClasses.CsvImportToOracle;
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
public class BatchJobF implements BatchJobInterface
{
    static final Logger logger = Logger.getLogger("BatchJobF");
    
    /**
     *
     * @param timer jobInfo parameters
     * 
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
     * @param jobInfo csvimporttooracle job parameters
     * 
     */
    @Override
    public void jobrunn(JobInfo jobInfo){ 
        
                String jobid = jobInfo.getJobId();
                String jobname = jobInfo.getJobName();
                String needjobcheck = jobInfo.getJobcheck();
                String needjobid = jobInfo.getNeedjobid();
                final String filefolderpath = jobInfo.getFilefolderpath();
                final String filestringname = jobInfo.getFilestringname();
                final String cvsSplitBy = jobInfo.getCvsSplitBy();
                final String oracledbname = jobInfo.getOracledbname();
                final String oracletabel = jobInfo.getOracletabel();
                final String oradeletetabel = jobInfo.getOradeletetabel();
                final String oraclecol = jobInfo.getOraclecol();
                final String MovePatch = jobInfo.getMovePatch();
                final String fileformattypetoora = jobInfo.getFileformattypetoora();
                
                logger.log(Level.INFO, "Start of BatchJobF "+jobid+" "+jobname+" at {0}...", new Date());     
                        
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
                                vid = l.logentry(Integer.parseInt(jobid), jobname, "Start of BatchJobF "+jobid+" "+jobname+" at " + new Date() + "...");
                            } catch (SQLException ex) {
                                Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        try {
                            
                            CsvImportToOracle u= new CsvImportToOracle();
                            String results = u.csvimporttooracle(filefolderpath, filestringname, oracletabel, oraclecol, oracledbname, cvsSplitBy, oradeletetabel, MovePatch,fileformattypetoora);
                            
                            String resultsr = "-";
                            
                            
                                    if (results == "success - file now not found") {
                                        resultsr = "success";

                                    } else if (results=="success") {
                                        resultsr = "success";

                                    } else if (results=="success - exist, i cant rewrite") {
                                        resultsr = "success";

                                    } else if (results=="success - Delete operation is failed.") {
                                        resultsr = "success";

                                    } else {
                                        resultsr = "Failed!";

                                    }
                            
                            if (resultsr.equals("success")){
                                   System.err.println("Az 'F' batch lefutott "+jobInfo.getJobName()+" - "+jobInfo.getDescription());
                                   logger.log(Level.INFO, "Running job: {0}", jobInfo);

                                   try {
                                           l.logclose(vid, "Az 'F' batch lefutott "+jobInfo.getJobName() + " - " + results, "");
                                       } catch (SQLException ex1) {
                                           Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex1);
                                       }
                                   Thread.sleep(60000);
                                   
                            } else {
                                    try {
                                            l.logclose(vid, "Az 'F' batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error "+resultsr);
                                        } catch (SQLException ex1) {
                                            Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex1);
                                        }
                                    Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, results);  
                            }
                            
                        } catch (Exception ex) {
                            try {
                                    l.logclose(vid, "Az 'F' batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error "+ex.getMessage());
                                } catch (SQLException ex1) {
                                    Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex1);
                                }
                            Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        logger.log(Level.INFO, "End of BatchJobF at {0}", new Date());
                
                } else if (needjobcheck.equals("false")) {    
                        
                        Loggtodb l = new Loggtodb();
                        int vid = 0;
                        
                        try {
                                vid = l.logentry(Integer.parseInt(jobid), jobname, "Start of BatchJobF "+jobid+" "+jobname+" at " + new Date() + "...");
                            } catch (SQLException ex) {
                                Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        try {
                            
                            CsvImportToOracle u= new CsvImportToOracle();
                            String results = u.csvimporttooracle(filefolderpath, filestringname, oracletabel, oraclecol, oracledbname, cvsSplitBy, oradeletetabel, MovePatch,fileformattypetoora);
                            
                            
                            String resultsr = "-";
                            
                                    if (results == "success - file now not found") {
                                        resultsr = "success";

                                    } else if (results=="success") {
                                        resultsr = "success";

                                    } else if (results=="success - exist, i cant rewrite") {
                                        resultsr = "success";

                                    } else if (results=="success - Delete operation is failed.") {
                                        resultsr = "success";

                                    } else {
                                        resultsr = "Failed!";

                                    }
                            
                            if (resultsr.equals("success")){
                                   System.err.println("Az 'F' batch lefutott "+jobInfo.getJobName()+" - "+jobInfo.getDescription());
                                   logger.log(Level.INFO, "Running job: {0}", jobInfo);

                                   try {
                                           l.logclose(vid, "Az 'F' batch lefutott "+jobInfo.getJobName() + " - " + results, "");
                                       } catch (SQLException ex1) {
                                           Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex1);
                                       }
                                   Thread.sleep(60000);
                            } else {
                                    try {
                                            l.logclose(vid, "Az 'F' batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error "+resultsr);
                                        } catch (SQLException ex1) {
                                            Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex1);
                                        }
                                    Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, results);  
                            }
                            
                        } catch (Exception ex) {
                            try {
                                    l.logclose(vid, "Az 'F' batch "+jobInfo.getJobName(), "hibával futott le: owner: "+ jobInfo.getUsername()+ ", error "+ex.getMessage());
                                } catch (SQLException ex1) {
                                    Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex1);
                                }
                            Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        logger.log(Level.INFO, "End of BatchJobF "+jobid+" "+jobname+" at {0}", new Date());
                
                    } else {
                            Thread.sleep(30000);
                            logger.log(Level.INFO, "End of BatchJobE "+jobid+" "+jobname+" at {0}", new Date());
                            logger.log(Level.INFO, "It does not have the time {0} {1}", new Object[]{jobid, jobname}); }
                } catch (Exception ex) {Logger.getLogger(BatchJobF.class.getName()).log(Level.SEVERE, null, ex);}
                        
    }

}

