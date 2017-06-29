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
package com.schedulerapp.FixClasses;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fekete András Demeter 
 */
public class Lastlogtime {
    
    /**
     *
     * @param logid from LogMe
     * @return String 
     * @throws SQLException SQLException from oracle
     */
    public String lastlogtime(String logid) throws SQLException
    {
        Loggtodb K = new Loggtodb();
        String datastring = K.last_log_time(logid);
        if (datastring.equals("null")){
            System.err.println("never ran");
            return "never ran";
        } else {
           if (datastring.substring(0,10).equals(new SimpleDateFormat("yyyy.MM.dd").format(new Date()))){
               System.err.println("completed today");
               return "completed today";
           } else {
               System.err.println("still has not run");
               return "still has not run";
           }
        }
        
    }
}
