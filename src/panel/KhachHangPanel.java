package panel;

import dao.KhachHangDAO;
import dao.KhachHangDAOImpl;
import model.KhachHang;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class KhachHangPanel extends JPanel {
    private final TaiKhoan currentTaiKhoan;
    private final KhachHangDAO khachHangDAO;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public KhachHangPanel(TaiKhoan taiKhoan) {
        this.currentTaiKhoan = taiKhoan;
        khachHangDAO = new KhachHangDAOImpl();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.decode("#F0F2F5"));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.decode("#007BFF"));
        add(lblTitle, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columnNames = {"ID", "Họ Tên", "Số Điện Thoại", "Địa Chỉ"};
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
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách khách hàng",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.decode("#343A40")
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Panel chức năng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        controlPanel.setBackground(Color.decode("#F0F2F5"));
        
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        
        // Thiết lập màu sắc và font cho các nút
        btnThem.setBackground(Color.decode("#28A745"));
        btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(Color.decode("#FFC107"));
        btnSua.setForeground(Color.BLACK);
        btnXoa.setBackground(Color.decode("#DC3545"));
        btnXoa.setForeground(Color.WHITE);
        btnLamMoi.setBackground(Color.decode("#6C757D"));
        btnLamMoi.setForeground(Color.WHITE);

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        btnThem.setFont(buttonFont);
        btnSua.setFont(buttonFont);
        btnXoa.setFont(buttonFont);
        btnLamMoi.setFont(buttonFont);
        
        controlPanel.add(btnThem);
        controlPanel.add(btnSua);
        controlPanel.add(btnXoa);
        controlPanel.add(btnLamMoi);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Thêm sự kiện
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> loadKhachHangData());

        loadKhachHangData();
    }
    
    private void loadKhachHangData() {
        try {
            tableModel.setRowCount(0);
            List<KhachHang> danhSach = khachHangDAO.getAllKhachHang();
            for (KhachHang kh : danhSach) {
                Object[] row = {kh.getId(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi()};
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void themKhachHang() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Khách Hàng", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
        
        // Panel chính sử dụng GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.decode("#F8F9FA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtHoTen = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField txtDiaChi = new JTextField();

        // Thêm các JLabel và JTextField
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtDiaChi, gbc);

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
            try {
                String hoTen = txtHoTen.getText().trim();
                String sdt = txtSDT.getText().trim();
                String diaChi = txtDiaChi.getText().trim();
                
                if (hoTen.isEmpty() || sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ Họ Tên và Số Điện Thoại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                KhachHang khMoi = new KhachHang(0, hoTen, sdt, diaChi);
                if (khachHangDAO.createKhachHang(khMoi)) {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    loadKhachHangData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình thêm: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dialog.setVisible(true);
    }
    
    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long id = (long) tableModel.getValueAt(selectedRow, 0);
            KhachHang kh = khachHangDAO.getKhachHangById(id);
            if (kh == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng. Vui lòng làm mới bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Khách Hàng", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 250);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
            
            // Panel chính sử dụng GridBagLayout
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            mainPanel.setBackground(Color.decode("#F8F9FA"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField txtHoTen = new JTextField(kh.getHoTen());
            JTextField txtSDT = new JTextField(kh.getSoDienThoai());
            JTextField txtDiaChi = new JTextField(kh.getDiaChi());
            
            // Thêm các JLabel và JTextField
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
            mainPanel.add(new JLabel("ID:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
            mainPanel.add(new JLabel(String.valueOf(kh.getId())), gbc);

            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
            mainPanel.add(new JLabel("Họ Tên:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
            mainPanel.add(txtHoTen, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
            mainPanel.add(new JLabel("Số Điện Thoại:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
            mainPanel.add(txtSDT, gbc);

            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
            mainPanel.add(new JLabel("Địa Chỉ:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
            mainPanel.add(txtDiaChi, gbc);

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
                try {
                    String hoTen = txtHoTen.getText().trim();
                    String sdt = txtSDT.getText().trim();
                    String diaChi = txtDiaChi.getText().trim();
                    
                    if (hoTen.isEmpty() || sdt.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ Họ Tên và Số Điện Thoại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    kh.setHoTen(hoTen);
                    kh.setSoDienThoai(sdt);
                    kh.setDiaChi(diaChi);
                    if (khachHangDAO.updateKhachHang(kh)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
                        loadKhachHangData();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình sửa: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lấy dữ liệu khách hàng: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

 private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long id = (long) tableModel.getValueAt(selectedRow, 0);
            String hoTen = (String) tableModel.getValueAt(selectedRow, 1);
            
            // Bước kiểm tra mới: Khách hàng có hóa đơn không?
            if (khachHangDAO.khachHangCoHoaDon(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa khách hàng '" + hoTen + "' vì người này đã có giao dịch tại cửa hàng.", 
                    "Lỗi ngoại lệ", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng '" + hoTen + "'?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (khachHangDAO.deleteKhachHang(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                    loadKhachHangData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình xóa: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}