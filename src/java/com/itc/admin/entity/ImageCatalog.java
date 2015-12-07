package com.itc.admin.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jgmnx
 */
@Entity
@Table(name = "IMAGES_CATALOG", catalog = "", schema = "ITC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImageCatalog.findByType", query = "SELECT i FROM ImageCatalog i WHERE i.type = :type ORDER BY i.order"),
    @NamedQuery(name = "ImageCatalog.findByTypeOrder", query = "SELECT i FROM ImageCatalog i WHERE i.type = :type AND i.order = :order")
})
public class ImageCatalog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "NORDER", nullable = false)
    private Integer order;
    @Column(name = "IMAGE")
    private byte[] image;
    @Column(name = "CHKSUM")
    private long checksum;

    public ImageCatalog() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    public long getChecksum() {
        return checksum;
    }
    
    public void setChecksum(long checksum) {
        this.checksum = checksum;
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
        if (!(object instanceof ImageCatalog)) {
            return false;
        }
        ImageCatalog other = (ImageCatalog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itc.admin.entity.ImageCatalog[ id=" + id + " ]";
    }
    
}
