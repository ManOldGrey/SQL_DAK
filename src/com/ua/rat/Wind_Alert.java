package com.ua.rat;

import javax.swing.*;

/**
 * Created by a.krysa on 18.05.2016.
 */

public class Wind_Alert  extends JFrame {

        public void init(String str_text) {
            JOptionPane.showMessageDialog( this, "<html><table width=400>"+str_text, "Ошибка", JOptionPane.DEFAULT_OPTION );
        }

    public void initOk(String str_text) {
        JOptionPane.showMessageDialog( this, "<html><table width=100>"+str_text, "", JOptionPane.DEFAULT_OPTION );
    }

    public boolean initQuest(String str_text) {

        int int_quest = JOptionPane.showConfirmDialog( this, "<html><table width=400>"+str_text, "Вопрос", JOptionPane.YES_NO_OPTION);
        System.out.println(int_quest);
        if (int_quest == 0){return true;}
        return false;
//        JOptionPane optionPane = new JOptionPane(
//                str_text,
//                JOptionPane.QUESTION_MESSAGE,
//                JOptionPane.YES_NO_OPTION);
//        //optionPane.getInputValue();
//        System.out.println(optionPane.getInputValue());

    }
}
