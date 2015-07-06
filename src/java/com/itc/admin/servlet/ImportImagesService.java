/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.servlet;

import com.itc.admin.entity.Product;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jgmnx
 */
@WebServlet(name = "ImportImagesService", urlPatterns = {"/restricted/ImportImagesService"})
public class ImportImagesService extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet ImportImagesService</title>");
        out.println("</head>");
        out.println("<body>");

        List<Product> products = m_productFacade.findAll();
        out.println("<ul>");
        for(Product product : products) {
            out.println("<li>" + setImage(product, request, "small") + "</li>");
            out.println("<li>" + setImage(product, request, "large") + "</li>");
        }
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }

    private String setImage(Product product, HttpServletRequest request, String type) throws IOException  {
        String msg = type + ": " + product.getId();
        String imagePath = "/restricted/images/" + type + "/" + product.getId().replaceAll("/", "_") + ".jpg";
        InputStream imageIs = request.getServletContext().getResourceAsStream(imagePath);
        if (imageIs == null ){
            msg += " not found!!!";
        } else {
            BufferedImage bufferedImage = ImageIO.read(imageIs);
            ByteArrayOutputStream imageOs = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", imageOs);
            byte[] imgBytes = imageOs.toByteArray();
            imageOs.close();
            if ("small".equals(type)) {
                product.setSmallPic(imgBytes);
            } else {
                product.setBigPic(imgBytes);
            }
            m_productFacade.edit(product);
            msg += " saved correctly!!!";
        }
        return msg;
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
