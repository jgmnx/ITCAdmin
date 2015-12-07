package com.itc.admin.view;

import com.itc.admin.entity.Agent;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "agentsView")
@ViewScoped
public class AgentsView implements Serializable {

    @EJB
    private com.itc.admin.session.AgentFacade m_agentsFacade;
    private List<Agent> m_agents;
    private List<Agent> m_filteredAgents;
    private Agent m_selectedAgent;
    
    @PostConstruct
    public void init() {
        m_agents = m_agentsFacade.findAll();
    }

    public List<Agent> getAgents() {
        return m_agents;
    }
    
    public void setSelectedAgent(Agent selectedAgent) {
        m_selectedAgent = selectedAgent;
    }
    
    public Agent getSelectedAgent() {
        return m_selectedAgent;
    }

    public void setFilteredAgents(List<Agent> filteredAgents) {
        this.m_filteredAgents = filteredAgents;
    }

    public List<Agent> getFilteredAgents() {
        return m_filteredAgents;
    }
    
}
