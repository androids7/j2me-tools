/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.tools;

import javax.microedition.lcdui.StringItem;

/**
 *
 * @author willian
 */
public class StringGaugeProcess extends StringItem {

    public StringGaugeProcess(String text) {
        super(null, text);
        setLayout(StringItem.LAYOUT_CENTER | StringItem.LAYOUT_EXPAND);
    }

    public void setString(String string) {
        //setText(string);
    }

    public void setStringPercente(String stringPercente) {
        //setLabel(stringPercente);
    }

}
