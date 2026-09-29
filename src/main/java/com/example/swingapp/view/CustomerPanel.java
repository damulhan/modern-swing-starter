package com.example.swingapp.view;

import com.example.swingapp.model.Customer;
import com.example.swingapp.presentation.CustomerPresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerPanel extends JPanel {
    private final CustomerPresentationModel presentationModel;
    private final Runnable onDataChangedCallback;

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    // Bound UI components
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JCheckBox chkActive;

    public CustomerPanel(CustomerPresentationModel presentationModel, Runnable onDataChangedCallback) {
        this.presentationModel = presentationModel;
        this.onDataChangedCallback = onDataChangedCallback;

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        initComponents();
        loadTableData();
    }

    private void initComponents() {
        // Top Toolbar: Search + New Button
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel titleLabel = new JLabel("고객 관리 (Customers)");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField(18);
        txtSearch.putClientProperty("JTextField.placeholderText", "이름, 이메일, 전화번호 검색...");
        txtSearch.addActionListener(e -> performSearch());

        JButton btnSearch = new JButton("검색");
        btnSearch.addActionListener(e -> performSearch());

        JButton btnNew = new JButton("+ 신규 등록");
        btnNew.putClientProperty("FlatLaf.styleClass", "accent");
        btnNew.addActionListener(e -> {
            customerTable.clearSelection();
            presentationModel.createNewCustomer();
            txtName.requestFocusInWindow();
        });

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnNew);
        topBar.add(searchPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Center split: Left Table / Right Edit Form
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.58);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);

        // Left Table Panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        String[] cols = {"ID", "이름 (Name)", "이메일 (Email)", "전화번호 (Phone)", "상태 (Status)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(32);
        customerTable.setAutoCreateRowSorter(true);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onTableRowSelected();
            }
        });

        JScrollPane tableScroll = new JScrollPane(customerTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        leftPanel.add(tableScroll, BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Right Edit Form using JGoodies FormLayout & JGoodies Binding
        JPanel rightCard = new JPanel(new BorderLayout());
        rightCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("고객 정보 편집 (JGoodies Forms & Binding)");
        formTitle.setFont(formTitle.getFont().deriveFont(Font.BOLD, 15f));
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        rightCard.add(formTitle, BorderLayout.NORTH);

        // JGoodies FormLayout setup
        FormLayout layout = new FormLayout(
                "right:pref, 8dlu, fill:default:grow",
                "pref, 8dlu, pref, 8dlu, pref, 8dlu, pref, 16dlu, pref"
        );

        JPanel formPanel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();

        // JGoodies Binding: BasicComponentFactory creates bound Swing components
        txtName = BasicComponentFactory.createTextField(presentationModel.getNameModel());
        txtEmail = BasicComponentFactory.createTextField(presentationModel.getEmailModel());
        txtPhone = BasicComponentFactory.createTextField(presentationModel.getPhoneModel());
        chkActive = BasicComponentFactory.createCheckBox(presentationModel.getActiveModel(), "활성 고객 (Active)");

        formPanel.add(new JLabel("이름 (Name):"), cc.xy(1, 1));
        formPanel.add(txtName, cc.xy(3, 1));

        formPanel.add(new JLabel("이메일 (Email):"), cc.xy(1, 3));
        formPanel.add(txtEmail, cc.xy(3, 3));

        formPanel.add(new JLabel("전화번호 (Phone):"), cc.xy(1, 5));
        formPanel.add(txtPhone, cc.xy(3, 5));

        formPanel.add(new JLabel("상태 (Status):"), cc.xy(1, 7));
        formPanel.add(chkActive, cc.xy(3, 7));

        // Buttons: Save / Delete / Cancel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnSave = new JButton("저장 (Save)");
        btnSave.putClientProperty("FlatLaf.styleClass", "accent");
        btnSave.addActionListener(e -> onSave());

        JButton btnDelete = new JButton("삭제 (Delete)");
        btnDelete.addActionListener(e -> onDelete());

        JButton btnCancel = new JButton("취소 (Cancel)");
        btnCancel.addActionListener(e -> onCancel());

        btnPanel.add(btnDelete);
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        formPanel.add(btnPanel, cc.xy(3, 9));

        rightCard.add(formPanel, BorderLayout.CENTER);
        splitPane.setRightComponent(rightCard);

        add(splitPane, BorderLayout.CENTER);
    }

    private void onTableRowSelected() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = customerTable.convertRowIndexToModel(selectedRow);
            Long id = (Long) tableModel.getValueAt(modelRow, 0);
            List<Customer> all = presentationModel.getAllCustomers();
            for (Customer c : all) {
                if (c.getId().equals(id)) {
                    // Update JGoodies PresentationModel -> automatically reflects in bound textfields
                    presentationModel.setCustomer(c);
                    break;
                }
            }
        }
    }

    private void onSave() {
        String name = (String) presentationModel.getNameModel().getValue();
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "고객 이름을 입력해 주세요.", "입력 확인", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocusInWindow();
            return;
        }

        presentationModel.saveCurrent();
        loadTableData();
        if (onDataChangedCallback != null) {
            onDataChangedCallback.run();
        }
        JOptionPane.showMessageDialog(this, "성공적으로 저장되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onDelete() {
        Customer cur = presentationModel.getCurrentCustomer();
        if (cur.getId() == null) {
            JOptionPane.showMessageDialog(this, "삭제할 고객을 목록에서 선택하세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int opt = JOptionPane.showConfirmDialog(this,
                "선택한 고객(" + cur.getName() + ")을 삭제하시겠습니까?",
                "삭제 확인",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opt == JOptionPane.YES_OPTION) {
            presentationModel.deleteCurrent();
            loadTableData();
            if (onDataChangedCallback != null) {
                onDataChangedCallback.run();
            }
        }
    }

    private void onCancel() {
        onTableRowSelected();
    }

    private void performSearch() {
        loadTableData();
    }

    public void loadTableData() {
        String kw = (txtSearch != null) ? txtSearch.getText() : "";
        List<Customer> list = presentationModel.search(kw);

        tableModel.setRowCount(0);
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone(),
                    c.isActive() ? "Active" : "Inactive"
            });
        }
    }
}
