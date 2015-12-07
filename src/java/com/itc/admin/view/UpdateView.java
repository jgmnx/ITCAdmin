package com.itc.admin.view;

import com.itc.admin.entity.Agent;
import com.itc.admin.entity.Category;
import com.itc.admin.entity.Client;
import com.itc.admin.entity.Line;
import com.itc.admin.entity.Product;
import com.itc.admin.entity.ProductSpec;
import com.itc.admin.session.AgentFacade;
import com.itc.admin.session.CategoryFacade;
import com.itc.admin.session.ClientFacade;
import com.itc.admin.session.LineFacade;
import com.itc.admin.session.ProductSpecFacade;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
@ManagedBean(name = "updateView")
@ViewScoped
public class UpdateView implements Serializable {

    private static final String SMALL_IMG = "small";
    private static final String LARGE_IMG = "large";
            
    private final String AGENTS = JsfUtil.getMessage("update_agents");
    private final String CLIENTS = JsfUtil.getMessage("update_clients");
    private final String PRODUCTS = JsfUtil.getMessage("update_products");
    private final String PRODUCTS_SPECS = JsfUtil.getMessage("update_product_specs");
    
    private Map<String, Line> m_linesMap;
    private Map<String, Category> m_categoriesMap;
    
    private boolean m_updateAgents;
    private boolean m_updateClients;
    private boolean m_updateProducts;
    
    @EJB
    private com.itc.admin.session.ProductFacade m_productFacade;
    @EJB
    private ProductSpecFacade m_productSpecFacade;
    @EJB
    private LineFacade m_lineFacade;
    @EJB
    private CategoryFacade m_categoryFacade;
    @EJB
    private AgentFacade m_agentsFacade;
    @EJB
    private ClientFacade m_clientsFacade;

    public boolean isUpdateAgents() {
        return m_updateAgents;
    }

    public void setUpdateAgents(boolean updateAgents) {
        m_updateAgents = updateAgents;
    }

    public boolean isUpdateClients() {
        return m_updateClients;
    }

    public void setUpdateClients(boolean updateClients) {
        m_updateClients = updateClients;
    }

    public boolean isUpdateProducts() {
        return m_updateProducts;
    }

    public void setUpdateProducts(boolean updateProducts) {
        m_updateProducts = updateProducts;
    }
    
