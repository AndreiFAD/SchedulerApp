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

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Fekete András Demeter
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private String pwd;
	private String msg;
	private String user;

    /**
     *
     * @return password
     */
    public String getPwd() {
		return pwd;
	}

    /**
     *
     * @param pwd set password
     */
    public void setPwd(String pwd) {
		this.pwd = pwd;
	}

    /**
     *
     * @return message
     */
    public String getMsg() {
		return msg;
	}

    /**
     *
     * @param msg set message
     */
    public void setMsg(String msg) {
		this.msg = msg;
	}

    /**
     *
     * @return username
     */
    public String getUser() {
		return user;
	}

    /**
     *
     * @param user set username
     */
    public void setUser(String user) {
		this.user = user;
	}

	//validate login

    /**
     *validate login
     * @return login or active JobList
     */
	public String validateUsernamePassword() {
		boolean valid = LoginDAO.validate(user, pwd);
		if (valid) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", user.toUpperCase());
			return "JobList";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Password",
							"Please enter correct username and Password"));
			return "login";
		}
	}
    //logout event, invalidate session

    /**
     *logout event, invalidate session
     * @return login
     */
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}
}