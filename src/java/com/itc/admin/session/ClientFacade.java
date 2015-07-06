package com.itc.admin.session;

import com.itc.admin.entity.Client;
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
public class ClientFacade extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "ITCAdminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }

    public Client findByUserName(String userName) {
        Query query = getEntityManager().createNamedQuery("Client.findByUsername");
        return (Client) query.setParameter("username", userName).getSingleResult();
    }
    
    public List<Client> findByAgentId(Integer agentId) { 
        Query query = getEntityManager().createNamedQuery("Client.findByAgentId");
        return query.setParameter("agentId", agentId).getResultList();
    }
}
