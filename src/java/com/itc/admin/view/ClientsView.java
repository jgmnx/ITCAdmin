package com.itc.admin.view;

import com.itc.admin.entity.Client;
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
@ManagedBean(name = "clientsView")
@ViewScoped
public class ClientsView implements Serializable {

    @EJB
    private com.itc.admin.session.ClientFacade m_clientFacade;
    private List<Client> m_clients;
    private List<Client> m_filteredClients;
    private Client m_selectedClient;
    
    @PostConstruct
    public void init() {
        m_clients = m_clientFacade.findAll();
    }

    public List<Client> getClients() {
        return m_clients;
    }
    
    public void setSelectedClient(Client selectedClient) {
        m_selectedClient = selectedClient;
    }
    
    public Client getSelectedClient() {
        return m_selectedClient;
    }

    public void setFilteredClients(List<Client> filteredClients) {
        this.m_filteredClients = filteredClients;
    }

    public List<Client> getFilteredClients() {
        return m_filteredClients;
    }

    public void edit() {
        try {
            m_clientFacade.edit(m_selectedClient);
            JsfUtil.addSuccessMessage("clients_edit_success", m_selectedClient.getName());
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "clients_edit_error", m_selectedClient.getName());
        }
    }
}
