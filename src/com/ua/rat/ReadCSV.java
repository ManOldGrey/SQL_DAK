package com.ua.rat;
/**
 * Created by a.krysa on 18.05.2016.
 */

import java.io.IOException;
import java.nio.charset.*;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;

public class ReadCSV {
    public Map<String, Integer> hmHand = new HashMap<String, Integer>();
    public String[] Had;// = new String[100];
    public CsvReader products;
    public int  int_countRecord=0;

public CsvReader Init_Reader(FirstFrame cont) throws IOException {
    String argsPath =cont.a5;
    try {
        Charset cs1 = java.nio.charset.Charset.forName("CP1251");

        //';', Charset.forName("Cp1250")argsPath

        products = new CsvReader(argsPath,';' ,cs1 );
        return products;

    } catch (IOException e) {
        e.printStackTrace();
        cont.Stat = 1;  //error
        Wind_Alert Wind_Alert = new Wind_Alert();
        Wind_Alert.init(e.getMessage());

//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
    }
    return products;
}

    public void mainReader(FirstFrame cont) throws IOException {
        products = Init_Reader(cont);
        products.readHeaders();

    }
    public void main(FirstFrame cont) throws IOException {

        products = Init_Reader(cont);

        products.readHeaders();
            Integer int_CountHead = products.getHeaderCount();//getColumnCount();

            Had = new String[int_CountHead];

            Validator valid = new Validator();

            //прочитаем шапку и создадим таблицу
            for (int k = 0; k < int_CountHead; k++) {
                String str_Name_Head = valid.ToString(products.getHeader(k));
                Had[k]=str_Name_Head;
                hmHand.put(str_Name_Head, 0);
            };

            //проанализируем первую строку бы распарсить дату, число и строку по колонкам.
            //0 - строка, 1 - число, 2 - дата
            int quit =0;
            while (products.readRecord()) {
                int_countRecord = 1;
                for (int k = 0; k < int_CountHead; k++) {
                    String str_Name_Head = products.get(k);
                    String str_Name_Kol = Had[k].toString();
                    switch (str_Name_Head) {
                        case "":
                            int_countRecord = 0;//ошибка в данных не грузим
                            break;
                        case "F"://Float
                            hmHand.put(str_Name_Kol, 1);
                            break;
                        case "S"://String
                            hmHand.put(str_Name_Kol, 0);
                            break;
                        case "D"://Date
                            hmHand.put(str_Name_Kol, 2);
                            break;
                        default:
                            int_countRecord = 0;//ошибка в данных не грузим
                            break;
                    }
                }
                break;
            }
            if (int_countRecord==1) {
                //получим общее количество
                while (products.readRecord()) {
                    quit++;
                }
                int_countRecord = quit++;
                //System.out.println(quit);
                products.close();
                //int_countRecord =0;
            }

//            //проанализируем первых 1000 записей что бы распарсить дату, число и строку по колонкам.
//            Validator valid = new Validator();
//            //0 - строка, 1 - число, 2 - дата
//            int quit =0;
//            while (products.readRecord()) {
//                quit ++;
//                for (int k = 0; k < int_CountHead; k++) {
//
//                    String str_Name_Head = products.get(k);
//                    if (str_Name_Head == "") {continue;}//если пустое значение - не проверяем
//                    //текущее значение проверим на дату
//
//                    boolean boolValid = false;
//                    boolValid = valid.ValidatorDate(str_Name_Head);
//
//                    if (boolValid) {//это дата
//                        //получим имя колонки по номеру
//                        String str_Name_Kol = Had[k].toString();
//                        int int_znach = hmHand.get(str_Name_Kol);
//                        if (int_znach == 0) {
//                            hmHand.put(str_Name_Kol, 2);
//                            continue;
//                        }
//                    } else {
//                        //  boolean boolValid1 = false;
//                        boolValid = valid.ValidatorDate1(str_Name_Head);
//                        if (boolValid) {//это дата
//                            String str_Name_Kol = Had[k].toString();
//                            int int_znach = hmHand.get(str_Name_Kol);
//                            if (int_znach == 0) {
//                                hmHand.put(str_Name_Kol, 2);
//                                continue;
//                            }
//                        }
//                    }
//
//                    boolValid = valid.ValidatorFloat(str_Name_Head);
//
//                    if (boolValid) {//это число
//                        //получим имя колонки по номеру
//                        String str_Name_Kol = Had[k].toString();
//                        int int_znach = hmHand.get(str_Name_Kol);
//                        if (int_znach == 0) {
//                            hmHand.put(str_Name_Kol, 1);
//                        }
//                    }else {
//                        String str_Name_Kol = Had[k].toString();
//                        int int_znach = hmHand.get(str_Name_Kol);
//                        if (int_znach == 1) {//было число но преобразовать текущую не смогли
//                            hmHand.put(str_Name_Kol, 5);//ставим строку принудительно
//                        }
//
//                    }
//
//                }
//                if (quit > 10000){
//                    break;
//                }
//            }
//            while (products.readRecord()) {
//                quit ++;
//            }
//            int_countRecord = quit;
//            products.close();
//
//        //пробежимся еще раз что бы корректно проанализировать числовые колонки
//        products = Init_Reader(cont);
//
//        products.readHeaders();
//        int_CountHead = products.getHeaderCount();
//
//        //0 - строка, 1 - число, 2 - дата
//        quit =0;
//        while (products.readRecord()) {
//            quit ++;
//            for (int k = 0; k < int_CountHead; k++) {
//
//                String str_Name_Head = products.get(k);
//                //текущее значение проверим на дату
//                String str_Name_Kol = Had[k].toString();
//                int int_znach = hmHand.get(str_Name_Kol);
//                if (int_znach == 1) {
//                    if (str_Name_Head == ""){
//                        continue;
//                    }
//                    Boolean boolValid = false;
//                    boolValid = valid.ValidatorFloat(str_Name_Head);
//
//                    if (!boolValid) {//это число
//                        hmHand.put(str_Name_Kol, 0);//это все же не число
//                    }
//                }
//            }
//            if (quit > 1000){
//                break;
//            }
//        }
//
//        products.close();


    }

}
