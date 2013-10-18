/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.utils;

/**
 *
 * @author willian
 */
public class Conversion {

    /**
     * All folder and file names that contains special characters have to be 
     * encoded in order to be used as href URL. 
     * For more details refer to <a href="http://www.rfc-editor.org/rfc/rfc1738.txt">RFC 1738</a>. 
     * This method encodes a string to the "x-www-form-urlencoded" form, 
     * enhanced with the UTF-8-in-URL proposal.   
     * 
     * This is what happens: 
     * <ul> 
     * <li><p>The ASCII characters 'a' through 'z', 'A' through 'Z', 
     *        and '0' through '9' remain the same. 
     *         
     * <li><p>The reserved characters 
     * TODO 
     * 
     * <li><p>The unreserved characters - _ . ! ~ * ' ( ) remain the same. 
     * 
     * <li><p>The space character ' ' is converted into a plus sign '+'. 
     * 
     * <li><p>All other ASCII characters are converted into the 
     *        3-character string "%xy", where xy is 
     *        the two-digit hexadecimal representation of the character 
     *        code 
     * 
     * <li><p>All non-ASCII characters are encoded in two steps: first 
     *        to a sequence of 2 or 3 bytes, using the UTF-8 algorithm; 
     *        secondly each of these bytes is encoded as "%xx". 
     * </ul> 
     * 
     * @param url 
     * @return 
     * @see #decodeURL(String)
     */
    public static String encodeURL(String url) {

        StringBuffer sbuf = new StringBuffer();
        int len = url.length();
        int ch;
        for (int i = 0; i < len; i++) {
            ch = url.charAt(i);
            if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' 
                sbuf.append((char) ch);
            } else if ('a' <= ch && ch <= 'z') { // 'a'..'z' 
                sbuf.append((char) ch);
            } else if ('0' <= ch && ch <= '9') { // '0'..'9' 
                sbuf.append((char) ch);
            } else if (ch == ' ') { // space 
                sbuf.append("%20");
            } else if (ch == '&') {
                sbuf.append("%26");
            } else if (ch == '$' // reserved 
                    || ch == '+' || ch == ',' || ch == ':' || ch == '/' || ch == ';' || ch == '=' || ch == '?' || ch == '@') {
                sbuf.append((char) ch);
            } else if (ch == '-' || ch == '_' // unreserved 
                    || ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
                sbuf.append((char) ch);
            } else if (ch <= 0x007f) { // other ASCII 
                sbuf.append(getHTMLHex(ch));
            } else if (ch <= 0x07FF) { // non-ASCII <= 0x7FF 
                sbuf.append(getHTMLHex(0xc0 | (ch >> 6)));
                sbuf.append(getHTMLHex(0x80 | (ch & 0x3F)));
            } else { // 0x7FF < ch <= 0xFFFF 
                sbuf.append(getHTMLHex(0xe0 | (ch >> 12)));
                sbuf.append(getHTMLHex(0x80 | ((ch >> 6) & 0x3F)));
                sbuf.append(getHTMLHex(0x80 | (ch & 0x3F)));
            }
        }
        return sbuf.toString();
    }

    /** 
     * Returns the correspondent charactere to a value 
     * @param value
     * @return 
     */
    public static String getHTMLHex(int value) {
        StringBuffer result = new StringBuffer("%00");
        int digit;
        for (int i = 0; i < 2 && value != 0; i++) {
            digit = value % 16;
            value = value / 16;
            result.setCharAt(2 - i, (char) ((digit < 10) ? (digit + '0')
                    : (digit - 10 + 'a')));
        }
        return result.toString();
    }

    private static char[] map1 = new char[64];

    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            map1[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            map1[i++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            map1[i++] = c;
        }
        map1[i++] = '+';
        map1[i++] = '/';
    }

    public static String base64Encode(String in) {
        return base64Encode(in.getBytes());
    }

    public static String base64Encode(byte[] in) {
        int iLen = in.length;
        int oDataLen = (iLen * 4 + 2) / 3;// output length without padding
        int oLen = ((iLen + 2) / 3) * 4;// output length including padding
        char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        int i0;
        int i1;
        int i2;
        int o0;
        int o1;
        int o2;
        int o3;
        while (ip < iLen) {
            i0 = in[ip++] & 0xff;
            i1 = ip < iLen ? in[ip++] & 0xff : 0;
            i2 = ip < iLen ? in[ip++] & 0xff : 0;
            o0 = i0 >>> 2;
            o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        }
        return new String(out);
    }
}
