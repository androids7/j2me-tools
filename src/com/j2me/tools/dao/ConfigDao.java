/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.tools.dao;

import com.j2me.rms.DAO;
import com.j2me.rms.Entity;
import com.j2me.tools.entity.Config;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Willian
 */
public class ConfigDao extends DAO {

    public ConfigDao() {
        super(Config.ENTITY_NAME);
    }

    public Entity byteToEntity(DataInputStream dataInputStream) {
        Config config = null;
        try {
            config = new Config();
            config.setKey(new Integer(dataInputStream.readInt()));
            config.setParam(dataInputStream.readInt());
            config.setValue(dataInputStream.readUTF());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return config;
    }

    public void entityToByte(DataOutputStream data, Entity entity) {
        try {
            Config config = (Config) entity;

            data.writeInt(Integer.parseInt(String.valueOf(config.getKey())));
            data.writeInt(config.getParam());
            data.writeUTF(config.getValue());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}