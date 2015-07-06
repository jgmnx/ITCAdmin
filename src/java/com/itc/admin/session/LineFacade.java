package com.itc.admin.session;

import com.itc.admin.entity.Line;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jgmnx
 */
@Stateless
public class LineFacade extends AbstractFacade<Line> {
    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineFacade() {
        super(Line.class);
    }
    
}
