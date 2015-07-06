/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.session;

import com.itc.admin.entity.Category;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author jgmnx
 */
@Stateless
public class CategoryFacade extends AbstractFacade<Category> {
    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryFacade() {
        super(Category.class);
    }
    
    public Category findCategory(String legacyLineName, String legacyCategoryName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> category = cq.from(Category.class);
        Predicate p1 = cb.equal(category.get("line").get("legacyName"), legacyLineName);
        Predicate p2 = cb.equal(category.get("legacyName"), legacyCategoryName);
        cq.select(category).where(cb.and(p1, p2));
        return getEntityManager().createQuery(cq).getSingleResult();
    }
    
    public Integer getProductsListSize(Integer categoryId) {
        Query query = getEntityManager().createNamedQuery("Category.getProductsListSize");
        query.setParameter("id", categoryId);
        try {
            return (Integer)query.getSingleResult();
        } catch(NoResultException nre) {
            return 0;
        }
    }
}
