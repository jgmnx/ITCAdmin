package com.itc.admin.view;

import com.itc.admin.entity.Product;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "productsView")
@ViewScoped
public class ProductsView implements Serializable {
    
    @EJB
    private com.itc.admin.session.ProductFacade m_productFacade;
    private List<Product> m_products;
    private List<Product> m_filteredProducts;
    private Product m_selectedProduct;
    
    private String m_imageToUpdate;

    @PostConstruct
    public void init() {
        m_products = m_productFacade.findAll();
    }
    
    public List<Product> getProducts() {
        return m_products;
    }

    public void setSelectedProduct(Product selectedProduct) {
        m_selectedProduct = selectedProduct;
    }

    public Product getSelectedProduct() {
        return m_selectedProduct;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        m_filteredProducts = filteredProducts;
    }

    public List<Product> getFilteredProducts() {
        return m_filteredProducts;
    }

    public void setImageToUpdate(String m_imageToUpdate) {
        this.m_imageToUpdate = m_imageToUpdate;
    }

    public String getImageToUpdate() {
        return m_imageToUpdate;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            String id = m_selectedProduct.getId();
            BufferedImage bufferedImage = ImageIO.read(file.getInputstream());
            ByteArrayOutputStream imageOs = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", imageOs);
            byte[] imgBytes = imageOs.toByteArray();
            imageOs.close();
            if("small".equals(m_imageToUpdate)){
                m_selectedProduct.setSmallPic(imgBytes);
            } else {
                m_selectedProduct.setBigPic(imgBytes);
            }
            m_productFacade.edit(m_selectedProduct);
            JsfUtil.addSuccessMessage("products_upload_file_success", id);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "products_update_error");
        }
    }
    
}