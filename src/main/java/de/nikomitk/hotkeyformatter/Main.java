package de.nikomitk.hotkeyformatter;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static String pathname = "data";
    private static File dir = new File(pathname);
    private static File hotkeyFile = new File(pathname + File.separator + "hotkeyFile.txt");
    public static List<Integer> hotkeyButtons;
    private static boolean [] holdKeys;

    public static void main(String[] args) throws IOException, NativeHookException {

        //gespeicherten hotkey einlesen
        hotkeyButtons = new ArrayList<>();
        dir.mkdir();
        if(!hotkeyFile.createNewFile()){
            try{
                Scanner sc = new Scanner(hotkeyFile);
                while(true){
                    try{
                        hotkeyButtons.add(Integer.parseInt(sc.nextLine()));
                    }
                    catch(Exception ex){
                        break;
                    }
                }
                if(hotkeyButtons.size()==0) throw new Exception("Keine heißen Schlüssel gefunden");
            }
            catch(Exception e){
                e.printStackTrace();
                hotkeyButtons.add(29);
                hotkeyButtons.add(16);
            }
        }

        holdKeys = new boolean[hotkeyButtons.size()];

        Gui gui = new Gui(hotkeyButtons);
        // the stuff for the hotkey listener
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
                for(int i = 0; i < hotkeyButtons.size(); i++){
                    if(nativeKeyEvent.getKeyCode() == hotkeyButtons.get(i)){
                        holdKeys[i] = true;
                    }
                }
                boolean allTrue = true;
                for(boolean b : holdKeys) {
                    if (!b) {
                        allTrue = false;
                        break;
                    }
                }
                if(allTrue) Handler.toSarcastic();
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
                for(int i = 0; i < hotkeyButtons.size()-1; i++){
                    if(nativeKeyEvent.getKeyCode() == hotkeyButtons.get(i)){
                        holdKeys[i] = false;
                    }
                }
            }
        });
    }

    public static void storeNewHotkey(List<Integer> hkButtons){
        hotkeyButtons = hkButtons;
        holdKeys = new boolean[hotkeyButtons.size()];
    }
}