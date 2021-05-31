package de.nikomitk.hotkeyformatter;

import lombok.Getter;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    @Getter
    private static int [] hotkeyButtons;
    private static final File storageFile = new File("storage.yapion");
    private static Storage storage;
    private static boolean[] holdKeys;

    public static void main(String[] args) throws IOException, NativeHookException {
        createStorage();
        hotkeyButtons = storage.getHotkeyButtons();
        holdKeys = new boolean[hotkeyButtons.length];

        new Gui();

        // the stuff for the hotkey listener
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
                // This method had to be implemented but it doesn't work as it should
            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

                // Add the newly pressed button to the list of pressed buttons from the hotkey array
                for (int i = 0; i < hotkeyButtons.length; i++) {
                    if (nativeKeyEvent.getKeyCode() == hotkeyButtons[i]) {
                        holdKeys[i] = true;
                    }
                }

                // Check if every button is pressed
                boolean allTrue = true;
                for (boolean b : holdKeys) {
                    if (!b) {
                        allTrue = false;
                        break;
                    }
                }
                if (allTrue) Handler.toSarcastic();
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

                // Remove the released button from the list of pressed buttons
                for (int i = 0; i < hotkeyButtons.length; i++) {
                    if (nativeKeyEvent.getKeyCode() == hotkeyButtons[i]) {
                        holdKeys[i] = false;
                    }
                }
            }
        });
    }

    private static void createStorage() throws IOException{
        if (storageFile.exists()) {
            storage = (Storage) YAPIONDeserializer.deserialize(YAPIONParser.parse(storageFile));
        } else {
            storage = new Storage();
        }
    }

    public static void setHotkeyButtons(int [] hotKey){
        hotkeyButtons = hotKey;
        holdKeys = new boolean[hotKey.length];
        storage.save();
    }

}