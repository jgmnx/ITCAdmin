package com.itc.admin.servlet;

import com.itc.admin.entity.Product;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jgmnx
 */
@WebServlet("/getImage/*")
public class GetImageService extends HttpServlet {

    private static final int DEFAULT_BUFFER_SIZE = 10 * 1024;  // 10KB

    @EJB
    private com.itc.admin.session.ProductFacade m_productFacade;

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

        String type = request.getParameter("type");
        String id = request.getParameter("id");
        if (!"product".equals(type) || "promo".equals(type) || "news".equals(type) || (id == null || id.isEmpty())) {
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

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
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

}
