package com.example.swingapp.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private final JFrame mainFrame;
    private JRadioButton rbLight;
    private JRadioButton rbDark;

    public SettingsPanel(JFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        initComponents();
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("환경 설정 (Settings)");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Settings Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel themeHeader = new JLabel("테마 설정 (FlatLaf Theme)");
        themeHeader.setFont(themeHeader.getFont().deriveFont(Font.BOLD, 15f));
        card.add(themeHeader);
        card.add(Box.createVerticalStrut(12));

        // FormLayout for Theme Options
        FormLayout layout = new FormLayout(
                "left:pref, 16dlu, left:pref",
                "pref, 6dlu, pref"
        );
        JPanel form = new JPanel(layout);
        CellConstraints cc = new CellConstraints();

        rbLight = new JRadioButton("Light Theme (밝은 테마)", true);
        rbDark = new JRadioButton("Dark Theme (어두운 테마)", false);

        ButtonGroup group = new ButtonGroup();
        group.add(rbLight);
        group.add(rbDark);

        boolean isDark = FlatLaf.isLafDark();
        rbDark.setSelected(isDark);
        rbLight.setSelected(!isDark);

        rbLight.addActionListener(e -> changeTheme(false));
        rbDark.addActionListener(e -> changeTheme(true));

        form.add(rbLight, cc.xy(1, 1));
        form.add(rbDark, cc.xy(1, 3));

        card.add(form);
        card.add(Box.createVerticalStrut(20));

        // Info
        JLabel infoLabel = new JLabel("<html>FlatLaf의 런타임 Look & Feel 전환 기능을 사용하여<br>애플리케이션 재시작 없이 즉시 테마가 실시간 반영됩니다.</html>");
        infoLabel.setForeground(new Color(100, 116, 139));
        card.add(infoLabel);

        contentPanel.add(card);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void changeTheme(boolean dark) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (dark) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                FlatLaf.updateUI();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
