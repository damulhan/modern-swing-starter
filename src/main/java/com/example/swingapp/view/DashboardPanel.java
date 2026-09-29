package com.example.swingapp.view;

import com.example.swingapp.model.Order;
import com.example.swingapp.presentation.DashboardPresentationModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardPanel extends JPanel {
    private final DashboardPresentationModel presentationModel;
    private JLabel lblCustomersVal;
    private JLabel lblOrdersVal;
    private JLabel lblRevenueVal;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;

    public DashboardPanel(DashboardPresentationModel presentationModel) {
        this.presentationModel = presentationModel;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        initComponents();
        refreshData();
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("대시보드 (Dashboard)");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(16));

        // Metric Cards using JGoodies FormLayout
        FormLayout cardsLayout = new FormLayout(
                "fill:default:grow, 16dlu, fill:default:grow, 16dlu, fill:default:grow",
                "pref"
        );
        JPanel cardsPanel = new JPanel(cardsLayout);
        cardsPanel.setOpaque(false);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblCustomersVal = new JLabel("0");
        lblOrdersVal = new JLabel("0");
        lblRevenueVal = new JLabel("$0");

        cardsPanel.add(createMetricCard("총 고객수 (Users)", lblCustomersVal, "👥", new Color(14, 165, 233)), "1, 1");
        cardsPanel.add(createMetricCard("총 주문수 (Orders)", lblOrdersVal, "📦", new Color(16, 185, 129)), "3, 1");
        cardsPanel.add(createMetricCard("총 매출액 (Revenue)", lblRevenueVal, "💰", new Color(245, 158, 11)), "5, 1");

        contentPanel.add(cardsPanel);
        contentPanel.add(Box.createVerticalStrut(28));

        // Section Title: Recent Orders
        JLabel ordersTitle = new JLabel("최근 주문 현황 (Recent Orders)");
        ordersTitle.setFont(ordersTitle.getFont().deriveFont(Font.BOLD, 16f));
        ordersTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(ordersTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        // Orders Table
        String[] columnNames = {"주문번호 (ID)", "고객명 (Customer)", "상태 (Status)", "결제금액 (Amount)", "주문일자 (Date)"};
        ordersTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(ordersTableModel);
        ordersTable.setRowHeight(32);
        ordersTable.setShowHorizontalLines(true);
        ordersTable.setShowVerticalLines(false);
        ordersTable.setAutoCreateRowSorter(true);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersTable.getTableHeader().setFont(ordersTable.getTableHeader().getFont().deriveFont(Font.BOLD));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        ordersTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        ordersTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        ordersTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, String icon, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.putClientProperty("FlatLaf.styleClass", "card");

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(titleLbl.getFont().deriveFont(Font.PLAIN, 12f));
        titleLbl.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 22f));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(iconLbl.getFont().deriveFont(24f));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLbl);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(valueLabel);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconLbl, BorderLayout.EAST);

        return card;
    }

    public void refreshData() {
        NumberFormat curFormat = NumberFormat.getCurrencyInstance(Locale.US);
        lblCustomersVal.setText(String.format("%,d", presentationModel.getCustomerCount()));
        lblOrdersVal.setText(String.format("%,d", presentationModel.getOrderCount()));
        lblRevenueVal.setText(curFormat.format(presentationModel.getTotalRevenue()));

        ordersTableModel.setRowCount(0);
        List<Order> orders = presentationModel.getRecentOrders();
        for (Order o : orders) {
            ordersTableModel.addRow(new Object[]{
                    o.getId(),
                    o.getCustomerName(),
                    o.getStatus(),
                    curFormat.format(o.getAmount()),
                    o.getDate().toString()
            });
        }
    }
}
