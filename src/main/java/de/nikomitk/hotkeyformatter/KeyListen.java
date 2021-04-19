package de.nikomitk.hotkeyformatter;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class KeyListen implements NativeKeyListener {

    boolean strgActive = false;

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == 29) strgActive = true;

        try {
            if (strgActive && nativeKeyEvent.getKeyCode() == 16) {
                String clipEntry = null;
                try {
                    clipEntry = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                c.setContents(new StringSelection(Handler.toSarcastic(clipEntry)), new StringSelection(Handler.toSarcastic(clipEntry)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == 29) strgActive = false;
    }
}
