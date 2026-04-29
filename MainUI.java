package com.project.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.project.service.StegoService;

public class MainUI {

    // --- PREMIUM PALETTE ---
    private static final Color BG_DARK = new Color(5, 7, 12);
    private static final Color CARD_COLOR = new Color(20, 24, 35);
    private static final Color CYAN_NEON = new Color(0, 215, 255);
    private static final Color PURPLE_NEON = new Color(130, 50, 250);
    private static final Color TEXT_WHITE = new Color(240, 245, 255);
    private static final Color STATUS_GREEN = new Color(50, 255, 150);
    private static final Color ERROR_RED = new Color(255, 70, 70);

    private static final int FRAME_WIDTH = 1100;
    private static final int FRAME_HEIGHT = 700;

    static JFrame frame = new JFrame("AXON PRIME | SECURE INTERFACE");
    static MockService service = new MockService(); 
    static StegoService stego = new StegoService();

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
            showHome();
        });
    }

    // --- CUSTOM UI COMPONENTS ---

    private static JButton createEliteButton(String text, Color accentColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) g2.setColor(accentColor.darker());
                else if (getModel().isRollover()) g2.setColor(accentColor);
                else g2.setColor(CARD_COLOR);

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                if (getModel().isRollover()) {
                    g2.setColor(accentColor);
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 15, 15));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(400, 55));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private static JPanel createBasePanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, BG_DARK, 0, getHeight(), new Color(15, 20, 35));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    // --- CORE SCREENS ---

    public static void showHome() {
        // Clear focus listeners from previous screens
        for (WindowFocusListener wl : frame.getWindowFocusListeners()) frame.removeWindowFocusListener(wl);

        frame.getContentPane().removeAll();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel container = createBasePanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(80, 80, 80, 80));

        JLabel logo = new JLabel("AXON");
        logo.setFont(new Font("Verdana", Font.BOLD, 82));
        logo.setForeground(CYAN_NEON);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("CRYPTOGRAPHIC PORTAL");
        tagline.setFont(new Font("Monospaced", Font.BOLD, 14));
        tagline.setForeground(new Color(100, 110, 140));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEnc = createEliteButton("ENCRYPT DATA STREAM", CYAN_NEON);
        JButton btnDec = createEliteButton("DECRYPT DATA STREAM", CYAN_NEON);
        JButton btnStego = createEliteButton("IMAGE STEGANOGRAPHY", PURPLE_NEON);

        container.add(logo);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(tagline);
        container.add(Box.createRigidArea(new Dimension(0, 120)));
        container.add(btnEnc);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(btnDec);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(btnStego);

        btnEnc.addActionListener(e -> showSender());
        btnDec.addActionListener(e -> showReceiver());
        btnStego.addActionListener(e -> showStego());

        frame.add(container);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public static void showStego() {
        renderSubPage("STEGANOGRAPHY MODULE", panel -> {
            JTextField msgInput = createStyledField("Enter message to hide...");
            JTextArea output = createStyledArea();
            JLabel status = createStatusLabel();

            JButton encode = createEliteButton("ENCODE & SAVE IMAGE", CYAN_NEON);
            JButton decode = createEliteButton("UPLOAD & EXTRACT", PURPLE_NEON);
            JButton back = createEliteButton("BACK TO MAIN", new Color(50, 50, 60));

            encode.addActionListener(e -> {
                if (msgInput.getText().isEmpty()) return;
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (!file.getName().endsWith(".png")) file = new File(file.getAbsolutePath() + ".png");
                    stego.encodeMessage(msgInput.getText(), file);
                    status.setText("● STATUS: SUCCESS - IMAGE SAVED");
                    status.setForeground(STATUS_GREEN);
                }
            });

            decode.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    String result = stego.decodeMessage(fc.getSelectedFile().getAbsolutePath());
                    output.setText(result);
                    status.setText("● STATUS: DATA EXTRACTED");
                    status.setForeground(CYAN_NEON);
                }
            });

            back.addActionListener(e -> showHome());

            panel.add(new JLabel("INPUT STRING:") {{ setForeground(Color.GRAY); }});
            panel.add(msgInput);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(encode);
            panel.add(Box.createRigidArea(new Dimension(0, 40)));
            panel.add(new JLabel("EXTRACTED DATA:") {{ setForeground(Color.GRAY); }});
            panel.add(new JScrollPane(output) {{ setBorder(null); }});
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(decode);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(status);
            panel.add(Box.createVerticalGlue());
            panel.add(back);
        });
    }

    public static void showReceiver() {
        renderSubPage("INBOUND DECRYPTION", panel -> {
            JTextField keyInput = createStyledField("Enter Secure Access Key...");
            JTextArea output = createStyledArea();
            JLabel status = createStatusLabel();
            
            // --- SECURITY: ANTI-COPY PROTOCOL ---
            output.setTransferHandler(null); // Disable default Drag and Drop/Clipboard
            output.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // Block Ctrl+C (Copy), Ctrl+V (Paste), Ctrl+X (Cut)
                    if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_V || e.getKeyCode() == KeyEvent.VK_X)) {
                        e.consume();
                        status.setText("● SECURITY WARNING: COPYING PROHIBITED");
                        status.setForeground(ERROR_RED);
                    }
                }
            });

            // --- SECURITY: SCREENSHOT PROTECTION ---
            WindowFocusListener screenshotShield = new WindowFocusListener() {
                String tempCache = "";
                public void windowGainedFocus(WindowEvent e) { if(!tempCache.isEmpty()) { output.setText(tempCache); tempCache=""; } }
                public void windowLostFocus(WindowEvent e) {
                    if(!output.getText().equals("[ DATA PURGED ]") && !output.getText().isEmpty()) {
                        tempCache = output.getText();
                        output.setText("[ SCREEN PROTECTED - FOCUS LOST ]");
                    }
                }
            };
            frame.addWindowFocusListener(screenshotShield);

            JButton accessBtn = createEliteButton("AUTHORIZE ACCESS", CYAN_NEON);
            JButton back = createEliteButton("BACK", new Color(50, 50, 60));

            final int[] time = {0};

            accessBtn.addActionListener(e -> {
                String code = keyInput.getText().trim();
                MessageData data = service.getMessage(code);

                if (data == null) {
                    int attemptsLeft = 3 - service.trackAttempt(code);
                    status.setText("● ACCESS DENIED: " + attemptsLeft + " ATTEMPTS REMAINING");
                    status.setForeground(ERROR_RED);
                    if (attemptsLeft <= 0) {
                        JOptionPane.showMessageDialog(frame, "SECURITY BREACH: MESSAGE DELETED.");
                        service.wipe(code);
                    }
                } else {
                    output.setText(data.content);
                    time[0] = data.ttl;
                    accessBtn.setEnabled(false);
                    
                    Timer t = new Timer(1000, event -> {
                        time[0]--;
                        status.setText("● SYSTEM PURGE IN: " + time[0] + "s");
                        status.setForeground(ERROR_RED);
                        if (time[0] <= 0) {
                            output.setText("[ DATA PURGED ]");
                            service.wipe(code);
                            ((Timer)event.getSource()).stop();
                        }
                    });
                    t.start();
                }
            });

            back.addActionListener(e -> {
                frame.removeWindowFocusListener(screenshotShield);
                showHome();
            });

            panel.add(new JLabel("ACCESS KEY:") {{ setForeground(Color.GRAY); }});
            panel.add(keyInput);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(accessBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 30)));
            panel.add(new JLabel("DECRYPTED CONTENT (PROTECTED):") {{ setForeground(Color.GRAY); }});
            panel.add(new JScrollPane(output) {{ setBorder(null); }});
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(status);
            panel.add(Box.createVerticalGlue());
            panel.add(back);
        });
    }

    public static void showSender() {
        renderSubPage("OUTBOUND ENCRYPTION", panel -> {
            JTextField msgInput = createStyledField("Message to encrypt...");
            JTextField timeInput = createStyledField("Timer (seconds)");
            timeInput.setText("15");
            JTextArea codeOutput = createStyledArea();
            JLabel status = createStatusLabel();

            JButton genBtn = createEliteButton("GENERATE GHOST KEY", CYAN_NEON);
            JButton copyBtn = createEliteButton("COPY GHOST KEY", STATUS_GREEN);
            copyBtn.setVisible(false); // Only show after generation
            
            JButton back = createEliteButton("BACK", new Color(50, 50, 60));

            genBtn.addActionListener(e -> {
                try {
                    String code = service.createMessage(msgInput.getText(), Integer.parseInt(timeInput.getText()));
                    codeOutput.setText(code);
                    status.setText("● STATUS: PROTOCOL ACTIVE");
                    status.setForeground(STATUS_GREEN);
                    copyBtn.setVisible(true);
                } catch (Exception ex) { status.setText("● ERROR: INVALID TIME"); }
            });

            copyBtn.addActionListener(e -> {
                StringSelection selection = new StringSelection(codeOutput.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
                status.setText("● COPIED TO SYSTEM CLIPBOARD");
            });

            back.addActionListener(e -> showHome());

            panel.add(new JLabel("DATA TO PROTECT:") {{ setForeground(Color.GRAY); }});
            panel.add(msgInput);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(new JLabel("TIME-TO-LIVE (SECONDS):") {{ setForeground(Color.GRAY); }});
            panel.add(timeInput);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(genBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(copyBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 30)));
            panel.add(new JScrollPane(codeOutput) {{ setBorder(null); }});
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(status);
            panel.add(Box.createVerticalGlue());
            panel.add(back);
        });
    }

    // --- HELPER UI METHODS ---

    private static void renderSubPage(String title, java.util.function.Consumer<JPanel> builder) {
        frame.getContentPane().removeAll();
        JPanel panel = createBasePanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 22));
        t.setForeground(CYAN_NEON);
        panel.add(t);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        builder.accept(panel);
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private static JTextField createStyledField(String hint) {
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        f.setBackground(CARD_COLOR);
        f.setForeground(Color.WHITE);
        f.setCaretColor(CYAN_NEON);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(40, 45, 60)), new EmptyBorder(5, 15, 5, 15)));
        return f;
    }

    private static JTextArea createStyledArea() {
        JTextArea a = new JTextArea(5, 20);
        a.setEditable(false);
        a.setBackground(CARD_COLOR);
        a.setForeground(CYAN_NEON);
        a.setFont(new Font("Monospaced", Font.BOLD, 15));
        a.setLineWrap(true);
        return a;
    }

    private static JLabel createStatusLabel() {
        JLabel l = new JLabel("● STATUS: SYSTEM READY");
        l.setFont(new Font("Monospaced", Font.BOLD, 12));
        l.setForeground(Color.DARK_GRAY);
        return l;
    }

    // --- MOCK SERVICE LOGIC ---
    static class MessageData { String content; int ttl; MessageData(String c, int t) {this.content=c; this.ttl=t;}}
    static class MockService {
        private Map<String, MessageData> db = new HashMap<>();
        private Map<String, Integer> attempts = new HashMap<>();
        public String createMessage(String m, int t) {
            String code = "AX-" + (int)(Math.random()*9000+1000);
            db.put(code, new MessageData(m, t));
            return code;
        }
        public MessageData getMessage(String code) { return db.get(code); }
        public int trackAttempt(String code) {
            int count = attempts.getOrDefault(code, 0) + 1;
            attempts.put(code, count);
            return count;
        }
        public void wipe(String code) { db.remove(code); attempts.remove(code); }
    }
}