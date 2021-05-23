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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Gui extends JFrame implements NativeKeyListener {

    private static String pathname = "data";
    private static File dir = new File(pathname);
    private static File hotkeyFile = new File(pathname + File.separator + "hotkeyFile.txt");
    private static JPanel everyThing, up, down, downRight;
    private static JScrollPane hotkeyField;
    private static JButton sarcastic, record;
    private static JLabel hotkeyLabel;
    private static String hotkey;
    private List<Integer> hotkeyButtons;

    public Gui(List<Integer> hotkeyButtons) throws IOException {
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
        hotkey = "";
        this.hotkeyButtons = hotkeyButtons;
        for (int i = 0; i < hotkeyButtons.size(); i++) {
            String plus = " + ";
            if (i == 0) plus = "";
            hotkey += plus + NativeKeyEvent.getKeyText(hotkeyButtons.get(i));
        }
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                setExtendedState(JFrame.ICONIFIED);
            }
        });
        setTitle("HotKeyFormatter");
        setIconImage(ImageIO.read(new FileInputStream("pics/logo.png")));
        setVisible(true);
        setDefaultCloseOperation(JFrame.ICONIFIED);
        setResizable(false);
        everyThing = new JPanel();
        everyThing.setVisible(true);
        everyThing.setLayout(new GridLayout(2, 1));
        up = new JPanel();
        up.setLayout(new GridLayout(1, 2));
        down = new JPanel();
        down.setLayout(new GridLayout());

        sarcastic = new JButton("SaRcAsTiC");
        sarcastic.setFocusable(false);
        sarcastic.addActionListener((ActionEvent e) -> Handler.toSarcastic());
        record = new JButton("Record");
        record.setFocusable(false);
        record.addActionListener((ActionEvent e) -> {
            if (record.getText().equals("Record")) {
                this.hotkeyButtons = new ArrayList<>();
                GlobalScreen.addNativeKeyListener(this);
                hotkey = "";
                record.setText("Stop");
                hotkeyLabel.setBackground(Color.white);
            } else {
                try {
                    GlobalScreen.removeNativeKeyListener(this);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                hotkeyLabel.setBackground(Color.lightGray);
                record.setText("Record");
                storeHotkey(this.hotkeyButtons);
                Main.storeNewHotkey(this.hotkeyButtons);
            }
        });
        hotkeyLabel = new JLabel(hotkey);
        hotkeyLabel.setBackground(Color.lightGray);
        hotkeyField = new JScrollPane(hotkeyLabel);

        up.add(sarcastic);
        up.add(record);
        down.add(hotkeyLabel);
        everyThing.add(up);
        everyThing.add(down);
        add(everyThing);
        pack();
        setSize(240, 125);

        // now the part with minimizing to tray instead of closing
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(new FileInputStream("pics/logo.png"));
            ActionListener exitListener = e -> System.exit(0);
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Open");
            defaultItem.addActionListener(e -> {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            });
            popup.add(defaultItem);
            defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            TrayIcon trayIcon = new TrayIcon(image, "HotkeyFormatter", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> setVisible(true));
            addWindowStateListener(e -> {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                }
                if (e.getNewState() == WindowEvent.WINDOW_CLOSING) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            });
        }
    }

    public void storeHotkey(List<Integer> hotkeyButtons) {

        // for safety if the file somehow went missing
        try {
            hotkeyFile.createNewFile();
            new FileWriter(pathname + File.separator + "hotkeyFile.txt").close();
            for (int i : hotkeyButtons) {
                Printer.printToFile("" + i, pathname + File.separator + "hotkeyFile.txt", true);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        String plus = " + ";
        if (hotkey.length() == 0) plus = "";
        if (nativeKeyEvent.getKeyCode() != 42)
            hotkey += plus + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
        else hotkey += " + " + "Shift";
        hotkeyLabel.setText(hotkey);
        hotkeyButtons.add(nativeKeyEvent.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
