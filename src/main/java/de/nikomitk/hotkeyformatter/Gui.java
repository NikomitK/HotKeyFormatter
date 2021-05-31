package de.nikomitk.hotkeyformatter;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Arrays;

public class Gui extends JFrame implements NativeKeyListener {

    private int [] tempHotkeys;
    private final JButton recordButton;
    private JLabel hotkeyLabel;

    public Gui() {

        setLAF();
        setFrameOptions();

        JPanel everyThing = new JPanel(new GridLayout(2, 1));
        JPanel up = new JPanel(new GridLayout(1, 2));
        JPanel down = new JPanel(new GridLayout());

        JButton sarcastic = new JButton("SaRcAsTiC");
        sarcastic.setFocusable(false);
        sarcastic.addActionListener((ActionEvent e) -> Handler.toSarcastic());

        recordButton = new JButton("Record");
        recordButton.setFocusable(false);
        recordButton.addActionListener((ActionEvent e) -> {
            if (recordButton.getText().equals("Record")) {
                tempHotkeys = new int[0];
                recordButton.setText("Stop");
                hotkeyLabel.setBackground(Color.white);
                GlobalScreen.addNativeKeyListener(this);

            } else {
                GlobalScreen.removeNativeKeyListener(this);
                hotkeyLabel.setBackground(Color.lightGray);
                recordButton.setText("Record");
                Main.setHotkeyButtons(tempHotkeys);
            }
        });

        hotkeyLabel = new JLabel();
        hotkeyLabel.setBackground(Color.lightGray);

        up.add(sarcastic);
        up.add(recordButton);
        down.add(hotkeyLabel);
        everyThing.add(up);
        everyThing.add(down);
        add(everyThing);
        pack();
        setSize(240, 125);
        displayHotkey(Main.getHotkeyButtons());

        addToSystemTray();
    }

    private void setLAF(){
        FlatLightLaf.install();
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void displayHotkey(int [] hotkeyButtons){
        StringBuilder bld = new StringBuilder();
        bld.append(NativeKeyEvent.getKeyText(hotkeyButtons[0]));
        for(int i = 1; i<hotkeyButtons.length; i++){
            bld.append(" + ").append(NativeKeyEvent.getKeyText(hotkeyButtons[i]));
        }
        hotkeyLabel.setText(bld.toString());
    }

    private void setFrameOptions(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                setExtendedState(Frame.ICONIFIED);
            }
        });
        setTitle("HotKeyFormatter");
        try {
            setIconImage(ImageIO.read(new FileInputStream("pics/logo.png")));
        } catch (Exception e){
            e.printStackTrace();
        }
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
    }

    private void addToSystemTray() {
        try{
            if (SystemTray.isSupported()) {

                Image image = ImageIO.read(new FileInputStream("pics/logo.png"));
                ActionListener exitListener = e -> System.exit(0);
                PopupMenu popup = new PopupMenu();
                MenuItem defaultItem = new MenuItem("Open");
                defaultItem.addActionListener(e -> {
                    setVisible(true);
                    setExtendedState(Frame.NORMAL);
                });
                popup.add(defaultItem);
                defaultItem = new MenuItem("Exit");
                defaultItem.addActionListener(exitListener);
                popup.add(defaultItem);
                TrayIcon trayIcon = new TrayIcon(image, "HotkeyFormatter", popup);
                trayIcon.setImageAutoSize(true);
                trayIcon.addActionListener(e -> setVisible(true));
                addWindowStateListener(e -> {
                    if (e.getNewState() == WindowEvent.WINDOW_CLOSING || e.getNewState() == 7 || e.getNewState() == ICONIFIED) {
                        addTrayIcon(trayIcon);
                    }
                    if (e.getNewState() == MAXIMIZED_BOTH || e.getNewState() == NORMAL) {
                        removeTrayIcon(trayIcon);
                    }
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addTrayIcon(TrayIcon trayIcon){
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
            setVisible(false);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    private void removeTrayIcon(TrayIcon trayIcon){
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        setVisible(true);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        // This method had to be implemented but it doesn't work as it should
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int [] tempHotkeybuttons = Arrays.copyOf(tempHotkeys, tempHotkeys.length+1);
        tempHotkeybuttons[tempHotkeybuttons.length-1] = nativeKeyEvent.getKeyCode();
        tempHotkeys = tempHotkeybuttons;
        displayHotkey(tempHotkeys);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        // This method had to be implemented but I have no need for it
    }
}
