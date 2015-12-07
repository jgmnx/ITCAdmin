package com.itc.admin.view;

import com.itc.admin.entity.AppVersion;
import com.itc.admin.entity.ImageCatalog;
import com.itc.admin.entity.Product;
import com.itc.admin.session.AppVersionFacade;
import com.itc.admin.session.ImageCatalogFacade;
import com.itc.admin.session.ProductFacade;
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
    private AppVersionFacade m_appVersionsFacade;
    @EJB
    private ImageCatalogFacade m_imageCatalogFacade;
    @EJB
    private ProductFacade m_productsFacade;
    
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
        String basePath = "/public/images/";
        try {
            for(ImageCatalog np : m_imageCatalogFacade.findAllNewProducts()) {
                ImageHelper.writeImage(np.getImage(), basePath, "new_" + np.getOrder());
            }
            for(ImageCatalog promo : m_imageCatalogFacade.findAllPromos()) {
                ImageHelper.writeImage(promo.getImage(), basePath, "promo_" + promo.getOrder());
            }
            for(Product p : m_productsFacade.findAll()) {
                ImageHelper.writeImage(p.getSmallPic(), basePath, "small_" + p.getId());
                ImageHelper.writeImage(p.getSmallPic(), basePath, "large_" + p.getId());
            }
            m_appVersionsFacade.create(m_selectedAppVersion);
            m_appVersions = m_appVersionsFacade.findAll();
            m_selectedAppVersion = new AppVersion();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessageSummary(e, "app_versions_add_error_images");
        }
        
    }
    
    public void deleteAppVersion() {
        m_appVersionsFacade.remove(m_selectedAppVersion);
        m_appVersions = m_appVersionsFacade.findAll();
    }

}
