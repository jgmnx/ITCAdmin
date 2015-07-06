package com.itc.admin.session;

import com.itc.admin.entity.AppVersion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jgmnx
 */
@Stateless
public class AppVersionFacade extends AbstractFacade<AppVersion> {
    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppVersionFacade() {
        super(AppVersion.class);
    }
    
}
