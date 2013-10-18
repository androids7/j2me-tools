/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.utils;

import com.j2me.rms.DAO;
import com.j2me.rms.Entity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author willian
 */
public class Config {
    public static final int PARAM_HOST_SERVER = 1;
    public static final int PARAM_CHANNEL_SSL = 2;
    public static final int PARAM_APP_USER = 3;

    private String serverUrlDefault;
    private ConfigDAO controleConfig;

    public Config(){
        this(null);
    }

    public Config(String serverUrlDefault){
        this.serverUrlDefault = serverUrlDefault;
        controleConfig = new ConfigDAO();
    }

    /**
     *
     * @return
     */
    public ConfigDAO getConfigDao(){
        return controleConfig;
    }

    public String getValue(int param){
        String value = "";

        ConfigEntity configEntity =
                new ConfigEntity(param);

        controleConfig.searchKey(configEntity);

        if(controleConfig.hasNextElement()){
            configEntity = (ConfigEntity) controleConfig.nextElement();
            value = configEntity.getValue();
        }else if(param == Config.PARAM_HOST_SERVER){
            value = this.serverUrlDefault;
        }else if(param == Config.PARAM_CHANNEL_SSL){
            if(this.serverUrlDefault != null
            && !this.serverUrlDefault.equals("")){
                if(this.serverUrlDefault.toUpperCase().startsWith("HTTPS")){
                    value = "true";
                }
            }
        }

        return value;
    }

    public void setValue(int param, String value){

        controleConfig.searchKey(new ConfigEntity(param
                                                , value));

        if(controleConfig.hasNextElement()){
            ConfigEntity config =
                    (ConfigEntity) controleConfig.nextElement();

            config.setValue(value);

            controleConfig.update(config);
        }else{
            controleConfig.insert(new ConfigEntity(param
                                                 , value));
        }
    }

    private class ConfigEntity extends Entity {
        public final static String ENTITY_NAME = "Config";

        private int param;
        private String value;

        public ConfigEntity() {
            super();
        }

        public ConfigEntity(int param) {
            super(new Integer(param));
            this.param = param;
        }

        public ConfigEntity(int param, String value) {
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
                ByteArrayInputStream bis =
                    new ByteArrayInputStream(bytes);
                DataInputStream dis =
                    new DataInputStream(bis);

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
    }

    private class ConfigDAO extends DAO {

        public ConfigDAO() {
            super(ConfigEntity.ENTITY_NAME);
        }

        protected Entity byteToEntity(DataInputStream dataInputStream) {
            ConfigEntity config = null;
            try {
                config = new ConfigEntity();
                config.setKey(new Integer(dataInputStream.readInt()));
                config.setParam(dataInputStream.readInt());
                config.setValue(dataInputStream.readUTF());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return config;
        }

        protected byte[] entityToByte(Entity entity) {
            try {
                ConfigEntity config = (ConfigEntity) entity;

                ByteArrayOutputStream byteArray =
                        new ByteArrayOutputStream(); //cria um novo byte
                DataOutputStream data =
                        new DataOutputStream(byteArray);

                data.writeInt(Integer.parseInt(String.valueOf(config.getKey())));
                data.writeInt(config.getParam());
                data.writeUTF(config.getValue());

                data.flush();

                byte[] dados = byteArray.toByteArray();

                byteArray.close();
                data.close();

                return dados;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

    }
}