/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.tools;

/**
 *
 * @author Willian
 */
public class StringOperations {

    public static java.lang.String replace(java.lang.String needle, java.lang.String replacement, java.lang.String haystack) {
        java.lang.String result = "";
        int index = haystack.indexOf(needle);
        if (index == 0) {
            result = replacement + haystack.substring(needle.length());
            return replace(needle, replacement, result);
        } else if (index > 0) {
            result = haystack.substring(0, index) + replacement + haystack.substring(index + needle.length());
            return replace(needle, replacement, result);
        } else {
            return haystack;
        }
    }

    public static boolean isNullorEmpty(java.lang.String value) {
        boolean nullorempty = true;

        if (value != null && !value.equals("")) {
            nullorempty = false;
        }
        
        return nullorempty;
    }
}
