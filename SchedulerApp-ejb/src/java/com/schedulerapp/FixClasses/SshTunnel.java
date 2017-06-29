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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Fekete András Demeter
 */
public final class SshTunnel {
    
    /**
     *
     */
    public Session sessionssh;

    /**
     *
     */
    public Connection conn;

    /**
     *
     */
    public String results;
    
    /**
     * Java Program to connect to remote database through SSH using port forwarding
     * @param sshuser sshuser
     * @param sshpassword sshpassword
     * @param sshhost sshhost
     * @param dbuserName dbuserName
     * @param dbpassword dbpassword
     * @param rhost rhost
     * @param rsid rsid
     * @param rport rport
     * @throws SQLException SQLException
     */
    
    public SshTunnel(String sshuser, String sshpassword, String sshhost, String dbuserName, String dbpassword, String rhost, String rsid, int rport ) throws SQLException {
 
        int lport = nextFreePort(1000,8000);
        
        String url = "jdbc:oracle:thin:@localhost:"+lport+":"+rsid;
        String driverName="oracle.jdbc.driver.OracleDriver";
        
        try{
            
            //Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            this.sessionssh=jsch.getSession(sshuser, sshhost, 22);
            this.sessionssh.setPassword(sshpassword);
            this.sessionssh.setConfig(config);
            this.sessionssh.connect();
            System.out.println("Connected");
            
            this.sessionssh.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+lport+":"+rsid+" -> "+rhost+":"+rport);
            System.out.println("Port Forwarded");
             
            Class.forName(driverName).newInstance();
            this.conn = DriverManager.getConnection (url, dbuserName, dbpassword);
            System.out.println ("Database connection established");
            DatabaseMetaData meta = this.conn.getMetaData();
            System.out.println("JDBC driver version is " + meta.getDriverVersion() + " - Product Version: "+ meta.getDatabaseProductVersion());
            System.out.println("DONE");
            this.results = "success";
            
        } catch (JSchException e) { System.err.println(e.getMessage()); this.results = e.getMessage(); 
        } catch (ClassNotFoundException e) { System.err.println(e.getMessage()); this.results = e.getMessage(); 
        } catch (InstantiationException e) { System.err.println(e.getMessage()); this.results = e.getMessage(); 
        } catch (IllegalAccessException e) { System.err.println(e.getMessage()); this.results = e.getMessage(); 
        } catch (SQLException e) { System.err.println(e.getMessage()); this.results = e.getMessage(); 
        }  

    }
    
    /**
     *
     * @return sshsession
     */
    public Session getSessionssh() {
        return sessionssh;
    }

    /**
     *
     * @return sshconnection
     */
    public Connection getConn() {
        return conn;
    }
    
    /**
     *
     * @return authresults
     */
    public String getResults() {
        return results;
    }
        
    private int nextFreePort(int from, int to) {
        Random rand = new Random();
        int port = rand.nextInt(to - from + 1) + from;
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to);
            }
        }
    }

    private boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
}


