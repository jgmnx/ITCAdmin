package com.itc.admin.servlet;

import com.itc.admin.entity.ImageCatalog;
import com.itc.admin.entity.Product;
import com.itc.admin.session.ImageCatalogFacade;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jgmnx
 */
@WebServlet(urlPatterns = {"/getImage/*"}, asyncSupported = true)
public class GetImageService extends HttpServlet {

    private static final int DEFAULT_BUFFER_SIZE = 10 * 1024;  // 10KB

    @EJB
    private com.itc.admin.session.ProductFacade m_productFacade;
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

        AsyncContext asyncContext = request.startAsync();
        asyncContext.addListener(new MyAsyncListener());
        asyncContext.setTimeout(0);
        
        ((ThreadPoolExecutor)request.getServletContext().getAttribute("myAsyncThreadPool"))
                .execute(new AsyncGetImageWorker(asyncContext));
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    }// </editor-fold>
    
    private class AsyncGetImageWorker implements Runnable {

        private final AsyncContext m_asyncContext;
        
        private AsyncGetImageWorker(AsyncContext asyncContext) {
            m_asyncContext = asyncContext;
        }
        
        @Override
        public void run() {
            HttpServletRequest request = (HttpServletRequest)m_asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse)m_asyncContext.getResponse();
            try {
                String type = request.getParameter("type");
                String id = request.getParameter("id");
                if ((!"product".equals(type) || !"new_product".equals(type) || !"promo".equals(type)) &&
                        (id == null || id.isEmpty())) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                byte[] imgBytes = null;
                String imgName = null;
                if ("product".equals(type)) {
                    String size = request.getParameter("size");
                    if (size == null || !("small".equals(size) || "large".equals(size))) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    Product product = m_productFacade.find(id);
                    if (product == null || ("small".equals(size) && product.getSmallPic() == null)
                            || ("large".equals(size) && product.getBigPic() == null)) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    imgBytes = "small".equals(size) ? product.getSmallPic() : product.getBigPic();
                    imgName = product.getId().toUpperCase().replace("/", "_");
                } else if ("new_product".equals(type)) {
                    try {
                        ImageCatalog newProductImage = m_imageCatalogFacade.findByTypeOrder("NEW", Integer.valueOf(id));
                        imgBytes = newProductImage.getImage();
                        imgName  = "" + newProductImage.getOrder();
                    } catch(javax.persistence.NoResultException nre) {
                        nre.printStackTrace();
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                } else if ("promo".equals(type)) {
                    try {
                        ImageCatalog promoImage = m_imageCatalogFacade.findByTypeOrder("PROMO", Integer.valueOf(id));
                        imgBytes = promoImage.getImage();
                        imgName  = "" + promoImage.getOrder();
                    } catch(javax.persistence.NoResultException nre) {
                        nre.printStackTrace();
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                try {

                    bis = new BufferedInputStream(new ByteArrayInputStream(imgBytes), DEFAULT_BUFFER_SIZE);
                    bos = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

                    response.reset();
                    response.setBufferSize(DEFAULT_BUFFER_SIZE);
                    response.setContentType("image/jpg");
                    response.setContentLength(imgBytes.length);
                    response.setHeader("Content-Length", String.valueOf(imgBytes.length));
                    response.setHeader("Content-Disposition", "inline; filename=\"" + imgName + "\"");

                    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                    int length;
                    while ((length = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, length);
                    }
                    response.flushBuffer();
                    response.getOutputStream().flush();
                } finally {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                }
                
            } catch(IOException io) {
                throw new RuntimeException(io);
            } finally {
                m_asyncContext.complete();
            }
        }
        
    }
    
    
    

}
