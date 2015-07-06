package com.itc.admin.view;

import com.itc.admin.entity.Category;
import com.itc.admin.entity.Product;
import com.itc.admin.entity.ProductSpec;
import com.itc.admin.session.CategoryFacade;
import com.itc.admin.session.ProductSpecFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author jgmnx
 */
@ManagedBean(name = "updateProductsView")
@ViewScoped
public class UpdateProductsView implements Serializable {
    
    @EJB
    private com.itc.admin.session.ProductFacade m_productFacade;
    @EJB
    private ProductSpecFacade m_productSpecFacade;
    @EJB
    private CategoryFacade m_categoryFacade;

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        JsfUtil.addSuccessMessage("products_update_file", file.getFileName(), file.getContentType());

        try {
            String type = file.getFileName().endsWith(".xls") ? "HSSF" : "XSSF";
            Workbook wb;
            if ("HSSF".equals(type)) {
                wb = new HSSFWorkbook(file.getInputstream());
            } else {
                wb = new XSSFWorkbook(file.getInputstream());
            }
            // Process products
            Sheet ws = wb.getSheet("PRODUCTOS");
            Row row;
            String productId;
            Product product;
            int r;
            int nProducts;
            boolean isNew;
            Category category;
            for (r = 1; r <= ws.getLastRowNum(); r++) {
                row = ws.getRow(r);
                productId = row.getCell(0).getStringCellValue();
                product = m_productFacade.find(productId);
                isNew = product == null;
                if (product == null) {
                    product = new Product();
                } else {
                    for(ProductSpec pe : product.getProductSpecList()) {
                        m_productSpecFacade.remove(pe);
                    }
                }
                product.setDescription(row.getCell(1).getStringCellValue());
                product.setBasePrice(row.getCell(2).getNumericCellValue());
                product.setPrice5(row.getCell(3).getNumericCellValue());
                product.setPrice6(row.getCell(4).getNumericCellValue());
                product.setPrice7(row.getCell(5).getNumericCellValue());
                product.setPrice8(row.getCell(6).getNumericCellValue());
                product.setPromotion(row.getCell(7).getNumericCellValue());
                product.setIsPackage(row.getCell(8).getNumericCellValue() == 1);
                category = m_categoryFacade.findCategory(
                        row.getCell(9).getStringCellValue(), 
                        row.getCell(10).getStringCellValue());
                product.setCategory(category);
                
                if (isNew) {
                    m_productFacade.create(product);
                } else {
                    m_productFacade.edit(product);
                }
            }
            nProducts = r;
            // Process product specs
            ws = wb.getSheet("PRODUCT_SPECS");
            String spec;
            String value;
            ProductSpec productSpec;
            for (r = 1; r <= ws.getLastRowNum(); r++) {
                row = ws.getRow(r);
                productId = row.getCell(0).getStringCellValue();
                product = m_productFacade.find(productId);
                if (product != null) {
                    spec = row.getCell(1).getStringCellValue();
                    value = row.getCell(2).getStringCellValue();
                    productSpec = new ProductSpec();
                    productSpec.setSpec(spec);
                    productSpec.setValue(value);
                    productSpec.setProduct(product);
                    m_productSpecFacade.create(productSpec);
                }
            }
            JsfUtil.addSuccessMessage("products_update_sucess", nProducts);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "products_update_error");
        }
    }
    
}