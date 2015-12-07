package com.itc.admin.session;

import com.itc.admin.entity.ImageCatalog;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jgmnx
 */
@Stateless
public class ImageCatalogFacade extends AbstractFacade<ImageCatalog> {
    
    public static final String NEW_PRODUCT = "NEW";
    public static final String PROMO = "PROMO";
    
    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImageCatalogFacade() {
        super(ImageCatalog.class);
    }
    
    public List<ImageCatalog> findAllByType(String type) {
        Query query = getEntityManager().createNamedQuery("ImageCatalog.findByType");
        query.setParameter("type", type);
        return query.getResultList();
    }
    
    public ImageCatalog findByTypeOrder(String type, int order) {
        Query query = getEntityManager().createNamedQuery("ImageCatalog.findByTypeOrder");
        query.setParameter("type", type);
        query.setParameter("order", order);
        return (ImageCatalog) query.getSingleResult();
    }
    
    public List<ImageCatalog> findAllNewProducts() {
        return findAllByType(NEW_PRODUCT);
    }
    
    public List<ImageCatalog> findAllPromos() {
        return findAllByType(PROMO);
    }
    
}
