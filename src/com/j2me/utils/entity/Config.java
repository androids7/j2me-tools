/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.utils.entity;

import com.j2me.rms.Entity;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.kxml2.io.KXmlParser;

/**
 *
 * @author Willian
 */
public class Config extends Entity {

    public final static String ENTITY_NAME = "Config";

    private int param;
    private String value;

    public Config() {
        super();
    }

    public Config(int param) {
        super(new Integer(param));
        this.param = param;
    }

    public Config(int param, String value) {
        super(new Integer(param));
        this.param = param;
        this.value = value;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;

        this.setKey(new Integer(param));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getKey(byte[] bytes) {
        Integer keyRMS = null;
        try {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(bytes);
            DataInputStream dis
                    = new DataInputStream(bis);

            keyRMS = new Integer(dis.readInt());

            bis.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keyRMS;
    }

    public String toStringXML() {
        return xml;
    }

    public void parse(KXmlParser kxp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
}