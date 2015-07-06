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
import javax.persistence.Id;
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
@Table(name = "LINE", catalog = "", schema = "ITC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Line.findAll", query = "SELECT l FROM Line l"),
    @NamedQuery(name = "Line.findById", query = "SELECT l FROM Line l WHERE l.id = :id"),
    @NamedQuery(name = "Line.findByName", query = "SELECT l FROM Line l WHERE l.name = :name"),
    @NamedQuery(name = "Line.findByText", query = "SELECT l FROM Line l WHERE l.text = :text"),
    @NamedQuery(name = "Line.findByLegacyName", query = "SELECT l FROM Line l WHERE l.legacyName = :legacyName"),
    @NamedQuery(name = "Line.findByDescription", query = "SELECT l FROM Line l WHERE l.description = :description"),
    @NamedQuery(name = "Line.findByImage", query = "SELECT l FROM Line l WHERE l.image = :image"),
    @NamedQuery(name = "Line.findByNorder", query = "SELECT l FROM Line l WHERE l.norder = :norder")})
public class Line implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Size(max = 20)
    @Column(name = "NAME", length = 20)
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
    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY)
    private List<Category> categoryList;

    public Line() {
    }

    public Line(Integer id) {
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

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
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
        if (!(object instanceof Line)) {
            return false;
        }
        Line other = (Line) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itc.admin.entity.Line[ id=" + id + " ]";
    }
    
}
