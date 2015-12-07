/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.session;

import com.itc.admin.entity.Product;
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
public class ProductFacade extends AbstractFacade<Product> {
    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }
    
    public List<Product> findByCategory(Integer categoryId) {
        Query query = getEntityManager().createNamedQuery("Product.findByCategoryId");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
    
    public List<Product> findByLine(Integer lineId) {
        Query query = getEntityManager().createNamedQuery("Product.findByLineId");
        query.setParameter("lineId", lineId);
        return query.getResultList();
    }
    
     public List<Product> findByLineWithPromo(Integer lineId) {
        Query query = getEntityManager().createNamedQuery("Product.findByLineIdWithPromo");
        query.setParameter("lineId", lineId);
        return query.getResultList();
    }
    
}