    public void updateChecksListener() {
        System.out.println("Agents: " + isUpdateAgents());
        System.out.println("Clients: " + isUpdateClients());
        System.out.println("Products: " + isUpdateProducts());
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        
        JsfUtil.addSuccessMessage("update_file", file.getFileName(), file.getContentType());

        try {
            String type = file.getFileName().endsWith(".xls") ? "HSSF" : "XSSF";
            Workbook wb;
            if ("HSSF".equals(type)) {
                wb = new HSSFWorkbook(file.getInputstream());
            } else {
                wb = new XSSFWorkbook(file.getInputstream());
            }

            if (isUpdateAgents()) {
                processUpdateAgents(wb.getSheet("AGENTES"));
            }
            if (isUpdateClients()) {
                processUpdateClients(wb.getSheet("CLIENTES"));
            }
            if (isUpdateProducts()) {
                processUpdateProducts(wb.getSheet("PRODUCTOS"), 
                                      wb.getSheet("FICHAS_TECNICAS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessageSummary(e, "update_error");
        }
    }
    
    private Cell getCell(Row row, int i) {
        return row.getCell(i, Row.CREATE_NULL_AS_BLANK);
    }

    private void processUpdateAgents(Sheet agentsSheet) {
        Row row;
        Integer agentId;
        boolean isNew;
        Agent agent;
        Set<Integer> newAgents = new HashSet<Integer>();
        Set<Integer> editedAgents = new HashSet<Integer>();
        Set<Integer> deletedAgents = new HashSet<Integer>();
        
        try {
            for(int r = 1; r <= agentsSheet.getLastRowNum(); r++ ) {
                row = agentsSheet.getRow(r);
                agentId = getNumericCellValue(getCell(row, 0)).intValue();
                agent = m_agentsFacade.find(agentId);
                isNew = agent == null;
                if (isNew) {
                    agent = new Agent();
                    agent.setId(agentId);
                }
                agent.setName(getStringCellValue(getCell(row, 1)));
                agent.setSuperuser(getBooleanCellValue(getCell(row, 2)));
                agent.setUsername(getStringCellValue(getCell(row, 3)));
                agent.setPasswd(getStringCellValue(getCell(row, 4)));
                agent.setActive(getBooleanCellValue(getCell(row, 5)));
                if (isNew) {
                    m_agentsFacade.create(agent);
                    newAgents.add(agentId);
                } else {
                    m_agentsFacade.edit(agent);
                    editedAgents.add(agentId);
                }
            }
            for (Agent a : m_agentsFacade.findAll()) {
                if (!(newAgents.contains(a.getId()) || 
                        editedAgents.contains(a.getId()))) 
                {
                    deletedAgents.add(a.getId());
                    m_agentsFacade.remove(a);
                }
            }
            if (newAgents.size() > 0) {
                JsfUtil.addSuccessMessage("update_add_sucess", 
                        newAgents.size(), AGENTS);
            }
            if (editedAgents.size() > 0) {
                JsfUtil.addSuccessMessage("update_edit_sucess", 
                        editedAgents.size(), AGENTS);
            }
            if (deletedAgents.size() > 0) {
                JsfUtil.addSuccessMessage("update_delete_sucess", 
                        deletedAgents.size(), AGENTS);
            }
        } catch(Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessageSummary(e, "update_error_sheet", AGENTS);
        }
    }

    private void processUpdateClients(Sheet clientsSheet) {
        Row row;
        Integer clientId;
        boolean isNew;
        Client client;
        Set<Integer> newClients = new HashSet<Integer>();
        Set<Integer> editedClients = new HashSet<Integer>();
        Set<Integer> deletedClients = new HashSet<Integer>();
        
        try {
            for(int r = 1; r <= clientsSheet.getLastRowNum(); r++ ) {
                row = clientsSheet.getRow(r);
                System.out.println("Processing row: " + r);
                clientId = getNumericCellValue(getCell(row, 0)).intValue();
                client = m_clientsFacade.find(clientId);
                isNew = client == null;
                if (isNew) {
                    client = new Client();
                    client.setId(clientId);
                }
                client.setName(getStringCellValue(getCell(row, 1)));
                int iValue = getNumericCellValue(getCell(row, 2)).intValue();
                Agent agent = m_agentsFacade.find(iValue);
                if (agent == null) {
                    throw new IllegalArgumentException(
                            JsfUtil.getMessage("update_error_invalid_agent", 
                                    iValue, clientId));
                }
                client.setAgent(agent);
                iValue = getNumericCellValue(getCell(row, 3)).intValue();
                if (iValue < 1 || iValue > 8) {
                    throw new IllegalArgumentException(
                            JsfUtil.getMessage("update_error_invalid_price_list",
                                    iValue, clientId));
                }
                client.setPriceList(iValue);
                client.setUsername(getStringCellValue(getCell(row, 4)));
                client.setPasswd(getStringCellValue(getCell(row, 5)));
                client.setActive(getBooleanCellValue(getCell(row, 6)));
                if (isNew) {
                    m_clientsFacade.create(client);
                    newClients.add(clientId);
                } else {
                    m_clientsFacade.edit(client);
                    editedClients.add(clientId);
                }
            }
            for (Client c : m_clientsFacade.findAll()) {
                if (!(newClients.contains(c.getId()) || 
                        editedClients.contains(c.getId()))) 
                {
                    deletedClients.add(c.getId());
                    m_clientsFacade.remove(c);
                }
            }
            
            if (newClients.size() > 0) {
                JsfUtil.addSuccessMessage("update_add_sucess", 
                        newClients.size(), CLIENTS);
            }
            if (editedClients.size() > 0) {
                JsfUtil.addSuccessMessage("update_edit_sucess", 
                        editedClients.size(), CLIENTS);
            }
            if (deletedClients.size() > 0) {
                JsfUtil.addSuccessMessage("update_delete_sucess", 
                        deletedClients.size(), CLIENTS);
            }
        } catch(Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessageSummary(e, "update_error_sheet", CLIENTS);
        }
    }
    
    private void loadLinesMap() {
        List<Line> allLines = m_lineFacade.findAll();
        m_linesMap = new HashMap<String, Line>();
        for (Line line : allLines) {
            m_linesMap.put(line.getLegacyName(), line);
        }
    }
    
    private void loadCategoriesMap() {
        List<Category> allCategories = m_categoryFacade.findAll();
        m_categoriesMap = new HashMap<String, Category>();
        for (Category category : allCategories) {
            m_categoriesMap.put(category.getLine().getLegacyName()
                    + "_" + category.getLegacyName(), category);
        }
    }
    
    private Category findCategory(String lineName, String categoryName, String productIcon) {
        Category category = m_categoriesMap.get(lineName + "_" + categoryName);
        if (category != null && productIcon != null) {
            category.setImage(productIcon);
            m_categoryFacade.edit(category);
        }
        if (category == null) {
            category = new Category();
            Line line = m_linesMap.get(lineName);
            category.setLine(line);
            category.setName(line.getName());
            category.setLegacyName(categoryName);
            category.setText(capitalizeWords(categoryName));
            category.setDescription("&nbsp;");
            category.setImage(productIcon);
            m_categoryFacade.create(category);
            m_categoriesMap.put(lineName + "_" + categoryName, category);
        }
        return category;
    }
    
    private String capitalizeWords(String str) {
        StringBuffer result = new StringBuffer();
        Matcher m = Pattern.compile("([a-z])([a-z]*)", 
                Pattern.CASE_INSENSITIVE).matcher(str);
        while (m.find()) {
            m.appendReplacement(result, 
                m.group(1).toUpperCase() + m.group(2).toLowerCase());
        }
        return m.appendTail(result).toString();
    }
    
    private void updateCategories() {
        List<Category> allCategories = m_categoryFacade.findAll();
        Map<String, List<Category>> categoriesByLine = 
                new HashMap<String, List<Category>>();
        for(Category category : allCategories) {
            if (categoriesByLine.get(category.getLine().getName()) == null) {
                categoriesByLine.put(category.getLine().getName(),
                        new ArrayList<Category>());
            }
            categoriesByLine.get(category.getLine().getName()).add(category) ;
        }
        for (String lineName : categoriesByLine.keySet()) {
            int i = 2;
            for (Category category : categoriesByLine.get(lineName)) {
                if (!"all".equals(category.getName()) 
                        && !"paqs_promos".equals(category.getName())) 
                {
                    if (m_categoryFacade.getProductsListSize(category.getId()) > 0)
                    {
                        category.setName(lineName + "_" + i);
                        category.setNorder(i);
                        m_categoryFacade.edit(category);
                        i++;
                    } else {
                        m_categoryFacade.remove(category);
                    }
                }
            }
        }
    }
    
    private void processUpdateProducts(Sheet productsSheet, Sheet specsSheet) throws Exception {
        Row row;
        String productId;
        Product product;
        boolean isNew;
        byte[] smallImg;
        byte[] largeImg;
        Set<String> newProducts = new HashSet<String>();
        Set<String> editedProducts = new HashSet<String>();
        Set<String> deletedProducts = new HashSet<String>();
        int column;
        
        String currentSheet = PRODUCTS;
        try {
            loadLinesMap();
            loadCategoriesMap();
            for (int r = 1; r <= productsSheet.getLastRowNum(); r++) {
                column = 0;
                System.out.println("Processing row " + r);
                row = productsSheet.getRow(r);
                productId = getStringCellValue(getCell(row, column++));
                product = m_productFacade.find(productId);
                isNew = product == null;
                if (isNew) {
                    product = new Product();
                    product.setId(productId);
                    smallImg = getImageFromFile(productId, SMALL_IMG);
                    largeImg = getImageFromFile(productId, LARGE_IMG);
                } else {
                    for (ProductSpec pe : product.getProductSpecList()) {
                        m_productSpecFacade.remove(pe);
                    }
                    smallImg = product.getSmallPic();
                    if (smallImg == null) {
                        smallImg = getImageFromFile(productId, SMALL_IMG);
                    }
                    largeImg = product.getBigPic();
                    if (largeImg == null) {
                        largeImg = getImageFromFile(productId, LARGE_IMG);
                    }
                }
                product.setDescription(getStringCellValue(getCell(row, column++)));
                product.setBasePrice(getNumericCellValue(getCell(row, column++)));
                product.setPrice2(getNumericCellValue(getCell(row, column++)));
                product.setPrice3(getNumericCellValue(getCell(row, column++)));
                product.setPrice4(getNumericCellValue(getCell(row, column++)));
                product.setPrice5(getNumericCellValue(getCell(row, column++)));
                product.setPrice6(getNumericCellValue(getCell(row, column++)));
                product.setPrice7(getNumericCellValue(getCell(row, column++)));
                product.setPrice8(getNumericCellValue(getCell(row, column++)));
                product.setPromotion(getNumericCellValue(getCell(row, column++)));
                product.setIsPackage(getBooleanCellValue(getCell(row, column++)));
                product.setSmallPic(smallImg);
                product.setBigPic(largeImg);

                Category category = findCategory(
                        getStringCellValue(getCell(row, column++)),
                        getStringCellValue(getCell(row, column++)),
                        "I".equals(getStringCellValue(getCell(row, column++))) ? productId : null);
                product.setCategory(category);

                if (isNew) {
                    m_productFacade.create(product);
                    newProducts.add(productId);
                } else {
                    m_productFacade.edit(product);
                    editedProducts.add(productId);
                }
            }
            // Delete remaining products
            for(Product p : m_productFacade.findAll()) {
                if (!(newProducts.contains(p.getId()) || 
                        editedProducts.contains(p.getId()))) 
                {
                    deletedProducts.add(p.getId());
                    m_productFacade.remove(p);
                }
            }

            // Process specs
            currentSheet = PRODUCTS_SPECS;
            ProductSpec productSpec;
            Set<String> specsProducts = new HashSet<String>();
            for (int r = 1; r <= specsSheet.getLastRowNum(); r++) {
                row = specsSheet.getRow(r);
                productId = getStringCellValue(getCell(row, 0));
                product = m_productFacade.find(productId);
                if (product != null) {
                    productSpec = new ProductSpec();
                    productSpec.setSpec(getStringCellValue(getCell(row, 1)));
                    productSpec.setValue(getStringCellValue(getCell(row, 4)));
                    productSpec.setProduct(product);
                    m_productSpecFacade.create(productSpec);
                    specsProducts.add(productId);
                }
            }

            updateCategories();

            if (newProducts.size() > 0) {
                JsfUtil.addSuccessMessage("update_add_sucess", 
                        newProducts.size(), PRODUCTS);
            }
            if (editedProducts.size() > 0) {
                JsfUtil.addSuccessMessage("update_edit_sucess", 
                        editedProducts.size(), PRODUCTS);
            }
            if (deletedProducts.size() > 0) {
                JsfUtil.addSuccessMessage("update_delete_sucess", 
                        deletedProducts.size(), PRODUCTS);
            }
            if (specsProducts.size() > 0) {
                JsfUtil.addSuccessMessage("update_edit_sucess", 
                        specsProducts.size(), PRODUCTS_SPECS);
            }
        } catch(Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessageSummary(e, "update_error_sheet", currentSheet);
        }
    }
    
    private Double getNumericCellValue(Cell cell) {
        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            return cell.getNumericCellValue();
        } else if (Cell.CELL_TYPE_BLANK == cell.getCellType()) {
            return 0.0;
        }
        throw new IllegalArgumentException(
                JsfUtil.getMessage("update_error_cell", cell.getColumnIndex() + 1, cell.getRowIndex() + 1));
    }
    
    private boolean getBooleanCellValue(Cell cell) {
        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            return cell.getNumericCellValue() == 1;
        }
        throw new IllegalArgumentException(
                JsfUtil.getMessage("update_error_cell", cell.getColumnIndex() + 1, cell.getRowIndex() + 1));
    }
    
    private String getStringCellValue(Cell cell) {
        if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            Double dValue = cell.getNumericCellValue();
            return dValue % 1 == 0 ? 
                    String.valueOf(dValue.intValue()) : String.valueOf(dValue);
        } else if (Cell.CELL_TYPE_BLANK == cell.getCellType()) {
            return "";
        } else if (Cell.CELL_TYPE_STRING == cell.getCellType()){
            return cell.getStringCellValue();
        } else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
            return cell.getStringCellValue();
        }
        throw new IllegalArgumentException(
                JsfUtil.getMessage("update_error_cell", cell.getColumnIndex() + 1, cell.getRowIndex() + 1));
    }
    
    private byte[] getImageFromFile(String productId, String type) throws IOException  {
        byte[] imgBytes = null;
        
        String imagePath = "/restricted/images/" + type + "/" + productId.replaceAll("/", "_") + ".jpg";
        InputStream imageIs = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(imagePath);
        if (imageIs == null ){
            imagePath = "/restricted/images/" + type + "/ITC.jpg";
            System.out.println(imagePath);
            imageIs = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(imagePath);
        }
        BufferedImage bufferedImage = ImageIO.read(imageIs);
        ByteArrayOutputStream imageOs = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", imageOs);
        imgBytes = imageOs.toByteArray();
        imageOs.close();
        return imgBytes;
    }
    

}
