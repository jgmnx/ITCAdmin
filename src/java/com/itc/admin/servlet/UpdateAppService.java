package com.itc.admin.servlet;

import com.itc.admin.entity.Agent;
import com.itc.admin.entity.Category;
import com.itc.admin.entity.Client;
import com.itc.admin.entity.ImageCatalog;
import com.itc.admin.entity.Line;
import com.itc.admin.entity.Product;
import com.itc.admin.entity.ProductSpec;
import com.itc.admin.session.ImageCatalogFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jgmnx
 */
@WebServlet(name = "UpdateAppService", urlPatterns = {"/UpdateAppService"})
public class UpdateAppService extends HttpServlet {

    @EJB
    private com.itc.admin.session.LineFacade m_lineFacade;
    @EJB
    private com.itc.admin.session.CategoryFacade m_categoryFacade;
    @EJB
    private com.itc.admin.session.ClientFacade m_clientFacade;
    @EJB
    private com.itc.admin.session.AgentFacade m_agentFacade;
    @EJB
    private com.itc.admin.session.ProductFacade m_productFacade;
    @EJB
    private com.itc.admin.session.ProductSpecFacade m_productSpecFacade;
    @EJB
    private ImageCatalogFacade m_imageCatalogFacade;
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String sUserId = request.getParameter("u");
        String type = request.getParameter("t");
        
        JSONObject db = new JSONObject();
        PrintWriter out = response.getWriter();
        try {
            if (sUserId == null || sUserId.isEmpty() || type == null || type.isEmpty()) {
                db = new JSONObject();
                db.put("error", "Invalid input");
                db.put("details", "Datos invalidos");
            } else {
                Integer userId = Integer.valueOf(sUserId);
                db.put("lines", getLines());
                db.put("categories", getCategories());
                db.put("clients", getClients(userId, type));
                db.put("agents", getAgents(userId, type));
                db.put("products", getProducts());
                db.put("product_specs", getProductSpecs());
                db.put("new_products", getCatalogNewProducts());
                db.put("promos", getCatalogPromos());
            }
        } catch (Exception e) {
            db = new JSONObject();
            db.put("error", e.getMessage());
            db.put("details", ExceptionUtils.getStackTrace(e));
        } finally {
            out.write(db.toJSONString());
        }
    }

    private JSONArray getLines() {
        List<Line> allLines = m_lineFacade.findAll();
        JSONArray jsonArray = new JSONArray();
        for (Line line : allLines) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", line.getId());
            jsonObj.put("type", "product_line");
            jsonObj.put("name", line.getName());
            jsonObj.put("text", line.getText());
            jsonObj.put("description", line.getDescription());
            jsonObj.put("image", line.getImage());
            jsonObj.put("order", line.getNorder());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private JSONArray getCategories() {
        List<Category> allCategories = m_categoryFacade.findAll();
        JSONArray jsonArray = new JSONArray();
        for (Category category : allCategories) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", category.getId());
            jsonObj.put("type", "product_line_category");
            jsonObj.put("name", category.getName());
            jsonObj.put("line", category.getLine().getId());
            jsonObj.put("text", category.getText());
            jsonObj.put("description", category.getDescription());
            jsonObj.put("image", category.getImage());
            jsonObj.put("order", category.getNorder());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private JSONArray getClients(Integer userId, String type) {
        List<Client> clients = null;
        
        if ("a".equals(type.toLowerCase())) {
            if (m_agentFacade.find(userId).getSuperuser()) {
                clients = m_clientFacade.findAll();
            } else {
                clients = m_clientFacade.findByAgentId(userId);
            }
        } else if ("c".equals(type.toLowerCase())){
            clients = new ArrayList<Client>();
            clients.add(m_clientFacade.find(userId));
        }
        JSONArray jsonArray = new JSONArray();
        for (Client client : clients) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", client.getId());
            jsonObj.put("user", client.getUsername());
            jsonObj.put("passwd", client.getPasswd());
            jsonObj.put("name", StringEscapeUtils.escapeHtml(client.getName()));
            jsonObj.put("agent_id", client.getAgent().getId());
            jsonObj.put("price_type", client.getPriceList());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private JSONArray getAgents(Integer userId, String type) {
        List<Agent> agents = new ArrayList<Agent>();
        
        if ("a".equals(type.toLowerCase())) {
            //agents.add(m_agentFacade.find(userId));
            agents.addAll(m_agentFacade.findAll());
        }
        JSONArray jsonArray = new JSONArray();
        for (Agent agent : agents) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", agent.getId());
            jsonObj.put("user", agent.getUsername());
            jsonObj.put("passwd", agent.getPasswd());
            jsonObj.put("super", agent.getSuperuser());
            jsonObj.put("name", agent.getName());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private JSONArray getProducts() {
        List<Product> products = m_productFacade.findAll();
        JSONArray jsonArray = new JSONArray();
        for (Product product : products) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", product.getId());
            jsonObj.put("name", product.getDescription());
            jsonObj.put("line", product.getCategory().getLine().getId());
            jsonObj.put("category", product.getCategory().getId());
            jsonObj.put("base_price", product.getBasePrice());
            jsonObj.put("price_2", product.getPrice2());
            jsonObj.put("price_3", product.getPrice3());
            jsonObj.put("price_4", product.getPrice4());
            jsonObj.put("price_5", product.getPrice5());
            jsonObj.put("price_6", product.getPrice6());
            jsonObj.put("price_7", product.getPrice7());
            jsonObj.put("price_8", product.getPrice8());
            jsonObj.put("promotion", product.getPromotion());
            jsonObj.put("is_package", (product.getIsPackage() != null && product.getIsPackage()) ? 1 : 0);
            jsonObj.put("chksum_small", product.getChecksumSmallPic());
            jsonObj.put("chksum_big", product.getChecksumBigPic());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private JSONArray getProductSpecs() {
        List<ProductSpec> productSpecs = m_productSpecFacade.findAll();
        JSONArray jsonArray = new JSONArray();
        for (ProductSpec productSpec : productSpecs) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("product_id", productSpec.getProduct().getId());
            jsonObj.put("spec", productSpec.getSpec());
            jsonObj.put("value", productSpec.getValue());
            jsonArray.add(jsonObj);
        }
        return jsonArray; 
    }
    
    private JSONArray getCatalogNewProducts() {
        List<ImageCatalog> allNewProductsImages = m_imageCatalogFacade.findAllNewProducts();
        JSONArray jsonArray = new JSONArray();
        for (ImageCatalog newProductImage : allNewProductsImages) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("order", newProductImage.getOrder());
            jsonObj.put("chksum", newProductImage.getChecksum());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }
    
    private JSONArray getCatalogPromos() {
        List<ImageCatalog> allPromoImages = m_imageCatalogFacade.findAllPromos();
        JSONArray jsonArray = new JSONArray();
        for (ImageCatalog promoImages : allPromoImages) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("order", promoImages.getOrder());
            jsonObj.put("chksum", promoImages.getChecksum());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
