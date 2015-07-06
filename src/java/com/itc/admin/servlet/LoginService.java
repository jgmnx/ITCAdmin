package com.itc.admin.servlet;

import com.itc.admin.entity.Agent;
import com.itc.admin.entity.Client;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author jgmnx
 */
@WebServlet(name = "LoginService", urlPatterns = {"/LoginService"})
public class LoginService extends HttpServlet {

    @EJB
    private com.itc.admin.session.ClientFacade m_clientFacade;
    @EJB
    private com.itc.admin.session.AgentFacade m_agentFacade;

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

        String callback = request.getParameter("callback");
        String userName = request.getParameter("u");
        String password = request.getParameter("p");
        String type = request.getParameter("t");

        JSONObject json = new JSONObject();
        PrintWriter out = response.getWriter();
        
        boolean passwdCorrect = false;
        boolean active = false;
        int userId = 0;
        if ("a".equals(type)) {
            Agent agent = m_agentFacade.findByUserName(userName);
            if (agent != null) {
                passwdCorrect = agent.getPasswd().equals(password);
                active = agent.getActive();
                userId = agent.getId();
            }
        } else if ("c".equals(type)) {
            Client client = m_clientFacade.findByUserName(userName);
            if (client != null) {
                passwdCorrect = client.getPasswd().equals(password);
                active = client.getActive();
                userId = client.getId();
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            json.put("rc", passwdCorrect && active);
            json.put("id", userId);
            if (!(passwdCorrect && active)) {
                String reason = !passwdCorrect ? "Password incorrecto" : "Usuario inactivo";
                json.put("reason", reason);
            }
        } catch (Exception e) {
            json = new JSONObject();
            json.put("error", e.getMessage());
            json.put("details", ExceptionUtils.getStackTrace(e));
        } finally {
            out.write( callback + "(" + json.toJSONString() + ")");
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
