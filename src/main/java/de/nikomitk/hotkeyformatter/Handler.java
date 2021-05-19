package de.nikomitk.hotkeyformatter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class Handler {

    public static void toSarcastic() {
        String pText = null;
        try {
            pText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rText = "";
        for (int i = 0; i < pText.length(); i++) {
            // char has no uppercase method and can somehow not be casted into string wtf
            // update a lot later: I guess I was stupid, primitive types can't really have methods.
            if (i % 2 == 0) rText += ("" + pText.charAt(i)).toUpperCase();
            else rText += ("" + pText.charAt(i)).toLowerCase();
        }

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(new StringSelection(rText), new StringSelection(rText));
    }

}
