package com.itc.admin.view;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author jgmnx
 */
@ManagedBean
@RequestScoped
public class LoginView implements Serializable {

    public static final String AUTH_KEY = "admin.user.name";
    
    private static final String ADMIN_USER = "itcadmin";
    private static final String ADMIN_PASSWD = "1tc4dm1n";
    private static final String OK_OUTCOME = "ok";
    private static final String ERROR_OUTCOME = "error";

    private String m_userName;
    private String m_password;

    public LoginView() {
    }

    public String login() {
        try {
            if (ADMIN_USER.equals(getUserName()) && ADMIN_PASSWD.equals(getPassword())) {
                JsfUtil.addSuccessMessage("login_success");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(AUTH_KEY, getUserName());
                return OK_OUTCOME;
            } else {
                JsfUtil.addErrorMessage("login_failed");
                m_userName = m_password = null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "login_failed");
        }
        return ERROR_OUTCOME;
    }

    public void setUserName(String userName) {
        m_userName = userName;
    }

    public String getUserName() {
        return m_userName;
    }
    
    public void setPassword(String password) {
        m_password = password;
    }

    public String getPassword() {
        return m_password;
    }

}
