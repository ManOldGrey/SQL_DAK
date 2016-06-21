package com.ua.rat;
/**
 * Created by a.krysa on 18.05.2016.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class  Validator {
    public static Date date_Persing;
    public static float Float_Persing;
    public static int Integer_Persing;


    public boolean ValidatorDate(String sDate) {
        if ((sDate.length()> 25)) {date_Persing =null;return false;}//это не может быть дата
        if ((sDate.length()< 10)) {date_Persing =null;return false;}//это не может быть дата
        String sds = sDate.substring(0,10);

        int count = 0;
        Pattern p = Pattern.compile("[-]", Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sds);
        while(m.find()) count++;
        if (count>2) {date_Persing =null;return false;}//это не может быть дата

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat.getDateInstance(DateFormat.LONG);
        Date date_Per = null;
        try {
            date_Per = sdf.parse(sds);
            date_Persing = new java.sql.Date(date_Per.getTime());
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public boolean ValidatorDate1(String sDate) {
        if ((sDate.length()> 25)) {date_Persing =null;return false;}// & (sDate.indexOf("00:00:00.000")==0)
        if ((sDate.length()< 10)) {date_Persing =null;return false;}//это не может быть дата
        String sds = sDate.substring(0,10);

        int count = 0;
        Pattern p = Pattern.compile("[.]", Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sds);
        while(m.find()) count++;
        if (count>2) {date_Persing =null;return false;}//это не может быть дата

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat.getDateInstance(DateFormat.LONG);
        Date date_Per = null;

        try {
            date_Per = sdf.parse(sds);//sDate
            date_Persing = new java.sql.Date(date_Per.getTime());
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public boolean ValidatorFloat(String sData) {
        Float_Persing = 0;
        try {
            if (sData.indexOf(",") > 0){
                sData= sData.replace(",",".");
            }
            Float_Persing = Float.valueOf(sData).floatValue();//).parseFloat(
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public boolean ValidatorInteger(String sData) {
        Integer_Persing = 0;
        try {
            Integer_Persing = Integer.valueOf(sData);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public String ToString(String str_Name_Kol) {
        if (str_Name_Kol.indexOf("/") > 0){
            str_Name_Kol = str_Name_Kol.replace("/","_");
        }
        if (str_Name_Kol.indexOf("’") > 0){
            str_Name_Kol = str_Name_Kol.replace("’","_");
        }
        if (str_Name_Kol.indexOf("'") > 0){
            str_Name_Kol = str_Name_Kol.replace("'","_");
        }

        if (str_Name_Kol.indexOf("(") > 0){
            str_Name_Kol = str_Name_Kol.replace("(","");
        }
        if (str_Name_Kol.indexOf(")") > 0){
            str_Name_Kol = str_Name_Kol.replace(")","");
        }
        if (str_Name_Kol.indexOf("*") > 0){
            str_Name_Kol = str_Name_Kol.replace("*","");
        }
        if (str_Name_Kol.indexOf("-") > 0){
            str_Name_Kol = str_Name_Kol.replace("-","");
        }
        return str_Name_Kol;
    }

}