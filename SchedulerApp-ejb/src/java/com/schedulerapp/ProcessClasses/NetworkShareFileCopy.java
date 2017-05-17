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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
/*
* @author Andrei
*/
public class NetworkShareFileCopy {
   
    /**
     *
     * @param from_name from file path with name
     * @param to_name to file path with name
     * @return result string
     * @throws Exception Exception
     */
    public String NetworkShareFileCopy(String from_name,String to_name) throws Exception {    

        String results = null;
        String rewrite = "Y";
        File OLd_FILE = new File(from_name);
        File to_file = new File(to_name);
        if (to_file.exists()) {
            if (to_file.canWrite()) {
                    if (rewrite.equals("Y")){
                        copyfile(from_name, to_name);
                        System.err.println("exist, rewrite Y, , copy successful");
                        if(OLd_FILE.delete()){
                                System.out.println(OLd_FILE.getName() + " is deleted!");
                                results = "success";
                        }else{
                                results = "success - Delete operation is failed.";
                                System.out.println("Delete operation is failed.");
                        }
                    } else {
                        results = "success - exist, rewrite N";
                        System.err.println("exist, rewrite N");
                    }
            } else {
                results = "success - exist, i cant rewrite";
                System.err.println("exist, i cant rewrite");
            }
        } else {
            copyfile(from_name, to_name);
            System.err.println("is not exist, copy successful");
            if(OLd_FILE.delete()){
                System.out.println(OLd_FILE.getName() + " is deleted!");
                results = "success";
            }else{
                results = "success - Delete operation is failed.";
                System.out.println("Delete operation is failed.");
            }
        }
        return results;
  }
 
    /**
     *
     * @param from_file  from file path with name
     * @param to_file  to file path with name
     * @throws FileNotFoundException FileNotFoundException
     * @throws IOException IOException
     */
    public static void copyfile(String from_file, String to_file) throws FileNotFoundException, IOException {
    FileInputStream from = null; // Stream to read from source
    FileOutputStream to = null; // Stream to write to destination
    try {
      from = new FileInputStream(from_file);
      to = new FileOutputStream(to_file);
      byte[] buffer = new byte[100000000];
      int bytes_read;
      while ((bytes_read = from.read(buffer)) != -1)
        to.write(buffer, 0, bytes_read); // write
    }
    finally {
      if (from != null)
        try {
          from.close();
        } catch (IOException e) {
        }
      if (to != null)
        try {
          to.close();
        } catch (IOException e) {
        }
    }
  }
 
  
}