package com.HTMLEditor.listeners;

import com.HTMLEditor.View;
import com.javarush.task.task32.task3209.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class TextEditMenuListener implements MenuListener {

   private View view;

    public TextEditMenuListener(View view) {
        this.view = view;
    }

    @Override
    public void menuSelected(MenuEvent event) {
        JMenu menu = (JMenu) event.getSource();

        for (Component comp: menu.getMenuComponents()) {
            comp.setEnabled(view.isHtmlTabSelected());
        }


    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
