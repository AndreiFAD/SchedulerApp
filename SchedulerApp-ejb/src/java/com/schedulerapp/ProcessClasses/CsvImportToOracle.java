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

import com.schedulerapp.FixClasses.JdbcHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Fekete András Demeter 
 */
public class CsvImportToOracle {
 
    /**
     *
     * @param filepath file path
     * @param filestringname file substring name
     * @param oracletabel oracle table name
     * @param oraclecol oracle table cols name
     * @param oracledbname oracle database name rep_1 or rep_2 or apex_1
     * @param cvsSplitBy csv file split char or characters
     * @param oradeletetabel if Y trunc table before insert
     * @param MovePatch after import move file
     * @param fileformattypetoora file format csv or xlsx
     * @return String results
     * @throws SQLException SQLException
     * @throws InterruptedException InterruptedException
     */
    public  String csvimporttooracle(String filepath,String filestringname,String oracletabel,String oraclecol,String oracledbname,String cvsSplitBy,String oradeletetabel,String MovePatch,String fileformattypetoora) throws SQLException, InterruptedException {

        String resultsrun = null;
        String results = null;
        File folder = new File(filepath);
        File[] listOfFiles = folder.listFiles();
        String filenames = null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                
                if (file.getName().indexOf(filestringname.replace("*", "")) >= 0 && file.getName().endsWith(fileformattypetoora)==true) {
                    filenames = file.getName();
                    System.out.println(file.getName());

                            String csvFile = filepath+filenames;
                            BufferedReader br = null;
                            String line = "";

                        if ("xlsx".equals(fileformattypetoora)){
                            java.sql.Connection conn = null;
                            try {
                                final DataFormatter dataFormatter = new DataFormatter();
                                Workbook wb = WorkbookFactory.create(new File(csvFile));
                                Sheet mySheet = wb.getSheetAt(0);  // wb.getSheet("Page1_1")
                                Iterator<Row> rowIter = mySheet.rowIterator();
                                Iterator<Row> rowIterator = mySheet.iterator();
                                int b = 0;
                                int cikl =0;
                                int count = mySheet.getLastRowNum()+1;
                                if (oradeletetabel=="N") {

                                } else {
                                    System.err.println("truncate table");

                                    try{

                                        String dbname = oracledbname;
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection(dbname);
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora;
                                        rsora = stmtora.executeQuery("truncate table " + oracletabel );
                                        conn.commit();
                                        conn.close();
                                    } catch (SQLException e){
                                        conn.rollback();
                                        conn.close();
                                        System.out.println(e.getMessage());
                                    }

                                }
                                String Rows = "insert into " + oracletabel + " (" + oraclecol + ") ";
                                while (rowIterator.hasNext()) {
                                    Row row = rowIterator.next();
                                    String items ="";
                                    Iterator<Cell> cellIterator = row.cellIterator();
                                    while (cellIterator.hasNext()) {

                                        Cell cell = cellIterator.next();
                                        CellStyle cellStyle = cell.getCellStyle();
                                         switch (cell.getCellType()) {
                                            case Cell.CELL_TYPE_BLANK:
                                                items += "','";
                                                break;
                                            case Cell.CELL_TYPE_STRING:
                                                String formtatedValue = cell.getStringCellValue();
                                                items += formtatedValue.replace("'", "''") + "','";
                                                break;
                                            case Cell.CELL_TYPE_NUMERIC:
                                                String formtatedValueNum = dataFormatter.formatRawCellContents(cell.getNumericCellValue(), cellStyle.getDataFormat(), cellStyle.getDataFormatString());
                                                items += formtatedValueNum.replace("'", "''") + "','";
                                                break;
                                            case Cell.CELL_TYPE_BOOLEAN:
                                                String formtatedValueBool = cell.getBooleanCellValue() + "',";
                                                items += formtatedValueBool.replace("'", "''") + "','";
                                                break;
                                            default :
                                        }
                                    }

                                    cikl += 1;
                                    b += 1;
                                    //               
                                    //insert ha meg van a sorszám
                                    //

                                    if (cikl==1){
                                    } else {
                                            //System.out.println("select '"+items.substring(0, items.length()-3)+"' from dual \r\n union all \r\n ");
                                            Rows += "select '"+items.substring(0, items.length()-3)+"' from dual \r\n union all \r\n ";
                                    }     
                                    if (b==900){
                                        conn = null;
                                        Rows=Rows.substring(0, Rows.length() - 13);
                                        String SqlQuery=Rows;
                                        System.out.println(SqlQuery);
                                        String dbname = oracledbname;
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection(dbname);
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora;
                                        rsora = stmtora.executeQuery(SqlQuery);
                                        conn.commit();
                                        conn.close();
                                        conn = null;
                                        b=0;
                                        Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;

                                    }  else if (cikl==count){
                                        conn = null;
                                        Rows=Rows.substring(0, Rows.length() - 13);
                                        String SqlQuery=Rows;
                                        String dbname = oracledbname;
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection(dbname);
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora;
                                        rsora = stmtora.executeQuery(SqlQuery);
                                        conn.commit();
                                        conn.close();
                                        conn = null;
                                        b=0;
                                        Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;

                                    }  else {
                                    }

                                }
                                wb.close();
                            } catch (Exception r) {
                                System.err.print(r.getMessage());
                            } finally {
                                if (br != null) {
                                    try {
                                        br.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Thread.sleep(30000);
                                try{

                                   File afile =new File(csvFile);
                                   NetworkShareFileCopy co = new NetworkShareFileCopy();
                                   results = co.NetworkShareFileCopy(csvFile, MovePatch + afile.getName());

                                }catch(Exception e){
                                        e.printStackTrace();
                                }
                                
                            }
                        
                        }else if ("csv".equals(fileformattypetoora)){
                            java.sql.Connection conn = null;
                            try {
                                br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "cp1250"));
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "cp1250"));
                                int b = 0;
                                int cikl =0;
                                String input;
                                int count = 0;
                                while((input = bufferedReader.readLine()) != null)
                                {
                                    count++;
                                }
                                bufferedReader=null;
                                input=null;
                                if (oradeletetabel.equals("N")) {

                                } else {
                                    System.err.println("truncate table");
                                    conn = null;
                                    try{

                                        String dbname = oracledbname;
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection(dbname);
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora;
                                        rsora = stmtora.executeQuery("truncate table " + oracletabel );
                                        conn.commit();
                                        conn.close();
                                    } catch (SQLException e){

                                        System.out.println(e.getMessage());
                                        conn.rollback();
                                        conn.close();
                                    }

                                }
                                String Rows = "insert into " + oracletabel + " (" + oraclecol + ") ";

                                while ((line = br.readLine()) != null) {
                                    cikl += 1;
                                    b += 1;
                                    line = line.replace("'", "''");
                                    //System.err.println("select '"+line.replace(";", "','")+"' from dual \r\n union all \r\n ");
                                    if (cikl==1){
                                    } else {
                                        if ((line.replace(cvsSplitBy, "','").substring(0,1)).equals("\"") && (line.replace(cvsSplitBy, "','").substring(line.replace(cvsSplitBy, "','").length() - 1).equals("\""))){
                                            Rows += "select '"+line.replace(cvsSplitBy, "','").substring(1,line.replace(cvsSplitBy, "','").length() - 1)+"' from dual \r\n union all \r\n ";
                                        }else{
                                            Rows += "select '"+line.replace(cvsSplitBy, "','")+"' from dual \r\n union all \r\n ";
                                        }
                                    }     
                                    if (b==900){
                                        conn = null;
                                        Rows=Rows.substring(0, Rows.length() - 13);
                                        String SqlQuery=Rows;
                                        String dbname = oracledbname;
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection(dbname);
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora;
                                        rsora = stmtora.executeQuery(SqlQuery);
                                        conn.commit();
                                        conn.close();
                                        conn = null;
                                        b=0;
                                        Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;

                                    }  else if (cikl==count){
                                        conn = null;
                                        Rows=Rows.substring(0, Rows.length() - 13);
                                        String SqlQuery=Rows;
                                        String dbname = oracledbname;
                                        JdbcHandler Hand =new JdbcHandler();
                                        conn = Hand.getConnection(dbname);
                                        conn.setAutoCommit(false);
                                        Statement stmtora = conn.createStatement();
                                        ResultSet rsora;
                                        rsora = stmtora.executeQuery(SqlQuery);
                                        conn.commit();
                                        conn.close();
                                        conn = null;
                                        b=0;
                                        Rows ="insert into " + oracletabel + " (" + oraclecol + ") " ;

                                    }  else {
                                    }
                                }
                            
                            

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (br != null) {
                                    try {
                                        br.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Thread.sleep(30000);
                                try{

                                   File afile =new File(csvFile);
                                   NetworkShareFileCopy co = new NetworkShareFileCopy();
                                   results = co.NetworkShareFileCopy(csvFile, MovePatch + afile.getName());
                                   
                                }catch(Exception e){
                                        e.printStackTrace();
                                }
                            }
                            }
                    } else {
                    resultsrun = "success - file now not found";
                    System.err.println("file now not found");
                }
            }
        }
        
        
        if (resultsrun == "success - file now not found") {
            return resultsrun;
            
        } else if (results=="success") {
            return results;
            
        } else if (results=="success - exist, i cant rewrite") {
            return results;
            
        } else if (results=="success - Delete operation is failed.") {
            return results;
                    
        } else {
            return "Failed!";
                    
        }
       
    }

}