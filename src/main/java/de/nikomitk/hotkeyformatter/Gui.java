package de.nikomitk.hotkeyformatter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Gui extends JFrame {
    private static JPanel panel;
    private static JButton sarcastic, uppercase, lowercase;

    public Gui() throws IOException {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                setExtendedState(JFrame.ICONIFIED);
            }
        });
        setTitle("HotKeyFormatter");
        setIconImage(ImageIO.read(new FileInputStream("pics/logo.png")));
        setVisible(true);
        setDefaultCloseOperation(JFrame.ICONIFIED);
        panel = new JPanel();
        panel.setVisible(true);
        panel.setLayout(new GridLayout(2, 2));
        add(panel);


        sarcastic = new JButton("SaRcAsTiC");
        sarcastic.addActionListener((ActionEvent e) -> {
            String data = null;
            try {
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(new StringSelection(Handler.toSarcastic(data)), new StringSelection(Handler.toSarcastic(data)));
        });


        uppercase = new JButton("UPPERCASE");
        uppercase.addActionListener((ActionEvent e) -> {
            String data = null;
            try {
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(new StringSelection(Handler.toUppercase(data)), new StringSelection(Handler.toUppercase(data)));
        });


        lowercase = new JButton("lowercase");
        lowercase.addActionListener((ActionEvent e) -> {
            String data = null;
            try {
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(new StringSelection(Handler.toLowercase(data)), new StringSelection(Handler.toLowercase(data)));
        });
        panel.add(sarcastic);
        panel.add(uppercase);
        panel.add(lowercase);
        pack();


        // now the part with minimizing to tray instead of closing
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(new FileInputStream("pics/logo.png"));
            ActionListener exitListener = e -> {
                System.exit(0);
            };
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
            TrayIcon trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> {
                setVisible(true);
            });
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

}
