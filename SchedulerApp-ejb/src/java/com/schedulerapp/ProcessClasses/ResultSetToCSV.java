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
import java.sql.ResultSet;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Fekete András Demeter 
 */
public class ResultSetToCSV {
    
    /**
     *
     * @param rs csv query resultSet
     * @param filename filename to export
     * @throws SQLException SQLException
     * @throws FileNotFoundException FileNotFoundException
     */
    public  ResultSetToCSV(ResultSet rs, String filename) throws SQLException, FileNotFoundException {

	PrintWriter csvWriter ;
    try {
        
        csvWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), "cp1250"));
        ResultSetMetaData meta = rs.getMetaData() ; 
        int numberOfColumns = meta.getColumnCount() ; 
        String dataHeaders = "\"" + meta.getColumnName(1) + "\"" ; 
        for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) { 
                dataHeaders += ";\"" + meta.getColumnName(i) + "\"" ;
        }
        csvWriter.println(dataHeaders) ;
        while (rs.next()) {
            String row = "\"" + rs.getString(1) + "\""  ; 
            for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) {
                row += ";\"" + rs.getString(i) + "\"" ;
            }
        csvWriter.println(row) ;
        }
        csvWriter.close();
    
    } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(ResultSetToCSV.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }

        

}
