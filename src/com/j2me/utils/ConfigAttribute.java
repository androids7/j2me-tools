/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.utils;

import com.j2me.utils.dao.ConfigDao;
import com.j2me.utils.entity.Config;

/**
 *
 * @author willian
 */
public class ConfigAttribute {

    public static final int PARAM_HOST_SERVER = 1;
    public static final int PARAM_CHANNEL_SSL = 2;
    public static final int PARAM_APP_USER = 3;

    private String serverUrlDefault;
    private ConfigDao controleConfig;

    public ConfigAttribute() {
        this(null);
    }

    public ConfigAttribute(String serverUrlDefault) {
        this.serverUrlDefault = serverUrlDefault;
        controleConfig = new ConfigDao();
    }

    /**
     *
     * @return
     */
    public ConfigDao getConfigDao() {
        return controleConfig;
    }

    public String getValue(int param) {
        String value = "";

        Config configEntity
                = new Config(param);

        controleConfig.searchKey(configEntity);

        if (controleConfig.hasNextElement()) {
            configEntity = (Config) controleConfig.nextElement();
            value = configEntity.getValue();
        } else if (param == ConfigAttribute.PARAM_HOST_SERVER) {
            value = this.serverUrlDefault;
        } else if (param == ConfigAttribute.PARAM_CHANNEL_SSL) {
            if (this.serverUrlDefault != null
                    && !this.serverUrlDefault.equals("")) {
                if (this.serverUrlDefault.toUpperCase().startsWith("HTTPS")) {
                    value = "true";
                }
            }
        }

        return value;
    }

    public void setValue(int param, String value) {

        controleConfig.searchKey(new Config(param, value));

        if (controleConfig.hasNextElement()) {
            Config config
                    = (Config) controleConfig.nextElement();

            config.setValue(value);

            controleConfig.update(config);
        } else {
            controleConfig.insert(new Config(param, value));
        }
    }
}
