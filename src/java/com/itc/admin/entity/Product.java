package com.itc.admin.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jgmnx
 */
@Entity
@Table(name = "PRODUCT", catalog = "", schema = "ITC")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
    @NamedQuery(name = "Product.findByIsPackage", query = "SELECT p FROM Product p WHERE p.isPackage = :isPackage"),
    @NamedQuery(name = "Product.findByCategoryId", query = "SELECT p FROM Product p WHERE p.category.id = :categoryId"),
    @NamedQuery(name = "Product.findByLineId", query = "SELECT p FROM Product p WHERE p.category.line.id = :lineId"),
    @NamedQuery(name = "Product.findByLineIdWithPromo", query = "SELECT p FROM Product p WHERE p.category.line.id = :lineId AND p.promotion > 0.0")
})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ID", nullable = false, length = 30)
    private String id;
    @Size(max = 200)
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "BASE_PRICE", precision = 52)
    private Double basePrice;
    @Column(name = "PRICE_2", precision = 52)
    private Double price2;
    @Column(name = "PRICE_3", precision = 52)
    private Double price3;
    @Column(name = "PRICE_4", precision = 52)
    private Double price4;
    @Column(name = "PRICE_5", precision = 52)
    private Double price5;
    @Column(name = "PRICE_6", precision = 52)
    private Double price6;
    @Column(name = "PRICE_7", precision = 52)
    private Double price7;
    @Column(name = "PRICE_8", precision = 52)
    private Double price8;
    @Column(name = "PROMOTION", precision = 52)
    private Double promotion;
    @Column(name = "IS_PACKAGE")
    private Boolean isPackage;
    @Column(name = "SMALL_PIC")
    private byte[] smallPic;
    @Column(name = "CHKSUM_SMALL")
    private long checksumSmallPic;
    @Column(name = "BIG_PIC")
    private byte[] bigPic;
    @Column(name = "CHKSUM_BIG")
    private long checksumBigPic;
    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
    private List<ProductSpec> productSpecList;
    @JoinColumn(name = "CATEGORY", referencedColumnName = "ID")
    @ManyToOne
    private Category category;

    public Product() {
    }

    public Product(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
    
    public Double getPrice2() {
        return price2;
    }

    public void setPrice2(Double price2) {
        this.price2 = price2;
    }
    
    public Double getPrice3() {
        return price3;
    }

    public void setPrice3(Double price3) {
        this.price3 = price3;
    }
    
    public Double getPrice4() {
        return price4;
    }

    public void setPrice4(Double price4) {
        this.price4 = price4;
    }

    public Double getPrice5() {
        return price5;
    }

    public void setPrice5(Double price5) {
        this.price5 = price5;
    }

    public Double getPrice6() {
        return price6;
    }

    public void setPrice6(Double price6) {
        this.price6 = price6;
    }

    public Double getPrice7() {
        return price7;
    }

    public void setPrice7(Double price7) {
        this.price7 = price7;
    }

    public Double getPrice8() {
        return price8;
    }

    public void setPrice8(Double price8) {
        this.price8 = price8;
    }

    public Double getPromotion() {
        return promotion;
    }

    public void setPromotion(Double promotion) {
        this.promotion = promotion;
    }

    public Boolean getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(Boolean isPackage) {
        this.isPackage = isPackage;
    }

    public byte[] getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(byte[] smallPic) {
        this.smallPic = smallPic;
    }
    
    public long getChecksumSmallPic() {
        return checksumSmallPic;
    }
    
    public void setChecksumSmallPic(long checksumSmallPic) {
        this.checksumSmallPic = checksumSmallPic;
    }

    public byte[] getBigPic() {
        return bigPic;
    }

    public void setBigPic(byte[] bigPic) {
        this.bigPic = bigPic;
    }
    
    public long getChecksumBigPic() {
        return checksumBigPic;
    }
    
    public void setChecksumBigPic(long checksumBigPic) {
        this.checksumBigPic = checksumBigPic;
    }
    
    public List<ProductSpec> getProductSpecList() {
        return productSpecList;
    }

    public void setProductSpecList(List<ProductSpec> productSpecList) {
        this.productSpecList = productSpecList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.itc.admin.entity.Product[ id=" + id + " ]";
    }
    
}
