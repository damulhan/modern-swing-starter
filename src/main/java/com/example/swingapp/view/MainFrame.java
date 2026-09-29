package com.example.swingapp.view;

import com.example.swingapp.presentation.CustomerPresentationModel;
import com.example.swingapp.presentation.DashboardPresentationModel;
import com.example.swingapp.service.CustomerService;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CustomerService customerService;
    private final CustomerPresentationModel customerPresentationModel;
    private final DashboardPresentationModel dashboardPresentationModel;

    private JPanel contentCards;
    private CardLayout cardLayout;
    private DashboardPanel dashboardPanel;
    private CustomerPanel customerPanel;
    private SettingsPanel settingsPanel;

    private JLabel lblStatus;
    private JButton btnNavDashboard;
    private JButton btnNavCustomers;
    private JButton btnNavSettings;

    public MainFrame() {
        this.customerService = new CustomerService();
        this.customerPresentationModel = new CustomerPresentationModel(customerService);
        this.dashboardPresentationModel = new DashboardPresentationModel(customerService);

        initUI();
    }

    private void initUI() {
        setTitle("Swing Modern Desktop Sample");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);

        // Root Container
        JPanel rootPanel = new JPanel(new BorderLayout());

        // 1. Header Bar
        JPanel headerBar = createHeaderBar();
        rootPanel.add(headerBar, BorderLayout.NORTH);

        // 2. Center Work Area: Navigation Sidebar + Content CardLayout
        JPanel centerArea = new JPanel(new BorderLayout());

        JPanel navSidebar = createSidebar();
        centerArea.add(navSidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel(dashboardPresentationModel);
        customerPanel = new CustomerPanel(customerPresentationModel, () -> dashboardPanel.refreshData());
        settingsPanel = new SettingsPanel(this);

        contentCards.add(dashboardPanel, "Dashboard");
        contentCards.add(customerPanel, "Customers");
        contentCards.add(settingsPanel, "Settings");

        centerArea.add(contentCards, BorderLayout.CENTER);
        rootPanel.add(centerArea, BorderLayout.CENTER);

        // 3. Status Bar
        JPanel statusBar = createStatusBar();
        rootPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(rootPanel);
        switchView("Dashboard");
    }

    private JPanel createHeaderBar() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel title = new JLabel("Swing Modern Desktop Sample");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
        header.add(title, BorderLayout.WEST);

        // Header Right: Quick Theme toggle & Settings icon
        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightTools.setOpaque(false);

        JButton btnTheme = new JButton(FlatLaf.isLafDark() ? "☀" : "☾");
        btnTheme.setToolTipText("테마 간편 전환 (Light/Dark)");
        btnTheme.setFocusable(false);
        btnTheme.addActionListener(e -> {
            boolean toDark = !FlatLaf.isLafDark();
            try {
                if (toDark) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    btnTheme.setText("☀");
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    btnTheme.setText("☾");
                }
                FlatLaf.updateUI();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton btnGear = new JButton("⚙");
        btnGear.setToolTipText("설정으로 이동");
        btnGear.setFocusable(false);
        btnGear.addActionListener(e -> switchView("Settings"));

        rightTools.add(btnTheme);
        rightTools.add(btnGear);
        header.add(rightTools, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(226, 232, 240)));

        sidebar.add(Box.createVerticalStrut(14));

        btnNavDashboard = createNavButton("📊  Dashboard", "Dashboard");
        btnNavCustomers = createNavButton("👥  Customers", "Customers");
        btnNavSettings  = createNavButton("⚙  Settings", "Settings");

        sidebar.add(btnNavDashboard);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnNavCustomers);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnNavSettings);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createNavButton(String text, String targetCard) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 38));
        btn.setPreferredSize(new Dimension(180, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");

        btn.addActionListener(e -> switchView(targetCard));
        return btn;
    }

    private void switchView(String cardName) {
        cardLayout.show(contentCards, cardName);

        // Highlight active nav button
        updateNavState(btnNavDashboard, "Dashboard".equals(cardName));
        updateNavState(btnNavCustomers, "Customers".equals(cardName));
        updateNavState(btnNavSettings, "Settings".equals(cardName));

        if ("Dashboard".equals(cardName)) {
            dashboardPanel.refreshData();
        } else if ("Customers".equals(cardName)) {
            customerPanel.loadTableData();
        }

        lblStatus.setText("화면: " + cardName + " | Ready");
    }

    private void updateNavState(JButton btn, boolean active) {
        if (active) {
            btn.putClientProperty("FlatLaf.styleClass", "accent");
            btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        } else {
            btn.putClientProperty("FlatLaf.styleClass", null);
            btn.setFont(btn.getFont().deriveFont(Font.PLAIN));
        }
        btn.repaint();
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));

        lblStatus = new JLabel("Ready");
        lblStatus.setFont(lblStatus.getFont().deriveFont(11.5f));
        lblStatus.setForeground(new Color(100, 116, 139));
        bar.add(lblStatus, BorderLayout.WEST);

        String jVer = System.getProperty("java.version");
        JLabel lblJava = new JLabel("Java " + jVer + " (JDK) | Swing + JGoodies + FlatLaf");
        lblJava.setFont(lblJava.getFont().deriveFont(11.5f));
        lblJava.setForeground(new Color(100, 116, 139));
        bar.add(lblJava, BorderLayout.EAST);

        return bar;
    }
}
