/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.tools.media;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author hugocampos
 */
public class Image {
    
    public Image(){}
    
    public javax.microedition.lcdui.Image load(String url) throws IOException {
        HttpConnection conn = null;
        InputStream dis = null;
        try {
            
            conn = (HttpConnection) Connector.open(url, Connector.READ, false);
            conn.setRequestMethod( HttpConnection.GET);

            int rc = conn.getResponseCode();
            if (rc != HttpConnection.HTTP_OK) {
                throw new Exception("ResponseCode = " + rc);
            }

            byte []b = new byte[(int) conn.getLength()]; 
            
            dis = conn.openInputStream();
            dis.read(b);
            dis.close();            
            conn.close();

            javax.microedition.lcdui.Image img = javax.microedition.lcdui.Image.createImage(b, 0, b.length);
            
            return img;

        } catch (Exception e) {
            return null;
        }
    }
    
}
