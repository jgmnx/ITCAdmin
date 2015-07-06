/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jgmnx
 */
@Entity
@Table(name = "CLIENT", catalog = "", schema = "ITC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id"),
    @NamedQuery(name = "Client.findByName", query = "SELECT c FROM Client c WHERE c.name = :name"),
    @NamedQuery(name = "Client.findByPriceList", query = "SELECT c FROM Client c WHERE c.priceList = :priceList"),
    @NamedQuery(name = "Client.findByUsername", query = "SELECT c FROM Client c WHERE c.username = :username"),
    @NamedQuery(name = "Client.findByPasswd", query = "SELECT c FROM Client c WHERE c.passwd = :passwd"),
    @NamedQuery(name = "Client.findByAgentId", query = "SELECT c FROM Client c WHERE c.agent.id = :agentId"),
    @NamedQuery(name = "Client.findByActive", query = "SELECT c FROM Client c WHERE c.active = :active")})
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Size(max = 100)
    @Column(name = "NAME", length = 100)
    private String name;
    @Column(name = "PRICE_LIST")
    private Integer priceList;
    @Size(max = 20)
    @Column(name = "USERNAME", length = 20)
    private String username;
    @Size(max = 20)
    @Column(name = "PASSWD", length = 20)
    private String passwd;
    @Column(name = "ACTIVE")
    private Boolean active;
    @JoinColumn(name = "AGENT", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Agent agent;

    public Client() {
    }

    public Client(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriceList() {
        return priceList;
    }

    public void setPriceList(Integer priceList) {
        this.priceList = priceList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itc.admin.entity.Client[ id=" + id + " ]";
    }
    
}
