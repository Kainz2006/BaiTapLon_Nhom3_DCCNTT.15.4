package ui;

import dao.TaiKhoanDAO;
import dao.TaiKhoanDAOImpl;
import model.TaiKhoan;
import mainframe.MainFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Objects;

public class LoginFrame extends JFrame {
    private final JTextField txtTenDangNhap;
    private final JPasswordField txtMatKhau;
    private final JRadioButton rbAdmin;
    private final JRadioButton rbNhanVien;
    private final JButton btnDangNhap;
    private final JButton btnThoat; // NEW: Nút Thoát
    private final TaiKhoanDAO taiKhoanDAO;

    public LoginFrame() {
        setTitle("Đăng nhập");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        taiKhoanDAO = new TaiKhoanDAOImpl();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setBackground(Color.decode("#F5F5F5"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.decode("#007BFF"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Tên đăng nhập
        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblTenDangNhap, gbc);
        txtTenDangNhap = new JTextField(15);
        txtTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtTenDangNhap, gbc);

        // Mật khẩu
        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblMatKhau, gbc);
        txtMatKhau = new JPasswordField(15);
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtMatKhau, gbc);
        
        // Vai trò
        rbAdmin = new JRadioButton("Admin");
        rbNhanVien = new JRadioButton("Nhân viên");
        ButtonGroup vaiTroGroup = new ButtonGroup();
        vaiTroGroup.add(rbAdmin);
        vaiTroGroup.add(rbNhanVien);
        rbNhanVien.setSelected(true); 

        JPanel vaiTroPanel = new JPanel();
        vaiTroPanel.setBackground(Color.decode("#F5F5F5"));
        vaiTroPanel.add(rbAdmin);
        vaiTroPanel.add(rbNhanVien);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(vaiTroPanel, gbc);

        // Panel chứa 2 nút (Đăng nhập và Thoát)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 hàng, 2 cột
        buttonPanel.setBackground(Color.decode("#F5F5F5"));
        
        // Nút đăng nhập
        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangNhap.setBackground(Color.decode("#28A745"));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        
        // NEW: Nút Thoát
        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Arial", Font.BOLD, 14));
        btnThoat.setBackground(Color.decode("#DC3545")); // Màu đỏ cảnh báo
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setFocusPainted(false);
        
        buttonPanel.add(btnDangNhap);
        buttonPanel.add(btnThoat);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc); // Thêm Panel chứa 2 nút

        // NEW: Thêm sự kiện cho nút Thoát
        btnThoat.addActionListener(e -> {
            System.exit(0); // Đóng toàn bộ chương trình
        });
        
        btnDangNhap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tenDangNhap = txtTenDangNhap.getText();
                String matKhau = new String(txtMatKhau.getPassword());
                
                if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Tên đăng nhập và mật khẩu không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanByTenDangNhap(tenDangNhap);
                    
                    if (taiKhoan != null) {
                        // Kiểm tra mật khẩu
                        if (taiKhoan.getMatKhau().equals(matKhau)) {
                            // Cập nhật thời gian đăng nhập cuối cùng
                            taiKhoanDAO.updateThoiGianDangNhapCuoi(taiKhoan.getId(), new Timestamp(System.currentTimeMillis()));

                            // Kiểm tra vai trò
                            if (rbAdmin.isSelected() && "admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                                new MainFrame(taiKhoan).setVisible(true);
                                dispose();
                            } else if (rbNhanVien.isSelected() && "nhanvien".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                                new MainFrame(taiKhoan).setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(LoginFrame.this, "Vai trò bạn chọn không khớp với tài khoản.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Mật khẩu không đúng.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Tên đăng nhập không tồn tại.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        add(panel);
    }
}