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

package com.schedulerapp.web;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author Fekete András Demeter
 */
public class LoginDAO {

    /**
     * authentication with AD in domain RES
     *
     * @param user username
     * @param password password 
     * @return boolean ( Result of validation ) 
     */
    public static boolean validate(String user, String password) {

		try {
                           return autAD(user,password);
                    
                    } catch (Exception ex) {
                            System.out.println("Login error -->" + ex.getMessage());
                            return false;
                    }
	}
    
    /**
     *
     * @param env Hashtable
     * @return boolean
     */
    public static  boolean check(Hashtable env){
        try {
	    // Create initial context
	    DirContext ctx = new InitialDirContext(env);
            ctx.close();
            return true;
	    
	} catch (NamingException e) {
            return false;
	}
    }
    
    /**
     * SECURITY_AUTHENTICATION, PROVIDER_URL
     * @param UserName username
     * @param UserPass password
     * @return boolean ( Result of authentication with AD ) 
     */
    public static  boolean autAD(String UserName, String UserPass) {

	// Set up environment for creating initial context
	Hashtable env = new Hashtable(11);

	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	env.put(Context.PROVIDER_URL, "ldap://ldaphost:port");

	// Authenticate as S. User and password "mysecret"
	env.put(Context.SECURITY_AUTHENTICATION, "simple");
	env.put(Context.SECURITY_PRINCIPAL, UserName+"@ldaphost");
	env.put(Context.SECURITY_CREDENTIALS, UserPass);

        return check(env);
  
    }
}
