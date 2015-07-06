package com.itc.admin.session;

import com.itc.admin.entity.Agent;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jgmnx
 */
@Stateless
public class AgentFacade extends AbstractFacade<Agent> {

    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentFacade() {
        super(Agent.class);
    }

    public Agent findByUserName(String userName) {
        Query query = getEntityManager().createNamedQuery("Agent.findByUsername");
        return (Agent) query.setParameter("username", userName).getSingleResult();
    }

}
