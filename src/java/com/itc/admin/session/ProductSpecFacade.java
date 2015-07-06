/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.session;

import com.itc.admin.entity.ProductSpec;
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
public class ProductSpecFacade extends AbstractFacade<ProductSpec> {

    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductSpecFacade() {
        super(ProductSpec.class);
    }

    public List<ProductSpec> findByProductId(String productId) {
        Query query = getEntityManager().createNamedQuery("ProductSpec.findByProductId");
        query.setParameter("productId", productId);
        return query.getResultList();
    }
}
