/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itc.admin.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

/**
 *
 * @author jgmnx
 */
public class ImageHelper {
    
    private static String getFilePathName(String path, String name) 
            throws MalformedURLException, URISyntaxException 
    {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String realPath = ec.getRealPath(path);
        return realPath + File.separator + name.replaceAll("/", "_") + ".jpg";
    }
    
    public static void writeImage(byte[] imgBytes, String path, String name) throws IOException, MalformedURLException, URISyntaxException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
        BufferedImage bufferedImg = ImageIO.read(bais);
        String filePathName = getFilePathName(path, name);
        System.out.println("Writing image to " + filePathName);
        File file = new File(filePathName);
        if (file.exists()) {
            file.delete();
        }
        ImageIO.write(bufferedImg, "jpeg", file);
    }
    
}
