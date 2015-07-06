package com.itc.admin.view;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static void addErrorMessageSummary(Exception ex, String summaryMsgId, Object... args) {
        String summaryMsg = getMessage(summaryMsgId, args);
        String msg = ex.getLocalizedMessage();
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summaryMsg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static void addErrorMessage(Exception ex, String msgId, Object... args) {
        String summaryMsg = getMessage(msgId, args);
        String msg = ex.getLocalizedMessage();
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summaryMsg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static void addErrorMessageSummary(String summaryMsgId, String msgId, Object... args) {
        String summaryMsg = getMessage(summaryMsgId, args);
        String msg = getMessage(msgId, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summaryMsg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addErrorMessage(String msgId, Object... args) {
        String summaryMsg = getMessage("global_error_msg");
        String message = getMessage(msgId, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summaryMsg, message);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static void addSuccessMessageSummary(String summaryMsgId, String msgId, Object... args) {
        String summaryMsg = getMessage(summaryMsgId, args);
        String message = getMessage(msgId, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, summaryMsg, message);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addSuccessMessage(String msgId, Object... args) {
        String summaryMsg = getMessage("global_success_msg");
        String message = getMessage(msgId, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, summaryMsg, message);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }
    
    public static String getMessage(String msgId, Object... args) {
        ResourceBundle rb = ResourceBundle.getBundle("com.itc.admin.i18n.Bundle");
        return MessageFormat.format(rb.getString(msgId), args);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }
}
