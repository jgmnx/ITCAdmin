package com.itc.admin.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guillermo
 */
@Entity
@Table(name = "APP_VERSION", catalog = "", schema = "ITC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppVersion.findAll", query = "SELECT a FROM AppVersion a"),
    @NamedQuery(name = "AppVersion.findById", query = "SELECT a FROM AppVersion a WHERE a.id = :id"),
    @NamedQuery(name = "AppVersion.findByComment", query = "SELECT a FROM AppVersion a WHERE a.comment = :comment")})
public class AppVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ID", nullable = false, length = 30)
    private String id;
    @Size(max = 200)
    @Column(name = "COMMENT", length = 200)
    private String comment;

    public AppVersion() {
    }

    public AppVersion(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        if (!(object instanceof AppVersion)) {
            return false;
        }
        AppVersion other = (AppVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itc.admin.entity.AppVersion[ id=" + id + " ]";
    }
    
}
