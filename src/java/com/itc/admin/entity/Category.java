/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jgmnx
 */
@Entity
@Table(name = "CATEGORY", catalog = "", schema = "ITC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findById", query = "SELECT c FROM Category c WHERE c.id = :id"),
    @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE c.name = :name"),
    @NamedQuery(name = "Category.findByLegacyName", query = "SELECT c FROM Category c WHERE c.legacyName = :legacyName"),
    @NamedQuery(name = "Category.findByNorder", query = "SELECT c FROM Category c WHERE c.norder = :norder"),
    @NamedQuery(name = "Category.getProductsListSize", query = "SELECT size(c.productList) FROM Category c WHERE c.id = :id")
})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Size(max = 50)
    @Column(name = "NAME", length = 50)
    private String name;
    @Size(max = 50)
    @Column(name = "TEXT", length = 50)
    private String text;
    @Size(max = 50)
    @Column(name = "LEGACY_NAME", length = 50)
    private String legacyName;
    @Size(max = 100)
    @Column(name = "DESCRIPTION", length = 100)
    private String description;
    @Size(max = 50)
    @Column(name = "IMAGE", length = 50)
    private String image;
    @Column(name = "NORDER")
    private Integer norder;
    @JoinColumn(name = "LINE", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Line line;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> productList;

    public Category() {
    }

    public Category(Integer id) {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLegacyName() {
        return legacyName;
    }

    public void setLegacyName(String legacyName) {
        this.legacyName = legacyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNorder() {
        return norder;
    }

    public void setNorder(Integer norder) {
        this.norder = norder;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itc.admin.entity.Category[ id=" + id + " ]";
    }
    
}
