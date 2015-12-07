package com.itc.admin.view;

import com.itc.admin.entity.ImageCatalog;
import com.itc.admin.session.ImageCatalogFacade;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "newProductsCatalogView")
@ViewScoped
public class NewProductsCatalogView implements Serializable {
    
    @EJB
    private ImageCatalogFacade m_imageCatalogFacade;
    
    private List<String> m_imagesIds;
    private String m_selectedImageId;
    
    private int m_numberOfFilesToUpload;
    private Map<String, Integer> m_imagesOrder;
    private Map<String, ImageCatalog> m_uploadedImages; 
    
    private void loadAllImages() {
        List<ImageCatalog> images = m_imageCatalogFacade.findAllNewProducts();
        m_imagesIds = new ArrayList<String>();
        if (images != null) {
            for(ImageCatalog image : images) {
                m_imagesIds.add(String.valueOf(image.getOrder()));
            }
        }
        m_selectedImageId = null;
    }
    
    @PostConstruct
    public void init() {
        loadAllImages();
        m_numberOfFilesToUpload = 0;
    }
    
    public List<String> getImagesIds() {
        return m_imagesIds;
    }
    
    public void setImagesIds(List<String> imagesIds) {
        m_imagesIds = imagesIds;
    }
    
    public void selectImageAction(){
        m_selectedImageId = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imageId");
    }
    
    public void reorderImages() {
        try {
            List<ImageCatalog> modifiedImages = new ArrayList<ImageCatalog>();
            // Extract images
            for(String imageId : m_imagesIds) {
                modifiedImages.add(m_imageCatalogFacade.findByTypeOrder(ImageCatalogFacade.NEW_PRODUCT, 
                        Integer.valueOf(imageId)));
            }
            
            // Set order
            int nOrder = 1;
            for (ImageCatalog imageCatalog : modifiedImages) {
                imageCatalog.setOrder(nOrder);
                nOrder++;
            }
            
            // Update
            for (ImageCatalog imageCatalog : modifiedImages) {
                m_imageCatalogFacade.edit(imageCatalog);
            }
            
            loadAllImages();
            
            JsfUtil.addSuccessMessage("new_products_reorder_success", "");
        } catch (Exception e) {
            JsfUtil.addErrorMessageSummary(e, "new_products_reorder_error");
        }
    }
    
    public void deleteImage() {
        try {
            if (m_selectedImageId != null) {
                System.out.println("Image: " + m_selectedImageId);
                ImageCatalog imageCatalog = m_imageCatalogFacade.findByTypeOrder(ImageCatalogFacade.NEW_PRODUCT, 
                        Integer.valueOf(m_selectedImageId));
                if (imageCatalog != null) {
                    m_imageCatalogFacade.remove(imageCatalog);
                }
                loadAllImages();
                JsfUtil.addSuccessMessage("new_products_drop_success", "");
                reorderImages();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessageSummary(e, "new_products_drop_error");
        }
    }
    
    public void startHook() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int nFiles = Integer.valueOf(params.get("nFiles"));
        m_numberOfFilesToUpload = nFiles;
        m_imagesOrder = new HashMap<String, Integer>();
        for(int i = 0; i < nFiles; i++) {
            m_imagesOrder.put(params.get("file_" + i), (i + 1));
            System.out.println(params.get("file_" + i) + ", order " + i);
        }
        m_uploadedImages = new ConcurrentHashMap<String, ImageCatalog>();
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputstream());
            ByteArrayOutputStream imageOs = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", imageOs);
            byte[] imgBytes = imageOs.toByteArray();
            imageOs.close();
            
            ImageCatalog newProductImage = new ImageCatalog();
            newProductImage.setImage(imgBytes);
            newProductImage.setType(ImageCatalogFacade.NEW_PRODUCT);
            Checksum checksum = new CRC32();
            checksum.update(imgBytes, 0, imgBytes.length);
            newProductImage.setChecksum(checksum.getValue());
            m_uploadedImages.put(file.getFileName(), newProductImage);
            
            if (m_uploadedImages.size() == m_numberOfFilesToUpload) {
                for(String fileName : m_uploadedImages.keySet()) {
                    ImageCatalog image = m_uploadedImages.get(fileName);
                    System.out.println(fileName + ", order " + m_imagesOrder.get(fileName));
                    image.setOrder(m_imagesIds.size() + m_imagesOrder.get(fileName));
                    m_imageCatalogFacade.create(image);
                }
                loadAllImages();
                JsfUtil.addSuccessMessage("new_products_upload_success", "");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessageSummary(e, "new_products_upload_error");
        }
    }
    
}