package panel;

import dao.SanPhamDAO;
import dao.SanPhamDAOImpl;
import model.SanPham;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SanPhamPanel extends JPanel {
    private final SanPhamDAO sanPhamDAO;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TaiKhoan currentTaiKhoan;

    public SanPhamPanel(TaiKhoan taiKhoan) {
        this.currentTaiKhoan = taiKhoan;
        sanPhamDAO = new SanPhamDAOImpl();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.decode("#F0F2F5"));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.decode("#007BFF"));
        add(lblTitle, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columnNames = {"ID", "Tên", "Hãng", "Giá", "Tồn Kho", "Loại", "Tình Trạng"};
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
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách sản phẩm",
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
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnLamMoi.addActionListener(e -> loadSanPhamData());
        
        loadSanPhamData();
    }
    
    private void loadSanPhamData() {
        try {
            tableModel.setRowCount(0);
            List<SanPham> danhSach = sanPhamDAO.getAllSanPham();
            for (SanPham sp : danhSach) {
                Object[] row = {sp.getId(), sp.getTen(), sp.getHang(), sp.getGia(), sp.getTonKho(), sp.getLoai(), sp.getTinhTrang()};
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void themSanPham() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Sản Phẩm", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(Color.decode("#F8F9FA"));
        
        JTextField txtTen = new JTextField();
        JTextField txtHang = new JTextField();
        JTextField txtGia = new JTextField();
        JTextField txtTonKho = new JTextField();
        JComboBox<String> cmbLoai = new JComboBox<>(new String[]{"Laptop", "Linh kiện", "Màn hình", "Khác"});
        
        formPanel.add(new JLabel("Tên:"));
        formPanel.add(txtTen);
        formPanel.add(new JLabel("Hãng:"));
        formPanel.add(txtHang);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(txtGia);
        formPanel.add(new JLabel("Tồn kho:"));
        formPanel.add(txtTonKho);
        formPanel.add(new JLabel("Loại:"));
        formPanel.add(cmbLoai);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBackground(Color.decode("#007BFF"));
        btnLuu.setForeground(Color.WHITE);
        
        btnLuu.addActionListener(e -> {
            try {
                String ten = txtTen.getText().trim();
                String hang = txtHang.getText().trim();
                
                if (ten.isEmpty() || hang.isEmpty() || txtGia.getText().trim().isEmpty() || txtTonKho.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ tất cả các trường.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double gia = Double.parseDouble(txtGia.getText());
                int tonKho = Integer.parseInt(txtTonKho.getText());
                String loai = (String) cmbLoai.getSelectedItem();
                
                SanPham spMoi = new SanPham(0, ten, hang, gia, tonKho, loai, "Mới");
                if (sanPhamDAO.createSanPham(spMoi)) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                    loadSanPhamData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá và tồn kho phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình thêm: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        buttonPanel.add(btnLuu);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    private void suaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            SanPham sp = sanPhamDAO.getSanPhamById(id);
            if (sp == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm. Vui lòng làm mới bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Sản Phẩm", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            formPanel.setBackground(Color.decode("#F8F9FA"));
            
            JTextField txtTen = new JTextField(sp.getTen());
            JTextField txtHang = new JTextField(sp.getHang());
            JTextField txtGia = new JTextField(String.valueOf(sp.getGia()));
            JTextField txtTonKho = new JTextField(String.valueOf(sp.getTonKho()));
            JComboBox<String> cmbLoai = new JComboBox<>(new String[]{"Laptop", "Linh kiện", "Màn hình", "Khác"});
            cmbLoai.setSelectedItem(sp.getLoai());
            
            formPanel.add(new JLabel("ID:"));
            formPanel.add(new JLabel(String.valueOf(sp.getId())));
            formPanel.add(new JLabel("Tên:"));
            formPanel.add(txtTen);
            formPanel.add(new JLabel("Hãng:"));
            formPanel.add(txtHang);
            formPanel.add(new JLabel("Giá:"));
            formPanel.add(txtGia);
            formPanel.add(new JLabel("Tồn kho:"));
            formPanel.add(txtTonKho);
            formPanel.add(new JLabel("Loại:"));
            formPanel.add(cmbLoai);

            dialog.add(formPanel, BorderLayout.CENTER);
            
            JButton btnLuu = new JButton("Lưu");
            btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnLuu.setBackground(Color.decode("#007BFF"));
            btnLuu.setForeground(Color.WHITE);
            
            btnLuu.addActionListener(e -> {
                try {
                    String ten = txtTen.getText().trim();
                    String hang = txtHang.getText().trim();
                    
                    if (ten.isEmpty() || hang.isEmpty() || txtGia.getText().trim().isEmpty() || txtTonKho.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ tất cả các trường.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    sp.setTen(ten);
                    sp.setHang(hang);
                    sp.setGia(Double.parseDouble(txtGia.getText()));
                    sp.setTonKho(Integer.parseInt(txtTonKho.getText()));
                    sp.setLoai((String) cmbLoai.getSelectedItem());
                    
                    if (sanPhamDAO.updateSanPham(sp)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                        loadSanPhamData();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Giá và tồn kho phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình sửa: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.decode("#F8F9FA"));
            buttonPanel.add(btnLuu);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lấy dữ liệu sản phẩm: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void xoaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String ten = (String) tableModel.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm '" + ten + "'?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (sanPhamDAO.deleteSanPham(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    loadSanPhamData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình xóa: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
//thêm phần ngoại lệ khi chưa có dữ liệu và thêm tìm kiếm ở 4 ô tiêu đề của bảng như tìm kiếm theo id , theo tên,.... và khi tìm thì hiển thị riêng ra 1 bảng mới ở 1 jframe mới