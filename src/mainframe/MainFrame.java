package mainframe;

import model.TaiKhoan;
import panel.HoaDonPanel;
import panel.KhachHangPanel;
import panel.SanPhamPanel;
import panel.TaiKhoanPanel;
import ui.LoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private final TaiKhoan currentTaiKhoan;
    private final JTabbedPane tabbedPane = new JTabbedPane(); // Khởi tạo ngay để final chắc chắn

    public MainFrame(TaiKhoan taiKhoan) {
        this.currentTaiKhoan = taiKhoan;

        try {
            // Thiết lập JFrame
            setTitle("Quản lý Cửa hàng Máy tính - " + taiKhoan.getVaiTro().toUpperCase());
            setSize(1200, 800);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Header panel với tiêu đề và nút logout
            JButton btnLogout = new JButton("Đăng xuất");
            btnLogout.setFont(new Font("Arial", Font.PLAIN, 14));
            btnLogout.addActionListener(e -> {
                try {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        this.dispose();
                        new LoginFrame().setVisible(true);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi đăng xuất: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            headerPanel.setBackground(new Color(245, 245, 245));

            JLabel lblTitle = new JLabel("Hệ thống quản lý", SwingConstants.LEFT);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitle.setForeground(new Color(30, 144, 255));
            headerPanel.add(lblTitle, BorderLayout.CENTER);
            headerPanel.add(btnLogout, BorderLayout.EAST);

            // Thiết lập tabbedPane font đẹp
            tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

            // Thêm các tab với try-catch riêng
            try {
                tabbedPane.addTab("Sản Phẩm", new SanPhamPanel(currentTaiKhoan));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo tab Sản Phẩm: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            try {
                tabbedPane.addTab("Khách Hàng", new KhachHangPanel(currentTaiKhoan));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo tab Khách Hàng: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            try {
                tabbedPane.addTab("Hóa Đơn", new HoaDonPanel(currentTaiKhoan));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo tab Hóa Đơn: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            try {
                tabbedPane.addTab("Tài Khoản", new TaiKhoanPanel(currentTaiKhoan));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo tab Tài Khoản: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            // Thêm header và tabbedPane vào JFrame
            add(headerPanel, BorderLayout.NORTH);
            add(tabbedPane, BorderLayout.CENTER);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi khởi tạo MainFrame: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}