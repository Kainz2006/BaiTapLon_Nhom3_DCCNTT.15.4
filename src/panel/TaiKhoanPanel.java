package panel;

import dao.TaiKhoanDAO;
import dao.TaiKhoanDAOImpl;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TaiKhoanPanel extends JPanel {
    private final TaiKhoanDAO taiKhoanDAO;
    private DefaultTableModel tableModel;
    private JTable table;
    private TaiKhoan currentTaiKhoan;
    private JLabel lblLastLogin;
    
    // NEW: Các thành phần tìm kiếm
    private JTextField txtTimKiem; 
    private JButton btnTimKiem;    
    private JButton btnLamMoi; // Sử dụng lại nút này để reset tìm kiếm

    public TaiKhoanPanel(TaiKhoan taiKhoan) {
        this.currentTaiKhoan = taiKhoan;
        taiKhoanDAO = new TaiKhoanDAOImpl();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.decode("#F0F2F5"));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.decode("#007BFF"));
        add(lblTitle, BorderLayout.NORTH);

        if ("admin".equalsIgnoreCase(currentTaiKhoan.getVaiTro())) {
            setupAdminView();
        } else {
            setupEmployeeView();
        }
    }
    
    // THAY THẾ: Phương thức setupAdminView được sửa đổi để thêm giao diện tìm kiếm
    private void setupAdminView() {
        // 1. Bảng dữ liệu Setup
        String[] columnNames = {"ID", "Tên Đăng Nhập", "Vai Trò", "Thời Gian Đăng Nhập Cuối"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.decode("#E9ECEF"));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách tài khoản",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.decode("#343A40")
        ));

        // 2. Panel Tìm kiếm Setup (NEW)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtTimKiem = new JTextField(30);
        btnTimKiem = new JButton("Tìm Kiếm");
        
        searchPanel.setBackground(Color.decode("#F0F2F5"));
        searchPanel.add(new JLabel("Tìm kiếm (Tên ĐN, Vai Trò):"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        
        // Panel trung tâm chứa tìm kiếm và bảng
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // 3. Panel chức năng (Control Panel)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        controlPanel.setBackground(Color.decode("#F0F2F5"));
        
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới"); // Sửa lại thành biến instance
        
        // Thiết lập màu sắc và font cho các nút
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        btnThem.setBackground(Color.decode("#28A745")); btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(Color.decode("#FFC107")); btnSua.setForeground(Color.BLACK);
        btnXoa.setBackground(Color.decode("#DC3545")); btnXoa.setForeground(Color.WHITE);
        btnLamMoi.setBackground(Color.decode("#6C757D")); btnLamMoi.setForeground(Color.WHITE);

        btnThem.setFont(buttonFont); btnSua.setFont(buttonFont); btnXoa.setFont(buttonFont); btnLamMoi.setFont(buttonFont);
        
        controlPanel.add(btnThem);
        controlPanel.add(btnSua);
        controlPanel.add(btnXoa);
        controlPanel.add(btnLamMoi);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // 4. Thêm sự kiện
        btnThem.addActionListener(e -> themTaiKhoan());
        btnSua.addActionListener(e -> suaTaiKhoan());
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        
        // NEW: Sự kiện tìm kiếm và làm mới
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            loadTaiKhoanData(keyword);
        });
        
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadTaiKhoanData(null);
        });

        loadTaiKhoanData(null); // Tải dữ liệu ban đầu
    }
    
    // Phương thức setupEmployeeView giữ nguyên logic
    private void setupEmployeeView() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.decode("#F8F9FA"));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Thẻ thông tin
        JPanel infoCard = new JPanel(new GridLayout(0, 1, 5, 5));
        infoCard.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Thông Tin Tài Khoản",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.decode("#343A40")
        ));
        infoCard.setBackground(Color.WHITE);
        infoCard.setPreferredSize(new Dimension(350, 150));

        JLabel lblUsername = new JLabel("Tên đăng nhập: " + currentTaiKhoan.getTenDangNhap());
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lblRole = new JLabel("Vai trò: " + currentTaiKhoan.getVaiTro());
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        lblLastLogin = new JLabel("Đăng nhập gần nhất: ");
        lblLastLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        infoCard.add(lblUsername);
        infoCard.add(lblRole);
        infoCard.add(lblLastLogin);

        // Nút đổi mật khẩu
        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDoiMatKhau.setBackground(Color.decode("#007BFF"));
        btnDoiMatKhau.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        buttonPanel.add(btnDoiMatKhau);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(infoCard, gbc);

        gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(buttonPanel, gbc);

        btnDoiMatKhau.addActionListener(e -> doiMatKhau());

        add(contentPanel, BorderLayout.CENTER);
        
        // Gọi phương thức cập nhật thời gian đăng nhập
        updateEmployeeInfo();
    }
    
    // Phương thức cập nhật thông tin nhân viên (Giữ nguyên)
    private void updateEmployeeInfo() {
        TaiKhoan latestTaiKhoan = taiKhoanDAO.getTaiKhoanById(currentTaiKhoan.getId());
        if (latestTaiKhoan != null) {
            currentTaiKhoan = latestTaiKhoan;
            lblLastLogin.setText("Đăng nhập gần nhất: " + currentTaiKhoan.getThoiGianDangNhapCuoi());
        }
    }
    
    // THAY THẾ: Phương thức loadTaiKhoanData được cập nhật để nhận tham số tìm kiếm và xử lý ngoại lệ
    private void loadTaiKhoanData(String keyword) {
        if (tableModel != null) {
            try {
                tableModel.setRowCount(0);
                List<TaiKhoan> danhSach;
                
                if (keyword != null && !keyword.isEmpty()) {
                    danhSach = taiKhoanDAO.searchTaiKhoan(keyword);
                } else {
                    danhSach = taiKhoanDAO.getAllTaiKhoan();
                }

                if (danhSach.isEmpty()) {
                    String message = keyword != null && !keyword.isEmpty() 
                                     ? "Không tìm thấy tài khoản nào khớp với từ khóa: '" + keyword + "'."
                                     : "Chưa có dữ liệu tài khoản nào trong hệ thống.";
                    JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                for (TaiKhoan tk : danhSach) {
                    Object[] row = {tk.getId(), tk.getTenDangNhap(), tk.getVaiTro(), tk.getThoiGianDangNhapCuoi()};
                    tableModel.addRow(row);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    // Giữ nguyên các phương thức CRUD
    
    private void themTaiKhoan() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Tài Khoản", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.decode("#F8F9FA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTenDangNhap = new JTextField();
        JPasswordField txtMatKhau = new JPasswordField();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Tên Đăng Nhập:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtTenDangNhap, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Mật Khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtMatKhau, gbc);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBackground(Color.decode("#007BFF"));
        btnLuu.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        buttonPanel.add(btnLuu);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnLuu.addActionListener(e -> {
            String tenDN = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword());
            if (!tenDN.isEmpty() && !matKhau.isEmpty()) {
                TaiKhoan tkMoi = new TaiKhoan();
                tkMoi.setTenDangNhap(tenDN);
                tkMoi.setMatKhau(matKhau);
                tkMoi.setVaiTro("nhanvien");
                if (taiKhoanDAO.createTaiKhoan(tkMoi)) {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
                    loadTaiKhoanData(null);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }
    
    private void suaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long id = (long) tableModel.getValueAt(selectedRow, 0);
        String tenDN = (String) tableModel.getValueAt(selectedRow, 1);
        
        if ("admin".equalsIgnoreCase(tenDN)) {
            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(this, pf, "Xác minh tài khoản Admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (okCxl == JOptionPane.OK_OPTION) {
                String password = new String(pf.getPassword());
                
                TaiKhoan adminTaiKhoan = taiKhoanDAO.getTaiKhoanById(currentTaiKhoan.getId());
                
                if (adminTaiKhoan != null && adminTaiKhoan.getMatKhau().equals(password)) {
                    showEditDialog(id, tenDN);
                } else {
                    JOptionPane.showMessageDialog(this, "Mật khẩu Admin không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            showEditDialog(id, tenDN);
        }
    }

    private void showEditDialog(long id, String tenDN) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Tài Khoản", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.decode("#F8F9FA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPasswordField txtMatKhauMoi = new JPasswordField();
        JComboBox<String> cmbVaiTro = new JComboBox<>(new String[]{"Admin", "Nhân Viên"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Tên Đăng Nhập:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel(tenDN), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Mật Khẩu Mới:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtMatKhauMoi, gbc);
        
        if ("admin".equalsIgnoreCase(currentTaiKhoan.getVaiTro())) {
            gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
            mainPanel.add(new JLabel("Vai trò:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
            mainPanel.add(cmbVaiTro, gbc);
            
            TaiKhoan taiKhoanHienTai = taiKhoanDAO.getTaiKhoanById(id);
            if (taiKhoanHienTai != null) {
                cmbVaiTro.setSelectedItem(taiKhoanHienTai.getVaiTro());
            }
        }

        dialog.add(mainPanel, BorderLayout.CENTER);
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBackground(Color.decode("#007BFF"));
        btnLuu.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        buttonPanel.add(btnLuu);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnLuu.addActionListener(e -> {
            String matKhauMoi = new String(txtMatKhauMoi.getPassword());
            TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(id);
            if (tk != null) {
                if (!matKhauMoi.isEmpty()) {
                    tk.setMatKhau(matKhauMoi);
                }

                if ("admin".equalsIgnoreCase(currentTaiKhoan.getVaiTro())) {
                    tk.setVaiTro((String) cmbVaiTro.getSelectedItem());
                }

                if (taiKhoanDAO.updateTaiKhoan(tk)) {
                    if (currentTaiKhoan.getId() == tk.getId()) {
                        currentTaiKhoan.setMatKhau(tk.getMatKhau());
                        currentTaiKhoan.setVaiTro(tk.getVaiTro());
                        updateEmployeeInfo(); // Cập nhật lại giao diện Employee nếu cần
                    }
                    JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!");
                    loadTaiKhoanData(null);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        dialog.setVisible(true);
    }
    
    private void xoaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long id = (long) tableModel.getValueAt(selectedRow, 0);
        String tenDN = (String) tableModel.getValueAt(selectedRow, 1);
        
        if ("admin".equalsIgnoreCase(tenDN)) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản Admin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản '" + tenDN + "'?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (taiKhoanDAO.deleteTaiKhoan(id)) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
                loadTaiKhoanData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doiMatKhau() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đổi Mật Khẩu", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.decode("#F8F9FA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField txtMatKhauHienTai = new JPasswordField();
        JPasswordField txtMatKhauMoi = new JPasswordField();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Mật Khẩu Hiện Tại:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtMatKhauHienTai, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Mật Khẩu Mới:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtMatKhauMoi, gbc);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBackground(Color.decode("#007BFF"));
        btnLuu.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        buttonPanel.add(btnLuu);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnLuu.addActionListener(e -> {
            String matKhauHienTai = new String(txtMatKhauHienTai.getPassword());
            String matKhauMoi = new String(txtMatKhauMoi.getPassword());
            
            if (matKhauHienTai.isEmpty() || matKhauMoi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            TaiKhoan taiKhoanHienTaiMoiNhat = taiKhoanDAO.getTaiKhoanById(currentTaiKhoan.getId());
            
            if (taiKhoanHienTaiMoiNhat == null || !taiKhoanHienTaiMoiNhat.getMatKhau().equals(matKhauHienTai)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu hiện tại không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            taiKhoanHienTaiMoiNhat.setMatKhau(matKhauMoi);
            if (taiKhoanDAO.updateTaiKhoan(taiKhoanHienTaiMoiNhat)) {
                currentTaiKhoan.setMatKhau(matKhauMoi);
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công!");
                updateEmployeeInfo(); 
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }
}