package de.nikomitk.hotkeyformatter;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

@UtilityClass
public class Handler {

    public void toSarcastic() {
        String pText = null;
        try {
            pText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(pText == null){
            return;
        }
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < pText.length(); i++) {
            // char has no uppercase method and can somehow not be casted into string wtf
            // update a lot later: I guess I was stupid, primitive types can't really have methods.
            if (i % 2 == 0) bld.append(("" + pText.charAt(i)).toUpperCase());
            else bld.append(("" + pText.charAt(i)).toLowerCase());
        }

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(new StringSelection(bld.toString()), new StringSelection(bld.toString()));
    }

}
