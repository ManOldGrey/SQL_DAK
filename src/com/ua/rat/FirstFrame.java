package com.ua.rat;
/**
 * Created by a.krysa on 18.05.2016.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FirstFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel a;
    private JTextField a2;
    private JTextField b1;
    private JTextField c1;
    private JComboBox d1;
    public JLabel a3;
    public JTextField b3;
    private JFileChooser a4;
    public String a5;
    public int Stat;
    private JTextField e5;
    private JTextField p1;
    public JProgressBar objProgress;



    public FirstFrame() {
        this.setTitle("CSV - > SQL");
        Dimension var2;
        Double var3 = Double.valueOf((var2 = Toolkit.getDefaultToolkit().getScreenSize()).getWidth());
        Double var4 = Double.valueOf(var2.getHeight());
        this.setDefaultCloseOperation(3);
        this.setBounds(var3.intValue() / 4, var4.intValue() / 4, var3.intValue() / 2, var4.intValue() / 2);
        this.a = new JPanel();
        this.a.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.a);
        this.a.setLayout(new GridLayout(8, 2, 0, 0));
        JLabel var5 = new JLabel("SQL Server");
        this.a.add(var5);
        this.b3 = new JTextField();
        b3.setText("kvss9");
        this.a.add(this.b3);
        var5 = new JLabel("Username");
        this.a.add(var5);
        this.b1 = new JTextField();
        b1.setText("");
        this.a.add(this.b1);
        this.b1.setColumns(10);
        var5 = new JLabel("Password");
        this.a.add(var5);
        this.c1 = new JPasswordField();
        c1.setText("");
        this.a.add(this.c1);
        this.c1.setColumns(10);

        var5 = new JLabel("Port");
        this.a.add(var5);
        this.e5 = new JTextField();
        this.a.add(this.e5);
        this.e5.setColumns(10);
        this.e5.setText("1433");

        var5 = new JLabel("Base SQL");
        this.a.add(var5);
        JComboBox vaar = new JComboBox();
//        vaar.addItemListener(this);

        vaar.setModel(new DefaultComboBoxModel(new String[]{"Выбор базы"}));
        this.d1 = vaar;//new JComboBox();
        vaar.addPopupMenuListener(new TrenSelectorPopupListener(this));
        this.a.add(this.d1);

        var5 = new JLabel("Table import SQL");
        this.a.add(var5);

        this.a2 = new JTextField();
        this.a.add(this.a2);
        this.a2.setColumns(10);
        this.a2.setText("ALL_Temp");


        JButton var8 = new JButton("Выполнить");
        var8.setActionCommand("Submit");
        var8.addActionListener(this);

        this.a3 = new JLabel("File: ");
        this.a.add(a3);

        JButton var7;
        var7 = new JButton("Выбрать файл");
        var7.setActionCommand("Browse");
        var7.addActionListener(this);
        this.a.add(var7);

        objProgress = new JProgressBar(0, 100);
        objProgress.setValue(0);
        objProgress.setStringPainted(true);
        this.a.add(objProgress);
        this.a.add(var8);
        objProgress.setVisible(false);
    }

    public  String toStringParam(int param) {
        //1-user; 2=pass;3-SQL server; 4-port sql; 5 - name base SQL; 6 - name table
        if (param==1){return  this.b1.getText();}
        else if (param==2){return  this.c1.getText();}
        else if (param==3){return  this.b3.getText();}
        else if (param==4){return  this.e5.getText();}
        else if (param==5){return  this.d1.getSelectedItem().toString();}
        else if (param==6){return  this.a2.getText();}
        else
        return "";
    }
    public  void toAddStringParam(String param) {
        this.d1.addItem(param);
    }

    public  void toRemAllItem() {
        this.d1.removeAllItems();
    }


    public void actionPerformed(ActionEvent var1) {
        String s1 = var1.getActionCommand();

        if (s1.toString() == "Browse"){
            this.a4 = new JFileChooser();
            this.a4.setCurrentDirectory(new File("."));
            this.a4.setDialogTitle("Выберите файл для загрузки");
            this.a4.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            this.a4.setFileFilter(new ExtFileFilter("csv", "*.csv Текстовые файлы") );
            this.a4.setAcceptAllFileFilterUsed(true);

            File var2;
            if(this.a4.showOpenDialog(this) == 0 && (var2 = this.a4.getSelectedFile()) != null) {
                this.a5 = var2.getAbsolutePath();
                this.a3.setText("File: " + var2.getAbsolutePath());
              }
        }
        Wind_Alert Wind_Alert = new Wind_Alert();
        if (s1.toString() == "Submit"){
            System.out.println(this.a5);
            if (this.a5 == null){
                Wind_Alert.init("Не выбран файл импорта");
            }
            else if (this.d1.getSelectedItem().toString() =="Выбор базы"){
                        Wind_Alert.init("Не выбрана база импорта");
                    }
            else if (this.a2.getText()==null){
                Wind_Alert.init("Не выбрана таблица импорта");
            }
            else {
                Stat = 0;
                objProgress.setValue(0);
                objProgress.setVisible(true);
                long startTime = System.currentTimeMillis();
                new Work_SQL().Conect(this, "import");
                long timeSpent = System.currentTimeMillis() - startTime;
                objProgress.setVisible(false);
                if (Stat == 0) {
//                System.out.println("Время - " + timeSpent / 1000);
                    Wind_Alert.initOk("Время - " + timeSpent / 1000 + " (сек)");
                }
                else {
                    Wind_Alert.init("Импорт не выполнен из-за ошибок!");
                }
            }
        }
    }


    protected void process(int countRec, int rec) {
        double var2 = Math.ceil((rec*100/countRec));
        if (var2 == Math.ceil(((rec-1)*100/countRec))){

        }else {
            objProgress.setValue(objProgress.getValue() + 1);
            this.update(this.getGraphics());
        }
    }

}
