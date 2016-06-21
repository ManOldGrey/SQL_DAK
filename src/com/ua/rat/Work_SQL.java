package com.ua.rat;
/**
 * Created by a.krysa on 18.05.2016.
 */

import com.csvreader.CsvReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Map;

public class Work_SQL {

    private static Statement stmt;
    private static ResultSet rs;
    private static Connection con = null;
    private static  Validator valid = new Validator();

    public static void Conect(FirstFrame cont, String command)
    {

        String login = cont.toStringParam(1);
        String pass = cont.toStringParam(2);
        String SQLServ = cont.toStringParam(3);
        String SQLPort = cont.toStringParam(4);
        String connectionUrl = "";

        Boolean boolValid = valid.ValidatorInteger(SQLPort);
        if (boolValid) {//это число - номер порта
            //int PortSQL = valid.Integer_Persing;
        }

        if (command == "list"){

            if (boolValid) {//это число - номер порта
                //connectionUrl = "jdbc:sqlserver://kvss9:1433;databaseName=temp;integratedSecurity=false;user=dak;password="; instanceName
                //54709
                if (login.length()==0){
                    connectionUrl = "jdbc:sqlserver://"+SQLServ+":"+SQLPort+";integratedSecurity=true";//integratedSecurity=false;user="+login+";password="+pass;
                }
                else
                {
                    connectionUrl = "jdbc:sqlserver://"+SQLServ+":"+SQLPort+";integratedSecurity=false;user="+login+";password="+pass;

                }
            }
            else
            {
                if (login.length()==0){
                    connectionUrl = "jdbc:sqlserver://" + SQLServ + ";instanceName=" + SQLPort + ";integratedSecurity=true";

                }
                else {
                    connectionUrl = "jdbc:sqlserver://" + SQLServ + ";instanceName=" + SQLPort + ";integratedSecurity=false;user=" + login + ";password=" + pass;
                }
            }
        }
        if (command == "import"){
            String SQLBase = cont.toStringParam(5);
            if (boolValid) {//это число - номер порта
                if (login.length()==0) {

                    connectionUrl = "jdbc:sqlserver://" + SQLServ + ":" + SQLPort + ";databaseName=" + SQLBase + ";integratedSecurity=true";
                }
                else {

                    connectionUrl = "jdbc:sqlserver://" + SQLServ + ":" + SQLPort + ";databaseName=" + SQLBase + ";integratedSecurity=false;user=" + login + ";password=" + pass;//":"+SQLPort+
                }
            }
            else
            {
                if (login.length()==0) {
                    connectionUrl = "jdbc:sqlserver://" + SQLServ + ";instanceName=" + SQLPort + ";databaseName=" + SQLBase + ";integratedSecurity=true";
                }
                else
                {
                    connectionUrl = "jdbc:sqlserver://" + SQLServ + ";instanceName=" + SQLPort + ";databaseName=" + SQLBase + ";integratedSecurity=false;user=" + login + ";password=" + pass;//":"+SQLPort+

                }
            }
        }
        Wind_Alert Wind_Alert = new Wind_Alert();
//        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean base_is = false;

        try {
            // Establish the connection.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();

            con = DriverManager.getConnection(connectionUrl);

            if (command == "list"){GetListSQLConnect(cont);}
            if (command == "import"){
                //проверим есть ли наша таблица
                String SQLBase = cont.toStringParam(5);
                String SQLTable = cont.toStringParam(6);
                String SQL = "select table_name from "+SQLBase+".information_schema.tables";
                stmt = con.createStatement();
                rs = stmt.executeQuery(SQL);
                while (rs.next()) {
                    if (rs.getString(1).equals(SQLTable)) {
                        base_is = true;
                        boolean bul_quest = Wind_Alert.initQuest("Данная таблица " +SQLTable+" существует - удалить?");
                        if (bul_quest){
                            //Droping
                            SQL = "USE ["+SQLBase+"] DROP TABLE [dbo].["+SQLTable+"]";
                            try {
                                stmt.executeUpdate(SQL);
                                base_is = false;
                                break;
                            } catch (SQLException e) {
                                cont.Stat = 1;  //error
                                Wind_Alert.init(e.getMessage());
                            }

                        }
                    }
                }
                    if (!base_is) {
                        SetCreateTable(cont);
                    }
            }

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            cont.Stat = 1;  //error
            e.printStackTrace();
            Wind_Alert.init(e.getMessage());
        }

        finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
        }

    }
    public static void GetListSQL(FirstFrame cont){

        Conect(cont,"list");

    }

    public static void GetListSQLConnect(FirstFrame cont) throws Exception  {
        String SQL = "select	name " +
                "from	sys.databases " +
                "where	name not in ('master','tempdb','model','msdb') " +
                "and is_distributor = 0 " +
                "and isnull(source_database_id,0) = 0";

        stmt = con.createStatement();
        rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            cont.toAddStringParam(rs.getString(1));
        }

    }

    public static void SetImportTable(FirstFrame cont ,ReadCSV CSVReader, String SQLTable) throws Exception {

        //con
        Map<String, Integer> hmHand = CSVReader.hmHand;
        String[] Had = CSVReader.Had;
        String SQL = "";
        String SQL1 = "";
        for (int k = 0; k < Had.length; k++) {
            String str_Name_Kol = valid.ToString(Had[k].toString());
            //
            SQL = SQL + str_Name_Kol;
            SQL1 = SQL1 + "?";
            if (k < Had.length-1) {
                SQL = SQL + ",";
                SQL1 = SQL1 + ",";
            }
        }

        con.setAutoCommit(false);
        String Str_Insert = "insert into " + SQLTable + "(" + SQL + ") values(" + SQL1 + ")";

        PreparedStatement statement = con.prepareStatement(Str_Insert);
        //реинициализация и чтение данных

        CSVReader.mainReader(cont);
        CsvReader products = CSVReader.products;
        int int_countRecord = CSVReader.int_countRecord;

        int quit = 0; int paketTranz = 0;

        while (products.readRecord()) {
            quit++; paketTranz++;
            if (quit==1) {continue;}//первую строку пропускаем, там данные
            cont.process(int_countRecord, quit);
            for (int k = 0; k < Had.length; k++) {
                String str_Name_Head = "";
                String str_Name_Kol = Had[k].toString();
                int int_znach = hmHand.get(str_Name_Kol);
                str_Name_Head = products.get(k);

                switch (int_znach) {
                    case 0: //String
                    case 5: //String

                        if (str_Name_Head == ""){
                            str_Name_Head = "";
                        }
                        statement.setString(k+1, str_Name_Head);
                        break;
                    case 2: //Data
                        if (str_Name_Head == ""){
                            statement.setNull(k+1, Types.DATE);//setDate(k+1,null);//пустое значение, пишем нулл
                            break;
                        }

                        if (valid.ValidatorDate(str_Name_Head)) {//это дата
                            statement.setDate(k+1,new java.sql.Date(valid.date_Persing.getTime()));
                            break;
                        } else {
                            if (valid.ValidatorDate1(str_Name_Head)) {//это дата
                                statement.setDate(k+1,new java.sql.Date(valid.date_Persing.getTime()));
                                break;
                            }
                            else {
                                System.out.println("В строке "+quit+" в колонке ("+str_Name_Kol+ ") не смогли преобразовать в дату - "+str_Name_Head);
                                statement.setNull(k+1, Types.DATE);//setDate(k+1,null);//пустое значение, пишем нулл
//                                statement.setDate(k+1,null);//пишем нулл - ерунда с преобразование
                            }
                        }
                    case 1: //Float
                        if (str_Name_Head == ""){
                            statement.setFloat(k+1,0);
                            break;
                        }

                        if (valid.ValidatorFloat(str_Name_Head)) {//это Float
                            statement.setString(k+1,str_Name_Head.replace(",","."));
                            break;
                        }
                        else {
                            System.out.println("В строке "+quit+" в колонке ("+str_Name_Kol+ ") не смогли преобразовать в число - "+str_Name_Head);
                            statement.setFloat(k+1,0);//это для того если не смогли преобразовать в число - тогда обнуляем
                        }
                }
            }
            statement.addBatch();
            if (paketTranz == 100) {
                int[] updateCount = statement.executeBatch();
                con.commit();
                //statement.executeUpdate();// вместо этого поставили пакетную обновление
                paketTranz = 0;
            }

//            if (quit > 10){
//                break;
//            }
        }
        if (paketTranz != 0){
            int[] updateCount = statement.executeBatch();
            con.commit();
        }
        con.setAutoCommit(true);
    }

    public static void SetCreateTable(FirstFrame cont) throws Exception  {

        Wind_Alert Wind_Alert = new Wind_Alert();

        ReadCSV CSVReader = new ReadCSV();
        CSVReader.main(cont);
        if (CSVReader.int_countRecord==0){//Ошибка в заполнении шапки. прервем и выведем сообщение.
            throw new RuntimeException("В файле не корректно заполены данные по типу полей!");
//            Wind_Alert.init("В файле не корректно заполены данные по типу полей!");
        }
        String SQLTable = cont.toStringParam(6);
        Map<String, Integer> hmHand = CSVReader.hmHand;
        String[] Had= CSVReader.Had;
        String SQL = "set nocount on " +
                "CREATE TABLE [dbo].["+SQLTable+"](";
        for (int k = 0; k < Had.length; k++) {
            String str_Name_Kol = valid.ToString(Had[k].toString());
//            String str_Name_Kol = Had[k].toString();
            int int_znach = hmHand.get(str_Name_Kol);
            //удаляем спец символы
            //
            if ((int_znach == 0) | (int_znach == 5)){
                SQL=SQL+"["+str_Name_Kol+"] [nvarchar](4000) NULL";
            }
            if (int_znach == 2) {
                SQL=SQL+"["+str_Name_Kol+"] [datetime] NULL";
            }
            if (int_znach == 1) {
                SQL=SQL+"["+str_Name_Kol+"] [float] NULL";
            }
            if (k<Had.length){SQL = SQL + ",";}
        }
        SQL = SQL + ") ON [PRIMARY]";

        stmt = con.createStatement();
        try {
            stmt.executeUpdate(SQL);

            SetImportTable(cont, CSVReader, SQLTable);

        } catch (SQLException e) {
            Wind_Alert.init(e.getMessage());
            cont.Stat = 1;  //error
            //System.out.println(e.getStackTrace());
        }
//        finally {
//            if (con != null) {
//                con.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        }
//
//        while (rs.next()) {
//            //cont.d3.
//            cont.toAddStringParam(rs.getString(1));
    //    }

    }

}

