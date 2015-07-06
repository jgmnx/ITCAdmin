package com.itc.admin.view;

import com.itc.admin.entity.AppVersion;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "appVersionsView")
@ViewScoped
public class AppVersionsView implements Serializable {

    @EJB
    private com.itc.admin.session.AppVersionFacade m_appVersionsFacade;
    private List<AppVersion> m_appVersions;
    private AppVersion m_selectedAppVersion;
    
    private SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    
    @PostConstruct
    public void init() {
        m_appVersions = m_appVersionsFacade.findAll();
        m_selectedAppVersion = new AppVersion();
    }

    public List<AppVersion> getAppVersions() {
        return m_appVersions;
    }
    
    public void setSelectedAppVersion(AppVersion selectedAppVersion) {
        m_selectedAppVersion = selectedAppVersion;
    }
    
    public AppVersion getSelectedAppVersion() {
        return m_selectedAppVersion;
    }
    
    public void createAppVersion() {
        m_selectedAppVersion = new AppVersion();
        
        m_selectedAppVersion.setId("ITCAPP_" + m_sdf.format(new Date()));
        m_selectedAppVersion.setComment("");
    }
    
    public void addAppVersion() {
        m_appVersionsFacade.create(m_selectedAppVersion);
        m_appVersions = m_appVersionsFacade.findAll();
        m_selectedAppVersion = new AppVersion();
    }
    
    public void deleteAppVersion() {
        m_appVersionsFacade.remove(m_selectedAppVersion);
        m_appVersions = m_appVersionsFacade.findAll();
    }

}
