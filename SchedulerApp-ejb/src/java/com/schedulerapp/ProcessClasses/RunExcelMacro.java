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
package com.schedulerapp.ProcessClasses;

import com.schedulerapp.batchjob.BatchJobB;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fekete András Demeter 
 */
public class RunExcelMacro {
    
    /**
     *
     * @param Excelpath excel file path
     * @param excelname excel file name
     * @param macroname macro name
     * @return String result
     * @throws InterruptedException InterruptedException
     * @throws IOException IOException
     */
    public String RunExcelMacro(String Excelpath, String excelname, String macroname) throws InterruptedException, IOException{
        
            
            String runresults = null;
            
            String commanpipoutput;
            String commanpiperror;
            
            
            PrintWriter writer = new PrintWriter("c:/temp/"+excelname+macroname+".vbs", "cp1250");
            writer.print("  Set objExcel = CreateObject(\"Excel.Application\")\r\n" +
                         "  Set objWorkbook = objExcel.Workbooks.Open(\""+Excelpath+"/"+excelname+"\")\r\n" +
                         "  objExcel.Application.Run \""+excelname+"!"+macroname+"\" \r\n" +
                         "  objWorkbook.Save\r\n" +
                         "  objWorkbook.Close\r\n" +
                         "  objExcel.Application.Quit\r\n " +
                         "  Set objWorkbook = Nothing\r\n" +
                         "  Set objExcel = Nothing \r\n " +
                         "  WScript.Quit");
            writer.close();
            Thread.sleep(3000);
            String script = "c:/temp/"+excelname+macroname+".vbs";
            String cmdArr [] = {"wscript.exe", script};
            Runtime rt = Runtime.getRuntime();
            Process proc;
            String s = null;
            BufferedReader stdError = null;
                  
            
            
                 try {
                       
                     

                                proc = rt.exec(cmdArr);
                                BufferedReader stdInput = new BufferedReader(new 
                                InputStreamReader(proc.getInputStream()));

                                stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                                      // read the output from the command
                                System.out.println("Here is the standard output of the command:\n");
                                commanpipoutput = "";
                                while ((s = stdInput.readLine()) != null) {
                                     System.out.println(s);
                                     commanpipoutput = commanpipoutput +" "+ s;
                                }
                                
                                 System.out.println("Here is the standard error of the command (if any):\n");
                                 
                                 commanpiperror = "-";
                                 while ((s = stdError.readLine()) != null) {
                                     System.out.println(s);
                                     commanpiperror = commanpiperror +" "+ s;
                                 }
                                 
                                 if (commanpiperror.equals("-")){
                                     runresults = "success";
                                 } else {
                                     runresults = commanpiperror;
                                 }
                             
                       
                      } catch (IOException ex) {
                          Logger.getLogger(BatchJobB.class.getName()).log(Level.SEVERE, null, ex);
                          runresults = ex.getMessage();
                      }
                  
                  
                 
                 
                try{

                        File file = new File(script);

                        if(file.delete()){
                            
                                System.out.println(file.getName() + " is deleted! ");
                                
                        }else{
                            
                                System.out.println(" Delete operation is failed. ");
                                
                        }

                    }catch(Exception e){

                        e.printStackTrace();
                    }
                
                
                
                return runresults;
                
                
                
    }
}
