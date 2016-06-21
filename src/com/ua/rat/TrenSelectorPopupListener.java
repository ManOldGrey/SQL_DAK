package com.ua.rat;
/**
 * Created by a.krysa on 18.05.2016.
 */

import javax.swing.event.PopupMenuEvent;
    import javax.swing.event.PopupMenuListener;

    public class TrenSelectorPopupListener implements PopupMenuListener {

        FirstFrame cellEditor;
        public TrenSelectorPopupListener(FirstFrame cont){
            cellEditor = cont;
        }
        @Override
        public void popupMenuCanceled(PopupMenuEvent arg0) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
            //System.out.println("Удаляем");
            cellEditor.toRemAllItem();
            cellEditor.toAddStringParam("Выбор базы");
            new Work_SQL().GetListSQL(cellEditor);

        }
    }