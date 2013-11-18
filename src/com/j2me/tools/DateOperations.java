/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.tools;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author willian
 */
public class DateOperations {
    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String FORMAT_DATE_API = "yyyy-MM-dd";
    public static final String FORMAT_HOUR = "kk";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_HOUR_MONTH_SECOND = "kk:mm:ss";
    public static final String FORMAT_DATE_HOUR_API = "yyyy-MM-dd kk:mm:ss";
    public static final String FORMAT_DATE_HOUR_PT_BR = "dd/MM/yyyy kk:mm:ss";
    public static final String FORMAT_DATE_HOUR_VIEW = "dd.MM.yyyy às kkhmm";
    public static final String FORMAT_DATE_MATCH_VIEW = "DAY_WEEK, DAY MONTH YEAR";
    
    public static String format(java.util.Date date, String pattern){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String day_week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        
        String result = "";
        
        if(FORMAT_DATE_HOUR_VIEW.equals(pattern)){
            result = day + "." + month + "." + year + " às " + hour + "h" + minute;
        }
        else{
            result = day + "/" + month + "/" + year + " " + hour + ":" + minute + ":" + second;
        }
                
        return result;
    }
    
    public static String format(String date, String pattern){
        return format(transformaEmData(date), pattern);
    }
   
    public static java.util.Date transformaEmData(String d) {   
       //2008-05-10T19:42:28.703
        String ano, mes, dia;
        int nMes;

        ano = d.substring(12, 16);   
        mes = d.substring(8, 11);  
        nMes = getMonthByAbbreviation(mes);
        dia = d.substring(5, 7);
        
        Calendar cal = Calendar.getInstance();   

        cal.setTime(new java.util.Date());   

        cal.set(Calendar.YEAR, Integer.parseInt(ano));   
        cal.set(Calendar.MONTH, nMes);   
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));   

        return cal.getTime();
    }
    
    public static int getMonthByAbbreviation(String abbreviation){
        String ab = abbreviation.toLowerCase();
        if( ab.equals("jan")) return 0;
        if( ab.equals("feb")) return 1;
        if( ab.equals("mar")) return 2;
        if( ab.equals("apr")) return 3;
        if( ab.equals("may")) return 4;
        if( ab.equals("jun")) return 5;
        if( ab.equals("jul")) return 6;
        if( ab.equals("aug")) return 7;
        if( ab.equals("sep")) return 8;
        if( ab.equals("oct")) return 9;
        if( ab.equals("nov")) return 10;
        if( ab.equals("dec")) return 11;
        else return 0;
    }
    
    public static java.util.Date transformaEmDataHora(String d) {   
        //2008-05-10T19:42:28.703
        String ano, mes, dia, hora, minuto;   
            
        ano = d.substring(0, 4);   
        mes = d.substring(5, 7);   
        dia = d.substring(8, 10);
        hora = d.substring(11, 13);
        minuto = d.substring(14, 16);
            
        Calendar cal = Calendar.getInstance();   
            
        cal.setTime(new java.util.Date());
            
        cal.set(Calendar.YEAR, Integer.parseInt(ano));   
        cal.set(Calendar.MONTH, (Integer.parseInt(mes)-1));   
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));   
        //cal.set(Calendar.HOUR, Integer.parseInt(hora));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora));
        cal.set(Calendar.MINUTE, Integer.parseInt(minuto));
            
        return cal.getTime();
    }
    
    public static java.util.Date getTime(String d) {   
        //2008-05-10T19:42:28.703
        String ano, mes, dia, hora, minuto, segundo;   
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(new java.util.Date());
            
        ano = d.substring(0, 4);   
        mes = d.substring(5, 7);   
        dia = d.substring(8, 10);
        
        if(d.length() > 10){
            hora = d.substring(11, 13);
            minuto = d.substring(14, 16);
            segundo = d.substring(17, 19);
            
            cal.set(Calendar.YEAR, Integer.parseInt(ano));   
            cal.set(Calendar.MONTH, (Integer.parseInt(mes)-1));   
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));   
            
            if(d.length() > 10){
                hora = d.substring(11, 13);
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora));
                
                if(d.length() > 13){
                    minuto = d.substring(14, 16);
                    cal.set(Calendar.MINUTE, Integer.parseInt(minuto));
                    
                    if(d.length() > 16){
                        segundo = d.substring(17, 19);
                        cal.set(Calendar.SECOND, Integer.parseInt(segundo));
                    }else{
                        cal.set(Calendar.SECOND, 0);
                    }
                }else{
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                }
            }else{
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
            }
        }else{
            cal.set(Calendar.YEAR, Integer.parseInt(ano));   
            cal.set(Calendar.MONTH, (Integer.parseInt(mes)-1));   
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));   
            
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
            
        return cal.getTime();
    }
    
    public static String formataScreenData(String d) {
        //2008-05-10T19:42:28.703
        String data="";
            
        if(d.length() >= 16){
            data = d.substring(8,10)+"/"+d.substring(5, 7) + " " + d.substring(11, 13) + ":" + d.substring(14, 16);
        }else if(d.length() == 10){
            data = d.substring(8,10)+"/"+d.substring(5, 7);
        }
        
        return data;
    }
    
    public static String formateDotDate(String d) {
        //2008-05-10T19:42:28.703
        String data="";
            
        if(d.length() >= 16){
            data = d.substring(8,10)+"."+d.substring(5, 7) +"."+d.substring(0, 4) + " às " + d.substring(11, 13) + ":" + d.substring(14, 16);
        }else if(d.length() == 10){
            data = d.substring(8,10)+"."+d.substring(5, 7) +"."+d.substring(0, 4);
        }
        
        return data;
    }
    
    public static String formataData(java.util.Date d) {
        return formataData(d, false);
    }

    public static String formataData(java.util.Date d, boolean ptBR) {
        String ret = "";
        String ano, mes, dia;

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        ano = Integer.toString(cal.get(Calendar.YEAR));
        mes = Integer.toString((cal.get(Calendar.MONTH)+1));
        dia = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        mes = (mes.length() == 1 ? "0" : "") + mes;
        dia = (dia.length() == 1 ? "0" : "") + dia;

        if(ptBR){
            ret = dia + "/" + mes + "/" + ano;
        }else{
            ret = ano + "/" + mes + "/" + dia;
        }

        return ret;
    }

    public static String formataDataHora(java.util.Date d, boolean horaScreen) {
        String ret = formataData(d);   
        String hora, minuto;
           
        Calendar cal = Calendar.getInstance();   
        cal.setTime(d);           
        
        //hora = Integer.toString(cal.get(Calendar.HOUR));
        hora = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        minuto = Integer.toString(cal.get(Calendar.MINUTE));   
  
        hora = (hora.length() == 1 ? "0" : "") + hora;
        minuto = (minuto.length() == 1 ? "0" : "") + minuto;
  
        if(horaScreen){
            ret = ret.substring(8, 9) + "/" + ret.substring(5, 6) + " " + hora + ":" + minuto;
        }else{
            ret = ret + " " + hora + ":" + minuto;
        }
           
        return ret;   
    }
    
    public static String formataDataHora(java.util.Date d) {   
        return formataDataHora(d, false);
    }
    
    public static java.util.Date adicionaDiaNaData(java.util.Date data, long dias) {   
        long dataRecebida = data.getTime();

        if(dias > 0){
            dataRecebida = dataRecebida + (86400000L * dias);
        }else if(dias < 0){
            dataRecebida = dataRecebida - (86400000L * (dias*-1));            
        }

        return new java.util.Date(dataRecebida);
    }

    public static String toString(String dateNoSeparator) {
        String ret = "";
        String ano, mes, dia, hora, minuto, segundo;

        ano = dateNoSeparator.substring(0, 4);
        mes = dateNoSeparator.substring(4, 6);
        dia = dateNoSeparator.substring(6, 8);
        hora = dateNoSeparator.substring(8, 10);
        minuto = dateNoSeparator.substring(10, 12);
        segundo = dateNoSeparator.substring(12, 14);

        ret = ano + "/" + mes + "/" + dia;
        ret = ret + " " + hora + ":" + minuto + ":" + segundo;

        return ret;
    }

    public static String toString(java.util.Date d) {
        return toString(d, true);
    }

    public static String toString(java.util.Date d, boolean usingSeparator) {
        String ret = "";   
        String ano, mes, dia, hora, minuto, segundo;
           
        Calendar cal = Calendar.getInstance();   
        cal.setTime(d);           
           
        ano = Integer.toString(cal.get(Calendar.YEAR));   
        mes = Integer.toString((cal.get(Calendar.MONTH)+1));   
        dia = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        
        //hora = Integer.toString(cal.get(Calendar.HOUR));
        hora = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        minuto = Integer.toString(cal.get(Calendar.MINUTE));
        segundo = Integer.toString(cal.get(Calendar.SECOND));
  
        mes = (mes.length() == 1 ? "0" : "") + mes;   
        dia = (dia.length() == 1 ? "0" : "") + dia;
  
        hora = (hora.length() == 1 ? "0" : "") + hora;   
        minuto = (minuto.length() == 1 ? "0" : "") + minuto;
        segundo = (segundo.length() == 1 ? "0" : "") + segundo;

        if(usingSeparator){
            ret = ano + "/" + mes + "/" + dia;
            ret = ret + " " + hora + ":" + minuto + ":" + segundo;
        }else{
            ret = ano + mes + dia;
            ret = ret + hora + minuto + segundo;
        }
           
        return ret; 
    }
    
    public static int compare(String data1, String data2) throws Exception{
        int ret = -2;
        long ddData1;
        long ddData2;
        
        if(data1.indexOf('/') >= 0){
            data1 = data1.replace('/','-');
        }
        
        if(data2.indexOf('/') >= 0){
            data2 = data2.replace('/','-');
        }
        
        if(data1.equals(data2)){
            ret = 0;
        }else{
            ddData1 = getTime(data1).getTime();
            ddData2 = getTime(data2).getTime();
        
            if(ddData1 < ddData2){
                ret = -1;
            }else if(ddData1 > ddData2){
                ret = 1;
            }
        }
        
        if(ret != -2){
            return ret;
        }else{
            throw new Exception("Bad Date Format");
        }
    }
    
    public static String getMonthDescription(int month){
        switch(month){
            case 1:
                return "Jan";
            case 2:
                return "Fev";
            case 3:
                return "Mar";
            case 4:
                return "Abr";
            case 5:
                return "Mai";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Ago";
            case 9:
                return "Set";
            case 10:
                return "Out";
            case 11:
                return "Nov";
            case 12:
                return "Dez";
            default:
                return "";
        }
    }
    
    public static int getYear(String data){
        String ano;
        
        ano = data.substring(0, 4);   
        
        return Integer.parseInt(ano);
    }
    
    public static int getMonth(String data){
        String mes;
        
        mes = data.substring(5, 7);
        
        return Integer.parseInt(mes);
    }
    
    public static int getDay(String data){
        String dia;
        
        dia = data.substring(8, 10);
        
        return Integer.parseInt(dia);
    }

    public static java.util.Date transformaEmDataHoraCompleta(String d) {
        if(d != null && d.length() == 19){
            String ano, mes, dia, hora, minuto, segundo;

            ano = d.substring(0, 4);
            mes = d.substring(5, 7);
            dia = d.substring(8, 10);
            hora = d.substring(11, 13);
            minuto = d.substring(14, 16);
            segundo = d.substring(17, 19);

            Calendar cal = Calendar.getInstance();

            cal.setTime(new java.util.Date());

            cal.set(Calendar.YEAR, Integer.parseInt(ano));
            cal.set(Calendar.MONTH, (Integer.parseInt(mes)-1));
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
            //cal.set(Calendar.HOUR, Integer.parseInt(hora));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora));
            cal.set(Calendar.MINUTE, Integer.parseInt(minuto));
            cal.set(Calendar.SECOND, Integer.parseInt(segundo));

            return cal.getTime();
        }else{
            return null;
        }
    }

    public static String formataDataHoraCompleta(java.util.Date d) {
        return formataDataHoraCompleta(d, false);
    }

    public static String formataDataHoraCompleta(java.util.Date d, boolean ptBR) {
        String ret = formataData(d, ptBR);
        String hora, minuto, segundo;

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        //hora = Integer.toString(cal.get(Calendar.HOUR));
        hora = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        minuto = Integer.toString(cal.get(Calendar.MINUTE));
        segundo = Integer.toString(cal.get(Calendar.SECOND));

        hora = (hora.length() == 1 ? "0" : "") + hora;
        minuto = (minuto.length() == 1 ? "0" : "") + minuto;
        segundo = (segundo.length() == 1 ? "0" : "") + segundo;

        ret = ret + " " + hora + ":" + minuto + ":" + segundo;

        return ret;
    }
}
