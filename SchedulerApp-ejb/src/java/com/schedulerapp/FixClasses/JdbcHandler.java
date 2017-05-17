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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Fekete András Demeter 
 */
 public class JdbcHandler {

    /**
     *
     * @param dbname Database String : rep_1 or rep_2 or apex_1
     * @return Connection
     */
    public Connection getConnection(String dbname) {

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            Connection conn = null;
            

            if (dbname.equals("rep_1")) {

                try {
                    String URL = "jdbc:oracle:thin:@host.domain.com:1526:rep1";
                    conn = DriverManager.getConnection(URL, "user", "passwd");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (dbname.equals("rep_2")) {

                try {
                    String URL = "jdbc:oracle:thin:@host.domain.com:1526:rep2";
                    conn = DriverManager.getConnection(URL, "user", "passwd");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (dbname.equals("apex_1")) {

                try {
                    String URL = "jdbc:oracle:thin:@host.domain.com:1526:rep3";
                    conn = DriverManager.getConnection(URL, "user", "passwd");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            } else { conn = null;}

            return conn;
        }
        
    }

